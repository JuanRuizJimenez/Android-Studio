package es.ucm.gdv.switchdash;

import java.util.ArrayList;

import es.ucm.gdv.engine.Game;
import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;
import es.ucm.gdv.engine.Rect;
import es.ucm.gdv.engine.Sprite;


/**
 * La clase baseLogic es una implementacion abstracta de Logic, que contiene elementos comunes
 * a los demas estados como la lista de gameObjects y el pintado del fondo de la patalla.
 * Ademas contiene algunos metodos auxiliares comunes a los estados de lógica como
 * la gestion del cambio de estado, transparencias, fuentes, etc
 */
public abstract class BaseLogic implements es.ucm.gdv.engine.Logic {

    // Variables estaticas ya que se conservan entre estados
    protected static int _score;
    protected static int _backgroundColorIndex;

    // Gestores de inicio y bucle de juego, graficos e input
    protected Game _game;
    protected Graphics _graphics;
    protected Input _input;

    // Variables con las dimensiones de la pantala (ahorro de gets a graphics)
    protected int _screenW = 0;
    protected int _screenH = 0;

    // Estado actual del juego
    protected GameState _gameState;
    // Siguiente estado del juego, cuando difiere del actual, se cambia
    protected GameState _nextGameState;
    //Ambos se inician con el valor del estado actual en el init de dicho estado

    // Array con los posibles colores del fondo
    protected int[] _backgroundColors = {0xff41a85f, 0xff00a885, 0xff3d8eb9, 0xff2969b0, 0xff553982,
            0xff28324e, 0xfff37934, 0xffd14b41, 0xff75706b};

    // Objetos con sprites comunes a varios estados
    protected GameObject _backgroundObject;
    protected GameObject _bgSquare;
    protected GameObject _flashObject;
    protected GameObject _controlsObject;
    protected GameObject _soundObject;

    // Variables asociadas a dichos objetos comunes
    protected int _bgSquareSize = 32;
    protected int _bgPatronSize = 612; // pixels
    protected float _bgSpeed = 384;
    protected int _bgAlpha = 50;
    protected int _buttonSize = 140;
    protected int _buttonY = 30;

    protected Sprite _soundOn;
    protected Sprite _soundOff;
    protected boolean _sound = true;

    // Variables asociadas a los metodos que modifican la transparencia
    protected float _maxAlpha = 255;
    protected float _alphaIncr = 400;
    protected float _fadeOutDecr = -700;

    // Listas de objetos
    protected ArrayList<GameObject> _gameObjects;
    protected ArrayList<GameObject> _scoreObjects;

    // Numero maximo de digitos de la puntuacion
    protected int _maxScoreDigits = 3;

    // Variables relativas a la fuente
    protected int _fontColumns = 15;
    protected int _fontRows = 7;
    protected float _fontWidth = 125;
    protected float _fontHeight = 160;
    protected float _realFontWidth = 93;
    protected float _realFontHeight = 118;

    // String que contiene las letras de la fuente en le orden en que aparecen en la imagen
    protected  String fontString = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,;?!-_~#";

