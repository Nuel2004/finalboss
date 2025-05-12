package com.rodasfiti.model;

import java.util.Random;
import java.util.Set;

public class Enemigo extends Personaje{
    protected int percepcion;
    protected TipoEnemigo tipo;
    protected int fila;
    protected int columna;

    public Enemigo(TipoEnemigo tipo, int nivel, int ataque, int defensa, int vida, int velocidad, int percepcion, int x, int y) {
        super(nivel, ataque, defensa, vida, velocidad);
        this.percepcion = percepcion;
        this.tipo = tipo;
        this.fila = x;
        this.columna = y;
    }
    
    public int getPercepcion() {
        return percepcion;
    }
    public void setPercepcion(int percepcion) {
        this.percepcion = percepcion;
    }
    

    public void atacar(Personaje objetivo) {
        super.atacar(objetivo);
    }

    public TipoEnemigo getTipo() {
        return this.tipo;
    }

    public void setTipo(TipoEnemigo tipo) {
        this.tipo = tipo;
    }
    public int getFila() {
        return fila;
    }
    public void setFila(int fila) {
        this.fila = fila;
    }
    public int getColumna() {
        return columna;
    }
    public void setColumna(int columna) {
        this.columna = columna;
    }
    public void movimiento() {

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
    public void setPosicionAleatoria(Escenario escenario, Set<String> posicionesOcupadas) {
        char[][] mapa = escenario.getMapa();
        int filas = mapa.length;
        int columnas = mapa[0].length;
        Random rand = new Random();
    
        int intentos = 0;
        while (intentos < 1000) {
            int f = rand.nextInt(filas);
            int c = rand.nextInt(columnas);
            String pos = f + "," + c;
            if (mapa[f][c] == 'S' && !posicionesOcupadas.contains(pos)) {
                this.fila = f;
                this.columna = c;
                System.out.println("Enemigo colocado en: " + pos);
                return;
            }
            intentos++;
        }
        System.out.println("No se encontró posición válida para enemigo tras 1000 intentos.");
    }
    public String moverInteligente(int objetivoFila, int objetivoColumna, Escenario escenario, Set<String> posicionesOcupadas) {
        int dx = 0, dy = 0;
        int nuevaFila = fila;
        int nuevaColumna = columna;
    
        int distanciaX = Math.abs(columna - objetivoColumna);
        int distanciaY = Math.abs(fila - objetivoFila);
        
    
        if (distanciaX <= 2 && distanciaY <= 2) {
            // Perseguir
            if (fila < objetivoFila) dx = 1;
            else if (fila > objetivoFila) dx = -1;
            if (columna < objetivoColumna) dy = 1;
            else if (columna > objetivoColumna) dy = -1;
        } else {
            int direccion = new Random().nextInt(4);
            switch (direccion) {
                case 0: dx = -1; break;
                case 1: dx = 1; break;
                case 2: dy = -1; break;
                case 3: dy = 1; break;
            }
        }
    
        nuevaFila = fila + dx;
        nuevaColumna = columna + dy;
    
        String nuevaPos = nuevaFila + "," + nuevaColumna;
    
        if (nuevaFila >= 0 && nuevaFila < escenario.getMapa().length &&
            nuevaColumna >= 0 && nuevaColumna < escenario.getMapa()[0].length &&
            escenario.getMapa()[nuevaFila][nuevaColumna] == 'S' &&
            !posicionesOcupadas.contains(nuevaPos)) {
            
            this.fila = nuevaFila;
            this.columna = nuevaColumna;
            posicionesOcupadas.add(nuevaPos);
    
            if (dx == -1) return "arriba";
            if (dx == 1) return "abajo";
            if (dy == -1) return "izquierda";
            if (dy == 1) return "derecha";
        }
        
        return "quieto";
    }


}
