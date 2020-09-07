package es.ucm.gdv.engine;

import java.util.ArrayList;

/**
 * Clase que encapsula las funcionalidades básicas de la trata
 * de eventos pues es común a todas las plataformas
 *
 * Las coordenadas son en píxeles
 */
public abstract class AbstractInput implements Input {

    /**
     * Lista que contiene todos los eventos registrados en
     * una vuelta del bucle
     */
    protected ArrayList<TouchEvent> _touchEvents;

    /**
     * Necesitamos tener acceso al Graphics debido a su funcionalidad
     * de transformar coordenadas físicas a lógicas
     *
     * Lo usaremos ya que para que tenga coherencia con el resto del juego
     * y del motor, las posiciones de los inputs se almacenarán en coordenadas
     * lógicas
     */
    protected  Graphics _graphics;

    /**
     * Constructora
     *
     * @param g Graphics de la plataform
     */
    public AbstractInput(Graphics g) {
        _graphics = g;
        _touchEvents = new ArrayList<TouchEvent>();
    }

    /**
     * Devuelve la lista de eventos
     *Necesario que sea synchronized para evitar problemas a la hora de acceder a la lista
     *
     * @return la lista de eventos
     */
    @Override
    synchronized public ArrayList<TouchEvent> getTouchEvents() {
        return _touchEvents;
    }

    /**
     * Añade un evento a la lista de eventos
     * Necesario que sea synchronized para evitar problemas a la hora de acceder a la lista
     *
     * @param e evento a añadir
     */
    @Override
    synchronized public void setTouchEvent(TouchEvent e) {
        _touchEvents.add(e);
    }

    /**
     * Limpia la lista de eventos
     * Necesario que sea synchronized para evitar problemas a la hora de acceder a la lista
     */
    @Override
    synchronized public void clearTouchEvents() {
        _touchEvents.clear();
    }
}
