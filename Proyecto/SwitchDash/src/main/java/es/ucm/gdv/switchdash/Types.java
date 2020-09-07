package es.ucm.gdv.switchdash;

// Fichero que contiene enums utilizados en la logica del juego

/**
 * Tipo que indica la forma de las bolas y particulas
 */
enum BallType {Circle, Rectangle, Triangle, Hexagon, DoubleTriangle, Gout, Star, Cloud, Diamond, Special}

/**
 * Tipo que diferencia los distintos sprites de boton disponibles
 */
enum ButtonType {Controls, Exit, SoundOn, SoundOff, Home, Star, Dollar, Settings, Share, Shop}

/**
 * Tipo que diferencia los distintos estados logicos del juego
 */
enum GameState {Menu, Controls, Demo, GameOver}

/**
 * Tipo que indica el color de varios objetos en el juego
 */
enum Color {White, Black}