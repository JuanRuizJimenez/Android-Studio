package es.ucm.gdv.switchdash;

import es.ucm.gdv.engine.Sprite;

/**
 * GameObject que contiene las funcionalidades especificas
 * de una particula juego (color, sprites para blanco y negro,
 * ciclo de vida, velocidad y gravedad, transparencia etc)
 */
public class Particle extends GameObject{

    // Variables relativas al tamaño
    private int _particleSize = 128;
    private int _minSize = 30;
    private int _maxSize = 100;
    private int _randomSize = 0;

    // Centro de la pantalla
    private int _screenCenter = 0;

    // Variables relativas a la velocidad
    private float _minSpeedX = 0;
    private float _maxSpeedX = 280;
    private float _minSpeedY = 200;
    private float _maxSpeedY = 400;
    private float _velX = 0;
    private float _velY = 0;

    // Variables relativa al ciclo de vida
    private float _minDuration = 0.6f;
    private float _maxDuration = 1.2f;
    private float _duration = 0;
    private float _timer = 0;

    // Valor de la gravedad que afecta a la particula
    private float _gravity = 500;

    // Variables relativa a la transparencia
    private float _alphaDecr = 0;
    private float _maxAlpha = 255;

    // Color actual de la particula
    private Color _color;

    // Sendos sprites para blanco y negro
    private Sprite _whiteSprite;
    private Sprite _blackSprite;

    /**
     * Constructora
     *
     * @param type Tipo que determina la forma de la particula
     * @param color Color de la particula (blanco o negro)
     * @param wSprite Sprite con la imagen de la particula blanca
     * @param bSprite Sprite con la imagen de la particula negra
     * @param screenW Ancho de la pantalla
     * @param y Coordenada y en la que se crea la particula
     */
    public Particle(BallType type, Color color, Sprite wSprite, Sprite bSprite, int screenW, float y) {
        // En un principio se crean inactivas
        setActive(false);

        // Asignamos color y sprites
        _color = color;
        _whiteSprite = wSprite;
        _blackSprite = bSprite;

        // Calculamos centro de la pantalla
        _screenCenter = (screenW / 2);

        // Asignamos el sprite correspondiente al color
        updateSprite();

        // Llamamos a respawn con el color y la posicion pasados
        Respawn(color, y);
    }

    /**
     * Asignamos un tamaño aleatorio entre valores minimo y maximo
     */
    public void RandomizeSize(){
        _randomSize = (int)(Math.random()* (_maxSize-_minSize)) + _minSize;
        setSize(_randomSize, _randomSize);
    }

    /**
     * Asignamos una velocidad aleatoria para cada coordenada con sus limites correspondientes
     * En el eje X la direccion es aleatoria, mientras que en el Y siempre es hacia arriba
     */
    public void RandomizeSpeed(){
        _velX = (float)((Math.random()*(_maxSpeedX -_minSpeedX)) + _minSpeedX);
        _velX *= (Math.random() > 0.5f) ? -1 : 1;
        _velY = -(float)((Math.random()*(_maxSpeedY -_minSpeedY)) + _minSpeedY);
    }

    /**
     *  Asignamos una duracion aleatoria a la particula dentro de los limites
     *  y calculamos el decremento de alpha
     */
    public void RandomizeDuration(){
        _duration = (float)(Math.random()*(_maxDuration -_minDuration) + _minDuration);
        _alphaDecr = _maxAlpha/_duration;
    }

    /**
     * Metodo que reinicia la particula para un nuevo uso.
     * Asigna el color y la coordenada y pasados como parametro, coloca valores
     * aleatorios a sus caracteristicas y reinicia su ciclo de vida
     * @param color Color de la particula
     * @param y Coordenada y en que se coloca
     */
    public void Respawn(Color color, float y){
        setColor(color);
        RandomizeSize();
        RandomizeSpeed();
        RandomizeDuration();
        setCoords(_screenCenter - getW()/2, y);
        _timer = 0;
        setAlpha(_maxAlpha);
        setActive(true);
    }

    /**
     * Asignamos el color pasado y actualizamos el sprite
     * @param color Color de la particula
     */
    public void setColor(Color color){
        _color = color;
        updateSprite();
    }

    /**
     * Actualizamos el sprite de la particula para que se corresponda con el color
     */
    private void updateSprite(){
        if(_color == _color.Black) {
            setSprite(_blackSprite);
        }
        else {
            setSprite(_whiteSprite);
        }
    }

    /**
     * Update de la particula, que actualiza su estado (posicoin, ciclo de vida, alfa)
     * @param deltatime Tiempo transcurrido desde el ultimo frame
     */
    public void update(double deltatime){

        // Desplazamos la particula de acuerdo a la velocidad
        setX(getX() + (float)(_velX * deltatime));
        setY(getY() + (float)(_velY * deltatime));

        // La velocidad en el eje Y disminuye debido a la gravedad
        _velY+= (float)(_gravity*deltatime);

        // Aumentamos la transparencia de la particula
        setAlpha(getAlpha()-(float)(_alphaDecr*deltatime));

        // Aumentamos el timer, si alcanza su duracion, desactivamos la particula
        _timer+=deltatime;
        if(_timer >= _duration) {
            setActive(false);
        }
    }
}
