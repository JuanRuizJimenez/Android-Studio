package es.ucm.gdv.switchdash;

import java.util.ArrayList;

import es.ucm.gdv.engine.Game;
import es.ucm.gdv.engine.Image;
import es.ucm.gdv.engine.Input;
import es.ucm.gdv.engine.Sprite;
import es.ucm.gdv.engine.Rect;

/**
 * Clase que extiene BaseLogic e implementa la logica principal
 * del juego SwitchDash
 */
public class Demo extends BaseLogic{

    // Barra que controla el jugador
    private BarObject _barObject;

    // Sendas listas para particulas y bolas
    private ArrayList<Particle> _particles;
    private ArrayList<BallObject> _ballObjects;

    // Imagen de las bolas y particulas
    private Image _ballsImage;

    // Sendos sprites para bolas blancas y negras
    private Sprite _whiteBallSprite;
    private Sprite _blackBallSprite;


    // Variables asociadas al tamaño de las bolas
    private int _ballSize = 128;
    private int _realBallSize = 100;
    private int _sizeDiff = (_ballSize - _realBallSize)/2;

    // Separacoin entre bolas
    private int _ballSeparation = 395;


    // Numero de bolas que coexisten
    private int _nBalls = 5;

    // Numero de particulas que se generan cada vez que ganamos un punto
    private int _nParticles = 10;

    // Numero de bolas que superas entre cada incremento de velocidad
    private int _ballsPerIncrease = 10;

    // Contador de las bolas entre cada incremento de velocidad
    private  int _ballCounter = 0;

    // Incremento de la velocidad cuando superas cierto numero de volas
    private float _ballSpIncrease = 90;

    // Velocidad de las bolas, que va incrementando
    private float _ballSpeed = 430;

    // Tipo de las bolas, que determina su forma
    private BallType _ballType = BallType.Circle;

    // Valor maximo del alpha
    private float maxAlpha = 255;

    /**
     * Inicializa lo necesario para el funcionamiento del estado
     * (Randominza el color del fondo, coloca bolas y objetos ocn puntuacion etc)
     *
     * @param game necesita acceso al controlador del juego
     *
     * @return booleano de control si ha habido algún fallo durante la inicialización
     */
    @Override
    public boolean init(Game game) {

        // Obtenemos un color de fondo aleatorio entre los del array
        _backgroundColorIndex = (int)(Math.random()*8);
        boolean error = super.init(game);
        // Inicializamos arrays de bolas y particulas
        _ballObjects = new ArrayList<BallObject>();
        _particles = new ArrayList<Particle>();

        try {
            // Asignamos estado de juego
            _nextGameState = _gameState = GameState.Demo;
            // Reseteamos la puntuacion
            _score = 0;

            // Creamos la imagen de la que se sacan las bolas y particulas
            _ballsImage = _graphics.newImage("Sprites/balls.png");

            // Creamos los sprites de bolas y particulas
            _whiteBallSprite = new Sprite(_ballsImage, new Rect(_ballType.ordinal()*_ballSize + _sizeDiff,
                            Color.White.ordinal()*_ballSize + _sizeDiff*2, _realBallSize, _realBallSize));

            _blackBallSprite = new Sprite(_ballsImage, new Rect(_ballType.ordinal()*_ballSize + _sizeDiff,
                    Color.Black.ordinal()*_ballSize + _sizeDiff*2, _realBallSize, _realBallSize));

            // Creamos la barra
            _barObject = new BarObject(_graphics.newImage("Sprites/players.png"), _screenW);
            _gameObjects.add(_barObject);

            // Creamos nBalls bolas y asignamos transform
            for(int i = 0; i < _nBalls; i++) {
                BallObject ball = new BallObject(_ballType, _whiteBallSprite, _blackBallSprite,
                        new Rect(((_screenW / 2) - (_realBallSize / 2)), 0, _realBallSize, _realBallSize));
                _gameObjects.add(ball);
                _ballObjects.add(ball);
            }

            // Colocamos las bolas en sus posiciones
            repositionBalls();

            // Colocamos los objetos responsables de mostras la puntuacion
            for(int i = 0; i < _maxScoreDigits; i++){
                _scoreObjects.get(i).setCoords(_screenW - _scoreObjects.get(i).getW() * (i + 1),
                        _fontWidth/2);
            }

            // Actualizacion inicial de los sprites de la puntuacion (valor 0)
            updateScoreObjects();
            return true;
        }

        catch (Exception e) {
            // avisamos de errores
            System.err.println(e);
            return false;
        }
    }

