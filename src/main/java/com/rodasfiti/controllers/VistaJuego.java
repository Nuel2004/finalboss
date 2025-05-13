package com.rodasfiti.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import com.rodasfiti.interfaces.Observer;
import com.rodasfiti.model.*;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class VistaJuego implements Observer {

    @FXML
    private Label vida;

    @FXML
    private Label velocidad;

    @FXML
    private Label escudo;

    @FXML
    private Label ataque;

    @FXML
    private Label nivel;

    @FXML
    private Label nombrePersonaje;

    @FXML
    private Label movimientosFaltantes;

    @FXML
    private AnchorPane contenedorMapa;

    private GridPane mainGrid;
    private int posX = 1;
    private int posY = 1;
    private ImageView protagonistaView;
    private Escenario escenario;
    private int movimientosRestantes = 15;
    private static Random r = new Random();
    private ArrayList<Enemigo> listaEnemigos = new ArrayList<>();
    private ArrayList<ImageView> vistasEnemigos = new ArrayList<>();
    private Protagonista protagonista;

    @FXML
    public void initialize() {
        System.out.println(">>> [INICIO initialize()]");

        movimientosFaltantes.setText(String.valueOf(movimientosRestantes));
        mainGrid = new GridPane();
        mainGrid.setPadding(new Insets(10));
        mainGrid.setHgap(0);
        mainGrid.setVgap(0);
        AnchorPane.setTopAnchor(mainGrid, 0.0);
        AnchorPane.setLeftAnchor(mainGrid, 0.0);
        AnchorPane.setRightAnchor(mainGrid, 0.0);
        AnchorPane.setBottomAnchor(mainGrid, 0.0);

        escenario = cargarEscenarioDesdeRecursos("/com/rodasfiti/data/mapaprueba.csv");

        if (escenario != null) {
            protagonista = Proveedor.getInstance().getProtagonista();
            System.out.println(
                    "ANTES de setPosicionAleatoria: " + protagonista.getFila() + "," + protagonista.getColumna());

            protagonista.setPosicionAleatoria(escenario);
            Proveedor.getInstance().setProtagonista(protagonista);

            System.out.println(
                    "DESPUÉS de setPosicionAleatoria: " + protagonista.getFila() + "," + protagonista.getColumna());

            actualizarDatosProtagonista();
            listaEnemigos = GestorEnemigos.cargarEnemigosDesdeCSV("/com/rodasfiti/data/enemigos.csv");

            System.out.println("ANTES de mostrarMapa: " + protagonista.getFila() + "," + protagonista.getColumna());

            mostrarMapa();
        } else {
            System.err.println("No se pudo cargar el escenario.");
        }

        contenedorMapa.getChildren().add(mainGrid);

        mover();

        System.out.println(">>> [FIN initialize()]");
    }

    void actualizarDatosProtagonista() {
        Protagonista protagonista = Proveedor.getInstance().getProtagonista();
        nombrePersonaje.setText(protagonista.getNombre());
        vida.setText(String.valueOf(protagonista.getVida()));
        ataque.setText(String.valueOf(protagonista.getAtaque()));
        escudo.setText(String.valueOf(protagonista.getDefensa()));
        velocidad.setText(String.valueOf(protagonista.getVelocidad()));
        nivel.setText(String.valueOf(protagonista.getNivel()));
    }

    private Escenario cargarEscenarioDesdeRecursos(String ruta) {
        try {
            URL resource = getClass().getResource(ruta);
            if (resource == null) {
                System.err.println("No se encontró el archivo: " + ruta);
                return null;
            }
            Path path = Paths.get(resource.toURI());
            return Escenario.cargarDesdeCSV(path);
        } catch (Exception e) {
            System.err.println("Error al cargar el escenario: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void mostrarMapa() {
        char[][] mapa = escenario.getMapa();
        mainGrid.getChildren().clear();
        vistasEnemigos.clear();
        int filas = mapa.length;
        int columnas = mapa[0].length;
        int ancho = 973 / columnas;
        int alto = 673 / filas;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                String imagen = "/com/rodasfiti/images/";
                if (mapa[i][j] == 'P') {
                    imagen += "pared.png";
                } else {
                    imagen += "suelo.png";
                }
                ImageView bloque = new ImageView(cargarImagen(imagen));
                bloque.setFitWidth(ancho);
                bloque.setFitHeight(alto);
                mainGrid.add(bloque, j, i);
            }
        }
        protagonistaView = new ImageView(cargarImagen("/com/rodasfiti/images/caballero_abajo.png"));
        protagonistaView.setFitWidth(ancho);
        protagonistaView.setFitHeight(alto);
        mainGrid.add(protagonistaView, protagonista.getColumna(), protagonista.getFila());
        mostrarEnemigos(ancho, alto);
    }

    private void spawnEnemigos(int cantidad) {
        ArrayList<Enemigo> enemigosDesdeCSV = GestorEnemigos.cargarEnemigosDesdeCSV("/com/rodasfiti/data/enemigos.csv");
        ArrayList<Enemigo> enemigosASpawn = new ArrayList<>();
        int enemigosCreados = 0;
        ArrayList<Enemigo> duendes = new ArrayList<>();
        ArrayList<Enemigo> orcos = new ArrayList<>();
        ArrayList<Enemigo> esqueletos = new ArrayList<>();

        for (Enemigo enemigo : enemigosDesdeCSV) {
            switch (enemigo.getTipo()) {
                case DUENDE:
                    duendes.add(enemigo);
                    break;
                case ORCO:
                    orcos.add(enemigo);
                    break;
                case ESQUELETO:
                    esqueletos.add(enemigo);
                    break;
            }
        }

        ArrayList<Enemigo> todos = new ArrayList<>();
        todos.addAll(duendes);
        todos.addAll(orcos);
        todos.addAll(esqueletos);

        Random r = new Random();
        while (enemigosCreados < cantidad) {
            Enemigo base = todos.get(r.nextInt(todos.size()));

            int intentos = 0;
            while (intentos < 100) {
                int x = r.nextInt(escenario.getMapa().length);
                int y = r.nextInt(escenario.getMapa()[0].length);
                if (esPosicionValida(x, y)) {
                    enemigosASpawn.add(new Enemigo(
                            base.getTipo(),
                            base.getNivel(),
                            base.getAtaque(),
                            base.getDefensa(),
                            base.getVida(),
                            base.getVelocidad(),
                            base.getPercepcion(),
                            x, y));
                    enemigosCreados++;
                    break;
                }
                intentos++;
            }
        }

        listaEnemigos.clear();
        listaEnemigos.addAll(enemigosASpawn);
        vistasEnemigos.clear();
        mostrarMapa();
    }

    private boolean esPosicionValida(int x, int y) {
        return x >= 0 && y >= 0 && x < escenario.getMapa().length && y < escenario.getMapa()[0].length
                && puedeMoverA(x, y) && !(x == posX && y == posY);
    }

    private void mostrarEnemigos(int ancho, int alto) {
        char[][] mapa = escenario.getMapa();
        int filas = mapa.length;
        int columnas = mapa[0].length;
        ancho = 973 / columnas;
        alto = 673 / filas;

        mainGrid.getChildren().removeAll(vistasEnemigos);
        vistasEnemigos.clear();

        for (Enemigo enemigo : listaEnemigos) {

            if (puedeMoverA(enemigo.getColumna(), enemigo.getFila())) {
                String imagen = "/com/rodasfiti/images/";

                switch (enemigo.getTipo()) {
                    case ORCO:
                        imagen += "orco.png";
                        break;
                    case DUENDE:
                        imagen += "duende.png";
                        break;
                    case ESQUELETO:
                        imagen += "esqueleto.png";
                        break;
                    default:
                        imagen += "duende.png";
                        break;
                }

                try {

                    Image enemigoImagen = cargarImagen(imagen);
                    ImageView enemigoView = new ImageView(enemigoImagen);
                    enemigoView.setFitWidth(ancho);
                    enemigoView.setFitHeight(alto);

                    mainGrid.add(enemigoView, enemigo.getColumna(), enemigo.getFila());
                    vistasEnemigos.add(enemigoView);

                    System.out.println("Enemigo añadido en: X=" + enemigo.getColumna() + ", Y=" +
                            enemigo.getFila() + ", Tipo=" + enemigo.getTipo());
                } catch (Exception e) {
                    System.err.println("Error al cargar imagen de enemigo: " + imagen);
                    e.printStackTrace();
                }
            }
        }
    }

    private void mover() {
        contenedorMapa.setFocusTraversable(true);
        contenedorMapa.requestFocus();
        contenedorMapa.setOnKeyPressed(this::moverProtagonista);
    }

    private void moverProtagonista(KeyEvent e) {
        System.out.println("Tecla pulsada: " + e.getCode());

        int dx = 0, dy = 0;
        String img = "/com/rodasfiti/images/";

        switch (e.getCode()) {
            case W:
            case UP:
                dx = -1;
                img += "caballero_arriba.png";
                break;
            case S:
            case DOWN:
                dx = 1;
                img += "caballero_abajo.png";
                break;
            case A:
            case LEFT:
                dy = -1;
                img += "caballero_izquierda.png";
                break;
            case D:
            case RIGHT:
                dy = 1;
                img += "caballero_derecha.png";
                break;
            default:
                return;
        }

        int nuevaFila = protagonista.getFila() + dx;
        int nuevaColumna = protagonista.getColumna() + dy;

        for (Enemigo enemigo : listaEnemigos) {
            if (enemigo.getFila() == nuevaFila && enemigo.getColumna() == nuevaColumna) {
                combatirSiHayEnemigoCerca(nuevaFila, nuevaColumna);
                return;
            }
        }

        if (protagonista.mover(dx, dy, escenario)) {

            posX = protagonista.getFila();
            posY = protagonista.getColumna();

            protagonistaView.setImage(cargarImagen(img));

            mainGrid.getChildren().remove(protagonistaView);
            mainGrid.add(protagonistaView, posY, posX);

            movimientosRestantes--;
            movimientosFaltantes.setText(String.valueOf(movimientosRestantes));

            // Verifica si los movimientos han llegado a 0
            if (movimientosRestantes <= 0) {
                System.out.println("Movimientos restantes son 0, subiendo de nivel...");
                protagonista.subirNivel(); // Llamada para subir de nivel
                movimientosRestantes = 15; // Restablecer los movimientos restantes
                movimientosFaltantes.setText(String.valueOf(movimientosRestantes));

                // Actualizar solo los números de los atributos en la interfaz
                nivel.setText(String.valueOf(protagonista.getNivel()));
                ataque.setText(String.valueOf(protagonista.getAtaque()));
                escudo.setText(String.valueOf(protagonista.getDefensa()));
                velocidad.setText(String.valueOf(protagonista.getVelocidad()));
                vida.setText(String.valueOf(protagonista.getVida()));
            }

            moverEnemigos();
        }

        System.out.println("Intentando mover a (" + dx + ", " + dy + ")");
        System.out.println("Después de mover: (" + protagonista.getFila() + ", " + protagonista.getColumna() + ")");
    }

    private void moverEnemigos() {
        Set<String> posicionesOcupadas = new HashSet<>();
        for (Enemigo enemigo : listaEnemigos) {
            posicionesOcupadas.add(enemigo.getFila() + "," + enemigo.getColumna());
        }

        posicionesOcupadas.add(protagonista.getFila() + "," + protagonista.getColumna());
        for (Enemigo enemigo : listaEnemigos) {
            String direccion = enemigo.moverInteligente(posX, posY, escenario, posicionesOcupadas);

        }
        mostrarEnemigos(50, 50);
    }

    private void combatirEnemigosAdyacentes() {
        int x = protagonista.getFila();
        int y = protagonista.getColumna();

        ArrayList<Enemigo> enemigosAdyacentes = new ArrayList<>();

        for (Enemigo enemigo : listaEnemigos) {
            int ex = enemigo.getFila();
            int ey = enemigo.getColumna();

            boolean esAdyacente = (Math.abs(x - ex) == 1 && y == ey) || (Math.abs(y - ey) == 1 && x == ex);
            if (esAdyacente) {
                enemigosAdyacentes.add(enemigo);
            }
        }

        for (Enemigo enemigo : enemigosAdyacentes) {
            // Combate 1 vs 1
            if (protagonista.getVelocidad() >= enemigo.getVelocidad()) {
                protagonista.atacar(enemigo);
                System.out.println("Protagonista ataca primero.");
                if (!enemigo.estaMuerto()) {
                    enemigo.atacar(protagonista);
                    System.out.println("Enemigo contraataca.");
                }
            } else {
                enemigo.atacar(protagonista);
                System.out.println("Enemigo ataca primero.");
                if (protagonista.getVida() > 0) {
                    protagonista.atacar(enemigo);
                    System.out.println("Protagonista contraataca.");
                }
            }

            if (enemigo.estaMuerto()) {
                System.out.println("Enemigo derrotado: " + enemigo.getTipo());
                listaEnemigos.remove(enemigo);
                break; // Para evitar `ConcurrentModificationException`
            }

            if (protagonista.getVida() <= 0) {
                System.out.println("¡Has muerto!");
                // Aquí podrías llamar a una escena de Game Over
                break;
            }
        }

        // Actualiza vista
        actualizarDatosProtagonista();
        mostrarEnemigos(50, 50);
    }

    private void combatirSiHayEnemigoCerca(int nuevaFila, int nuevaColumna) {
        Enemigo enemigoEnCasilla = null;

        for (Enemigo enemigo : listaEnemigos) {
            if (enemigo.getFila() == nuevaFila && enemigo.getColumna() == nuevaColumna) {
                enemigoEnCasilla = enemigo;
                break;
            }
        }

        if (enemigoEnCasilla != null) {
            Protagonista p = Proveedor.getInstance().getProtagonista();

            System.out.println("¡Combate entre Protagonista y " + enemigoEnCasilla.getTipo() + "!");

            if (p.getVelocidad() >= enemigoEnCasilla.getVelocidad()) {
                enemigoEnCasilla.recibirDanio(p.getAtaque());
                if (enemigoEnCasilla.estaMuerto()) {
                    listaEnemigos.remove(enemigoEnCasilla);
                    mostrarMapa(); // para que desaparezca del mapa
                    System.out.println("¡Enemigo derrotado!");
                } else {
                    p.reducirVida(enemigoEnCasilla.getAtaque());
                    if (p.estaMuerto()) {
                        System.out.println("¡Has muerto!");
                        // Aquí podrías cambiar de escena o mostrar un mensaje de derrota
                    }
                }
            } else {
                p.reducirVida(enemigoEnCasilla.getAtaque());
                if (p.estaMuerto()) {
                    System.out.println("¡Has muerto!");
                } else {
                    enemigoEnCasilla.recibirDanio(p.getAtaque());
                    if (enemigoEnCasilla.estaMuerto()) {
                        listaEnemigos.remove(enemigoEnCasilla);
                        mostrarMapa();
                        System.out.println("¡Enemigo derrotado!");
                    }
                }
            }

            actualizarDatosProtagonista();
        }
    }

    private boolean puedeMoverA(int x, int y) {
        char[][] mapa = escenario.getMapa();
        if (x < 0 || x >= mapa.length || y < 0 || y >= mapa[0].length) {
            return false;
        }
        return mapa[x][y] == 'S';
    }

    private Image cargarImagen(String ruta) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(ruta)));
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + ruta);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onChange() {
        actualizarDatosProtagonista();

    }
}