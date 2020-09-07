package es.ucm.gdv.engine;

/**
 * Interfaz que contiene los métodos que implementarán
 * la funcionalidad de inicio del juego y el bucle principal
 */
public interface Game {

    /**
     * Añadimos la lógica sobre la cual se trabajará en el bucle principal
     *
     * @param logic estado de lógica a tratar
     */
    void setLogic(Logic logic);

    /**
     * Devuelve el controlador de los gráficos
     *
     * @return graphics
     */
    Graphics getGraphics();

    /**
     * Devuelve el controlador del input
     *
     * @return input
     */
    Input getInput();

    /**
     * Inicializa lo necesario para correr el juego
     *
     * @param logic lógica a tratar en el bucle principal
     *
     * @return booleano de control si hay error en la inicializacion
     */
    boolean init(Logic logic);

    /**
     * Gestiona el bucle principal del juego
     */
    void run();

}
