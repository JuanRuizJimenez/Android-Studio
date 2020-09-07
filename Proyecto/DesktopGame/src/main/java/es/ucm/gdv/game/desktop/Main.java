package es.ucm.gdv.game.desktop;

import es.ucm.gdv.engine.desktop.Game;
import es.ucm.gdv.switchdash.Menu;

/**
 * Clase que contiene el main y que lanzará la aplicación en PC
 */
public class Main {

    public static void main(String[] args){

        // Creamos el juego y el estado inicial
        Game game = new Game(540, 960);
        Menu menu = new Menu();

        // Necesario iniciar el Game y la Lógica
        // Si algo falla, nos salimos
        if (!game.init(menu))
            return;
        if (!menu.init(game))
            return;

        // Lanzamos el bucle principal del juego
        game.run();
    }
}
