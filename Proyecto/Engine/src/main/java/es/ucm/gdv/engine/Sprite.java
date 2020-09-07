package es.ucm.gdv.engine;

/**
 * Clase que encapsula las funcionalidades relativas a los sprites
 *
 * Cada sprite tendrá la información de su imagen y el rect fuente
 * de la imagen a partir de la cual es creado
 */
public class Sprite {

    /**
     * Imagen del sprite
     */
    private Image _img;

    /**
     * Rect fuente de la imagen del que será cargado el sprite
     */
    private Rect _rect;

    /**
     * Constructora
     *
     * @param image imagen del sprite
     * @param rect rectágunlo fuente
     */
    public Sprite(Image image, Rect rect){
        _img = image;
        _rect = rect;
    }

    /**
     * Constructora
     *
     * Si no se especifica el rect fuente se tomará toda la imagen
     *
     * @param image imagen del sprite
     */
    public Sprite(Image image){
        _img = image;
        _rect = new Rect(0,0, image.getWidth(), image.getHeight());
    }

    /**
     * Se encarga de llamar al pintado de la imagen
     *
     * @param g necesario acceso a graphics para poder llamar al pintado
     * @param dest rectágunlo destino donde se pintará el sprite
     * @param alpha transparencia de la imagen del sprite
     */
    public void draw(Graphics g, Rect dest, float alpha){
        g.drawImage(_img, dest, _rect, alpha);
    }

    /**
     * Devuelve el rectángulo fuente del sprite
     *
     * @return rectágulo fuente
     */
    public Rect getRect(){return _rect;};

    /**
     * Asigna el rectágunlo fuente del sprite
     *
     * @param rect rectángulo fuente
     */
    public void setRect(Rect rect){
        _rect = rect;
    }
}
