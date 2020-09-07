package es.ucm.gdv.game.android;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

import es.ucm.gdv.engine.android.Game;
import es.ucm.gdv.switchdash.Menu;

/**
 * Clase que contiene el onCreate, onResume y onPause y que lanzará la aplicación en Android
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Controlador del ciclo de juego y su inicialización
     */
    protected Game _game;

    /**
     * Estado de lógica relativa al menú principal del juego
     */
    protected Menu _menu;

    /**
     * Vista principal de la actividad que gestiona, además, el active
     * rendering
     */
    protected SurfaceView _surfaceView;

    /**
     * Método llamado por Android como parte del ciclo de vida de
     * la actividad. Se llama en el momento de lanzarla
     *
     * @param savedInstanceState Información de estado de la actividad
     *                           previamente serializada por ella misma
     *                           para reconstruirse en el mismo estado
     *                           tras un reinicio. Será null la primera
     *                           vez
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Llamamos a la constructora padre
        super.onCreate(savedInstanceState);

        // Gestor de assets
        AssetManager assetManager = this.getAssets();

        // Preparamos el contenido de la actividad
        _surfaceView = new SurfaceView(this);

        // Creamos el juego y el estado inicial
        _game = new Game(_surfaceView, assetManager);
        _menu = new Menu();

        // Necesario iniciar tanto el Game como la Lógica
        // Si algo falla, nos salimos
        if (!_game.init(_menu))
            return;
        if (!_menu.init(_game))
            return;

        // Añadimos el contenido de la actividad
        setContentView(_surfaceView);

    } // onCreate

    /**
     * Método llamado por Android como parte del ciclo de vida de la
     * actividad. Notifica que la actividad va a pasar a primer plano,
     * estando en la cima de la pila de actividades y completamente
     * visible.
     *
     * Es llamado durante la puesta en marcha de la actividad (algo después
     * de onCreate()) y también después de un periodo de pausa (notificado
     * a través de onPause()).
     */
    @Override
    protected void onResume() {

        // Avisamos a la vista (que es la encargada del active render)
        // de lo que está pasando
        super.onResume();
        _game.resume();

    } // onResume

    /**
     * Método llamado por Android como parte del ciclo de vida de la
     * actividad. Notifica que la actividad ha dejado de ser la de
     * primer plano. Es un indicador de que el usuario está, de alguna
     * forma, abandonando la actividad.
     */
    @Override
    protected void onPause() {

        // Avisamos a la vista (que es la encargada del active render)
        // de lo que está pasando
        super.onPause();
        _game.pause();

    } // onPause
}
