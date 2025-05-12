package com.rodasfiti.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import com.rodasfiti.SceneID;
import com.rodasfiti.SceneManager;
import com.rodasfiti.interfaces.Observer;
import com.rodasfiti.model.*;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.HashSet;
//import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class VistaJuego implements Observer {
    @FXML
    private MediaView musicaLevel;

    @FXML
    private Label vida;

    @FXML
    private Label duendes;

    @FXML
    private Label orcos;

    @FXML
    private Label esqueletos;

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
    //private static Random r = new Random();
    private ArrayList<Enemigo> listaEnemigos = new ArrayList<>();
    private ArrayList<ImageView> vistasEnemigos = new ArrayList<>();
    private Protagonista protagonista;
    private MediaPlayer mediaPlayer;

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
        if (movimientosRestantes <= 0) {
            subirNivel();
            spawnEnemigos(Proveedor.getInstance().getProtagonista().getNivel());
            movimientosRestantes = 15;
            movimientosFaltantes.setText(String.valueOf(movimientosRestantes));
        }

        actualizarDatosEnemigos();
        contenedorMapa.getChildren().add(mainGrid);

        mover();

        System.out.println(">>> [FIN initialize()]");
    }

    public void actualizarDatosProtagonista() {
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

    // Método para mover al protagonista
    private void moverProtagonista(KeyEvent e) {
        System.out.println("Tecla pulsada: " + e.getCode());

        int dx = 0;
        int dy = 0;
        String img = "/com/rodasfiti/images/";

        // Determina la dirección en la que se mueve el protagonista
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
                return; // No hace nada si no es una tecla válida
        }

        int nuevaFila = protagonista.getFila() + dx;
        int nuevaColumna = protagonista.getColumna() + dy;

        // Verifica si el movimiento es válido y si no hay un enemigo en la nueva posición
        boolean enemigoEncontrado = false;
        Enemigo enemigoAAtacar = null;
        
        for (Enemigo enemigo : listaEnemigos) {
            if (enemigo.getFila() == nuevaFila && enemigo.getColumna() == nuevaColumna) {
                System.out.println("Enemigo encontrado en (" + nuevaFila + ", " + nuevaColumna + ")");
                enemigoEncontrado = true;
                enemigoAAtacar = enemigo;
                break;
            }
        }
        
        // Si hay un enemigo en la casilla destino, atacarlo directamente
        if (enemigoEncontrado && enemigoAAtacar != null) {
            System.out.println("Atacando a enemigo: " + enemigoAAtacar.getTipo());
            if (protagonista.atacarSiCerca(enemigoAAtacar)) {
                System.out.println("Ataque realizado con éxito");
                if (enemigoAAtacar.getVida() <= 0) {
                    System.out.println("Enemigo derrotado");
                    listaEnemigos.remove(enemigoAAtacar);
                    mostrarMapa();
                }
                
                if (protagonista.getVida() <= 0) {
                    System.out.println("El protagonista ha muerto");
                    SceneManager.getInstance().loadScene(SceneID.FINAL);
                    return;
                }
                
                actualizarDatosProtagonista();
                actualizarDatosEnemigos();
            }
            return;
        }

        // Si el movimiento es válido, mueve al protagonista
        if (protagonista.mover(dx, dy, escenario, listaEnemigos)) {
            posX = protagonista.getFila();
            posY = protagonista.getColumna();

            // Actualiza la imagen del protagonista y su posición en el mapa
            protagonistaView.setImage(cargarImagen(img));
            mainGrid.getChildren().remove(protagonistaView);
            mainGrid.add(protagonistaView, posY, posX);

            // Actualiza los movimientos restantes
            movimientosRestantes--;
            movimientosFaltantes.setText(String.valueOf(movimientosRestantes));

            // Verifica el cambio de nivel
            if (movimientosRestantes <= 0) {
                subirNivel();
                spawnEnemigos(Proveedor.getInstance().getProtagonista().getNivel());
                movimientosRestantes = 15;
                movimientosFaltantes.setText(String.valueOf(movimientosRestantes));
            }
            
            // Mueve a los enemigos después de que el protagonista se mueve
            moverEnemigos();

            // Ahora sí comprobamos si hay enemigos adyacentes para atacar
            detectarYAtacarEnemigos();
        }

        System.out.println("Intentando mover a (" + dx + ", " + dy + ")");
        System.out.println("Después de mover: (" + protagonista.getFila() + ", " + protagonista.getColumna() + ")");
    }

    private void detectarYAtacarEnemigos() {
        Protagonista protagonista = Proveedor.getInstance().getProtagonista();
        ArrayList<Enemigo> enemigosAEliminar = new ArrayList<>();

        int filaProta = protagonista.getFila();
        int colProta = protagonista.getColumna();

        System.out.println("[DEBUG] Posición del protagonista: fila=" + filaProta + ", columna=" + colProta);

        for (Enemigo enemigo : listaEnemigos) {
            int filaEnemigo = enemigo.getFila();
            int colEnemigo = enemigo.getColumna();

            int dx = Math.abs(filaProta - filaEnemigo);
            int dy = Math.abs(colProta - colEnemigo);

            System.out.println("[DEBUG] Revisando enemigo en fila=" + filaEnemigo + ", columna=" + colEnemigo);

            if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
                System.out.println("[DEBUG] Enemigo adyacente encontrado");

                if (protagonista.atacarSiCerca(enemigo)) {
                    System.out.println("[DEBUG] Combate iniciado con " + enemigo.getTipo());

                    if (enemigo.getVida() <= 0) {
                        System.out.println("[DEBUG] Enemigo derrotado");
                        enemigosAEliminar.add(enemigo);
                    }

                    if (protagonista.getVida() <= 0) {
                        System.out.println("[DEBUG] El protagonista ha muerto");
                        SceneManager.getInstance().loadScene(SceneID.FINAL);
                        return;
                    }
                } else {
                    System.out.println("[DEBUG] El método atacarSiCerca devolvió false");
                }
            }
        }

        // Remover enemigos muertos
        listaEnemigos.removeAll(enemigosAEliminar);
        mostrarMapa();
        actualizarDatosProtagonista();
        actualizarDatosEnemigos();
    }

    private void moverEnemigos() {
    Set<String> posicionesOcupadas = new HashSet<>();
    for (Enemigo enemigo : listaEnemigos) {
        posicionesOcupadas.add(enemigo.getFila() + "," + enemigo.getColumna());
    }
    posicionesOcupadas.add(protagonista.getFila() + "," + protagonista.getColumna());

    for (Enemigo enemigo : listaEnemigos) {
        String direccion = enemigo.moverInteligente(
            protagonista.getFila(),
            protagonista.getColumna(),
            escenario,
            posicionesOcupadas
        );

        // Actualiza la posición si se mueve
        if (!direccion.equals("QUIETO")) {
            posicionesOcupadas.remove(enemigo.getFila() + "," + enemigo.getColumna());

            int nuevaFila = enemigo.getFila();
            int nuevaColumna = enemigo.getColumna();

            posicionesOcupadas.add(nuevaFila + "," + nuevaColumna);
        }

        // Verifica si está adyacente al protagonista y ataca
        if (estaAdyacente(enemigo.getFila(), enemigo.getColumna(),
                          protagonista.getFila(), protagonista.getColumna())) {
            enemigo.atacar(protagonista);
        }
    }

    mostrarEnemigos(50, 50);
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

    private void subirNivel() {
        Protagonista protagonista = Proveedor.getInstance().getProtagonista();
        protagonista.setNivel(protagonista.getNivel() + 1);
        protagonista.setVida(protagonista.getVida() + 1);
        protagonista.setAtaque(protagonista.getAtaque() + 1);
        protagonista.setDefensa(protagonista.getDefensa() + 1);
        protagonista.setVelocidad(protagonista.getVelocidad());
        actualizarDatosProtagonista();
        cargarMusica();
    }

    private void cargarMusica() {
        try {
            // Ruta fija (recomendado si sabes el nombre del archivo)
            String rutaAudio = "/com/rodasfiti/media/trompeta.mp3";

            URL url = getClass().getResource(rutaAudio);

            if (url == null) {
                System.err.println("Archivo de audio no encontrado: " + rutaAudio);
            } else {
                Media media = new Media(url.toExternalForm());
                this.mediaPlayer = new MediaPlayer(media); // Asignamos la instancia de MediaPlayer al campo
                musicaLevel.setMediaPlayer(this.mediaPlayer); // Usamos el mediaPlayer de la clase
                this.mediaPlayer.setAutoPlay(true);
                this.mediaPlayer.setVolume(0.6);
                this.mediaPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar audio: " + e.getMessage());
        }
    }

    public void actualizarDatosEnemigos() {
        int eq = 0;
        int or = 0;
        int dnd = 0;
        for (Enemigo enemigo : listaEnemigos) {
            switch (enemigo.getTipo()) {
                case ORCO:
                    or++;
                    break;
                case DUENDE:
                    dnd++;
                    break;
                case ESQUELETO:
                    eq++;
                    break;
                default:

                    break;
            }
        }
        orcos.setText("Orco: " + or);
        duendes.setText("Duende: " + dnd);
        esqueletos.setText("Esqueleto: " + eq);
    }
    private boolean estaAdyacente(int fila1, int col1, int fila2, int col2) {
    int df = Math.abs(fila1 - fila2);
    int dc = Math.abs(col1 - col2);
    return (df + dc == 1); // Está justo arriba, abajo, izquierda o derecha
}


    @Override
    public void onChange() {
        actualizarDatosProtagonista();
        actualizarDatosEnemigos();

    }
}

    