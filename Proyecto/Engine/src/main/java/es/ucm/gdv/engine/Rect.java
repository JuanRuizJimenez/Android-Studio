package es.ucm.gdv.engine;

/**
 * Clase que contiene la funcionalidad de un rectángulo
 * ya que puede ser útil tener esta información encapsulada
 * en distintas ocasiones, como en el manejo de sprites y sus imágenes
 *
 * La coordenada 0,0 del rect será la esquina superior izquierda
 * y las medidas van en píxeles
 */
public class Rect {

    /**
     * Coordenada X del rectángulo
     */
    public float _x;

    /**
     * Coordenada Y del rectángulo
     */
    public float _y;

    /**
     * Anchura del rectángulo
     */
    public float _w;

    /**
     * Altura del rectángulo
     */
    public float _h;

    /**
     * Constructora
     * @param x coordenada X
     * @param y coordenada Y
     * @param w anchura
     * @param h altura
     */
    public Rect(float x, float y, float w, float h){
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }

    /**
     * Devuelve
     *
     * @return
     */
    public float getRight(){
        return _x + _w;
    }
    public float getLeft(){
        return _x;
    }
    public float getBottom(){
        return _y + _h;
    }
    public float getTop(){
        return _y;
    }
}
