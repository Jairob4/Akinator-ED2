package com.akinator.controlador;

import com.akinator.App;
import com.akinator.ResultadoPartida;
import com.akinator.persistencia.ArbolSerializer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.ResourceBundle;

public class JuegoController implements Initializable {

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private Label    lblPregunta;
    @FXML private Label    lblPreguntaNum;
    @FXML private HBox     panelRespuestas;
    @FXML private VBox     panelAdivinanza;
    @FXML private VBox     panelAprendizaje;
    @FXML private Label    lblPersonajeAdivinado;
    @FXML private TextField txtNuevoPersonaje;
    @FXML private TextField txtNuevaPregunta;
    @FXML private Label    lblMetricaPreguntas;
    @FXML private Label    lblMetricaTiempo;
    @FXML private Label    lblMetricaMemoria;
    @FXML private Button   btnRetroceder;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarUI();
    }

    // ─────────────────────────────────────────────────────────────────────
    // FLUJO PRINCIPAL DEL JUEGO
    // ─────────────────────────────────────────────────────────────────────

    @FXML
    private void onSi() { responder(true); }

    @FXML
    private void onNo() { responder(false); }

    private void responder(boolean esSi) {
        App.arbol.responder(esSi);
        actualizarUI();
    }

    private void actualizarUI() {
        // Actualizar métricas en tiempo real
        int preguntas = App.arbol.getPreguntasRealizadas();
        long ms = App.arbol.getTiempoPartida() / 1_000_000;
        int memo  = App.arbol.getTamanioMemoria();

        lblMetricaPreguntas.setText("Preguntas: " + preguntas);
        lblMetricaTiempo.setText("Tiempo: " + ms + "ms");
        lblMetricaMemoria.setText("Memo: " + memo);
        lblPreguntaNum.setText("Pregunta " + (preguntas + 1));
        btnRetroceder.setDisable(!App.arbol.puedoRetroceder());

        if (App.arbol.juegoTerminado()) {
            // Llegamos a una hoja → mostrar panel de adivinanza
            mostrarAdivinanza();
        } else {
            // Mostrar pregunta normal
            lblPregunta.setText(App.arbol.getPreguntaActual());
            mostrarPanel(panelRespuestas, true);
            mostrarPanel(panelAdivinanza, false);
            mostrarPanel(panelAprendizaje, false);
        }
    }

    private void mostrarAdivinanza() {
        String personaje = App.arbol.getPreguntaActual();
        lblPersonajeAdivinado.setText("¿Es " + personaje + "?");
        lblPregunta.setText("¡Creo que lo sé!");
        mostrarPanel(panelRespuestas,  false);
        mostrarPanel(panelAdivinanza,  true);
        mostrarPanel(panelAprendizaje, false);
    }

    // ─────────────────────────────────────────────────────────────────────
    // RESULTADO: ACERTÓ O FALLÓ
    // ─────────────────────────────────────────────────────────────────────

    @FXML
    private void onAcertó() {
        // Akinator ganó
        ResultadoPartida r = App.arbol.generarResultado(true, false);
        ArbolSerializer.guardar(App.arbol);
        try {
            App.mostrarEstadisticas(r);
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onFalló() {
        // Mostrar panel de aprendizaje
        lblPregunta.setText("¡Ups! Voy a aprender...");
        mostrarPanel(panelRespuestas,  false);
        mostrarPanel(panelAdivinanza,  false);
        mostrarPanel(panelAprendizaje, true);
        txtNuevoPersonaje.clear();
        txtNuevaPregunta.clear();
    }

    // ─────────────────────────────────────────────────────────────────────
    // APRENDIZAJE
    // ─────────────────────────────────────────────────────────────────────

    @FXML
    private void onAprender_Si() { aprender(true); }

    @FXML
    private void onAprender_No() { aprender(false); }

    private void aprender(boolean respuestaEsSi) {
        String nuevoPersonaje = txtNuevoPersonaje.getText().trim();
        String nuevaPregunta  = txtNuevaPregunta.getText().trim();

        if (nuevoPersonaje.isEmpty() || nuevaPregunta.isEmpty()) {
            mostrarError("Por favor completa ambos campos.");
            return;
        }

        // Asegurarse que la pregunta termina con "?"
        if (!nuevaPregunta.endsWith("?"))
            nuevaPregunta += "?";

        // Aprender el nuevo personaje (backtracking + PD)
        App.arbol.aprenderPersonaje(nuevoPersonaje, nuevaPregunta, respuestaEsSi);

        // Guardar árbol actualizado en disco
        ArbolSerializer.guardar(App.arbol);

        // Generar resultado y mostrar estadísticas
        ResultadoPartida r = App.arbol.generarResultado(false, true);
        try {
            App.mostrarEstadisticas(r);
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // BACKTRACKING MANUAL (botón retroceder)
    // ─────────────────────────────────────────────────────────────────────

    @FXML
    private void onRetroceder() {
        if (App.arbol.retroceder()) {
            actualizarUI();
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // NAVEGACIÓN
    // ─────────────────────────────────────────────────────────────────────

    @FXML
    private void onVolverMenu() {
        try {
            App.mostrarMenu();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // UTILIDADES
    // ─────────────────────────────────────────────────────────────────────

    private void mostrarPanel(javafx.scene.Node panel, boolean visible) {
        panel.setVisible(visible);
        panel.setManaged(visible);
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Exponer puedoRetroceder para el FXML
    public boolean puedoRetroceder() {
        return App.arbol.puedoRetroceder();
    }
}