package es.ucm.gdv.engine.desktop;

/**
 * Implementa la interfaz “Image” del motor gracias a la funcionalidad
 * que ofrece la clase Image (AWT) de Java
 *
 * Las dimensiones son en píxeles
 */
public class Image implements es.ucm.gdv.engine.Image {

    /**
     * Imagen de PC
     */
    private java.awt.Image _image;

    /**
     * Constructora
     *
     * @param image Imagen de PC
     */
    public Image(java.awt.Image image){
        _image = image;
    }

    /**
     *  Devuelve el ancho de la imagen
     *
     * @return Ancho de la imagen
     */
    @Override
    public int getWidth(){
        return _image.getWidth(null);
    }

    /**
     * Devuelve el alto de la imagen
     *
     * @return Alto de la imagen
     */
    @Override
    public int getHeight(){
        return _image.getHeight(null);
    }

    /**
     * Devuelve la imagen de Java
     *
     * @return imagen Java AWT
     */
    public java.awt.Image getAWTImage() {
        return _image;
    }
}
