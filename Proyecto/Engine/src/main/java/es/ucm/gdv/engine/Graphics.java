package es.ucm.gdv.engine;

/**
 * Interfaz que contiene los métodos que se encargarán de gestionar to-do el funcionamiento de los gráficos,
 * como la creación de imágenes, su pintado y la gestión de la pantalla, el canvas y sus dimensiones
 *
 * Todas las medidas van en píxeles
 */
public interface Graphics {

    /**
     * Inicializa lo necesario para el funcionamiento del controlador
     * de gráficos
     *
     * @return booleano de control si ha habido algún fallo durante la inicialización
     */
    public boolean init();

    /**
     * Constructora de imagenes, crea imágenes cargándolas a partir
     * de una ruta dada
     *
     * @param fi    lename ruta donde se encuentra la imagen y su nombre
     */
    public Image newImage(String filename);

    /**
     * Dibuja completamente la imagen image en la posicion (x,y)
     * de la pantalla
     *
     * Mapea cada pixel de la imagen en un pixel de pantalla
     *
     * @param image Imagen a dibujar
     * @param dest Posisicion destino en la que dibujar en coordenadas logicas y sus dimensiones
     * @param source Porcion de imagen a dibujar
     * @param alpha Transparencia de la imagen
     */
    public void drawImage(Image image, Rect dest, Rect source, float alpha);

    /**
     * Dibuja completamente la imagen image en la posicion (x,y)
     * de la pantalla
     *
     * Calcula las dimensiones de la imagen en funcion de su rect fuente
     *
     * Mapea cada pixel de la imagen en un pixel de pantalla
     *
     * @param image Imagen a dibujar
     * @param destX Pos x destino en coord logicas
     * @param destY Pos y destino en coord logicas
     * @param source Porcion de imagen a dibujar
     * @param alpha Transparencia de la imagen
     */
    public void drawImage(Image image, float destX, float destY, Rect source, float alpha);

    /**
     * Pinta la pantalla al completo con un color dado
     *
     * @param color Color del que se pintara la pantalla
     */
    public void clear(int color);

    /**
     * Devuelve el ancho de la ventana fisica
     *
     * @return Ancho de la ventana
     */
    public int getWindowWidth();

    /**
     * Devuelve el alto de la ventana fisica
     *
     * @return Alto de la ventana
     */
    public int getWindowHeight();

    /**
     * Establece el tamáño lógico del canvas
     *
     * @param w anchura del canvas
     * @param h altura del canvas
     */
    public void setCanvasSize(int w, int h);

    /**
     * Devuelve la anchura del canvas logico
     *
     * @return anchura lógica del canvas
     */
    public int getCanvasWidth();

    /**
     * Devuelve la altura del canvas logico
     *
     * @return altura lógica del canvas
     */
    public int getCanvasHeight();

    /**
     * Transforma las coordenadas y la anchura de un rect dado
     * cambiando sus valores de lógico a físico
     *
     * @param dest rect destino que se pintará en la pantalla
     *
     * @return rect en coordenadas y dimensiones físicas
     */
    public Rect logicToPhysic(Rect dest);

    /**
     * Transforma las coordenadas y la anchura de un rect dado
     * cambiando sus valores de fisico a lógico
     *
     * Este método está pensado para pensar a la inversa que el anterior
     *
     * Resulta útil por ejemplo cuando queremos saber la posición de un input
     * ya que deberemos procesarla en el juego de manera lógica
     *
     * Lo que hará el método es deshacer los cambios y transformaciones
     * pensados en logicToPhysic
     *
     * @param dest rect que recibimos en función de los parámetros físicos
     *
     * @return rect en coordenadas y dimensiones lógicas
     */
    public Rect physicToLogic(Rect dest);
}
