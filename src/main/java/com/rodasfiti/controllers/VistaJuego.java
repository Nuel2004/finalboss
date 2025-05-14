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
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Clase que representa la vista del juego, incluyendo la visualización del
 * escenario, el protagonista, los enemigos,
 * y la lógica para gestionar el movimiento del protagonista, los enemigos, el
 * combate y la música del juego.
 */
public class VistaJuego implements Observer {

    /**
     * Etiqueta que muestra la vida del protagonista.
     */
    @FXML
    private Label vida;

    /**
     * Vista del reproductor de música para el nivel.
     */
    @FXML
    private MediaView musicaLevel;

    /**
     * Reproductor de medios para la música de fondo.
     */
    @FXML
    private MediaPlayer mediaPlayer1;
    /**
    * Reproductor de medios para la música de fondo.
    */
    @FXML
    private MediaPlayer mediaPlayer2;

    /**
     * Etiqueta que muestra la velocidad del protagonista.
     */
    @FXML
    private Label velocidad;

    /**
     * Etiqueta que muestra el valor del escudo del protagonista.
     */
    @FXML
    private Label escudo;

    /**
     * Etiqueta que muestra el valor de ataque del protagonista.
     */
    @FXML
    private Label ataque;

    /**
     * Etiqueta que muestra el nivel del protagonista.
     */
    @FXML
    private Label nivel;

    /**
     * Etiqueta que muestra el nombre del protagonista.
     */
    @FXML
    private Label nombrePersonaje;

    /**
     * Etiqueta que muestra los movimientos restantes del protagonista.
     */
    @FXML
    private Label movimientosFaltantes;

    /**
     * Contenedor principal del mapa del juego.
     */
    @FXML
    private AnchorPane contenedorMapa;

    /**
     * GridPane que contiene los elementos visuales del mapa del juego.
     */
    private GridPane mainGrid;

    /**
     * Posición en el eje X del protagonista.
     */
    private int posX = 1;

    /**
     * Posición en el eje Y del protagonista.
     */
    private int posY = 1;

    /**
     * Vista gráfica que representa al protagonista.
     */
    private ImageView protagonistaView;

    /**
     * Objeto que representa el escenario del juego.
     */
    private Escenario escenario;

    /**
     * Número de movimientos restantes para el protagonista en cada turno.
     */
    private int movimientosRestantes = 15;

    /**
     * Lista de enemigos presentes en el escenario.
     */
    private ArrayList<Enemigo> listaEnemigos = new ArrayList<>();

    /**
     * Lista de vistas gráficas de los enemigos.
     */
    private ArrayList<ImageView> vistasEnemigos = new ArrayList<>();

    /**
     * Objeto que representa al protagonista en el juego.
     */
    private Protagonista protagonista;

    /**
     * Método que inicializa los elementos del juego, carga el escenario y prepara
     * la vista.
     */
    @FXML
    public void initialize() {
        System.out.println(">>> [INICIO initialize()]");

        // Configuración inicial de los movimientos restantes
        movimientosFaltantes.setText(String.valueOf(movimientosRestantes));
        mainGrid = new GridPane();
        mainGrid.setPadding(new Insets(10));
        mainGrid.setHgap(0);
        mainGrid.setVgap(0);
        AnchorPane.setTopAnchor(mainGrid, 0.0);
        AnchorPane.setLeftAnchor(mainGrid, 0.0);
        AnchorPane.setRightAnchor(mainGrid, 0.0);
        AnchorPane.setBottomAnchor(mainGrid, 0.0);

        escenario = cargarEscenarioDesdeRecursos("/com/rodasfiti/data/mapaprueba2.csv");

        /**
         * Verifica si el escenario ha sido cargado correctamente. Si es así, configura
         * el protagonista,
         * asigna una posición aleatoria, actualiza los datos en la interfaz, carga los
         * enemigos desde un archivo
         * CSV y muestra el mapa. Si el escenario es {@code null}, muestra un mensaje de
         * error.
         */
        if (escenario != null) {
            // Configuración del protagonista
            protagonista = Proveedor.getInstance().getProtagonista();
            System.out.println(
                    "ANTES de setPosicionAleatoria: " + protagonista.getFila() + "," + protagonista.getColumna());

            protagonista.setPosicionAleatoria(escenario);
            Proveedor.getInstance().setProtagonista(protagonista);

            System.out.println(
                    "DESPUÉS de setPosicionAleatoria: " + protagonista.getFila() + "," + protagonista.getColumna());

            // Actualizar la interfaz con los datos del protagonista
            actualizarDatosProtagonista();

            // Cargar enemigos desde archivo CSV
            listaEnemigos = GestorEnemigos.cargarEnemigosDesdeCSV("/com/rodasfiti/data/enemigos.csv");

            System.out.println("ANTES de mostrarMapa: " + protagonista.getFila() + "," + protagonista.getColumna());

            // Mostrar el mapa en la vista
            mostrarMapa();
            
            
        } else {
            System.err.println("No se pudo cargar el escenario.");
        }
        contenedorMapa.getChildren().add(mainGrid);

        mover();

        System.out.println(">>> [FIN initialize()]");
    }

