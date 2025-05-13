package com.rodasfiti.model;

/**
 * La clase {@code Duende} representa un tipo específico de {@code Enemigo}.
 * Hereda de la clase {@code Enemigo} y proporciona comportamientos específicos
 * de este tipo de enemigo, como atacar y moverse en el juego.
 * 
 * <p>
 * El {@code Duende} es un enemigo con características como ataque, defensa,
 * vida, velocidad, y percepción que influyen en sus interacciones con el
 * personaje
 * del jugador durante el combate.
 * 
 */
public class Duende extends Enemigo {

    /**
     * Crea una instancia de un {@code Duende} con los atributos proporcionados.
     * 
     * @param tipo       El tipo de enemigo.
     * @param nivel      El nivel del duende.
     * @param ataque     El valor de ataque del duende.
     * @param defensa    El valor de defensa del duende.
     * @param vida       El valor de vida del duende.
     * @param velocidad  La velocidad del duende.
     * @param percepcion El nivel de percepción del duende.
     * @param x          La posición en el eje X del duende en el mapa.
     * @param y          La posición en el eje Y del duende en el mapa.
     */
    public Duende(TipoEnemigo tipo, int nivel, int ataque, int defensa, int vida, int velocidad, int percepcion, int x,
            int y) {
        super(tipo, nivel, ataque, defensa, vida, velocidad, percepcion, x, y);
    }

    /**
     * Realiza la acción de atacar a un objetivo (un {@code Personaje}) en el juego.
     * Este método invoca el comportamiento de ataque de la clase padre
     * {@code Enemigo}.
     * 
     * @param objetivo El {@code Personaje} que será atacado por el duende.
     */
    @Override
    public void atacar(Personaje objetivo) {
        super.atacar(objetivo); // Llama al ataque de la clase padre
    }

    /**
     * Realiza la acción de movimiento del duende. Este método invoca el
     * comportamiento
     * de movimiento de la clase padre {@code Enemigo}.
     */
    @Override
    public void movimiento() {
        super.movimiento(); // Llama al movimiento de la clase padre
    }
}