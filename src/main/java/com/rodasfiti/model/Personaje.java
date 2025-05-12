package com.rodasfiti.model;

import java.util.ArrayList;
import java.util.Random;
import com.rodasfiti.interfaces.Observer;

public abstract class Personaje {
    private ArrayList<Observer> observers;
    protected String nombre;
    protected int vida;
    protected int ataque;
    protected int defensa;
    protected int nivel;
    protected int velocidad;
    private static final Random random = new Random();

    public abstract int getFila();
    public abstract int getColumna();

    public Personaje(int vida, int ataque, int defensa, int nivel, int velocidad) {
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.nivel = nivel;
        this.velocidad = velocidad;
        this.observers = new ArrayList<>();
    }

    // Observer pattern
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        observers.forEach(Observer::onChange);
    }

    // Getters y setters
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        notifyObservers();
    }

    public int getVida() {
        return this.vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
        notifyObservers();
    }

    public int getAtaque() {
        return this.ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
        notifyObservers();
    }

    public int getDefensa() {
        return this.defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
        notifyObservers();
    }

    public int getVelocidad() {
        return this.velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
        notifyObservers();
    }

    public int getNivel() {
        return this.nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
        notifyObservers();
    }

    public ArrayList<Observer> getObservers() {
        return this.observers;
    }

    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
        notifyObservers();
    }

    // Lógica de combate
    public void atacar(Personaje objetivo) {
        if (random.nextInt(10) > objetivo.getDefensa()) {
            int nuevaVida = objetivo.getVida() - this.ataque;
            objetivo.setVida(Math.max(0, nuevaVida));
            System.out.println(this.nombre + " ha atacado a " + objetivo.getNombre() + " causando " + this.ataque + " de daño.");
        } else {
            System.out.println(this.nombre + " falló el ataque contra " + objetivo.getNombre());
        }
    }

    public boolean atacarSiCerca(Personaje objetivo) {
        if (!this.estaCercaDe(objetivo))
            return false;

        if (this.getVelocidad() >= objetivo.getVelocidad()) {
            this.atacar(objetivo);
            if (objetivo.getVida() > 0) {
                objetivo.atacar(this);
            }
        } else {
            objetivo.atacar(this);
            if (this.getVida() > 0) {
                this.atacar(objetivo);
            }
        }

        return true;
    }

    public boolean estaCercaDe(Personaje otro) {
        int dx = Math.abs(this.getColumna() - otro.getColumna());
        int dy = Math.abs(this.getFila() - otro.getFila());
        return dx <= 1 && dy <= 1;
    }

    // Acción de morir
    public void morir() {
        if (getVida() <= 0) {
            System.out.println(this.nombre + " ha muerto.");
        }
    }

    // Método abstracto que puede sobreescribirse en subclases
    public void movimiento() {
        // Implementación vacía por defecto
    }
}
