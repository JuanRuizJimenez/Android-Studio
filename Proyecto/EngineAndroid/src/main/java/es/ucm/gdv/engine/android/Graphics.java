package es.ucm.gdv.engine.android;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;

import es.ucm.gdv.engine.AbstractGraphics;
import es.ucm.gdv.engine.Rect;

/**
 * Extiende la clase “AbstractGraphics” del motor. Termina de implementar los métodos de Graphics
 * a través de las funcionalidades exclusivas de Android. Se apoya en la funcionalidad del Canvas,
 * el SurfaceView y el AssetManager de Android.
 *
 * Todas las medidas van en píxeles
 */
public class Graphics extends AbstractGraphics {

    /**
     * Canvas de Android, controla las llamadas de pintado
     */
    private Canvas _canvas;

    /**
     * Proporciona acceso a los archivos de la aplicación
     */
    private AssetManager _assetManager;

    /**
     * Superficie de dibujado en la pantalla
     */
    private SurfaceView _surfaceView;

    /**
     * Constructora
     *
     * @param assetManager gestor de assets
     * @param surfaceView superficie de pintado
     */
    public Graphics (AssetManager assetManager, SurfaceView surfaceView) {
        _assetManager = assetManager;
        _surfaceView = surfaceView;
    }

    /**
     * Inicializa lo necesario para el funcionamiento del controlador
     * de gráficos
     *
     * @return booleano de control si ha habido algún fallo durante la inicialización
     */
    @Override
    public boolean init() {
        return true;
    }

    /**
     * Constructora de imagenes, crea imágenes cargándolas a partir
     * de una ruta dada
     *
     * @param filename ruta donde se encuentra la imagen y su nombre
     */
    @Override
    public Image newImage(String filename) {
        // Para abrir el stream de carga de imágenes
        InputStream inputStream = null;
        // Intenta cargar la imagen con una ruta dada
        try {
            inputStream = _assetManager.open(filename);
            Bitmap aux = BitmapFactory.decodeStream(inputStream);
            return new Image(aux);
        }
        // En caso de fallo
        catch (IOException e) {
            android.util.Log.e("MainActivity", "Error leyendo el sprite");
            return null;
        }
        // Finalmente, haya pasado lo que haya pasado, cerramos el stream
        finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(Exception e) {
                    // Esto no debería ocurrir nunca... y si ocurre, el
                    // usuario tampoco tiene mucho de qué preocuparse,
                    // ¿para qué molestarle?
                }
            } // if (inputStream != null)
        } // try-catch-finally
    }

    //Draw image lo implementa la clase abstracta

    /**
     * Implementacion que se encarga del pintado exclusivo de la plataforma (Android)
     *
     * @param image imagen a pintar
     * @param dest rectangulo donde se pintara
     * @param source rectangulo fuente
     * @param alpha transparencia de la imagen
     */
    @Override
    public void drawImagePrivate(es.ucm.gdv.engine.Image image, Rect dest, Rect source, float alpha ){
        // el draw image espera una Bitmap no una Image
        // pero podemos hacer downcasting porque sabemos que en este
        // trozo de codigo solo entraremos en Android

        // drawImage de Android
        android.graphics.Rect aSource = new android.graphics.Rect((int)source._x, (int)source._y,
                (int)source._x+(int)source._w, (int)source._y + (int)source._h);

        android.graphics.Rect aDest = new android.graphics.Rect((int)dest._x, (int)dest._y,
                (int)dest._x + (int)dest._w, (int)dest._y + (int)dest._h);

        // Variable paint que contiene el alpha
        Paint paint = new Paint();
        paint.setAlpha((int)alpha);

        // Finalmente pintamos
        _canvas.drawBitmap(((es.ucm.gdv.engine.android.Image)image).getBitMapImage(), aSource, aDest, paint);
    }

    /**
     * Pinta la pantalla al completo con un color dado
     *
     * @param color Color del que se pintara la pantalla
     */
    @Override
    public void clear(int color){
        _canvas.drawColor(color); // ARGB hay que convertir
    }

    /**
     * Devuelve el ancho de la ventana fisica
     *
     * @return Ancho de la ventana
     */
    @Override
    public int getWindowWidth(){
        return _surfaceView.getWidth();
    }

    /**
     * Devuelve el alto de la ventana fisica
     *
     * @return Alto de la ventana
     */
    @Override
    public int getWindowHeight(){
        return _surfaceView.getHeight();
    }

    /**
     * Asigna el canvas
     * @param canvas Canvas de Android
     */
    public void setCanvas(Canvas canvas) {_canvas = canvas;}
}
