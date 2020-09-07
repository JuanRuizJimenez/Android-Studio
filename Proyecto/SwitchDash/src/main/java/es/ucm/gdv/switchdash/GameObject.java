package es.ucm.gdv.switchdash;

import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Rect;
import es.ucm.gdv.engine.Sprite;

/**
 * Encapsula la funcionalidad basica de lso objetos de la logica
 * Contiene un sprite, un transform ademas de un valor alpha y un booleano
 * que indica si esta activo
 */
public class GameObject {

    private Rect _transform;
    private float _alpha = 255;
    private Sprite _sprite;
    private boolean _active = true;

    /**
     * Constructora vacia, Rect con valores 0 y sprite a null
     */
    public GameObject() {
        _transform = new Rect(0,0,0,0);
        _sprite = null;
    }

    /**
     * Constructora con transform, deja sprite a null
     * @param transform Transform del GameObject
     */
    public GameObject(Rect transform){
        _transform = transform;
        _sprite = null;
    }

    /**
     * Constructora con sprite y transform
     *
     * @param sprite Sprite del GameObject
     * @param transform Transform del GameObject
     */
    public GameObject(Sprite sprite, Rect transform) {
        _transform = transform;
        _sprite = sprite;
    }

    /**
     * Constructora con sprite y coordenadas,
     * Construye el transform en base a las coordenadas y
     * las dimensiones del sprite
     *
     * @param sprite Sprite del GameObject
     * @param x Coordenada x
     * @param y Coordenada y
     */
    public GameObject(Sprite sprite, float x, float y){
        _transform = new Rect(x,y,sprite.getRect()._w, sprite.getRect()._h);
        _sprite = sprite;
    }

    /**
     * Constructora que recibe sprite
     * Construye el transform en la posicion 0,0 con
     * las dimensiones del sprite
     * @param sprite Sprite del GameObject
     */
    public GameObject(Sprite sprite){
        _transform = new Rect(0,0,sprite.getRect()._w, sprite.getRect()._h);
        _sprite = sprite;
    }


    /**
     * Devuelve la coordenada x
     * @return Coordenada x
     */
    public float getX() { return  _transform._x; }

    /**
     * Devuelve la coordenada y
     * @return Coordenada y
     */
    public float getY() { return  _transform._y; }

    /**
     * Devuelve el ancho
     * @return Ancho
     */
    public float getW() { return  _transform._w; }

    /**
     * Devuelve la altura
     * @return Altura
     */
    public float getH() { return  _transform._h; }

    /**
     * Devuelve el transform
     * @return Transform del GameObject
     */
    public Rect getTransform() { return _transform; }

    /**
     * Devuelve el sprite
     * @return Sprtie del GameObject
     */
    public Sprite getSprite() { return _sprite; }

    /**
     * Devuelve el alpha, que indica la transparencia
     * @return Valor del alpha
     */
    public float getAlpha() { return _alpha; }

    /**
     * Asigna la coordenada x
     * @param x Coordenada x
     */
    public void setX(float x) { _transform._x = x; }

    /**
     * Asigna la coordenada y
     * @param y Coordenada y
     */
    public void setY(float y) { _transform._y = y; }

    /**
     * Asigna las coordenadas x e y
     * @param x Coordenada x
     * @param y Coordenada y
     */
    public void setCoords(float x, float y) { _transform._x = x; _transform._y = y;}

    /**
     * Asigna la anchura
     * @param w Anchura
     */
    public void setW(float w) { _transform._w = w; }

    /**
     * Asigna la altura
     * @param h Altura
     */
    public void setH(float h) { _transform._h = h; }

    /**
     * Asigna anchura y altura
     * @param w Anchura
     * @param h Altura
     */
    public void setSize(float w, float h) { _transform._w = w; _transform._h = h;}

    /**
     * Asigna el transform del GameObject
     * @param transform Transform
     */
    public void setTransform(Rect transform) { _transform = transform; }

    /**
     * Asigna el sprite del GameObject
     * @param sprite Sprite
     */
    public void setSprite(Sprite sprite) { _sprite = sprite; }

    /**
     * Asigna el valor del alpha (transparencia)
     * @param alpha Transparencia
     */
    public void setAlpha(float alpha){ _alpha = alpha; }

    /**
     * Dibuja el sprite dle GameObject
     * @param graphics Gestor de graficos
     */
    public void drawSprite(Graphics graphics) {if(_sprite!=null)_sprite.draw(graphics, _transform, _alpha);}

    /**
     * Asigna si el GameObject esta activo
     * @param b Estado de actividad
     */
    public void setActive(boolean b){
        _active = b;
    }

    /**
     * Indica si el GameObject esta activo
     * @return Booleano que indica si esta activo
     */
    public boolean isActive(){
        return _active;
    }
}
