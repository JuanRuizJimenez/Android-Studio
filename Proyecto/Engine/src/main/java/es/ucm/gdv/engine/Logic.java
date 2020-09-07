package es.ucm.gdv.engine;

/**
 * Interfaz que encapsula to-do lo relativo a la funcionalidad básica de los estados lógicos,
 * que es su capacidad de iniciarse, actualizarse y pintarse
 */
public interface Logic {

    /**
     * Inicializa to-do lo necesario para el funcionamiento del estado lógico
     *
     * @param game necesita acceso al controlador del juego
     *
     * @return booleano de control si ha habido algún fallo durante la inicialización
     */
    public boolean init(Game game);

    /**
     * Actualiza to-do lo que pueda ser relativo a la lógica
     *
     * @param deltaTime tiempo en segundos desde el frame anterior
     */
    public void update(double deltaTime);

    /**
     * Encargado del renderizado
     */
    public void render();

}
