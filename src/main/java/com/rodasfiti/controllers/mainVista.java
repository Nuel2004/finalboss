package com.rodasfiti.controllers;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import java.net.URL;
import com.rodasfiti.SceneID;
import com.rodasfiti.SceneManager;
import com.rodasfiti.model.Protagonista;
import com.rodasfiti.model.Proveedor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

/**
 * Controlador principal de la vista en el menú principal del juego.
 * Se encarga de gestionar los controles de la interfaz gráfica de usuario
 * (GUI),
 * como los sliders para modificar los atributos del protagonista, la música de
 * fondo y la transición entre pantallas.
 */
public class mainVista {

    /** Etiqueta que muestra el valor de vida del protagonista. */
    @FXML
    private Label labelVida;

    /** Etiqueta que muestra el valor de defensa del protagonista. */
    @FXML
    private Label labelDefensa;

    /** Etiqueta que muestra el valor de ataque del protagonista. */
    @FXML
    private Label labelAtaque;

    /** Etiqueta que muestra el valor de velocidad del protagonista. */
    @FXML
    private Label labelVelocidad;

    /** Contenedor principal de la interfaz gráfica. */
    @FXML
    private AnchorPane contenedorPrincipal;

    /** Imagen del protagonista que se muestra en la interfaz. */
    @FXML
    private ImageView imagePersonaje;

    /** Slider para ajustar la vida del protagonista. */
    @FXML
    private Slider SliderVida;

    /** Imagen que representa la vida del protagonista en la interfaz. */
    @FXML
    private ImageView imageVida;

    /** Imagen que representa la defensa del protagonista en la interfaz. */
    @FXML
    private ImageView imageDefensa;

    /** Slider para ajustar la defensa del protagonista. */
    @FXML
    private Slider SliderDefensa;

    /** Imagen que representa el ataque del protagonista en la interfaz. */
    @FXML
    private ImageView imageAtaque;

    /** Campo de texto donde el usuario ingresa el nombre del protagonista. */
    @FXML
    private TextField nombrePersonaje;

    /** Slider para ajustar el ataque del protagonista. */
    @FXML
    private Slider SliderAtaque;

    /** Slider para ajustar la velocidad del protagonista. */
    @FXML
    private Slider SliderVelocidad;

    /**
     * Indicador de progreso que muestra el total de puntos asignados a los
     * atributos del protagonista.
     */
    @FXML
    private ProgressIndicator porcentajeAtributos;

    /**
     * Botón para iniciar el juego y realizar la transición a la pantalla de juego.
     */
    @FXML
    private Button botonJugar;

    /** Vista de video utilizada para mostrar la música de fondo. */
    @FXML
    private MediaView musica;

    /** Reproductor de medios que controla la música de fondo. */
    @FXML
    private MediaPlayer mediaPlayer;

    /** Contenedor de la imagen de fondo del menú principal. */
    @FXML
    private StackPane fondoImagen;

    /** Imagen del fondo del castillo en la interfaz. */
    @FXML
    private ImageView fondoCastillo;

    /**
     * Número máximo de puntos que se pueden asignar a los atributos del
     * protagonista.
     */
    private static final int MAX_PUNTOS = 20;

    /**
     * Inicializa los controles de la interfaz y los eventos de los sliders.
     * Asocia las acciones de cada slider con la actualización de los atributos del
     * protagonista.
     */
    @FXML
    public void initialize() {
        SliderVida.valueProperty().addListener((obs, oldVal, newVal) -> {
            int valor = newVal.intValue();
            labelVida.setText(String.valueOf(valor));
            manejarCambioSlider(SliderVida, oldVal, newVal);
        });

        SliderAtaque.valueProperty().addListener((obs, oldVal, newVal) -> {
            int valor = newVal.intValue();
            labelAtaque.setText(String.valueOf(valor));
            manejarCambioSlider(SliderAtaque, oldVal, newVal);
        });

        SliderDefensa.valueProperty().addListener((obs, oldVal, newVal) -> {
            int valor = newVal.intValue();
            labelDefensa.setText(String.valueOf(valor));
            manejarCambioSlider(SliderDefensa, oldVal, newVal);
        });

        SliderVelocidad.valueProperty().addListener((obs, oldVal, newVal) -> {
            int valor = newVal.intValue();
            labelVelocidad.setText(String.valueOf(valor));
            manejarCambioSlider(SliderDefensa, oldVal, newVal);
        });

        if (nombrePersonaje != null) {
            nombrePersonaje.textProperty().addListener((observable, oldValue, newValue) -> actualizarProtagonista());
            actualizarProtagonista();
        }

        botonJugar.setOnAction(event -> {
            if (mediaPlayer != null) {
                // Transición de volumen (fade out)
                new Thread(() -> {
                    try {
                        for (double vol = mediaPlayer.getVolume(); vol > 0; vol -= 0.05) {
                            final double v = vol;
                            javafx.application.Platform.runLater(() -> mediaPlayer.setVolume(v));
                            Thread.sleep(100); // esperar 100 ms entre cada paso
                        }
                        javafx.application.Platform.runLater(() -> mediaPlayer.stop());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        javafx.application.Platform.runLater(() -> mediaPlayer.stop());
                    }
                }).start();
            }
            actualizarProtagonista(); // Actualizar los datos del protagonista
            SceneManager.getInstance().loadScene(SceneID.JUEGO);
            VistaJuego controlador = (VistaJuego) SceneManager.getInstance().getController(SceneID.JUEGO);
            if (controlador != null) {
                controlador.reiniciarMusica();
                controlador.actualizarDatosProtagonista();
            }
        });

        cargarMusica(); // Cargar y reproducir la música de fondo
    }

