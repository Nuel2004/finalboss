package com.rodasfiti;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Clase que gestiona la administración de las diferentes escenas en la
 * aplicación JavaFX.
 * Implementa el patrón Singleton para garantizar una única instancia.
 */
public class SceneManager {

    /** Instancia única de SceneManager. */
    private static SceneManager instance;

    /** El escenario principal donde se cargan las escenas. */
    private Stage stage;

    /** URL de los estilos CSS. */
    private URL styles;

    /** Mapa de ID de escena y sus respectivas escenas. */
    private HashMap<SceneID, Scene> scenes;

    /** Mapa de ID de escena y sus respectivos cargadores de FXML. */
    private HashMap<SceneID, FXMLLoader> loaders = new HashMap<>();

    /**
     * Constructor privado para evitar la creación de instancias fuera de esta
     * clase.
     */
    private SceneManager() {
        scenes = new HashMap<>();
    }

    /**
     * Devuelve la instancia única de SceneManager.
     * Si no existe, la crea.
     *
     * @return La instancia única de SceneManager.
     */
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Inicializa el SceneManager con el escenario principal y el estilo CSS
     * especificado.
     *
     * @param stage  El escenario principal.
     * @param styles El nombre del archivo de estilo CSS.
     */
    @SuppressWarnings("exports")
    public void init(Stage stage, String styles) {
        this.stage = stage;
        this.styles = App.class.getResource("styles/" + styles + ".css");
    }

    /**
     * Inicializa el SceneManager con el escenario principal sin especificar un
     * estilo CSS.
     *
     * @param stage El escenario principal.
     */
    @SuppressWarnings("exports")
    public void init(Stage stage) {
        this.stage = stage;
    }

    /**
     * Establece una nueva escena en el SceneManager.
     * Carga el archivo FXML correspondiente y ajusta la escena al tamaño de la
     * pantalla.
     *
     * @param sceneID El identificador de la escena.
     * @param fxml    El nombre del archivo FXML de la escena.
     */
    public void setScene(SceneID sceneID, String fxml) {
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        try {
            URL url = App.class.getResource("views/" + fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, screenWidth * 0.66, screenHeight * 0.66);
            if (styles != null)
                scene.getStylesheets().add(styles.toExternalForm());
            scenes.put(sceneID, scene);
            loaders.put(sceneID, fxmlLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina una escena del SceneManager.
     *
     * @param sceneID El identificador de la escena a eliminar.
     */
    public void removeScene(SceneID sceneID) {
        scenes.remove(sceneID);
    }

    /**
     * Carga y muestra una escena en el escenario principal.
     *
     * @param sceneID El identificador de la escena a cargar.
     */
    public void loadScene(SceneID sceneID) {
        if (scenes.containsKey(sceneID)) {
            stage.setScene(scenes.get(sceneID));
            stage.show();
        }
    }

    /**
     * Obtiene el controlador asociado a una escena.
     *
     * @param sceneID El identificador de la escena.
     * @return El controlador de la escena o null si no se encuentra.
     */
    public Object getController(SceneID sceneID) {
        if (loaders.containsKey(sceneID)) {
            return loaders.get(sceneID).getController();
        } else {
            System.err.println("No se encontró el controlador para " + sceneID);
            return null;
        }
    }

    /**
     * Obtiene el escenario principal donde se gestionan las escenas.
     *
     * @return El escenario principal.
     */
    @SuppressWarnings("exports")
    public Stage getStage() {
        return stage;
    }

}