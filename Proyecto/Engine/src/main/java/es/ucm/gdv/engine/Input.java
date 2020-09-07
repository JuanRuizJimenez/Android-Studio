package es.ucm.gdv.engine;

import java.util.ArrayList;

/**
 * Interfaz que contiene lo relativo a gestionar to-do lo relativo al input
 *
 * Cada evento de input se llama TouchEvent y contiene la información básica
 * para poder manejarlo
 *
 * Las coordenadas van en píxeles
 */
public interface Input {

    /**
     * Tipos disponibles de TouchEvent
     */
    enum TouchEventType  {CLICKED, PRESSED, RELEASED, DRAGGED, MOVED, DEFAULT};

    /**
     * Clase que encapsula la lógica de evento como concepto
     */
    class TouchEvent{

        /**
         * Tipo del evento, lo usaremos para diferenciar los eventos entre si
         */
        TouchEventType _type;

        /**
         * Posición X en la que se ha detectado la pulsación del evento
         */
        int _x;

        /**
         * Posición Y en la que se ha detectado la pulsación del evento
         */
        int _y;

        /**
         * Identificador del evento, útil para diferenciar entre sí por ejemplo
         * cada fuente de input a la hora de controlar las pulsaciones
         */
        int _id;

        /**
         * Mensaje extra que se puede añadir a cada evento
         */
        String _message;

        /**
         * Constructora
         *
         * @param g controlador de gráficos, necesario para que el evento pueda procesarse en coords lógicas
         * @param t tipo del evento
         * @param x posición X en la que se ha detectado la pulsación (en coords físcicas)
         * @param y posición Y en la que se ha detectado la pulsación (en coords físcicas)
         * @param id identificador de la fuente
         */
        public TouchEvent(Graphics g, TouchEventType t, int x, int y, int id){

            // Necesario para que funcione según coordenadas logicas
            Rect rect = new Rect(x, y, 0, 0);
            Rect nRect = g.physicToLogic(rect);

            _type = t; _x = (int)nRect._x; _y = (int)nRect._y; _id = id; _message = "";
        };

        /**
         * Constructora
         *
         * @param g controlador de gráficos, necesario para que el evento pueda procesarse en coords lógicas
         * @param t tipo del evento
         * @param x posición X en la que se ha detectado la pulsación (en coords físcicas)
         * @param y posición Y en la que se ha detectado la pulsación (en coords físcicas)
         * @param id identificador de la fuente
         * @param message mensaje del evento
         */
        public TouchEvent(Graphics g, TouchEventType t, int x, int y, int id, String message){

            // Necesario para que funcione según coordenadas logicas
            Rect rect = new Rect(x, y, 0, 0);
            Rect nRect = g.physicToLogic(rect);

            _type = t; _x = (int)nRect._x; _y = (int)nRect._y; _id = id; _message = message;
        };

        /**
         * Devuelve el tipo del evento
         *
         * @return tipo del evento
         */
        public TouchEventType getEventType() { return  _type; }

        /**
         * Devuelve la X en la que se detectó el evento (en coords lógicas)
         *
         * @return X del evento
         */
        public int getEventX() { return  _x; }

        /**
         * Devuelve la Y en la que se detectó el evento (en coords lógicas)
         *
         * @return Y del evento
         */
        public int getEventY() { return  _y; }

        /**
         * Devuelve el id del evento
         *
         * @return id del evento
         */
        public int getEventId() { return  _id; }

        /**
         * Devuelve el mensaje del evento
         *
         * @return mensaje del evento
         */
        public String getEventMessage() { return  _message; }
    }

    /**
     * Devuelve la lista de eventos
     *
     * @return la lista de eventos
     */
    public ArrayList<TouchEvent> getTouchEvents();

    /**
     * Añade un evento a la lista de eventos
     *
     * @param e evento a añadir
     */
    public void setTouchEvent(TouchEvent e);

    /**
     * Limpia la lista de eventos
     */
    public void clearTouchEvents();
}
