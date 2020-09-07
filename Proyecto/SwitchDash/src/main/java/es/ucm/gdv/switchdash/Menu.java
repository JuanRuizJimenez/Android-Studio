package es.ucm.gdv.switchdash;

import java.util.ArrayList;

import es.ucm.gdv.engine.Game;
import es.ucm.gdv.engine.Input;
import es.ucm.gdv.engine.Sprite;

/**
 * Clase que extiende BaseLogic e implementa la pantalla del menu
 * principal del juego
 */
public class Menu extends BaseLogic {

    // Objetos que contienen imagens del menu
    private GameObject _logoObject;
    private GameObject _tapToPlayObject;

    // Variables relativas a dichos objetos
    private int _logoY = 356;
    private int _tapToPlayY = 950;

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
        // Al entrar al menu, el color de fondo cambia aleatoriamente
        _backgroundColorIndex = (int)(Math.random()*8);
        boolean error = super.init(game);

        try {
            _nextGameState = _gameState = GameState.Menu;

            //Creamos los objetos con las imagenes del logo y tap to play
            _logoObject = new GameObject(new Sprite(_graphics.newImage("Sprites/switchDashLogo.png")));
            _logoObject.setCoords(_graphics.getCanvasWidth()/2 - _logoObject.getW()/2, _logoY);
            _gameObjects.add(_logoObject);

            _tapToPlayObject = new GameObject(new Sprite(_graphics.newImage("Sprites/tapToPlay.png")));
            _tapToPlayObject.setCoords(_graphics.getCanvasWidth()/2 - _tapToPlayObject.getW()/2, _tapToPlayY);
            _gameObjects.add(_tapToPlayObject);

            // Activamos el logo de sonido y controles
            _soundObject.setActive(true);
            _controlsObject.setActive(true);

            return error;
        }
        catch (Exception e) {
            // Avisa si hay un error
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
     * los botones de controles  y sound
     */
    @Override
    public void checkInput() {

        ArrayList<Input.TouchEvent> events = _input.getTouchEvents();
        for(Input.TouchEvent e : events) {
            switch (e.getEventType()) {
                // Comprobamos las pulsaciones de pantalla, al pulsar el boton de controles cambiamos
                // el estado a controles, el de sonido alterna entre on y off
                case PRESSED:
                    if (isInBounds(e.getEventX(), e.getEventY(), _controlsObject)) {
                        _nextGameState = GameState.Controls;
                    } else if (isInBounds(e.getEventX(), e.getEventY(), _soundObject)) {
                        switchSound();
                    } // Pulsar en cualquier otro punto de la pantalla comienza una partida
                    else _nextGameState = GameState.Demo;
                    break;

                default:
                    break;
            }
        }
        // Limpiamos la lista de eventos
        _input.clearTouchEvents();

        // Se comprueba si el estado ha cambiado y se lanza el nuevo si es el caso
        // Necesario hacerlo después de haberse procesado el input para evitar fallos
        updateGameState();
    }
}
