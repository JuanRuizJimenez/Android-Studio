package es.ucm.gdv.engine.desktop;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import es.ucm.gdv.engine.AbstractInput;

/**
 * Extiende la clase “AbstractInput” del motor. Termina de implementar los métodos de Input a través
 * de las características exclusivas de PC.
 *
 * Se apoya en la funcionalidad de los métodos proporcionados por las clases MouseListener y
 * MouseMotionListener, de las cuales hereda, para crear los TouchEvents propios del motor
 *
 * Las coordenadas van en píxeles
 */
public class Input extends AbstractInput implements MouseListener, MouseMotionListener {

    /**
     * Constructora
     *
     * @param g Graphics de la plataforma (PC), para poder gestionar
     *          las coordenadas de los eventos de fomra lógica
     */
    public Input(Graphics g){
        super(g);
    }

    /**
     * Se llamará a este método cada vez que se detecte un click (pulsación + liberar pulsación)
     *
     * @param mouseEvent evento de ratón de PC
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // Creamos evento
        TouchEvent e = new TouchEvent(_graphics ,TouchEventType.CLICKED, mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getID());
        // Añadimos a la lista de eventos
        _touchEvents.add(e);
    }

    /**
     * Se llamará a este método cada vez que se detecte una pulsación
     *
     * @param mouseEvent evento de ratón de PC
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        // Creamos evento
        TouchEvent e = new TouchEvent(_graphics, TouchEventType.PRESSED, mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getID());
        // Añadimos a la lista de eventos
        _touchEvents.add(e);
    }

    /**
     * Se llamará a este método cada vez que se libere la pulsación de ratón
     *
     * @param mouseEvent evento de ratón de PC
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        // Creamos evento
        TouchEvent e = new TouchEvent(_graphics, TouchEventType.RELEASED, mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getID());
        // Añadimos a la lista de eventos
        _touchEvents.add(e);
    }

    /**
     * Se llamará a este método cada vez que el ratón entre en la ventana
     *
     * @param mouseEvent evento de ratón de PC
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    /**
     * Se llamará a este método cada vez que el ratón salga de la ventana
     *
     * @param mouseEvent evento de ratón de PC
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    /**
     * Se llamará a este método cada vez que el ratón clique y arrastre
     *
     * @param mouseEvent evento de ratón de PC
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        // Creamos evento
        TouchEvent e = new TouchEvent(_graphics, TouchEventType.DRAGGED, mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getID());
        // Añadimos a la lista de eventos
        _touchEvents.add(e);
    }

    /**
     * Se llamará a este método cada vez que el ratón se mueva
     *
     * @param mouseEvent evento de ratón de PC
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        // Creamos evento
        TouchEvent e = new TouchEvent(_graphics, TouchEventType.MOVED, mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getID());
        // Añadimos a la lista de eventos
        _touchEvents.add(e);
    }
}