    /**
     * Realiza la actualización de la lógica de la aplicación.
     * Movimiento de bolas, puntos, deteccion de gameover,
     * generacion de particulas etc
     *
     * @param deltaTime Tiempo transcurrido (en milisegundos) desde la invocación
     * anterior (frame anterior).
     */
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        // Para cada bola comprobamos si ha alcanzado la barra
        for (BallObject ball : _ballObjects) {
            // Si ha impactado con la barra
            if ((ball.getY() + _realBallSize) >= (_barObject.getY())) {
                // y = la posicion de la bola mas alta + dist entre bolas
                // asi colocamos la bola que ha impactado por encima de esta
                float y = 0;
                for (BallObject b : _ballObjects) {
                    y = Math.min(y, b.getY());
                }
                y -= _ballSeparation;


                // Si la bola es de color distinto a la barra
                if (_barObject.getColor() != ball.getColor()) {
                    //game over
                    _nextGameState = GameState.GameOver;
                } // Si hemos anotado un punto
                else {
                    // Creamos particulas
                    for (int i = 0; i < _nParticles; i++) {
                        spawnParticle(ball.getColor(), _barObject.getY());
                    }
                    // Actualizamos puntuacion
                    _score++;
                    updateScoreObjects();
                    // Gestionamos el incremento de velocidad
                    _ballCounter++;
                    if (_ballCounter >= _ballsPerIncrease) {
                        _ballSpeed += _ballSpIncrease;
                        _ballCounter = 0;
                    }
                }
                // Reiniciamos la bola
                ball.resetBall(y);
            } // Las bolas que no han impactado avanzan
            else {
                ball.setY(ball.getY() + (float) (_ballSpeed * deltaTime));
            }
        }

        // Llamamos al update de las particulas activas
        for (Particle p : _particles) {
            if (p.isActive()) p.update(deltaTime);
        }
        // comprobacion de input
        checkInput();
    } // update

    /**
     * Renderizado, llama al de BaseLogic
     */
    @Override
    public void render() {
        super.render();
    } // render

    /**
     * Comprueba los eventos en la lista de Input,
     * cualquier pulsacion cambia el color de la barra
     */
    public void checkInput() {
        ArrayList<Input.TouchEvent> events = _input.getTouchEvents();
        for(Input.TouchEvent e : events) {
            switch (e.getEventType()) {
                case PRESSED:
                    _barObject.swapSprite();
                    break;
                default:
                    break;
            }
        }

        // Limpiamos lista de eventos
        _input.clearTouchEvents();

        // Se comprueba si el estado ha cambiado y se lanza el nuevo si es el caso
        // Necesario hacerlo después de haberse procesado el input para evitar fallos
        updateGameState();
    }

    /**
     * Metodo que spawnea una particula con la posicion y color recibidos
     * para esto busca una particula inactiva en la lista, si no hay ninguna
     * crea una nueva
     * @param color Color de la particla
     * @param y Coordenada y de la particula
     */
    private void spawnParticle(Color color, float y){
        boolean found = false;
        // buscamos una particula inactiva
        for(Particle p : _particles){
            if(!p.isActive()) {
                p.Respawn(color, y);
                found = true;
                break;
            }
        }
        // si no la hay creamos una y la añadimos a las listas
        if(!found) {
            Particle aux = new Particle(_ballType, color, _whiteBallSprite, _blackBallSprite, _screenW, y);
            _particles.add(aux);
            _gameObjects.add(aux);
        }
    }

    /**
     * Coloca a todas las bolas en su posicion inicial
     */
    private void repositionBalls(){
        int aux = 0;
        for(BallObject b : _ballObjects) {
            float y = ((-1) * (b.getSprite().getRect()._h *1 )) - (_ballSeparation * aux);
            b.setY(y);
            aux++;
        }
    }
}