    /**
     * Actualiza la información mostrada del protagonista en la interfaz de usuario.
     */
    void actualizarDatosProtagonista() {
        Protagonista protagonista = Proveedor.getInstance().getProtagonista();
        nombrePersonaje.setText(protagonista.getNombre());
        vida.setText(String.valueOf(protagonista.getVida()));
        ataque.setText(String.valueOf(protagonista.getAtaque()));
        escudo.setText(String.valueOf(protagonista.getDefensa()));
        velocidad.setText(String.valueOf(protagonista.getVelocidad()));
        nivel.setText(String.valueOf(protagonista.getNivel()));
    }

    /**
     * Carga el escenario desde un archivo CSV ubicado en la ruta proporcionada.
     * 
     * @param ruta Ruta del archivo CSV que contiene la información del escenario.
     * @return Un objeto {@link Escenario} con los datos cargados, o null si no se
     *         pudo cargar.
     */
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

    /**
     * Método encargado de mostrar el mapa en la interfaz gráfica.
     * Este método genera una representación visual del mapa cargado desde el
     * escenario.
     * Las casillas del mapa se representan como bloques de imágenes, donde las
     * casillas de
     * tipo 'P' (pared) se muestran como imágenes de pared, y las de tipo 'S'
     * (suelo) como imágenes de suelo.
     * Además, se coloca al protagonista en su posición actual y se actualiza la
     * vista de los enemigos.
     */
    private void mostrarMapa() {
        char[][] mapa = escenario.getMapa();
        mainGrid.getChildren().clear();
        vistasEnemigos.clear();
        int filas = mapa.length;
        int columnas = mapa[0].length;
        int ancho = 973 / columnas;
        int alto = 673 / filas;

        Image imagenPared = cargarImagen("/com/rodasfiti/images/pared2.png");
        Image imagenSuelo = cargarImagen("/com/rodasfiti/images/suelo2.png");
        Image imageTrampa = cargarImagen("/com/rodasfiti/images/suelo.png");

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                ImageView bloque;
                if (mapa[i][j] == 'P') {
                    bloque = new ImageView(imagenPared);
                } else if(mapa[i][j] == 'S'){
                    bloque = new ImageView(imagenSuelo);
                } else{
                    bloque = new ImageView(imageTrampa);
                }
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

    /**
     * Método encargado de generar y colocar enemigos en el mapa.
     * Los enemigos se cargan desde un archivo CSV, se clasifican por tipo y luego
     * se seleccionan
     * aleatoriamente para su aparición en el mapa. Se intenta colocar un enemigo en
     * una posición
     * válida, repitiendo el proceso si no es posible.
     *
     * @param cantidad El número de enemigos que se desea generar y colocar en el
     *                 mapa.
     */
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
        listaEnemigos.addAll(enemigosASpawn);
        vistasEnemigos.clear();
        mostrarMapa();
        mostrarMapa();
    }

    /**
     * Método que verifica si una posición (x, y) es válida para colocar un enemigo.
     * La posición es válida si está dentro de los límites del mapa, si es una
     * casilla de suelo
     * y si no coincide con la posición actual del protagonista.
     *
     * @param x La fila de la posición a verificar.
     * @param y La columna de la posición a verificar.
     * @return `true` si la posición es válida, `false` en caso contrario.
     */
    private boolean esPosicionValida(int x, int y) {
        return x >= 0 && y >= 0 && x < escenario.getMapa().length && y < escenario.getMapa()[0].length
                && puedeMoverA(x, y) && !(x == posX && y == posY);
    }

    /**
     * Método encargado de mostrar a los enemigos en el mapa.
     * Este método recorre la lista de enemigos y, si su posición es válida (es
     * decir,
     * una casilla de suelo), los coloca en el mapa con la imagen correspondiente a
     * su tipo.
     * Además, asegura que las imágenes se ajusten al tamaño de la celda en el mapa.
     *
     * @param ancho El ancho de las celdas del mapa para ajustar el tamaño de las
     *              imágenes de los enemigos.
     * @param alto  El alto de las celdas del mapa para ajustar el tamaño de las
     *              imágenes de los enemigos.
     */
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

    /**
     * Método encargado de configurar el evento de teclado para mover al
     * protagonista.
     * Establece el contenedor del mapa como focos de entrada y asigna el evento que
     * se ejecutará
     * cuando una tecla sea presionada, lo cual invoca el método
     * `moverProtagonista`.
     */
    private void mover() {
        contenedorMapa.setFocusTraversable(true);
        contenedorMapa.requestFocus();
        contenedorMapa.setOnKeyPressed(this::moverProtagonista);
    }

