package es.ucm.gdv.engine.android;
import android.graphics.Bitmap;

/**
 * Implementa la interfaz “Image” del motor gracias a la funcionalidad
 * que ofrece la clase Bitmap de Android
 *
 * Las dimensiones son en píxeles
 */
public class Image implements es.ucm.gdv.engine.Image {

    /**
     * Imagen de Android, es un mapa de bits
     */
    private Bitmap _image;

    /**
     * Constructora
     *
     * @param image Imagen de Android
     */
    public Image(Bitmap image){
        _image = image;
    }

    /**
     *  Devuelve el ancho de la imagen
     *
     * @return Ancho de la imagen
     */
    @Override
    public int getWidth(){
        return _image.getWidth();
    }

    /**
     * Devuelve el alto de la imagen
     *
     * @return Alto de la imagen
     */
    @Override
    public int getHeight(){
        return _image.getHeight();
    }

    /**
     * Devuelve la imagen de Android
     *
     * @return imagen Android
     */
    public Bitmap getBitMapImage() {
        return _image;
    }
}
