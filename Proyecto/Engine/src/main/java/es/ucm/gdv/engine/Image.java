package es.ucm.gdv.engine;

/**
 * Interfaz que se encarga de gestionar to-do lo relativo a las imágenes
 *
 * Las dimensiones son en píxeles
 */
public interface Image {
    /**
     *  Devuelve el ancho de la imagen
     *
     * @return Ancho de la imagen
     */
    public int getWidth();

    /**
     * Devuelve el alto de la imagen
     *
     * @return Alto de la imagen
     */
    public int getHeight();

}
