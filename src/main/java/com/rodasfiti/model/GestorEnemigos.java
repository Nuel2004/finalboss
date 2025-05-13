package com.rodasfiti.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Clase utilitaria encargada de cargar enemigos desde un archivo CSV ubicado en
 * los recursos del proyecto.
 */
public class GestorEnemigos {

    /**
     * Carga una lista de enemigos desde un archivo CSV especificado.
     * Cada línea del archivo representa un enemigo con sus atributos en el
     * siguiente orden:
     * tipo, nivel, ataque, defensa, vida, velocidad, percepción, posición X,
     * posición Y.
     * Se omite la primera línea del archivo asumiendo que es un encabezado.
     *
     * @param rutaCSV Ruta al archivo CSV en los recursos del proyecto (por ejemplo:
     *                "/data/enemigos.csv").
     * @return Lista de enemigos cargados desde el archivo.
     */
    public static ArrayList<Enemigo> cargarEnemigosDesdeCSV(String rutaCSV) {
        ArrayList<Enemigo> listaEnemigos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        GestorEnemigos.class.getResourceAsStream(rutaCSV))))) {

            String linea;
            boolean esPrimera = true;
            while ((linea = br.readLine()) != null) {
                // Saltar la primera línea si es encabezado
                if (esPrimera) {
                    esPrimera = false;
                    continue;
                }

                // Separar los valores por comas
                String[] partes = linea.split(",");
                if (partes.length == 9) {
                    TipoEnemigo tipo = TipoEnemigo.valueOf(partes[0].toUpperCase().trim());
                    int nivel = Integer.parseInt(partes[1]);
                    int ataque = Integer.parseInt(partes[2]);
                    int defensa = Integer.parseInt(partes[3]);
                    int vida = Integer.parseInt(partes[4]);
                    int velocidad = Integer.parseInt(partes[5]);
                    int percepcion = Integer.parseInt(partes[6]);
                    int x = Integer.parseInt(partes[7]);
                    int y = Integer.parseInt(partes[8]);

                    // Crear y agregar enemigo a la lista
                    listaEnemigos.add(new Enemigo(tipo, nivel, ataque, defensa, vida, velocidad, percepcion, x, y));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEnemigos;
    }
}