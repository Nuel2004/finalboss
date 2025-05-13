package com.rodasfiti.model;

import java.util.ArrayList;
import java.util.Random;
import com.rodasfiti.interfaces.Observer;

/**
 * Clase abstracta que representa un personaje dentro del juego.
 * Contiene atributos comunes como vida, ataque, defensa, nivel y velocidad.
 * También implementa el patrón Observer para notificar cambios en sus
 * atributos.
 */
public abstract class Personaje {
    private ArrayList<Observer> observers;
    protected String nombre;
    protected int vida;
    protected int ataque;
    protected int defensa;
    protected int nivel;
    protected int velocidad;
    protected static Random r = new Random();

    /**
     * Constructor para crear un personaje con los atributos básicos.
     *
     * @param vida      Puntos de vida del personaje.
     * @param ataque    Valor de ataque del personaje.
     * @param defensa   Valor de defensa del personaje.
     * @param nivel     Nivel del personaje.
     * @param velocidad Velocidad del personaje.
     */
    public Personaje(int vida, int ataque, int defensa, int nivel, int velocidad) {
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.nivel = nivel;
        this.velocidad = velocidad;
        this.observers = new ArrayList<>();
    }

    /**
     * Añade un observador a la lista de observadores.
     *
     * @param observer Observador a añadir.
     */
    public void suscribe(Observer observer) {
        observers.add(observer);
    }

    /**
     * Elimina un observador de la lista de observadores.
     *
     * @param observer Observador a eliminar.
     */
    public void unsuscribe(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifica a todos los observadores del cambio.
     */
    public void notifyObservers() {
        observers.forEach(Observer::onChange);
    }

    /**
     * Devuelve el nombre del personaje.
     *
     * @return Nombre del personaje.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Establece el nombre del personaje y notifica a los observadores.
     *
     * @param nombre Nombre nuevo del personaje.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
        notifyObservers();
    }

    /**
     * Devuelve los puntos de vida del personaje.
     *
     * @return Vida actual.
     */
    public int getVida() {
        return this.vida;
    }

    /**
     * Establece los puntos de vida del personaje y notifica a los observadores.
     *
     * @param vida Nueva cantidad de vida.
     */
    public void setVida(int vida) {
        this.vida = vida;
        notifyObservers();
    }

    /**
     * Devuelve el valor de ataque del personaje.
     *
     * @return Ataque actual.
     */
    public int getAtaque() {
        return this.ataque;
    }

    /**
     * Establece el valor de ataque del personaje y notifica a los observadores.
     *
     * @param ataque Nuevo valor de ataque.
     */
    public void setAtaque(int ataque) {
        this.ataque = ataque;
        notifyObservers();
    }

    /**
     * Devuelve el valor de defensa del personaje.
     *
     * @return Defensa actual.
     */
    public int getDefensa() {
        return this.defensa;
    }

    /**
     * Establece el valor de defensa del personaje y notifica a los observadores.
     *
     * @param defensa Nuevo valor de defensa.
     */
    public void setDefensa(int defensa) {
        this.defensa = defensa;
        notifyObservers();
    }

    /**
     * Devuelve la velocidad del personaje.
     *
     * @return Velocidad actual.
     */
    public int getVelocidad() {
        return this.velocidad;
    }

    /**
     * Establece la velocidad del personaje.
     *
     * @param velocidad Nueva velocidad.
     */
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * Devuelve la lista de observadores registrados.
     *
     * @return Lista de observadores.
     */
    public ArrayList<Observer> getObservers() {
        return this.observers;
    }

    /**
     * Establece una nueva lista de observadores.
     *
     * @param observers Lista de observadores.
     */
    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
    }

    /**
     * Devuelve el nivel del personaje.
     *
     * @return Nivel actual.
     */
    public int getNivel() {
        return nivel;
    }

    /**
     * Establece el nivel del personaje y notifica a los observadores.
     *
     * @param nivel Nuevo nivel.
     */
    public void setNivel(int nivel) {
        this.nivel = nivel;
        notifyObservers();
    }

    /**
     * Método que permite atacar a otro personaje.
     * Si el ataque supera la defensa del objetivo, se le reduce la vida.
     *
     * @param objetivo Personaje que recibe el ataque.
     */
    public void atacar(Personaje objetivo) {
        if (random.nextInt(10) > objetivo.getDefensa()) {
            int nuevaVida = objetivo.getVida() - this.ataque;
            objetivo.setVida(Math.max(0, nuevaVida));
        }
    }

    /**
     * Método que indica si el personaje ha muerto.
     * Se ejecuta cuando su vida es menor o igual a cero.
     */
    public void morir() {
        if (getVida() <= 0) {
            System.out.println(this.nombre + " ha muerto.");
        }
    }

    /**
     * Método que define el movimiento del personaje.
     * Puede ser sobrescrito por las subclases.
     */
    public void movimiento() {
        // Implementación vacía por defecto
    }
}
