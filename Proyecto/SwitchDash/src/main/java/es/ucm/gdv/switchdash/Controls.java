package es.ucm.gdv.switchdash;

import java.util.ArrayList;

import es.ucm.gdv.engine.Game;
import es.ucm.gdv.engine.Input;
import es.ucm.gdv.engine.Rect;
import es.ucm.gdv.engine.Sprite;

/**
 * Clase que extiende BaseLogic e implementa la pantalla que
 * muestra los controles del juego
 */
public class Controls extends BaseLogic{
    // Objetos que contienen las imagenes del estado
    private GameObject _howToPlayObject;
    private GameObject _instructionsObject;
    private GameObject _tapToPlayObject;
    private GameObject _exitObject;

    // Variables relativas a dichos objetos
    private int _howToPlayY = 290;
    private int _instructionsY = 768;
    private int _tapToPlayY = 1464;

    /**
     * Inicializa lo necesario para el funcionamiento del estado, crea y coloca los
     * objetos con las imagenes
     *
     * @param game necesita acceso al controlador del juego
     *
     * @return booleano de control si ha habido algún fallo durante la inicialización
     */
    @Override
    public boolean init(Game game) {
        boolean error = super.init(game);

        try {
             _nextGameState = _gameState = GameState.Controls;

             // Creamos los objetos con el titulo del estado, las instrucciones y tap to play
            _howToPlayObject = new GameObject(new Sprite(_graphics.newImage("Sprites/howToPlay.png")));
            _howToPlayObject.setCoords(_screenW/2 - _howToPlayObject.getW()/2,
                    _howToPlayY);
            _gameObjects.add(_howToPlayObject);

            _instructionsObject = new GameObject(new Sprite(_graphics.newImage("Sprites/instructions.png")));
            _instructionsObject.setCoords(_screenW/2 - _instructionsObject.getW()/2,
                    _instructionsY);
            _gameObjects.add(_instructionsObject);

            _tapToPlayObject = new GameObject(new Sprite(_graphics.newImage("Sprites/tapToPlay.png")));
            _tapToPlayObject.setCoords(_screenW/2 - _tapToPlayObject.getW()/2,
                    _tapToPlayY);
            _gameObjects.add(_tapToPlayObject);

            // Creamos el objeto del boton de salir
            _exitObject = new GameObject(new Sprite(_graphics.newImage("Sprites/buttons.png"),
                    new Rect(ButtonType.Exit.ordinal() *_buttonSize,0,_buttonSize,_buttonSize)));
            _exitObject.setCoords(_screenW - (_buttonSize + _buttonSize / 3), _buttonY);
            _gameObjects.add(_exitObject);
            return error;

        }
        catch (Exception e) {
            // Avisamos del error si lo hay
            System.err.println(e);
            return false;
        }
    }

    /**
     * Actualiza el estado de logica, en concreto se encarga de
     * que la imagen tap to play oscile su transparencia y comprueba
     * el input
     *
     * @param deltaTime tiempo en segundos desde el frame anterior
     */
    @Override
    public void update (double deltaTime) {
        super.update(deltaTime);
        // Oscilamos el alfa de tap to play
        oscilateAlpha(_tapToPlayObject, deltaTime);
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
     * los botones exit y sound
     */
    @Override
    public void checkInput() {
        ArrayList<Input.TouchEvent> events = _input.getTouchEvents();
        for (Input.TouchEvent e : events) {
            switch (e.getEventType()) {
                case PRESSED:
                    // El boton de salir devuelve al menu
                    if (isInBounds(e.getEventX(), e.getEventY(), _exitObject)) {
                        _nextGameState = GameState.Menu;
                    // El boton de sonido alterna entre on y off
                    } else if (isInBounds(e.getEventX(), e.getEventY(), _soundObject)) {
                        switchSound();
                    } // Cualquier otro lugar comienza una partida
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

