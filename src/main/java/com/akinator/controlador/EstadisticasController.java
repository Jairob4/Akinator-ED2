package com.akinator.controlador;

import com.akinator.App;
import com.akinator.ResultadoPartida;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EstadisticasController {

    @FXML private Label    lblTituloResultado;
    @FXML private Label    lblPersonajeResultado;
    @FXML private Label    lblTiempo;
    @FXML private Label    lblPreguntas;
    @FXML private Label    lblProfundidad;
    @FXML private Label    lblMemoria;
    @FXML private Label    lblComplejidad;
    @FXML private Label    lblTotalNodos;
    @FXML private Label    lblTotalPersonajes;
    @FXML private Label    lblRutasMemo;
    @FXML private TextArea txtResumen;

    public void cargarResultado(ResultadoPartida r) {
        // Título
        if (r.isGanoAkinator()) {
            lblTituloResultado.setText("✅ ¡Adiviné!");
            lblTituloResultado.setStyle("-fx-text-fill: #ffd700;");
        } else {
            lblTituloResultado.setText("📚 ¡Aprendí algo nuevo!");
            lblTituloResultado.setStyle("-fx-text-fill: #55ff77;");
        }

        lblPersonajeResultado.setText(r.getPersonaje());

        // Tarjetas
        lblTiempo.setText(String.format("%.2f ms", r.getTiempoMs()));
        lblPreguntas.setText(String.valueOf(r.getPreguntasHechas()));
        lblProfundidad.setText(String.valueOf(r.getProfundidad()));
        lblMemoria.setText(String.format("%.1f KB", r.getMemoriaKB()));

        // Análisis
        lblComplejidad.setText(r.getComplejidad());
        lblTotalNodos.setText("Nodos: "      + r.getTotalNodos());
        lblTotalPersonajes.setText("Personajes: " + r.getTotalPersonajes());
        lblRutasMemo.setText("Rutas memo: "  + r.getTamanioMemo());

        // Resumen técnico completo
        txtResumen.setText(r.getResumenTexto());
    }

    @FXML
    private void onJugarDeNuevo() {
        try {
            App.arbol.reiniciarPartida();
            App.mostrarJuego();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onMenu() {
        try {
            App.mostrarMenu();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }
}