package com.akinator;

import com.akinator.controlador.EstadisticasController;
import com.akinator.modelo.ArbolDesicion;
import com.akinator.persistencia.ArbolSerializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static Stage stagePrincipal;
    public static ArbolDesicion arbol; // árbol compartido entre pantallas

    @Override
    public void start(Stage stage) throws Exception {
        stagePrincipal = stage;
        arbol = ArbolSerializer.cargar(); // cargar árbol persistido
        mostrarMenu();
    }

    public static void mostrarMenu() throws Exception {
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource("/com/akinator/menu.fxml")
        );
        Scene scene = new Scene(loader.load(), 700, 520);
        scene.getStylesheets().add(
            App.class.getResource("/com/akinator/styles.css").toExternalForm()
        );
        stagePrincipal.setTitle("Akinator — ED2");
        stagePrincipal.setScene(scene);
        stagePrincipal.setResizable(false);
        stagePrincipal.show();
    }

    public static void mostrarJuego() throws Exception {
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource("/com/akinator/juego.fxml")
        );
        Scene scene = new Scene(loader.load(), 700, 520);
        scene.getStylesheets().add(
            App.class.getResource("/com/akinator/styles.css").toExternalForm()
        );
        stagePrincipal.setScene(scene);
    }

    public static void mostrarEstadisticas(ResultadoPartida resultado) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource("/com/akinator/estadisticas.fxml")
        );
        Scene scene = new Scene(loader.load(), 700, 520);
        scene.getStylesheets().add(
            App.class.getResource("/com/akinator/styles.css").toExternalForm()
        );
        stagePrincipal.setScene(scene);
        EstadisticasController ctrl = loader.getController();
        ctrl.cargarResultado(resultado);
    }

    public static void main(String[] args) {
        launch(args);
    }
}