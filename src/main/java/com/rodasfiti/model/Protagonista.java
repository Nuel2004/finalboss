package com.rodasfiti.model;

import java.util.List;

public class Protagonista extends Personaje {

    private int fila;
    private int columna;
    private int posicion;

    public Protagonista(String nombre, int vida, int ataque, int defensa, int posicion, int nivel, int velocidad) {
        super(vida, ataque, defensa, nivel, velocidad);
        this.posicion = posicion;
        this.nombre = nombre;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getPosicion() {
        return this.posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    @Override
    public void movimiento() {

    }

    public void atacar(Personaje objetivo) {
        super.atacar(objetivo);
    }

    public boolean mover(int dx, int dy, Escenario escenario, List<Enemigo> enemigos) {
        int nuevaFila = this.fila + dx;
        int nuevaColumna = this.columna + dy;
        char[][] mapa = escenario.getMapa();

        if (!puedeMoverA(nuevaFila, nuevaColumna, escenario)) {
            return false;
        }

        for (Enemigo enemigo : enemigos) {
            if (enemigo.getFila() == nuevaFila && enemigo.getColumna() == nuevaColumna) {
                System.out.println("¡Enemigo detectado! Iniciando combate...");
                this.atacarSiCerca(enemigo);

                if (enemigo.getVida() <= 0) {
                    enemigos.remove(enemigo);
                    System.out.println("¡Has vencido al enemigo y puedes avanzar!");
                    this.fila = nuevaFila;
                    this.columna = nuevaColumna;
                    return true;
                } else {
                    System.out.println("El enemigo sigue vivo. Movimiento bloqueado.");
                    return false;
                }
            }
        }

        this.fila = nuevaFila;
        this.columna = nuevaColumna;
        return true;
    }

    public boolean puedeMoverA(int nuevaFila, int nuevaColumna, Escenario escenario) {
        char[][] mapa = escenario.getMapa();
        if (nuevaFila < 0 || nuevaFila >= mapa.length || nuevaColumna < 0 || nuevaColumna >= mapa[0].length) {
            return false;
        }
        return mapa[nuevaFila][nuevaColumna] == 'S';
    }

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
            if (intentos > 1000) { // Evitar bucle infinito
                System.out.println("No se encontró una posición válida en 1000 intentos.");
                break;
            }
        }
    }

    public boolean atacarSiCerca(Enemigo enemigo) {
        if (!this.estaCercaDe(enemigo))
            return false;

        if (this.getVelocidad() >= enemigo.getVelocidad()) {
            this.atacar(enemigo);
            if (enemigo.getVida() > 0) {
                enemigo.atacar(this);
            }
        } else {
            enemigo.atacar(this);
            if (this.getVida() > 0) {
                this.atacar(enemigo);
            }
        }

        return true;
    }

}