    /**
     * Carga la música de fondo y la configura para reproducción continua.
     * La pista se reproduce automáticamente en bucle y con volumen inicial del 60%.
     * Si el archivo no se encuentra, muestra un mensaje de error en consola.
     */
    private void cargarMusica() {
        try {
            // Ruta fija (recomendado si sabes el nombre del archivo)
            String rutaAudio = "/com/rodasfiti/media/Lord_of_the_Rings_Sound_of_The_Shire.mp3";

            URL url = getClass().getResource(rutaAudio);

            if (url == null) {
                System.err.println("Archivo de audio no encontrado: " + rutaAudio);
            } else {
                Media media = new Media(url.toExternalForm());
                this.mediaPlayer = new MediaPlayer(media); // Asignamos la instancia de MediaPlayer al campo
                musica.setMediaPlayer(this.mediaPlayer); // Usamos el mediaPlayer de la clase
                this.mediaPlayer.setAutoPlay(true);
                this.mediaPlayer.setVolume(0.6);
                this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                this.mediaPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar audio: " + e.getMessage());
        }
    }

    /**
     * Maneja los cambios en los sliders de los atributos del protagonista.
     * Si la suma total de puntos supera el máximo, restaura el valor anterior.
     * De lo contrario, actualiza el progreso y los atributos del protagonista.
     *
     * @param sliderModificado El slider que fue modificado.
     * @param oldVal           Valor anterior del slider.
     * @param newVal           Valor nuevo del slider.
     */
    private void manejarCambioSlider(Slider sliderModificado, Number oldVal, Number newVal) {
        int vida = (int) SliderVida.getValue();
        int ataque = (int) SliderAtaque.getValue();
        int defensa = (int) SliderDefensa.getValue();
        int velocidad = (int) SliderVelocidad.getValue();
        int total = vida + ataque + defensa + velocidad;
        if (total > MAX_PUNTOS) {
            sliderModificado.setValue(oldVal.doubleValue());
        } else {
            actualizarProgreso();
            actualizarProtagonista();
        }
    }

    /**
     * Actualiza el indicador de progreso que muestra el porcentaje de puntos
     * asignados a los atributos.
     */
    private void actualizarProgreso() {
        int vida = (int) SliderVida.getValue();
        int ataque = (int) SliderAtaque.getValue();
        int defensa = (int) SliderDefensa.getValue();
        int velocidad = (int) SliderVelocidad.getValue();

        int total = vida + ataque + defensa + velocidad;
        double progreso = Math.min(total, MAX_PUNTOS) / (double) MAX_PUNTOS;

        porcentajeAtributos.setProgress(progreso);
    }

    /**
     * Actualiza los atributos del protagonista con los valores actuales de la
     * interfaz.
     * Crea una nueva instancia del protagonista y la almacena en el proveedor de
     * datos.
     */
    private void actualizarProtagonista() {
        String nombre = nombrePersonaje.getText();
        int vida = Integer.parseInt(labelVida.getText());
        int ataque = Integer.parseInt(labelAtaque.getText());
        int defensa = Integer.parseInt(labelDefensa.getText());
        int velocidad = Integer.parseInt(labelVelocidad.getText());
        int atributos = (int) Math.round(porcentajeAtributos.getProgress() * MAX_PUNTOS);
        int nivel = 1;

        Protagonista protagonista = new Protagonista(nombre, vida, ataque, defensa, atributos, nivel, velocidad);
        Proveedor.getInstance().setProtagonista(protagonista);
    }
}