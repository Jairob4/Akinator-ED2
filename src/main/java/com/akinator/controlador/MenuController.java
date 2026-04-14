package com.akinator.controlador;

import com.akinator.App;
import com.akinator.service.MenuService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    
    private MenuService menuService;

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private Label lblPersonajes;
    @FXML private Label lblNodos;
    @FXML private Label lblAltura;
    @FXML private Label lblMemoria;

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
        // Actualizar stats después de inyectar el servicio
        actualizarStats();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No hacer nada aquí - esperar a que se inyecte el servicio
    }

    private void actualizarStats() {
        if (menuService == null) {
            // Silenciosamente ignorar si no hay servicio aún
            return;
        }

        lblPersonajes.setText(String.valueOf(menuService.contarPersonajes()));
        lblNodos.setText(String.valueOf(menuService.contarNodos()));
        lblAltura.setText(String.valueOf(menuService.obtenerAltura()));
        lblMemoria.setText(String.valueOf(menuService.getTamanioMemoria()));
    }

    @FXML
    private void onJugar() {
        try {
            if (menuService == null) {
                mostrarError("Error: Servicio no inicializado.");
                return;
            }

            App.arbol.reiniciarPartida();
            App.mostrarJuego();
        } catch (Exception e) {
            mostrarError("Error al iniciar el juego: " + e.getMessage());
        }
    }

    @FXML
    private void onVerPersonajes() {
        if (menuService == null) {
            mostrarError("Error: Servicio no inicializado.");
            return;
        }

        List<String> personajes = menuService.obtenerTodosPersonajes();
        String lista = personajes.isEmpty()
            ? "No hay personajes aún."
            : String.join("\n• ", personajes);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Personajes conocidos");
        alert.setHeaderText("El Akinator conoce " + personajes.size() + " personaje(s):");
        alert.setContentText("• " + lista);
        alert.showAndWait();
    }

    @FXML
    private void onVerArbol() {
        try {
            if (menuService == null) {
                mostrarError("Error: Servicio no inicializado.");
                return;
            }

            App.mostrarArbol();
        } catch (Exception e) {
            mostrarError("Error al mostrar el árbol: " + e.getMessage());
        }
    }

    @FXML
    private void onReiniciarArbol() {
        if (menuService == null) {
            mostrarError("Error: Servicio no inicializado.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reiniciar árbol");
        confirm.setHeaderText("¿Seguro que quieres reiniciar?");
        confirm.setContentText(
            "Se borrarán todos los personajes aprendidos\n" +
            "y el árbol volverá a su estado inicial."
        );
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                menuService.reiniciarArbol();
                actualizarStats();
            } catch (Exception e) {
                mostrarError("Error al reiniciar el árbol: " + e.getMessage());
            }
        }
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}