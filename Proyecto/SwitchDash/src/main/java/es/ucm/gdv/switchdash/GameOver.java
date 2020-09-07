package es.ucm.gdv.switchdash;

import java.util.ArrayList;

import es.ucm.gdv.engine.Game;
import es.ucm.gdv.engine.Input;
import es.ucm.gdv.engine.Rect;
import es.ucm.gdv.engine.Sprite;

/**
 * Clase que extiende BaseLogic e implementa la pantalla de gameOver
 * que muestra la puntuacion obtenida e invita a jugar otra partida
 */
public class GameOver extends BaseLogic {

    // String con el texto que se muestra bajo la puntuacoin
    private String _pointsString = "POINTS";
    // Array con lso objetos que contienen cada letra del texto
    private ArrayList<GameObject> _pointTextObjects;

    // Objetos que contienen las imagenes del estado
    private GameObject _gameOverObject;
    private GameObject _playAgainObject;

    // Variables relativas a dichos objetos
    private int _gameOverY = 364;
    private int _playAgainY = 1396;

    /**
     * Inicializa lo necesario para el funcionamiento del estado, crea y coloca los
     * objetos con las imagenes, ademas de lo relativo a la puntuacion y su texto
     *
     * @param game necesita acceso al controlador del juego
     *
     * @return booleano de control si ha habido algún fallo durante la inicialización
     */
    @Override
    public boolean init(Game game) {
        boolean error = super.init(game);

        try {
            _nextGameState = _gameState = GameState.GameOver;
            // Numero de digitos actual de la puntuacion (para pintarla centrada)
            int _currentScoreDigits = String.valueOf(_score).length();

            // Inicializamos lista para el texto de debajo de los puntos y los creamos
            _pointTextObjects = new ArrayList<GameObject>();
            for(int i = 0; i < _pointsString.length(); i++) {
                GameObject aux = new GameObject(new Sprite(_graphics.newImage("Sprites/scoreFont.png"),
                            getLetterRect(_pointsString.charAt(i))));
                _pointTextObjects.add(aux);
                _gameObjects.add(aux);
            }

            // Creamos imagen de GameOver y volver a jugar
            _gameOverObject = new GameObject(new Sprite(_graphics.newImage("Sprites/gameOver.png")));
            _gameOverObject.setCoords(_screenW/2 - _gameOverObject.getW()/2,
                    _gameOverY);
            _gameObjects.add(_gameOverObject);

            _playAgainObject = new GameObject(new Sprite(_graphics.newImage("Sprites/playAgain.png")));
            _playAgainObject.setCoords(_screenW/2 - _playAgainObject.getW()/2,
                    _playAgainY);
            _gameObjects.add(_playAgainObject);

            // Colocamos los objetos que pintan la puntuacion
            for(int i = 0; i < _currentScoreDigits; i++){
                float aux = (float)_currentScoreDigits/2.0f - (i+1);
                _scoreObjects.get(i).setCoords(_screenW/2 + _realFontWidth*(aux),
                        _screenH/2 - _realFontHeight);
            }

            // Colocamos los objeto de debajo de la puntuacion
            for(int i = 0; i < _pointTextObjects.size(); i++) {
                float aux = (float) _pointTextObjects.size() / 2.0f - i;
                _pointTextObjects.get(i).setTransform(new Rect(_screenW / 2 - _realFontWidth/2 * (aux),
                        _screenH / 2 , _realFontWidth / 2, _realFontHeight / 2));
            }
            // Actualizamos los sprites de la puntuacion
            updateScoreObjects();
            // Activamos botones de sonido y controles
            _soundObject.setActive(true);
            _controlsObject.setActive(true);

            return error;
        }
        catch (Exception e) {
            // Avisamos de error si lo hubiera
            System.err.println(e);
            return false;
        }
    }
    /**
     * Actualiza el estado de logica, en concreto se encarga de
     * que la imagen play again oscile su transparencia y comprueba
     * el input
     *
     * @param deltaTime tiempo en segundos desde el frame anterior
     */
    @Override
    public void update (double deltaTime) {
        super.update(deltaTime);
        // Oscilamos el alfa del objeto volver a jugar
        oscilateAlpha(_playAgainObject, deltaTime);
        checkInput();
    }

    /**
     * Renderizado, llama al de BaseLogic
     */
    @Override
    public void render () {
        super.render();
    }

    /**
     * Comprueba los eventos en la lista de Input, en concreto
     * los botones de controles y sound
     */
    @Override
    public void checkInput() {
        ArrayList<Input.TouchEvent> events = _input.getTouchEvents();
        for (Input.TouchEvent e : events) {
            switch (e.getEventType()) {
                case PRESSED:
                    // El boton de controles lleva al estado controles
                    if (isInBounds(e.getEventX(), e.getEventY(), _controlsObject)) {
                        _nextGameState = GameState.Controls;
                    }  // El boton de sonido alterna entre on y off
                    else if (isInBounds(e.getEventX(), e.getEventY(), _soundObject)) {
                        switchSound();
                    } // Pulsar en cualquier otro lugar comienza una partida
                    else _nextGameState = GameState.Demo;
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
}