    /**
     * Método encargado de mover al protagonista en el mapa en función de la tecla
     * presionada.
     * Determina la dirección de movimiento (arriba, abajo, izquierda, derecha) y
     * actualiza la posición
     * del protagonista. Si el protagonista se mueve a una casilla ocupada por un
     * enemigo, se inicia un combate.
     * Además, maneja la lógica de los movimientos restantes y el nivel del
     * protagonista.
     *
     * @param e El evento de teclado que contiene la tecla presionada.
     */
    private void moverProtagonista(KeyEvent e) {
        System.out.println("Tecla pulsada: " + e.getCode());
        int c = 1;

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
            casillaTrampa(protagonista.getFila(), protagonista.getColumna());
            // Verifica si los movimientos han llegado a 0
            if (movimientosRestantes <= 0) {
                c++;
                subirNivel();
                spawnEnemigos(c);
                movimientosRestantes = 15;
                movimientosFaltantes.setText(String.valueOf(movimientosRestantes));
            }
            moverEnemigos();
        }

        System.out.println("Intentando mover a (" + dx + ", " + dy + ")");
        System.out.println("Después de mover: (" + protagonista.getFila() + ", " + protagonista.getColumna() + ")");
    }

    /**
     * Método encargado de mover a los enemigos en el mapa de manera inteligente.
     * Los enemigos intentan acercarse al protagonista utilizando una lógica de
     * movimiento
     * en función de la posición del protagonista y las posiciones ocupadas en el
     * mapa.
     * Después de mover a los enemigos, se actualiza la vista del mapa.
     */
    private void moverEnemigos() {
        Set<String> posicionesOcupadas = new HashSet<>();
        for (Enemigo enemigo : listaEnemigos) {
            posicionesOcupadas.add(enemigo.getFila() + "," + enemigo.getColumna());
        }

        posicionesOcupadas.add(protagonista.getFila() + "," + protagonista.getColumna());
        for (Enemigo enemigo : listaEnemigos) {
            String direccion = enemigo.moverInteligente(posX, posY, escenario, posicionesOcupadas);
            System.out.println("El enemigo se movió hacia: " + direccion);
        }
        mostrarEnemigos(50, 50);
    }

    /**
     * Realiza un combate entre el protagonista y un enemigo si el enemigo se
     * encuentra
     * en la misma casilla que el protagonista. El combate se resuelve por turnos,
     * en
     * función de la velocidad de cada uno. Si el protagonista o el enemigo mueren
     * durante
     * el combate, se toman las acciones correspondientes, como eliminar al enemigo
     * o terminar el juego.
     * 
     * @param nuevaFila    La fila de la casilla a la que el protagonista intentó
     *                     moverse.
     * @param nuevaColumna La columna de la casilla a la que el protagonista intentó
     *                     moverse.
     */
    private void combatirSiHayEnemigoCerca(int nuevaFila, int nuevaColumna) {
        Enemigo enemigoEnCasilla = null;

        // Busca si hay un enemigo en la casilla donde el protagonista se ha movido
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
                    mostrarMapa(); // Para que desaparezca del mapa
                    System.out.println("¡Enemigo derrotado!");
                } else {
                    p.reducirVida(enemigoEnCasilla.getAtaque());
                    if (p.estaMuerto()) {
                        System.out.println("¡Has muerto!");
                        SceneManager.getInstance().loadScene(SceneID.FINAL);
                        finalJuego controller = (finalJuego) SceneManager.getInstance().getController(SceneID.FINAL);
                        if (controller != null) {
                            controller.reiniciarMusica();
                        }
                        return;
                    }
                }
            } else {
                p.reducirVida(enemigoEnCasilla.getAtaque());
                if (p.estaMuerto()) {
                    this.mediaPlayer1.stop();
                    System.out.println("¡Has muerto!");
                    SceneManager.getInstance().loadScene(SceneID.FINAL);
                    finalJuego controller = (finalJuego) SceneManager.getInstance().getController(SceneID.FINAL);
                    if (controller != null) {
                        controller.reiniciarMusica();
                    }
                    return;
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

    /**
     * Determina si el protagonista puede moverse a la casilla especificada.
     * Verifica si las coordenadas están dentro de los límites del mapa y si la
     * casilla
     * está disponible para ser ocupada (es decir, si es un espacio vacío 'S').
     * 
     * @param x La fila de la casilla a verificar.
     * @param y La columna de la casilla a verificar.
     * @return true si la casilla es válida para moverse, false en caso contrario.
     */
    private boolean puedeMoverA(int columna, int fila) {
        char[][] mapa = escenario.getMapa();
        return fila >= 0 && fila < mapa.length &&
                columna >= 0 && columna < mapa[0].length &&
                mapa[fila][columna] == 'S' || mapa[fila][columna] == 'T';
    }

    /**
     * Carga una imagen desde la ruta especificada y la devuelve como un objeto
     * {@link Image}.
     * Si ocurre un error al cargar la imagen, se imprime un mensaje de error y se
     * devuelve null.
     * 
     * @param ruta La ruta de la imagen a cargar.
     * @return La imagen cargada, o null si no se pudo cargar.
     */
    private Image cargarImagen(String ruta) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(ruta)));
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + ruta);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Carga la música del nivel actual y la reproduce. Si la música no se
     * encuentra, se imprime un mensaje de error.
     * La música se reproduce con un volumen del 60%.
     */
    private void cargarMusicaLevel() {
        try {
            // Ruta fija (recomendado si sabes el nombre del archivo)
            String rutaAudio = "/com/rodasfiti/media/trompeta.mp3";

            URL url = getClass().getResource(rutaAudio);

            if (url == null) {
                System.err.println("Archivo de audio no encontrado: " + rutaAudio);
            } else {
                Media media = new Media(url.toExternalForm());
                this.mediaPlayer2 = new MediaPlayer(media); // Asignamos la instancia de MediaPlayer al campo
                musicaLevel.setMediaPlayer(this.mediaPlayer2); // Usamos el mediaPlayer1 de la clase
                this.mediaPlayer2.setAutoPlay(true);
                this.mediaPlayer2.setVolume(1);
                this.mediaPlayer2.play();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar audio: " + e.getMessage());
        }
    }

    /**
     * Carga la música del juego y la reproduce. Si la música no se encuentra, se
     * imprime un mensaje de error.
     * La música se reproduce con un volumen del 60%.
     */
    private void cargarMusica() {
        try {
            // Ruta fija (recomendado si sabes el nombre del archivo)
            String rutaAudio = "/com/rodasfiti/media/juego.mp3";

            URL url = getClass().getResource(rutaAudio);

            if (url == null) {
                System.err.println("Archivo de audio no encontrado: " + rutaAudio);
            } else {
                Media media = new Media(url.toExternalForm());
                this.mediaPlayer1 = new MediaPlayer(media); // Asignamos la instancia de MediaPlayer al campo
                musicaLevel.setMediaPlayer(this.mediaPlayer1); // Usamos el mediaPlayer1 de la clase
                this.mediaPlayer1.setAutoPlay(true);
                this.mediaPlayer1.setVolume(0.6);
                this.mediaPlayer1.play();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar audio: " + e.getMessage());
        }
    }

    /**
     * Aumenta el nivel del protagonista y actualiza sus atributos. Incrementa el
     * nivel, la vida,
     * el ataque, la defensa y la velocidad del protagonista. Luego, se actualiza la
     * interfaz de usuario
     * con los nuevos valores y se carga la música correspondiente al nivel.
     */
    private void subirNivel() {
        Protagonista protagonista = Proveedor.getInstance().getProtagonista();
        protagonista.setNivel(protagonista.getNivel() + 1);
        protagonista.setVida(protagonista.getVida() + 1);
        protagonista.setAtaque(protagonista.getAtaque() + 1);
        protagonista.setDefensa(protagonista.getDefensa() + 1);
        protagonista.setVelocidad(protagonista.getVelocidad());
        actualizarDatosProtagonista();
        cargarMusicaLevel();
    }

    /**
     * Reinicia la música del juego. Detiene la música actual con un efecto de
     * fade-out y luego
     * recarga la música inicial del juego.
     */
    public void reiniciarMusicaJuego() {
        if (mediaPlayer1 != null) {
            mediaPlayer1.stop(); // Detiene la música actual.
        }
        cargarMusica();
    }

    public void casillaTrampa(int x, int y){
        Protagonista protagonista = Proveedor.getInstance().getProtagonista();
        char[][] mapa = escenario.getMapa();
        int vidanueva= protagonista.getVida();
        System.out.println("Antes del if ="+ vidanueva);
        if(mapa[x][y] == 'T'){
            vidanueva = vidanueva-1;
            System.out.println("He entrado en el if");
            System.out.println("Nueva vida = "+vidanueva);
            protagonista.setVida(vidanueva);
            actualizarDatosProtagonista();
        }
        if(protagonista.getVida()==0){
            SceneManager.getInstance().loadScene(SceneID.FINAL);
        }
    }

    /**
     * Método de callback que se llama cuando ocurre un cambio en el modelo de
     * datos.
     * Actualiza los datos del protagonista en la interfaz de usuario.
     */
    @Override
    public void onChange() {
        actualizarDatosProtagonista();
    }
}