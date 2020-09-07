package es.ucm.gdv.engine.android;

import android.view.MotionEvent;
import android.view.View;

import es.ucm.gdv.engine.AbstractInput;

/**
 * Extiende la clase “AbstractInput” del motor. Termina de implementar los métodos de Input a través
 * de las funcionalidades exclusivas de Android.
 * Al implementarse como TouchListener se apoya en la funcionalidad del método onTouch() de Android
 * para crear los TouchEvents propios del motor.
 *
 * Las coordenadas van en píxeles
 */
public class Input extends AbstractInput implements View.OnTouchListener {

    /**
     * Constructora
     *
     * @param g Graphics de la plataforma (Android), para poder gestionar
     *          las coordenadas de los eventos de fomra lógica
     */
    public Input(Graphics g){
        super(g);
    }

    /**
     * Entramos aquí cada vez que se detecte una pulsación en pantalla
     *
     * @param view Área de la pantalla
     * @param motionEvent Evento capturado
     * @return booleano de control
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        // Calculamos las coordenadas (físicas) donde se ha captado la pulsación
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        // Dependiendo de cada caso creamos el evento de un tipo u otro
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Creamos evento
                TouchEvent e1 = new TouchEvent(_graphics, TouchEventType.PRESSED, (int)x, (int)y, motionEvent.getDeviceId());
                // Añadimos a la lista de eventos
                _touchEvents.add(e1);
                break;
            case MotionEvent.ACTION_UP:
                // Creamos evento
                TouchEvent e2 = new TouchEvent(_graphics, TouchEventType.RELEASED, (int)x, (int)y, motionEvent.getDeviceId());
                // Añadimos a la lista de eventos
                _touchEvents.add(e2);
                break;
            case MotionEvent.ACTION_MOVE:
                // Creamos evento
                TouchEvent e3 = new TouchEvent(_graphics, TouchEventType.MOVED, (int)x, (int)y, motionEvent.getDeviceId());
                // Añadimos a la lista de eventos
                _touchEvents.add(e3);
                break;
            default:
                // Do nothing.
        }
        return true;
    }
}
