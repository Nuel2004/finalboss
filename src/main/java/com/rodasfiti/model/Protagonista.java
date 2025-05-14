package com.rodasfiti.model;

/**
 * Clase que representa al personaje principal controlado por el jugador.
 * Hereda de la clase Personaje e incluye información adicional sobre su
 * posición en el mapa.
 */
public class Protagonista extends Personaje {

    private int fila;
    private int columna;
    private int posicion;

    /**
     * Constructor del protagonista.
     *
     * @param nombre    Nombre del protagonista.
     * @param vida      Vida inicial.
     * @param ataque    Ataque inicial.
     * @param defensa   Defensa inicial.
     * @param posicion  Posición inicial (lineal).
     * @param nivel     Nivel inicial.
     * @param velocidad Velocidad inicial.
     */
    public Protagonista(String nombre, int vida, int ataque, int defensa, int posicion, int nivel, int velocidad) {
        super(vida, ataque, defensa, nivel, velocidad);
        this.posicion = posicion;
        this.nombre = nombre;
    }

    /**
     * Devuelve la fila actual del protagonista en el mapa.
     *
     * @return Fila actual.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Devuelve la columna actual del protagonista en el mapa.
     *
     * @return Columna actual.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Establece la fila del protagonista.
     *
     * @param fila Nueva fila.
     */
    public void setFila(int fila) {
        this.fila = fila;
    }

    /**
     * Establece la columna del protagonista.
     *
     * @param columna Nueva columna.
     */
    public void setColumna(int columna) {
        this.columna = columna;
    }

    /**
     * Devuelve la posición lineal del protagonista.
     *
     * @return Posición.
     */
    public int getPosicion() {
        return this.posicion;
    }

    /**
     * Establece la posición lineal del protagonista.
     *
     * @param posicion Nueva posición.
     */
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    /**
     * Movimiento personalizado del protagonista.
     * Este método está sobrescrito pero actualmente no tiene lógica.
     */
    @Override
    public void movimiento() {
    }

    /**
     * Realiza un ataque al objetivo especificado.
     *
     * @param objetivo Personaje al que se ataca.
     */
    @Override
    public void atacar(Personaje objetivo) {
        super.atacar(objetivo);
    }

    /**
     * Intenta mover al protagonista en el mapa según un desplazamiento.
     *
     * @param dx        Desplazamiento en filas.
     * @param dy        Desplazamiento en columnas.
     * @param escenario Escenario donde se encuentra el mapa.
     * @return true si el movimiento fue válido y realizado, false si no lo fue.
     */
    public boolean mover(int dx, int dy, Escenario escenario) {
        int nuevaFila = this.fila + dx;
        int nuevaColumna = this.columna + dy;
        char[][] mapa = escenario.getMapa();

        if (nuevaFila < 0 || nuevaFila >= mapa.length || nuevaColumna < 0 || nuevaColumna >= mapa[0].length) {
            return false;
        }

        if (mapa[nuevaFila][nuevaColumna] == 'S'||mapa[nuevaFila][nuevaColumna]=='T') {
            this.fila = nuevaFila;
            this.columna = nuevaColumna;
            return true;
        }

        return false;
    }

    /**
     * Verifica si se puede mover a una posición específica del mapa.
     *
     * @param nuevaFila    Fila a la que se quiere mover.
     * @param nuevaColumna Columna a la que se quiere mover.
     * @param escenario    Escenario que contiene el mapa.
     * @return true si es una casilla válida y transitable, false si no lo es.
     */
    public boolean puedeMoverA(int nuevaFila, int nuevaColumna, Escenario escenario) {
        char[][] mapa = escenario.getMapa();
        if (nuevaFila < 0 || nuevaFila >= mapa.length || nuevaColumna < 0 || nuevaColumna >= mapa[0].length) {
            return false;
        }
        return mapa[nuevaFila][nuevaColumna] == 'S';
    }

    /**
     * Asigna una posición aleatoria válida (suelo) al protagonista en el mapa.
     *
     * @param escenario Escenario que contiene el mapa.
     */
    public void setPosicionAleatoria(Escenario escenario) {
        char[][] mapa = escenario.getMapa();
        int filas = mapa.length;
        int columnas = mapa[0].length;
        java.util.Random rand = new java.util.Random();

        int intentos = 0;
        while (true) {
            int f = rand.nextInt(filas);
            int c = rand.nextInt(columnas);
            if (mapa[f][c] == 'S') {
                this.fila = f;
                this.columna = c;
                System.out.println("Posición aleatoria asignada a: (" + f + ", " + c + ")");
                break;
            }

            intentos++;
            if (intentos > 1000) {
                System.out.println("No se encontró una posición válida en 1000 intentos.");
                break;
            }
        }
    }

    /**
     * Ataca a un enemigo causando daño según el ataque y la defensa.
     *
     * @param enemigo Enemigo al que se ataca.
     */
    public void atacar(Enemigo enemigo) {
        int danio = Math.max(0, this.ataque - enemigo.getDefensa());
        enemigo.reducirVida(danio);
        System.out.println("Atacaste al enemigo y le hiciste " + danio + " de daño.");
    }

    /**
     * Reduce la vida del protagonista en una cantidad dada.
     *
     * @param cantidad Cantidad de vida a reducir.
     */
    public void reducirVida(int cantidad) {
        this.vida -= cantidad;
        if (this.vida < 0) {
            this.vida = 0;
        }
        System.out.println("Protagonista pierde " + cantidad + " de vida. Vida actual: " + this.vida);
    }

    /**
     * Indica si el protagonista ha muerto (vida menor o igual a 0).
     *
     * @return true si está muerto, false si sigue vivo.
     */
    public boolean estaMuerto() {
        return this.vida <= 0;
    }

    /**
     * Sube el nivel del protagonista y mejora sus atributos.
     * Aumenta en 1 el nivel, ataque, defensa, velocidad y vida.
     */
    public void subirNivel() {
        this.nivel++;
        this.ataque++;
        this.defensa++;
        this.velocidad++;
        this.vida++;
        System.out.println("¡Nivel aumentado a " + nivel + "! Todos los atributos han subido en 1.");
    }
}