package es.ucm.gdv.switchdash;

import es.ucm.gdv.engine.Rect;
import es.ucm.gdv.engine.Sprite;


/**
 * GameObject que contiene las funcionalidades especificas
 * de una bola del juego (color, sprites para blanco y negro etc)
 */
public class BallObject extends  GameObject {

    // Tipo y color de la bola
    private BallType _type;
    private Color _color;
    // Sendos sprites para blanco y negro
    private Sprite _whiteSprite;
    private Sprite _blackSprite;
    // Parcialidad a favor del color anterior al obtener uno aleatorio
    private float _bias = 0.7f;

    /**
     * Constructora
     *
     * @param type Tipo de la bola, que determina su forma
     * @param wSprite Sprite con la imagen de la bola blanca
     * @param bSprite Sprite con la imagen de la bola negra
     * @param transform Transform de la bola
     */
    public BallObject(BallType type, Sprite wSprite, Sprite bSprite, Rect transform) {
        // Asignamos el tipo
        _type = type;

        // Asignamos sendos sprites
        _whiteSprite = wSprite;
        _blackSprite = bSprite;

        // Randomizamos el color inicial de la bola
        setRndSprite();

        // Asignamos transform
        setTransform(transform);
    }

    /**
     * Alterna el color (y sprite) entre negro y blanco
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
     *  Asigna un color y correspondiente sprite aleatorio
     */
    public void setRndSprite() {
        int rnd = (int)(Math.random() * 2.0);

        if (rnd == 0) {
            setSprite(_whiteSprite);
            _color = Color.White;
        }

        else {
            setSprite(_blackSprite);
            _color = Color.Black;
        }
    }

    /**
     * Asigna un color y consecuente sprite de forma aleatoria
     * pero habiendo mas probabilidad de conservar el color anterior
     */
    public void setBiasedRandomSprite() {
        double rnd = Math.random();

        if (rnd > _bias) {
            swapSprite();
        }
    }

    /**
     * Reinicio de la bola, obtiene un color aleatorio con parcialidad
     * y la coloca en la altura posicion y
     * @param y Coordenada y
     */
    public void resetBall(float y) {
        setBiasedRandomSprite();
        setY(y);
    }

    /**
     * Devuelve el color de la bola
     * @return Color de la bola
     */
    public Color getColor(){
        return _color;
    }
}
