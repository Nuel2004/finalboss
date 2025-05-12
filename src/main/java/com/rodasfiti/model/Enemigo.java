package com.rodasfiti.model;

import java.util.Random;
import java.util.Set;

public class Enemigo extends Personaje {
    private int percepcion;
    private TipoEnemigo tipo;
    private int fila;
    private int columna;

    public Enemigo(TipoEnemigo tipo, int nivel, int ataque, int defensa, int vida, int velocidad, int percepcion, int fila, int columna) {
        super(vida, ataque, defensa, nivel, velocidad);
        this.percepcion = percepcion;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }

    // Getters y setters
    public int getPercepcion() {
        return percepcion;
    }

    public void setPercepcion(int percepcion) {
        this.percepcion = percepcion;
    }

    public TipoEnemigo getTipo() {
        return tipo;
    }

    public void setTipo(TipoEnemigo tipo) {
        this.tipo = tipo;
    }

    @Override
    public int getFila() {
        return fila;
    }

    @Override
    public int getColumna() {
        return columna;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    @Override
    public void movimiento() {
        // Implementación específica si se desea
    }

    public boolean mover(int dx, int dy, Escenario escenario) {
        int nuevaFila = fila + dx;
        int nuevaColumna = columna + dy;
        char[][] mapa = escenario.getMapa();

        if (nuevaFila >= 0 && nuevaFila < mapa.length &&
            nuevaColumna >= 0 && nuevaColumna < mapa[0].length &&
            mapa[nuevaFila][nuevaColumna] == 'S') {

            fila = nuevaFila;
            columna = nuevaColumna;
            return true;
        }
        return false;
    }

    public void setPosicionAleatoria(Escenario escenario, Set<String> posicionesOcupadas) {
        char[][] mapa = escenario.getMapa();
        int filas = mapa.length;
        int columnas = mapa[0].length;
        Random rand = new Random();

        for (int intentos = 0; intentos < 1000; intentos++) {
            int f = rand.nextInt(filas);
            int c = rand.nextInt(columnas);
            String pos = f + "," + c;
            if (mapa[f][c] == 'S' && !posicionesOcupadas.contains(pos)) {
                fila = f;
                columna = c;
                System.out.println("Enemigo colocado en: " + pos);
                return;
            }
        }
        System.out.println("No se encontró posición válida para enemigo tras 1000 intentos.");
    }

    public String moverInteligente(int objetivoFila, int objetivoColumna, Escenario escenario, Set<String> posicionesOcupadas) {
        int dx = 0, dy = 0;

        int distanciaX = Math.abs(columna - objetivoColumna);
        int distanciaY = Math.abs(fila - objetivoFila);

        // Movimiento hacia el objetivo si está cerca
        if (distanciaX <= 2 && distanciaY <= 2) {
            dx = Integer.compare(objetivoFila, fila);
            dy = Integer.compare(objetivoColumna, columna);
        } else {
            // Movimiento aleatorio
            switch (new Random().nextInt(4)) {
                case 0:
                    dx = -1;
                    break;
                case 1:
                    dx = 1;
                    break;
                case 2:
                    dy = -1;
                    break;
                case 3:
                    dy = 1;
                    break;
                default:
                    break;
            }
        }

        int nuevaFila = fila + dx;
        int nuevaColumna = columna + dy;
        String nuevaPos = nuevaFila + "," + nuevaColumna;

        if (nuevaFila >= 0 && nuevaFila < escenario.getMapa().length &&
            nuevaColumna >= 0 && nuevaColumna < escenario.getMapa()[0].length &&
            escenario.getMapa()[nuevaFila][nuevaColumna] == 'S' &&
            !posicionesOcupadas.contains(nuevaPos)) {

            fila = nuevaFila;
            columna = nuevaColumna;
            posicionesOcupadas.add(nuevaPos);

            if (dx == -1) return "arriba";
            if (dx == 1) return "abajo";
            if (dy == -1) return "izquierda";
            if (dy == 1) return "derecha";
        }

        return "quieto";
    }

    public boolean atacarSiCerca(Protagonista protagonista) {
        if (!estaCercaDe(protagonista))
            return false;

        if (velocidad >= protagonista.getVelocidad()) {
            atacar(protagonista);
            if (protagonista.getVida() > 0) {
                protagonista.atacar(this);
            }
        } else {
            protagonista.atacar(this);
            if (vida > 0) {
                atacar(protagonista);
            }
        }

        return true;
    }
}