    /**
     * Inicializa lo necesario para el funcionamiento del estado lógico, ademas de ciertos
     * elementos comunes a otros estados
     *
     * @param game necesita acceso al controlador del juego
     *
     * @return booleano de control si ha habido algún fallo durante la inicialización
     */
    @Override
    public boolean init(Game game) {
        _game = game;
        _input = game.getInput();
        _graphics = _game.getGraphics();
        // Inicializacoin listas de objetos
        _gameObjects = new ArrayList<GameObject>();
        _scoreObjects = new ArrayList<GameObject>();

        try {
            // Asignamos dimensioens
            _screenW = _graphics.getCanvasWidth();
            _screenH = _graphics.getCanvasHeight();

            // Creacion de las flechas del fondo
            _backgroundObject = new GameObject(new Sprite(_graphics.newImage("Sprites/arrowsBackground.png")));
            float _bgImageWidth = _backgroundObject.getW();
            float _bgImageHeight = _backgroundObject.getH();
            float _bgImageX = (_screenW / 2.0f) - (_bgImageWidth/ 2.0f);
            _backgroundObject.setX(_bgImageX);
            _backgroundObject.setAlpha(_bgAlpha);

            // Creacion del cuadrado que se pinta por debajo del fondo, del mismo color que el fondo pero mas claro
            _bgSquare = new GameObject(new Sprite(_graphics.newImage("Sprites/backgrounds.png"),
                    new Rect(_backgroundColorIndex*_bgSquareSize, 0, _bgSquareSize, _bgSquareSize)),
                    new Rect(_bgImageX, 0, _bgImageWidth, _bgImageHeight));
            _gameObjects.add(_bgSquare);

            // Añadimos las flechas despues para que se pinten por encima
            _gameObjects.add(_backgroundObject);

            // Creamos y añadimos los objetos que pintaran la puntuacion en base a sus digitos maximos
            for(int i = 0; i < _maxScoreDigits; i++) {
                GameObject aux = new GameObject(new Rect(0, 0,
                        _realFontWidth, _realFontHeight));
                _gameObjects.add(aux);
                _scoreObjects.add(aux);
            }

            // Creamos los sprites para el icono el sonido
            _soundOn = new Sprite(_graphics.newImage("Sprites/buttons.png"),
                    new Rect(ButtonType.SoundOn.ordinal() * _buttonSize,0,_buttonSize,_buttonSize));
            _soundOff = new Sprite(_graphics.newImage("Sprites/buttons.png"),
                    new Rect(ButtonType.SoundOff.ordinal() * _buttonSize,0,_buttonSize,_buttonSize));

            // Creamos los botones de controles y sonido y los dejamos a false, ya que no se usan
            // en todos los estados
            _soundObject = new GameObject(_soundOn);
            _soundObject.setCoords(_buttonSize / 3, _buttonY);
            _soundObject.setActive(false);
            _gameObjects.add(_soundObject);

            _controlsObject = new GameObject(new Sprite(_graphics.newImage("Sprites/buttons.png"),
                    new Rect(ButtonType.Controls.ordinal() *_buttonSize,0,_buttonSize,_buttonSize)));
            _controlsObject.setCoords(_screenW - (_buttonSize + _buttonSize / 3), _buttonY);
            _controlsObject.setActive(false);
            _gameObjects.add(_controlsObject);

            // Creamos la imagen en blanco que causa el efecto flash al hacerle un fadeout
            _flashObject = new GameObject(new Sprite(_graphics.newImage("Sprites/white.png")),
                    new Rect(0,0, _screenW, _screenH));
            _gameObjects.add(_flashObject);
            return true;
        }

        catch (Exception e) {
            // Avisamos en caso de fallo
            System.err.println(e);
            return false;
        }
    }

    /**
     * Actualiza el estado de logica, en concreto se encarga del
     * movimiento ciclico del fondo y los flashazos al inicio de los estados
     *
     * @param deltaTime tiempo en segundos desde el frame anterior
     */
    @Override
    public void update(double deltaTime) {
        // Movemos las flechas en loop para simular un desplazamiento hacia abajo constante
        if(_backgroundObject != null) {
            if (_backgroundObject.getY() + (_bgSpeed * deltaTime) < 0) {
                _backgroundObject.setY(_backgroundObject.getY() + (float) (_bgSpeed * deltaTime));
            }
            else
                _backgroundObject.setY(-_bgPatronSize);
        }
        // Mientras el flash siga activo llamamos a fadeOut
        if(_flashObject.isActive()){
            fadeOut(_flashObject, deltaTime);
        }
    }

    /**
     * En cada frame pinta el fondo con un clear y
     * renderiza los gameObjects activoss
     */
    @Override
    public void render() {
        _graphics.clear(_backgroundColors[_backgroundColorIndex]);

        for(GameObject g : _gameObjects){
            if(g.isActive())
                g.drawSprite(_graphics);
        }
    }


    /**
     * Metodo para manejar los inputs, las logicas lo implementan
     * segun los estados que les interesen
     */
    public abstract void checkInput();

