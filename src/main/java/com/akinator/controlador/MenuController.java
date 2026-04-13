package com.akinator.controlador;

import com.akinator.App;
import com.akinator.persistencia.ArbolSerializer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML private Label lblPersonajes;
    @FXML private Label lblNodos;
    @FXML private Label lblAltura;
    @FXML private Label lblMemoria;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarStats();
    }

    private void actualizarStats() {
        lblPersonajes.setText(String.valueOf(App.arbol.contarPersonajes()));
        lblNodos.setText(String.valueOf(App.arbol.contarNodos()));
        lblAltura.setText(String.valueOf(App.arbol.altura()));
        lblMemoria.setText(String.valueOf(App.arbol.getTamanioMemoria()));
    }

    @FXML
    private void onJugar() {
        try {
            App.arbol.reiniciarPartida();
            App.mostrarJuego();
        } catch (Exception e) {
            mostrarError("Error al iniciar el juego: " + e.getMessage());
        }
    }

    @FXML
    private void onVerPersonajes() {
        java.util.List<String> personajes = App.arbol.obtenerTodosPersonajes();
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
            // Borrar archivo y crear árbol nuevo
            java.io.File f = new java.io.File(
                System.getProperty("user.home") + java.io.File.separator + "arbol.dat"
            );
            if (f.exists()) f.delete();
            App.arbol = new com.akinator.modelo.ArbolDesicion();
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