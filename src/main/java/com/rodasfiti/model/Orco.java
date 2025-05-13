package com.rodasfiti.model;

/**
 * Clase que representa a un enemigo de tipo Orco.
 * Extiende la funcionalidad de la clase Enemigo y puede sobrescribir su
 * comportamiento.
 */
public class Orco extends Enemigo {

    /**
     * Constructor que crea un Orco con los atributos especificados.
     *
     * @param tipo       Tipo de enemigo.
     * @param nivel      Nivel del orco.
     * @param ataque     Valor de ataque del orco.
     * @param defensa    Valor de defensa del orco.
     * @param vida       Puntos de vida del orco.
     * @param velocidad  Velocidad del orco.
     * @param percepcion Nivel de percepción del orco.
     * @param x          Posición inicial en filas.
     * @param y          Posición inicial en columnas.
     */
    public Orco(TipoEnemigo tipo, int nivel, int ataque, int defensa, int vida, int velocidad, int percepcion, int x,
            int y) {
        super(tipo, nivel, ataque, defensa, vida, velocidad, percepcion, x, y);
    }

    /**
     * Método que permite al orco atacar a un personaje objetivo.
     * Utiliza el comportamiento de ataque definido en la clase padre.
     *
     * @param objetivo Personaje al que se ataca.
     */
    @Override
    public void atacar(Personaje objetivo) {
        super.atacar(objetivo);
    }

    /**
     * Método que define el movimiento del orco.
     * Utiliza el comportamiento de movimiento de la clase padre.
     */
    @Override
    public void movimiento() {
        super.movimiento();
    }
}