    /**
     * Metodo que actualiza los sprites de los objetos en ScoreObjects en base a la puntuacion actual
     */
    protected void updateScoreObjects() {
        int aux = _score;
        int digit;

        // Primer digito (se pinta aunque sea 0)
        digit = aux % 10;
        _scoreObjects.get(0).setSprite(new Sprite(_graphics.newImage("Sprites/scoreFont.png"),
                getLetterRect((char) (digit + '0'))));
        int i = 1;
        // El resto de digitos (no se pintan si son 0)
        while (i < _maxScoreDigits && aux >= 10) {
            aux = aux / 10;
            digit = aux % 10;

            _scoreObjects.get(i).setSprite(new Sprite(_graphics.newImage("Sprites/scoreFont.png"),
                    getLetterRect((char) (digit + '0'))));

            _scoreObjects.get(i).setActive(true);
            i++;
        }

    }


    /**
     *  Metodo que calcula y devuelve el rect del caracter c en base a la imagen con la fuente
     *
     * @param c Caracter del que buscar el rect
     * @return Rect del caracter en la imagen con la fuente
     */
    protected Rect getLetterRect(char c){
        int pos = fontString.indexOf(c);
        int row = (int)pos/_fontColumns;
        int column = pos - row*_fontColumns;
        return new Rect(_fontWidth*column + (_fontWidth-_realFontWidth)/2,
                _fontHeight*row +(_fontHeight-_realFontHeight)/2, _realFontWidth, _realFontHeight);
    }

    /**
     * Metodo que alterna el sprite de sonido entre on y off
     */
    protected void switchSound(){
        if(_sound){
            _soundObject.setSprite(_soundOff);
        }
        else {
            _soundObject.setSprite(_soundOn);
        }
        _sound = !_sound;
    }

    /**
     * Metodo que hace el alfa del objeto g oscilar entre 0 y el maximo a velocidad AlphaIncr
     *
     * @param g
     * @param deltatime
     */
    protected void oscilateAlpha(GameObject g, double deltatime){
        float aux = g.getAlpha();
        aux += _alphaIncr*deltatime;
        if(_alphaIncr>0 && aux >= _maxAlpha){
            _alphaIncr*=-1;
            aux = _maxAlpha;
        }
        else if (aux <=0){
            _alphaIncr*=-1;
            aux = 0;
        }
        g.setAlpha(aux);
    }



    /**
     * Metodo que reduce el alfa del objeto g hasta llegar a 0 a velocidad _fadeOutDecr, al llegar
     * a 0 el objeto se desactiva (ya no se llama a su render)
     * @param g Objeto al que se hace el fadeout
     * @param deltatime tiempo en segundos desde el frame anterior
     */
    protected void fadeOut(GameObject g, double deltatime){
        float aux = g.getAlpha();
        aux += _fadeOutDecr*deltatime;
        if (aux <=0){
            aux = 0;
            g.setActive(false);
        }
        g.setAlpha(aux);
    }



    /**
     * Metodo que actualiza el estado del juego (cambia la logica) cuando nextGameState difiera
     * del actual
     */
    protected void updateGameState(){
        if(_nextGameState != _gameState)
        switch(_nextGameState){
            case Menu:
                Menu menu = new Menu();
                menu.init(_game);
                _game.setLogic(menu);
                break;
            case Controls:
                Controls controls = new Controls();
                controls.init(_game);
                _game.setLogic(controls);
                break;
            case Demo:
                Demo demo = new Demo();
                demo.init(_game);
                _game.setLogic(demo);
                break;
            case GameOver:
                GameOver over = new GameOver();
                over.init(_game);
                _game.setLogic(over);
                break;
        }
    }

    /**
     * Metodo que comprueba si las coordenadas dasdas entran en el objeto g
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param g Gameobject objetivo
     * @return indica si las coordenadas estan dentro del objeto
     */
    protected boolean isInBounds(float x, float y, GameObject g){
        return (x >= g.getX() && x < g.getX() + g.getW()
                && y >= g.getY() && y < g.getY() + g.getH());
    }
}
