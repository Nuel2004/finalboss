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
    protected int posicion;
    protected int nivel;
    protected int velocidad;
    protected static Random r = new Random();

    public Personaje() {
        this.observers = new ArrayList<>();
    }

    public Personaje(String nombre, int vida, int ataque, int defensa, int posicion, int nivel, int velocidad) {
        this.nombre = nombre;
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.nivel = nivel;
        this.posicion = posicion;
        this.velocidad= velocidad;
    }

    public void suscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsuscribe(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        observers.forEach(x -> x.onChange());
    }

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
        if (this.vida > 0 && this.vida < 15) {
            this.vida = vida;
            notifyObservers();
        }
    }

    public int getAtaque() {
        return this.ataque;
    }

    public void setAtaque(int ataque) {
        if (this.ataque > 0 && this.ataque < 15) {
            this.ataque = ataque;
            notifyObservers();
        }
    }

    public int getDefensa() {
        return this.defensa;
    }

    public void setDefensa(int defensa) {
        if (this.defensa > 0 && this.defensa < 15) {
            this.defensa = defensa;
            notifyObservers();
        }
    }

    public int getVelocidad() {
        return this.velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public ArrayList<Observer> getObservers() {
        return this.observers;
    }

    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
    }

    public int getPosicion() {
        return this.posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
        notifyObservers();
    }

    public void atacar() {
        if (r.nextInt(10) > getDefensa()) {
            vida = getVida() - getAtaque();
        }
    }

    public void movimiento() {

    }

    public void morir() {
        if (getVida() <= 0) {
            System.out.println("El personaje ha muerto");
        }
    }
}