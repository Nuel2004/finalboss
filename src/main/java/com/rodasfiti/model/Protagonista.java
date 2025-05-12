package com.rodasfiti.model;

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

    @Override
    public void atacar(Personaje objetivo) {
        super.atacar(objetivo);
    }

    public boolean mover(int dx, int dy, Escenario escenario) {
        int nuevaFila = this.fila + dx;
        int nuevaColumna = this.columna + dy;
        char[][] mapa = escenario.getMapa();

        if (nuevaFila < 0 || nuevaFila >= mapa.length || nuevaColumna < 0 || nuevaColumna >= mapa[0].length) {
            return false;
        }

        if (mapa[nuevaFila][nuevaColumna] == 'S') {
            this.fila = nuevaFila;
            this.columna = nuevaColumna;
            return true;
        }

        return false;
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

}
