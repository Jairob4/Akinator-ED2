package com.akinator.controlador;

import com.akinator.App;
import com.akinator.service.MenuService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    // ── SERVICIOS ─────────────────────────────────────────────────────────
    private MenuService menuService;

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private Label lblPersonajes;
    @FXML private Label lblNodos;
    @FXML private Label lblAltura;
    @FXML private Label lblMemoria;

    // ── INYECCIÓN DE DEPENDENCIAS ─────────────────────────────────────────
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (menuService != null) {
            actualizarStats();
        }
    }

    private void actualizarStats() {
        lblPersonajes.setText(String.valueOf(menuService.contarPersonajes()));
        lblNodos.setText(String.valueOf(menuService.contarNodos()));
        lblAltura.setText(String.valueOf(menuService.obtenerAltura()));
        lblMemoria.setText(String.valueOf(menuService.getTamanioMemoria()));
    }

    @FXML
    private void onJugar() {
        try {
            App.mostrarJuego();
        } catch (Exception e) {
            mostrarError("Error al iniciar el juego: " + e.getMessage());
        }
    }

    @FXML
    private void onVerPersonajes() {
        java.util.List<String> personajes = menuService.obtenerTodosPersonajes();
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
            App.mostrarArbol();
        } catch (Exception e) {
            mostrarError("Error al mostrar el árbol: " + e.getMessage());
        }
    }

    @FXML
    private void onReiniciarArbol() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reiniciar árbol");
        confirm.setHeaderText("¿Seguro que quieres reiniciar?");
        confirm.setContentText(
            "Se borrarán todos los personajes aprendidos\n" +
            "y el árbol volverá a su estado inicial."
        );
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            menuService.reiniciarArbol();
            // Recargar árbol en App
            App.cargarArbol();
            actualizarStats();
        }
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}