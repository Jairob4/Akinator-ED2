package com.akinator.controlador;

import com.akinator.App;
import com.akinator.service.MenuService;
import com.akinator.modelo.ArbolDecision;
import com.akinator.modelo.dto.NodoVistaDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.*;

public class ArbolController implements Initializable {

    private MenuService menuService;
    private ArbolDecision arbol;

    @FXML private Canvas canvasArbol;
    @FXML private Label lblNodos;
    @FXML private Label lblPersonajes;
    @FXML private Label lblAltura;
    @FXML private Label lblMemoria;
    @FXML private Button btnVolver;
    @FXML private ScrollPane scrollPane;

    private static final double RADIO_NODO = 30;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // vacío — servicios aún no disponibles
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
        if (this.arbol != null) {
            activar();
        }
    }

    public void setArbol(ArbolDecision arbol) {
        this.arbol = arbol;
        if (this.menuService != null) {
            activar();
        }
    }

    // Se llama solo cuando ambas dependencias están listas
    private void activar() {
        menuService.getCanvasInteraccionService().habilitarDrag(canvasArbol, scrollPane);
        menuService.getCanvasInteraccionService().habilitarZoom(canvasArbol);
        menuService.getCanvasInteraccionService().centrarScroll(scrollPane);
        cargarEstadisticas();
        dibujarArbol();
    }

    private void cargarEstadisticas() {
        lblNodos.setText(String.valueOf(menuService.contarNodos()));
        lblPersonajes.setText(String.valueOf(menuService.contarPersonajes()));
        lblAltura.setText(String.valueOf(menuService.obtenerAltura()));
        lblMemoria.setText(String.valueOf(menuService.getTamanioMemoria()));
    }

    private void dibujarArbol() {
        GraphicsContext gc = canvasArbol.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasArbol.getWidth(), canvasArbol.getHeight());

        // Pedir nodos al servicio visual
        List<NodoVistaDTO> nodos = menuService.getVisualizationService().exportar();
        if (nodos.isEmpty()) return;

        // Pedir posiciones al servicio de capas
        Map<Integer, double[]> posiciones =
                menuService.getArbolCapaService().calcularPosiciones(nodos);

        // Dibujar conexiones
        for (NodoVistaDTO nodo : nodos) {
            if (nodo.getIdPadre() == -1) continue;

            double[] posPadre = posiciones.get(nodo.getIdPadre());
            double[] posHijo  = posiciones.get(nodo.getId());
            if (posPadre == null || posHijo == null) continue;

            gc.setStroke(Color.web("#444466"));
            gc.setLineWidth(2);
            gc.strokeLine(posPadre[0], posPadre[1], posHijo[0], posHijo[1]);

            double midX = (posPadre[0] + posHijo[0]) / 2;
            double midY = (posPadre[1] + posHijo[1]) / 2;
            gc.setFill(Color.web("#888899"));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(nodo.isEsDerecho() ? "SÍ" : "NO", midX, midY - 5);
        }

        // Dibujar nodos
        for (NodoVistaDTO nodo : nodos) {
            double[] pos = posiciones.get(nodo.getId());
            if (pos == null) continue;

            double x = pos[0];
            double y = pos[1];

            if (nodo.isEsNodoActual()) {
                gc.setFill(Color.web("#e0b0ff"));
                gc.fillOval(x - RADIO_NODO - 5, y - RADIO_NODO - 5,
                        (RADIO_NODO + 5) * 2, (RADIO_NODO + 5) * 2);
            }

            gc.setFill(nodo.isEsPersonaje() ? Color.web("#55ff77") : Color.web("#7c3aed"));
            gc.fillOval(x - RADIO_NODO, y - RADIO_NODO, RADIO_NODO * 2, RADIO_NODO * 2);

            gc.setStroke(Color.web("#e0b0ff"));
            gc.setLineWidth(2);
            gc.strokeOval(x - RADIO_NODO, y - RADIO_NODO, RADIO_NODO * 2, RADIO_NODO * 2);

            String texto = nodo.getContenido();
            if (texto.length() > 12) texto = texto.substring(0, 10) + "...";

            gc.setFill(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(javafx.scene.text.Font.font(10));

            String[] palabras = texto.split(" ");
            double yTexto = y - (palabras.length * 6) + 6;
            for (String palabra : palabras) {
                gc.fillText(palabra, x, yTexto);
                yTexto += 12;
            }
        }
    }

    @FXML
    private void onVolver() {
        try { App.mostrarMenu(); }
        catch (Exception e) { mostrarError(e.getMessage()); }
    }

    @FXML
    private void onMenu() {
        try { App.mostrarMenu(); }
        catch (Exception e) { mostrarError(e.getMessage()); }
    }

    @FXML
    private void onJugar() {
        try { App.mostrarJuego(); }
        catch (Exception e) { mostrarError(e.getMessage()); }
    }

    private void mostrarError(String msg) {
        javafx.scene.control.Alert alert =
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}