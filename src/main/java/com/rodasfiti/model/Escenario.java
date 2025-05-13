package com.rodasfiti.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase {@code Escenario} representa el entorno del juego en forma de un
 * mapa bidimensional,
 * donde cada celda se representa mediante un carácter.
 * 
 * <p>
 * El mapa puede cargarse desde un archivo CSV, donde cada celda está separada
 * por comas,
 * y cada línea representa una fila del mapa.
 */
public class Escenario {

    /**
     * Mapa del escenario representado como una matriz de caracteres.
     */
    private char[][] mapa;

    /**
     * Constructor que inicializa el escenario con un mapa dado.
     *
     * @param mapa Matriz de caracteres que representa el mapa del escenario.
     */
    public Escenario(char[][] mapa) {
        this.mapa = mapa;
    }

    /**
     * Devuelve el mapa del escenario.
     *
     * @return Una matriz bidimensional de caracteres que representa el mapa.
     */
    public char[][] getMapa() {
        return mapa;
    }

    /**
     * Carga un escenario desde un archivo CSV. Cada línea del archivo representa
     * una fila del mapa,
     * y cada celda se separa por comas. Se espera que cada celda contenga un solo
     * carácter.
     * 
     * <p>
     * En caso de error de lectura del archivo, se imprime el error y se retorna
     * {@code null}.
     *
     * @param rutaCSV Ruta del archivo CSV que contiene el mapa del escenario.
     * @return Una instancia de {@code Escenario} con el mapa cargado, o
     *         {@code null} si ocurre un error.
     */
    public static Escenario cargarDesdeCSV(Path rutaCSV) {

        List<char[]> filas = new ArrayList<>();
        try {
            BufferedReader reader = Files.newBufferedReader(rutaCSV);
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                char[] fila = new char[partes.length];
                for (int i = 0; i < partes.length; i++) {
                    fila[i] = partes[i].trim().charAt(0);
                }
                filas.add(fila);
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Error leyendo el archivo CSV:");
            e.printStackTrace();
            return null;
        }

        char[][] mapa = new char[filas.size()][];
        for (int i = 0; i < filas.size(); i++) {
            mapa[i] = filas.get(i);
        }

        return new Escenario(mapa);
    }
}