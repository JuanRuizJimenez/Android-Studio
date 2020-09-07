package es.ucm.gdv.engine.desktop;

import es.ucm.gdv.engine.Logic;
import javax.swing.JFrame;

/**
 * Implementación del “Game” del motor. De esta manera implementa lo relativo al inicio del juego y
 * del bucle principal, apoyándose en la funcionalidad de la hebra de Swing proporcionada por JFrame.
 */

public class Game implements es.ucm.gdv.engine.Game {

    /**
     * Encapsula las funcionalidades del JFrame de Java
     * Proporciona el soporte de la hebra de Swing
     */
    private JFrame _window;

    /**
     * Controlador de los gráficos
     */
    private Graphics _graphics;

    /**
     * Estado lógico que se tratará en el bucle ppal
     */
    private Logic _logic;

    /**
     * Controlador de las entradas de input
     */
    private Input _input;

    /**
     * Representa el mecanismo con el cual se organiza
     * la memoria en la ventana
     */
    private java.awt.image.BufferStrategy _strategy;

    /**
     * Anchura de la ventana (en pixeles)
     */
    private int _windowWidth;

    /**
     * Altura de la ventana (en píxeles)
     */
    private int _windowHeight;

    /**
     * Constructora
     *
     * @param windowWidth ancho de la ventana
     * @param windowHeight alto de la ventana
     */
    public Game(int windowWidth, int windowHeight){
        _windowWidth = windowWidth;
        _windowHeight = windowHeight;
    }

    /**
     * Añadimos la lógica sobre la cual se trabajará en el bucle principal
     *
     * @param logic estado de lógica a tratar
     */
    @Override
    public void setLogic(Logic logic) { _logic = logic; }

    /**
     * Devuelve el controlador de los gráficos
     *
     * @return graphics
     */
    @Override
    public Graphics getGraphics() {
        return _graphics;
    }

    /**
     * Devuelve el controlador del input
     *
     * @return input
     */
    @Override
    public Input getInput() { return  _input; }

    /**
     * Inicializa lo necesario para correr el juego
     *
     * @param logic lógica a tratar en el bucle principal
     *
     * @return booleano de control si hay error en la inicializacion
     */
    @Override
    public boolean init(Logic logic){

        // Asignamos la lógica
        _logic = logic;

        try {
            // Creamos la ventana con el tamaño correspondiente
            _window = new JFrame();
            _window.setSize(_windowWidth, _windowHeight);
            _window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Creamos el gestor de gráficos
            _graphics = new Graphics(_window);

            // Creamos el gestor de entradas de input y
            // lo añadimos como listener
            _input = new Input(_graphics);
            _window.addMouseListener(_input);

            return true;
        }
        catch (Exception e) {
            // En caso de error avisamos al usuario
            System.err.println(e);

            return false;
        }
    }

    /**
     * Método encargado de gestionar los buffers y cómo se van a manejar en la aplicación
     */
    public void bufferStrategy(){
        // Vamos a usar renderizado activo
        //
        // No queremos que Swing llame al método repaint() porque el repintado es continuo en cualquier caso
        _window.setIgnoreRepaint(true);

        // Hacemos visible la ventana.
        _window.setVisible(true);

        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                _window.createBufferStrategy(2);
                break;
            }
            catch(Exception e) {
            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }
        else {
            // En "modo debug" podríamos querer escribir esto.
            // System.out.println("BufferStrategy tras " + (100 - intentos) + " intentos.");
        }

        // Obtenemos el Buffer Strategy que se supone acaba de crearse.
        _strategy = _window.getBufferStrategy();
    }

    /**
     * Gestiona el bucle principal del juego
     */
    @Override
    public void run() {
        bufferStrategy();

        long lastFrameTime = System.nanoTime();
        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        boolean running = true;
        while(running) {

            // Cálculo de deltatime
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            double elapsedTime = (double) nanoElapsedTime / 1.0E9; //segundos

            // Llamamos al update de la lógica con el deltatime
            _logic.update(elapsedTime);

            //Informe de FPS
            //if (currentTime - informePrevio > 1000000000l) {
            //    long fps = frames * 1000000000l / (currentTime - informePrevio);
            //    System.out.println("" + fps + " fps");
            //    frames = 0;
            //    informePrevio = currentTime;
            //}
            //++frames;

            // Pintamos el frame con el BufferStrategy
            do {
                do {
                    java.awt.Graphics graphics = _strategy.getDrawGraphics();
                    _graphics.setGraphics(graphics);
                    try {
                        // Llamamos al render de la lógica
                        _logic.render();
                    }
                    finally {
                        graphics.dispose();
                    }
                } while(_strategy.contentsRestored());
                _strategy.show();
            } while(_strategy.contentsLost());
        }
    }
}
