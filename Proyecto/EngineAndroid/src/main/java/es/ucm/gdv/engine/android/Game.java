package es.ucm.gdv.engine.android;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.view.SurfaceView;

import es.ucm.gdv.engine.Logic;

/**
 * Extensión del “Game” del motor, también hereda de la clase “Runnable” de Android. De esta manera
 * implementa lo relativo al inicio del juego y del bucle principal, además de la funcionalidad
 * de pausar el juego o retomarlo.
 */
public class Game implements es.ucm.gdv.engine.Game, Runnable {

    /**
     * Superficie de dibujado en la pantalla
     */
    private SurfaceView _surfaceView;

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
     * Proporciona acceso a los archivos de la aplicación
     */
    private AssetManager _assetManager;

    /**
     * Objeto Thread que está ejecutando el método run() en una hebra
     * diferente. Cuando se pide a la vista que se detenga el active
     * rendering, se espera a que la hebra termine
     */
    private Thread _renderThread;

    /**
     * Bandera que indica si está o no en marcha la hebra de
     * active rendering, y que se utiliza para sincronización.
     * Es importante que el campo sea volatile
     *
     * Java proporciona un mecanismo integrado para solicitar la
     * detencción de una hebra, aunque por simplicidad nosotros
     * motamos el nuestro propio
     */
    volatile boolean _running = false;

    /**
     * Constructora
     *
     * @param surfaceView superficie de la pantalla
     * @param assetManager gestor de assets, proporciona el acceso a los archivos de la app
     */
    public Game(SurfaceView surfaceView, AssetManager assetManager){
        _surfaceView = surfaceView;
        _assetManager = assetManager;
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
    public Input getInput() {
         return _input;
     }

    /**
     * Inicializa lo necesario para correr el juego
     * en este caso la lógica, el gestor de gráficos y el de input,
     * registrando este último como listener
     *
     * @param logic lógica a tratar en el bucle principal
     *
     * @return booleano de control si hay error en la inicializacion
     */
    @Override
    public boolean init(Logic logic){

        // Asignamos la lógica
        _logic = logic;

        // Creamos el gestor de gráficos
        _graphics = new Graphics(_assetManager, _surfaceView);

        // Creamos el gestor de entradas de input
        // y lo asignamos como listener
        _input = new Input(_graphics);
        _surfaceView.setOnTouchListener(_input);

        return true;
    }

    /**
     * Método llamado para solicitar que se continue con el active rendering
     *
     * El "juego" se vuelve a poner en marcha (o se pone en marcha por primera vez).
     */
    public void resume() {

        if (!_running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva, nunca se sabe quién va a
            // usarnos)
            _running = true;

            // Lanzamos la ejecución de nuestro método run()
            // en una hebra nueva
            _renderThread = new Thread(this);
            _renderThread.start();
        }

    } // resume

    /**
     * Método llamado cuando el active rendering debe ser detenido
     *
     * Puede tardar un pequeño instante en volver, porque espera a que
     * se termine de generar el frame en curso
     *
     * Se hace así intencionadamente, para bloquear la hebra de UI
     * temporalmente y evitar potenciales situaciones de carrera (como
     * por ejemplo que Android llame a resume() antes de que el último
     * frame haya terminado de generarse)
     */
    public void pause() {

        if (_running) {
            _running = false;
            while (true) {
                try {
                    _renderThread.join();
                    _renderThread = null;
                    break;
                }
                catch (InterruptedException ie) {
                    // Esto no debería ocurrir nunca
                }
            } // while(true)
        } // if (_running)

    } // pause

    /**
     * Método que implementa el bucle principal del "juego" y que será
     * ejecutado en otra hebra. Aunque sea público, NO debe ser llamado
     * desde el exterior
     */
    @Override
    public void run() {
        if (_renderThread != Thread.currentThread()) {
            // ¿¿Quién es el tuercebotas que está llamando al
            // run() directamente?? Programación defensiva
            // otra vez, con excepción, por merluzo
            throw new RuntimeException("run() should not be called directly");
        }

        // Antes de saltar a la simulación, confirmamos que tenemos
        // un tamaño mayor que 0. Si la hebra se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada
        while(_running && _surfaceView.getWidth() == 0)
            // Espera activa
            ;

        long lastFrameTime = System.nanoTime();

        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        // Bucle principal
        while(_running) {

            // Cálculo del deltatime en segundos
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            double elapsedTime = (double) nanoElapsedTime / 1.0E9; //segundos

            // Llamamos a la lógica con el deltatime
            _logic.update(elapsedTime);

            //Informe de FPS
            //if (currentTime - informePrevio > 1000000000l) {
            //    long fps = frames * 1000000000l / (currentTime - informePrevio);
            //    System.out.println("" + fps + " fps");
            //    frames = 0;
            //    informePrevio = currentTime;
            //}
            //++frames;

            // Pintamos el frame
            while (!_surfaceView.getHolder().getSurface().isValid())
                ;

            // Antes de pintar bloqueamos el canvas, después de pintar lo liberamos
            Canvas canvas = _surfaceView.getHolder().lockHardwareCanvas();
            _graphics.setCanvas(canvas);
            // Llamamos al render de la lógica
            _logic.render();
            _surfaceView.getHolder().unlockCanvasAndPost(canvas);

        } // while

    } // run
}
