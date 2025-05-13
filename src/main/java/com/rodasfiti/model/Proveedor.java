package com.rodasfiti.model;

/**
 * Clase singleton que gestiona una instancia única del protagonista del juego.
 * Permite acceder y establecer el protagonista desde cualquier parte del
 * sistema.
 */
public class Proveedor {

    /** Instancia única de la clase Proveedor. */
    private static Proveedor instance;

    /** El protagonista del juego. */
    private Protagonista protagonista;

    /**
     * Constructor privado para prevenir la creación de instancias fuera de esta
     * clase.
     */
    private Proveedor() {

    }

    /**
     * Devuelve la instancia única de la clase Proveedor.
     * Si no existe, la crea.
     *
     * @return La instancia única de Proveedor.
     */
    public static Proveedor getInstance() {
        if (instance == null) {
            instance = new Proveedor();
        }
        return instance;
    }

    /**
     * Devuelve el protagonista actual.
     *
     * @return El protagonista actual.
     */
    public Protagonista getProtagonista() {
        return this.protagonista;
    }

    /**
     * Establece el protagonista del juego.
     *
     * @param protagonista El protagonista a establecer.
     */
    public void setProtagonista(Protagonista protagonista) {
        this.protagonista = protagonista;
    }
}