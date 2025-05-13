package com.rodasfiti.model;

import java.util.Random;
import java.util.Set;

/**
 * La clase {@code Enemigo} representa un personaje enemigo en el juego, hereda
 * de la clase {@code Personaje}
 * y define comportamientos específicos para los enemigos, como moverse, atacar
 * y recibir daño.
 * 
 * <p>
 * Los enemigos tienen atributos como percepción, tipo, y posición en el
 * escenario. Además, pueden realizar
 * acciones como moverse por el mapa, atacar al protagonista y recibir daño
 * durante el combate.
 * 
 */
public class Enemigo extends Personaje {

    /**
     * La percepción del enemigo, utilizada para detectar al protagonista u otros
     * eventos.
     */
    protected int percepcion;

    /**
     * El tipo de enemigo (por ejemplo, {@code Duende}, {@code Orco}).
     */
    protected TipoEnemigo tipo;

    /**
     * La fila en la que se encuentra el enemigo en el mapa.
     */
    protected int fila;

    /**
     * La columna en la que se encuentra el enemigo en el mapa.
     */
    protected int columna;

    /**
     * Constructor de la clase {@code Enemigo}, inicializa un enemigo con los
     * atributos proporcionados.
     * 
     * @param tipo       El tipo de enemigo.
     * @param nivel      El nivel del enemigo.
     * @param ataque     El valor de ataque del enemigo.
     * @param defensa    El valor de defensa del enemigo.
     * @param vida       El valor de vida del enemigo.
     * @param velocidad  La velocidad del enemigo.
     * @param percepcion La percepción del enemigo.
     * @param x          La posición del enemigo en el eje X del mapa.
     * @param y          La posición del enemigo en el eje Y del mapa.
     */
    public Enemigo(TipoEnemigo tipo, int nivel, int ataque, int defensa, int vida, int velocidad, int percepcion, int x,
            int y) {
        super(nivel, ataque, defensa, vida, velocidad);
        this.percepcion = percepcion;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Obtiene el valor de percepción del enemigo.
     * 
     * @return El valor de percepción.
     */
    public int getPercepcion() {
        return percepcion;
    }

    /**
     * Establece el valor de percepción del enemigo.
     * 
     * @param percepcion El valor de percepción a establecer.
     */
    public void setPercepcion(int percepcion) {
        this.percepcion = percepcion;
    }

    /**
     * Método de ataque del enemigo. Este método es una plantilla y se espera que se
     * implemente en las clases hijas.
     */
    public void atacar() {
    }

    /**
     * Obtiene el tipo de enemigo.
     * 
     * @return El tipo de enemigo.
     */
    public TipoEnemigo getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de enemigo.
     * 
     * @param tipo El tipo de enemigo a establecer.
     */
    public void setTipo(TipoEnemigo tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene la fila en la que se encuentra el enemigo en el mapa.
     * 
     * @return La fila del enemigo.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Establece la fila del enemigo en el mapa.
     * 
     * @param fila La fila del enemigo a establecer.
     */
    public void setFila(int fila) {
        this.fila = fila;
    }

    /**
     * Obtiene la columna en la que se encuentra el enemigo en el mapa.
     * 
     * @return La columna del enemigo.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Establece la columna del enemigo en el mapa.
     * 
     * @param columna La columna del enemigo a establecer.
     */
    public void setColumna(int columna) {
        this.columna = columna;
    }

    /**
     * Método que simula el movimiento del enemigo. Este método es una plantilla y
     * puede ser implementado
     * en las clases hijas.
     */
    public void movimiento() {
    }

    /**
     * Mueve al enemigo en el mapa, comprobando si la nueva posición es válida según
     * el escenario.
     * 
     * @param dx        El desplazamiento en la fila.
     * @param dy        El desplazamiento en la columna.
     * @param escenario El escenario donde el enemigo se moverá.
     * @return {@code true} si el movimiento fue exitoso, {@code false} si no es
     *         válido.
     */
    public boolean mover(int dx, int dy, Escenario escenario) {
        int nuevaFila = fila + dx;
        int nuevaColumna = columna + dy;
        char[][] mapa = escenario.getMapa();

        if (nuevaFila >= 0 && nuevaFila < mapa.length &&
                nuevaColumna >= 0 && nuevaColumna < mapa[0].length &&
                mapa[nuevaFila][nuevaColumna] == 'S') {

            fila = nuevaFila;
            columna = nuevaColumna;
            return true;
        }
        return false;
    }

    /**
     * Establece una posición aleatoria para el enemigo en el escenario, evitando
     * posiciones ocupadas.
     * 
     * @param escenario          El escenario donde se coloca el enemigo.
     * @param posicionesOcupadas Un conjunto con las posiciones ya ocupadas.
     */
    public void setPosicionAleatoria(Escenario escenario, Set<String> posicionesOcupadas) {
        char[][] mapa = escenario.getMapa();
        int filas = mapa.length;
        int columnas = mapa[0].length;
        Random rand = new Random();

        int intentos = 0;
        while (intentos < 1000) {
            int f = rand.nextInt(filas);
            int c = rand.nextInt(columnas);
            String pos = f + "," + c;
            if (mapa[f][c] == 'S' && !posicionesOcupadas.contains(pos)) {
                fila = f;
                columna = c;
                System.out.println("Enemigo colocado en: " + pos);
                return;
            }
        }
        System.out.println("No se encontró posición válida para enemigo tras 1000 intentos.");
    }

    /**
     * Método de movimiento inteligente del enemigo, para perseguir al protagonista
     * o moverse aleatoriamente.
     * 
     * @param objetivoFila       La fila objetivo del protagonista.
     * @param objetivoColumna    La columna objetivo del protagonista.
     * @param escenario          El escenario donde se encuentra el enemigo.
     * @param posicionesOcupadas Un conjunto con las posiciones ocupadas.
     * @return Una cadena que indica la dirección en la que el enemigo se mueve:
     *         "arriba", "abajo", "izquierda",
     *         "derecha" o "quieto" si no se mueve.
     */
    public String moverInteligente(int objetivoFila, int objetivoColumna, Escenario escenario,
            Set<String> posicionesOcupadas) {
        int dx = 0, dy = 0;
        int nuevaFila = fila;
        int nuevaColumna = columna;

        int distanciaX = Math.abs(columna - objetivoColumna);
        int distanciaY = Math.abs(fila - objetivoFila);

        if (distanciaX <= 2 && distanciaY <= 2) {
            // Perseguir
            if (fila < objetivoFila)
                dx = 1;
            else if (fila > objetivoFila)
                dx = -1;
            if (columna < objetivoColumna)
                dy = 1;
            else if (columna > objetivoColumna)
                dy = -1;
        } else {
            int direccion = new Random().nextInt(4);
            switch (direccion) {
                case 0:
                    dx = -1;
                    break;
                case 1:
                    dx = 1;
                    break;
                case 2:
                    dy = -1;
                    break;
                case 3:
                    dy = 1;
                    break;
            }
        }

        nuevaFila = fila + dx;
        nuevaColumna = columna + dy;

        String nuevaPos = nuevaFila + "," + nuevaColumna;

        if (nuevaFila >= 0 && nuevaFila < escenario.getMapa().length &&
                nuevaColumna >= 0 && nuevaColumna < escenario.getMapa()[0].length &&
                escenario.getMapa()[nuevaFila][nuevaColumna] == 'S' &&
                !posicionesOcupadas.contains(nuevaPos)) {

            this.fila = nuevaFila;
            this.columna = nuevaColumna;
            posicionesOcupadas.add(nuevaPos);

            if (dx == -1)
                return "arriba";
            if (dx == 1)
                return "abajo";
            if (dy == -1)
                return "izquierda";
            if (dy == 1)
                return "derecha";
        }

        return "quieto";
    }

    /**
     * Reduce la vida del enemigo por la cantidad de daño recibido.
     * 
     * @param danio La cantidad de daño a reducir de la vida.
     */
    public void reducirVida(int danio) {
        this.vida -= danio;
    }

    /**
     * Verifica si el enemigo está muerto, es decir, si su vida es menor o igual a
     * cero.
     * 
     * @return {@code true} si el enemigo está muerto, {@code false} si está vivo.
     */
    public boolean estaMuerto() {
        return this.vida <= 0;
    }

    /**
     * Recibe un daño y reduce la vida del enemigo en consecuencia. Si la vida baja
     * de cero, se establece a cero.
     * 
     * @param cantidad La cantidad de daño recibido.
     */
    public void recibirDanio(int cantidad) {
        this.vida -= cantidad;
        if (this.vida < 0)
            this.vida = 0;
        System.out.println("Enemigo recibió " + cantidad + " de daño. Vida restante: " + this.vida);
    }

    /**
     * Método de ataque al protagonista. El daño se calcula en base a la diferencia
     * entre el ataque del enemigo
     * y la defensa del protagonista.
     * 
     * @param protagonista El protagonista al que se atacará.
     */
    public void atacar(Protagonista protagonista) {
        int danio = Math.max(0, this.ataque - protagonista.getDefensa());
        protagonista.reducirVida(danio);
        System.out.println("Enemigo hizo " + danio + " de daño.");
    }
}