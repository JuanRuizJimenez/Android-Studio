package es.ucm.gdv.switchdash;

import es.ucm.gdv.engine.Image;
import es.ucm.gdv.engine.Rect;
import es.ucm.gdv.engine.Sprite;

/**
 * GameObject que contiene las funcionalidades especificas
 * de la barra del juego (color, sprites para blanco y negro, awapeo)
 */
public class BarObject extends  GameObject {

    // Variables relativas al transform de la barra
    private int _barW = 528;
    private int _barH = 192;
    private int _barY = 1200;

    // Color actual de la barra
    private Color _color;

    // Sendos sprites para blanco y negro
    private Sprite _whiteSprite;
    private Sprite _blackSprite;

    /**
     * Constructora
     *
     * @param image Imagen que contiene las barras
     * @param screenW Ancho de la pantalla
     */
    public BarObject(Image image, int screenW) {

        // Creamos sendos sprites con la imagen dada
        _blackSprite = new Sprite(image, new Rect(0, Color.Black.ordinal()*_barH, _barW, _barH));

        _whiteSprite = new Sprite(image, new Rect(0, Color.White.ordinal()*_barH, _barW, _barH));

        // La barra comienza siendo blanca
        _color = Color.White;
        // Asignamos su transform y sprite
        setTransform(new Rect(((screenW / 2) - (_barW / 2)), _barY, _barW, _barH));
        setSprite(_whiteSprite);

    }

    /**
     * Alterna entre sprite blanco y negro
     */
    public void swapSprite(){
        if(_color == _color.Black) {
            _color = Color.White;
            setSprite(_whiteSprite);
        }
        else {
            _color = Color.Black;
            setSprite(_blackSprite);
        }
    }

    /**
     * Devuelve el color actual de la barra
     * @return Color de la barra
     */
    public Color getColor(){
        return _color;
    }
}
