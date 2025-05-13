package com.rodasfiti.controllers;

import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;

import java.net.URL;

import com.rodasfiti.SceneID;
import com.rodasfiti.SceneManager;
import javafx.fxml.FXML;

/**
 * La clase {@code finalJuego} es el controlador de la vista final del juego.
 * Esta clase gestiona las acciones de los botones en la interfaz de usuario
 * para
 * finalizar el juego, reiniciar la música y volver al menú principal.
 *
 * <p>
 * Utiliza JavaFX para controlar la interfaz gráfica de la escena final del
 * juego,
 * mostrando opciones como volver a jugar, salir del juego, y reproduciendo
 * música
 * de fondo.
 * 
 */
public class finalJuego {

    /** El botón que permite salir del juego. */
    @FXML
    private Button salir;

    /** El botón que permite volver a jugar. */
    @FXML
    private Button volverAjugar;

    /** El {@code MediaView} que muestra el video de la música de fondo. */
    @FXML
    private MediaView musicaFinal;

    /** El reproductor de música que controla la reproducción del audio. */
    private MediaPlayer mediaPlayer;

    /**
     * Inicializa los controles de la vista final del juego.
     * Configura las acciones de los botones de "volver a jugar" y "salir".
     * Detiene la música si está sonando antes de realizar alguna acción.
     */
    @FXML
    public void initialize() {
        volverAjugar.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop(); // Detiene la música antes de reiniciar el juego.
            }
            SceneManager.getInstance().loadScene(SceneID.MAINVISTA); // Vuelve al menú principal.
        });

        salir.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop(); // Detiene la música antes de cerrar el juego.
            }
            SceneManager.getInstance().getStage().close(); // Cierra la aplicación.
        });
    }

    /**
     * Carga y reproduce la música de fondo para la vista final del juego.
     * La música comienza en volumen 0 y aumenta gradualmente hasta el 60%.
     * Si no se puede cargar el recurso de audio, se muestra un error en la consola.
     */
    private void cargarMusica() {
        URL resource = getClass().getResource("/com/rodasfiti/media/final.mp3");
        System.out.println("Ruta de audio: " + resource); // Agrega esta línea

        if (resource != null) {
            Media sound = new Media(resource.toExternalForm());
            mediaPlayer = new MediaPlayer(sound);
            musica.setMediaPlayer(mediaPlayer); // Opcional, si usas MediaView.

            mediaPlayer.setVolume(0); // Comienza en volumen 0.

            // Aumenta el volumen gradualmente hasta el 60%.
            mediaPlayer.setOnPlaying(() -> {
                new Thread(() -> {
                    try {
                        double targetVolume = 0.6;
                        for (double vol = 0; vol <= targetVolume; vol += 0.02) {
                            double finalVol = vol;
                            javafx.application.Platform.runLater(() -> mediaPlayer.setVolume(finalVol));
                            Thread.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            });

            mediaPlayer.play(); // Empieza a reproducir la música automáticamente.
        } else {
            System.err.println("No se pudo cargar el recurso de audio.");
        }
    }

    /**
     * Reinicia la música de fondo deteniendo el reproductor actual y cargando
     * nuevamente la música desde el principio.
     */
    public void reiniciarMusica() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Detiene la música actual.
        }
        cargarMusica(); // Vuelve a cargar y reproducir la música.
    }
}