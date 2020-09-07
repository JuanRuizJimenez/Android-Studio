package es.ucm.gdv.engine;

/**
 * Clase que encapsula el reescalado lógico y físico pues
 * es común a todas las plataformas
 *
 * Todas las medidas van en píxeles
 */
public abstract class AbstractGraphics implements Graphics {

    /**
     * Tamaño logico del canvas del juego
     */
    private int logic_width = 1080;
    private int logic_height = 1920;

    /**
     * Tamaño real del canvas del juego
     */
    private int canvasW = 0;
    private int canvasH = 0;

    /**
     * Tamaño de la ventana de la aplicacion
     */
    private int winW = 0;
    private int winH = 0;

    /**
     * Ratio logico de la pantalla
     */
    private  double _ratio = ((double)logic_width / (double)logic_height);

    /**
     * Dibuja la imagen con el alpha dado
     *
     * Calcula el rect destino a partir de las dimensiones de la imagen que le pasas
     * y llama al drawImage con dicho rect
     *
     * @param image Imagen a dibujar
     * @param destX Pos x en coord logicas
     * @param destY Pos y en coord logicas
     * @param source Porcion de imagen a dibujar
     * @param alpha Transparencia de la imagen
     */
    @Override
    public void drawImage(Image image, float destX, float destY, Rect source, float alpha) {
        Rect dest = new Rect(destX, destY, source._w, source._h);
        drawImage(image, dest, source, alpha);
    }

    /**
     * Dibuja la imagen con el alpha dado
     *
     * Antes de llamar al drawImage exclusivo de la plataforma realiza la transformación
     * de coordenadas lógicas a físicas
     *
     * @param image Imagen a dibujar
     * @param dest Posisicion en la que dibujar en coordenadas logicas
     * @param source Porcion de imagen a dibujar
     * @param alpha Transparencia de la imagen
     */
    @Override
    public void drawImage(Image image, Rect dest, Rect source, float alpha){

        // Transformamos las coordenadas lógicas a físicas
        Rect newDest = logicToPhysic(dest);

        // Por ultimo llamamos al exclusivo de la plataforma
        drawImagePrivate(image, newDest, source, alpha);
    }


    /**
     * Establece el tamáño lógico del canvas
     *
     * @param w anchura del canvas
     * @param h altura del canvas
     */
    @Override
    public void setCanvasSize(int w, int h) {
        logic_width = w;
        logic_height = h;
    }

    /**
     * Devuelve la anchura del canvas logico
     *
     * @return anchura lógica del canvas
     */
    @Override
    public int getCanvasWidth() {
        return logic_width;
    }

    /**
     * Devuelve la altura del canvas logico
     *
     * @return altura lógica del canvas
     */
    @Override
    public int getCanvasHeight() {
        return logic_height;
    }

    /**
     * Transforma las coordenadas y la anchura de un rect dado
     * cambiando sus valores de lógico a físico
     *
     * @param dest rect destino que se pintará en la pantalla
     *
     * @return rect en coordenadas y dimensiones físicas
     */
    @Override
    public Rect logicToPhysic(Rect dest) {

        // Calculamos el tamaño de la ventana
        calcWindowSize();

        // Calculamos el tamaño real de nuestro canvas de juego
        calcRealCanvasSize();

        // Anchura lógica
        float imgWidth = dest._w;
        float imgHeight = dest._h;

        // Calculamos el tamaño que va a tener la imagen en la pantalla
        // Multiplicamos el alto y el ancho por el aspect ratio
        float newImgWidth, newImgHeight;
        newImgWidth = (imgWidth * (canvasW / (float) logic_width));
        newImgHeight = (imgHeight * (canvasH / (float)logic_height));

        // Posición lógica
        float posX = dest._x;
        float posY = dest._y;

        // El offset es necesario para pintar el canvas y el contenido centrado
        float offsetX = ((winW - canvasW) / 2.0f);
        float offsetY = ((winH - canvasH) / 2.0f);

        // Calculamos la posicion de la img en pantalla
        // Multiplicamos la x,y por el aspect ratio
        float newPosX, newPosY;
        newPosX = (posX * canvasW / (float) logic_width);
        newPosX += offsetX;
        newPosY = (posY * canvasH / (float) logic_height);
        newPosY += offsetY;

        // Calculamos el rect final, con la posición y dimensión fisicas
        Rect newDest = new Rect(newPosX, newPosY, newImgWidth, newImgHeight);

        return newDest;
    }

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
    @Override
    public Rect physicToLogic(Rect dest) {

        // Calculamos el tamaño de la ventana
        calcWindowSize();

        // Calculamos el tamaño real de nuestro canvas de juego
        calcRealCanvasSize();

        // Para deshacer el reescalado de la img y calcular el logico
        float imgWidth = dest._w;
        float imgHeight = dest._h;

        float oldImgWidth, oldImgHeight;
        oldImgWidth = (imgWidth / (canvasW / (float) logic_width));
        oldImgHeight = (imgHeight / (canvasH / (float)logic_height));

        // Para deshacer el centrado en la pantalla y calcular la posicion logica
        float posX = dest._x;
        float posY = dest._y;

        float offsetX = ((winW - canvasW) / 2.0f);
        float offsetY = ((winH - canvasH) / 2.0f);

        // Importante restar antes el Offset que la transformación
        float oldPosX, oldPosY;
        posX -= offsetX;
        oldPosX = (posX * (float) logic_width) / canvasW;
        posY -= offsetY;
        oldPosY = (posY * (float) logic_height / canvasH);

        // Calculamos el rect final, con la posición y dimensión fisicas
        Rect oldDest = new Rect(oldPosX, oldPosY, oldImgWidth, oldImgHeight);

        return oldDest;
    }

    /**
     * Calcula el tamaño de la ventana
     */
    public void calcWindowSize() {
        winW = getWindowWidth();
        winH = getWindowHeight();
    }

    /**
     * Calcula el tamaño real del canvas del juego
     */
    public void calcRealCanvasSize() {
        // Buscamos el maximo tamaño posible del canvas
        // a lo ancho o a lo alto
        if (winW / (double)winH >= _ratio) {
            // nos quedamos con el alto maximo
            // para aprovechar el espacio
            canvasH = winH;
            canvasW = (int)((double)winH * logic_width / logic_height);
        }
        else {
            // nos quedamos con el ancho maximo
            // para aprovechar el espacio
            canvasW = winW;
            canvasH = (int)((double)winW * logic_height / logic_width);
        }
    }

    /**
     * Implementacion que se encarga del pintado exclusivo de cada plataforma
     *
     * @param image imagen a pintar
     * @param dest rectangulo donde se pintara
     * @param source rectangulo fuente
     * @param alpha transparencia de la imagen
     */
    protected abstract void drawImagePrivate(Image image, Rect dest, Rect source, float alpha);
}
