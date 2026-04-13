package com.akinator;

import com.akinator.controlador.*;
import com.akinator.modelo.ArbolDesicion;
import com.akinator.repository.impl.ArbolSerializer;
import com.akinator.service.JuegoService;
import com.akinator.service.MenuService;
import com.akinator.service.impl.JuegoServiceImpl;
import com.akinator.service.impl.MenuServiceImpl;
import com.akinator.service.impl.ArbolVisualServiceImpl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static Stage stagePrincipal;
    public static ArbolDesicion arbol; // árbol compartido entre pantallas
    public static ArbolSerializer ArbolSerializer = new ArbolSerializer();

    // ── SERVICIOS ─────────────────────────────────────────────────────────
    private static JuegoService juegoService;
    private static MenuService menuService;

    public static void guardarArbol() {
        ArbolSerializer.guardar(arbol);
    }

    public static void cargarArbol() {
        arbol = ArbolSerializer.cargar();
        inicializarServicios();
    }

    private static void inicializarServicios() {
        if (juegoService == null) {
            juegoService = new JuegoServiceImpl(arbol, ArbolSerializer);
        }
        if (menuService == null) {
            menuService = new MenuServiceImpl(arbol, ArbolSerializer, new ArbolVisualServiceImpl(arbol));
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stagePrincipal = stage;
        arbol = ArbolSerializer.cargar(); // cargar árbol persistido
        inicializarServicios();
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
        
        // Inyectar servicio
        MenuController ctrl = loader.getController();
        ctrl.setMenuService(menuService);

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
        
        // Inyectar servicio
        JuegoController ctrl = loader.getController();
        ctrl.setJuegoService(juegoService);
        juegoService.reiniciar(); // Reiniciar partida

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

    public static void mostrarArbol() throws Exception {
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource("/com/akinator/arbol.fxml")
        );
        Scene scene = new Scene(loader.load(), 700, 520);
        scene.getStylesheets().add(
            App.class.getResource("/com/akinator/styles.css").toExternalForm()
        );
        
        // Inyectar servicios
        ArbolController ctrl = loader.getController();
        ctrl.setMenuService(menuService);
        ctrl.setArbol(arbol);

        stagePrincipal.setTitle("Akinator — Visualización del Árbol");
        stagePrincipal.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}