package com.rodasfiti.model;

public class Esqueleto extends Enemigo {

    public Esqueleto(String nombre, int vida, int ataque, int defensa) {
        super(nombre, vida, ataque, defensa);
    }

    @Override
    public void atacar() {
        super.atacar();
    }

    @Override
    public void movimiento() {
        super.movimiento();
    }

}
