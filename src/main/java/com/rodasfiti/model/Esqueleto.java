package com.rodasfiti.model;

/**
 * La clase {@code Esqueleto} representa un enemigo específico del tipo
 * esqueleto dentro del juego.
 * Hereda de la clase {@link Enemigo} y puede sobrescribir sus comportamientos
 * para personalizarlos,
 * aunque actualmente reutiliza la lógica básica de ataque y movimiento definida
 * en la superclase.
 */
public class Esqueleto extends Enemigo {

    /**
     * Crea un nuevo enemigo de tipo Esqueleto con los atributos especificados.
     *
     * @param tipo       Tipo del enemigo (usualmente
     *                   {@code TipoEnemigo.ESQUELETO}).
     * @param nivel      Nivel del esqueleto, que puede afectar sus estadísticas.
     * @param ataque     Valor de ataque base del esqueleto.
     * @param defensa    Valor de defensa base del esqueleto.
     * @param vida       Vida máxima del esqueleto.
     * @param velocidad  Velocidad de movimiento o turno del esqueleto.
     * @param percepcion Rango de percepción (detección del protagonista).
     * @param x          Fila inicial donde se ubica el esqueleto.
     * @param y          Columna inicial donde se ubica el esqueleto.
     */
    public Esqueleto(TipoEnemigo tipo, int nivel, int ataque, int defensa, int vida, int velocidad, int percepcion,
            int x, int y) {
        super(tipo, nivel, ataque, defensa, vida, velocidad, percepcion, x, y);
    }

    /**
     * Ataca a un personaje objetivo utilizando la lógica definida en la superclase
     * {@link Enemigo}.
     *
     * @param objetivo El personaje al que se desea atacar.
     */
    @Override
    public void atacar(Personaje objetivo) {
        super.atacar(objetivo);
    }

    /**
     * Ejecuta el movimiento del esqueleto utilizando la lógica genérica definida en
     * {@link Enemigo}.
     */
    @Override
    public void movimiento() {
        super.movimiento();
    }
}