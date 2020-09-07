package es.ucm.gdv.engine.desktop;

import javax.swing.JFrame;
import java.awt.*;

import es.ucm.gdv.engine.AbstractGraphics;
import es.ucm.gdv.engine.Rect;

/**
 * Extiende la clase “AbstractGraphics” del motor. Termina de implementar los métodos de Graphics
 * a través de las funcionalidades exclusivas de PC.
 *
 * Se apoya en la funcionalidad del JFrame (para la funcionalidad de ventana) y el Graphics (AWT) de Java
 *
 * Todas las medidas van en píxeles
 */
public class Graphics extends AbstractGraphics {

    /**
     * Encapsula las funcionalidades del JFrame de Java
     * Proporciona el soporte de la hebra de Swing
     */
    private JFrame _window;

    /**
     * Controlador de los gráficos
     */
    private java.awt.Graphics _graphics;

    /**
     * Constructora
     *
     * @param window la ventana del juego con la funcionalidad de JFrame
     */
    public Graphics(JFrame window){
        _window = window;
    }

    /**
     * Asignamos el gestor de gráficos
     *
     * @param g gestor de gráficos (java.awt)
     */
    public void setGraphics(java.awt.Graphics g) {
        _graphics = g;
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
    public Image newImage(String filename){
        try {
            // Intentamos cargar la imagen a partir de la ruta recibida
            java.awt.Image aux = javax.imageio.ImageIO.read(new java.io.File(filename));
            return new Image(aux);
        }
        catch (Exception e) {
            // En caso de error avisamos al usuario
            System.err.println(e);
            return null;
        }
    }

    /**
     * Implementacion que se encarga del pintado exclusivo de cada plataforma (PC)
     *
     * @param image imagen a pintar
     * @param dest rectangulo donde se pintara
     * @param source rectangulo fuente
     * @param alpha transparencia de la imagen
     */
    @Override
    public void drawImagePrivate(es.ucm.gdv.engine.Image image, Rect dest, Rect source, float alpha ){
        // el draw image espera una java.awt.Image no una Image
        // pero podemos hacer downcasting porque sabemos que en este
        // trozo de codigo solo entraremos en el PC

        // El alpha toma valores entre 0 y 1, por lo que hay que hacer conversion, comprobamos que no se salga
        float dkAlpha = alpha/255.0f;
        if(dkAlpha < 0) dkAlpha = 0;
        else if (dkAlpha > 1) dkAlpha = 1;

        // Seteamos alpha composite en graphics2D para procesar el alpha
        Graphics2D aux = (Graphics2D)_graphics;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,dkAlpha);
        aux.setComposite(ac);

        // drawImage de Java
        _graphics.drawImage(((es.ucm.gdv.engine.desktop.Image)image).getAWTImage(),
                (int)dest._x, (int)dest._y, (int)dest.getRight() , (int)dest.getBottom(), (int)source._x,
                (int)source._y, (int)source.getRight(), (int)source.getBottom(), null);
    }

    /**
     * Le proporciona al contexto del graphics un color dado
     *
     * @param color color del graphics
     */
    public void setColor(int color) {
        Color colorAWT = new Color(color);
        _graphics.setColor(colorAWT);
    }

    /**
     * Pinta la pantalla al completo con un color dado
     *
     * @param color Color del que se pintara la pantalla
     */
    @Override
    public void clear(int color){
        setColor(color);
        _graphics.fillRect(0, 0, getWindowWidth(), getWindowHeight());
    }

    /**
     * Devuelve el ancho de la ventana fisica
     *
     * @return Ancho de la ventana
     */
    @Override
    public int getWindowWidth(){
        return _window.getWidth();
    }

    /**
     * Devuelve el alto de la ventana fisica
     *
     * @return Alto de la ventana
     */
    @Override
    public int getWindowHeight(){
        return _window.getHeight();
    }
}
