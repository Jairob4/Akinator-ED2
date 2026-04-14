package com.akinator.controlador;

import com.akinator.App;
import com.akinator.service.MenuService;
import com.akinator.modelo.ArbolDesicion;
import com.akinator.modelo.NodoArbol;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.*;

public class ArbolController implements Initializable {

    // ── SERVICIOS ─────────────────────────────────────────────────────────
    private MenuService menuService;

    //── MODELO ────────────────────────────────────────────────────────────
    private ArbolDesicion arbol; // Referencia al árbol compartido

    @FXML private Canvas canvasArbol;
    @FXML private Label lblNodos;
    @FXML private Label lblPersonajes;
    @FXML private Label lblAltura;
    @FXML private Label lblMemoria;
    @FXML private Button btnVolver;

    private static final double RADIO_NODO = 30;
    private static final double ESPACIO_HORIZONTAL = 120;
    private static final double ESPACIO_VERTICAL = 100;
    private static final double MARGEN = 40;

    // ── INYECCIÓN DE DEPENDENCIAS ─────────────────────────────────────────
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public void setArbol(ArbolDesicion arbol) {
        this.arbol = arbol;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (menuService != null && arbol != null) {
            cargarEstadisticas();
            dibujarArbol();
        }
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

        // Obtener raíz del árbol
        NodoArbol raiz = arbol.getRaiz();
        if (raiz == null) return;

        // Calcular posiciones usando árbol binario
        Map<NodoArbol, double[]> posiciones = new HashMap<>();
        calcularPosiciones(raiz, MARGEN, 0, canvasArbol.getWidth(), posiciones);

        // Dibujar conexiones primero (para que queden debajo)
        dibujarConexiones(gc, raiz, posiciones);

        // Dibujar nodos
        dibujarNodos(gc, raiz, posiciones);
    }

    private void calcularPosiciones(NodoArbol nodo, double x, double y,
                                   double ancho, Map<NodoArbol, double[]> posiciones) {
        if (nodo == null) return;

        posiciones.put(nodo, new double[]{x, y});

        double anchoParte = ancho / 2;
        double yProximos = y + ESPACIO_VERTICAL;

        // Hijo izquierdo
        if (nodo.getHijoIzquierdo() != null) {
            calcularPosiciones(nodo.getHijoIzquierdo(),
                    x - anchoParte / 2, yProximos, anchoParte, posiciones);
        }

        // Hijo derecho
        if (nodo.getHijoDerecho() != null) {
            calcularPosiciones(nodo.getHijoDerecho(),
                    x + anchoParte / 2, yProximos, anchoParte, posiciones);
        }
    }

    private void dibujarConexiones(GraphicsContext gc, NodoArbol nodo,
                                  Map<NodoArbol, double[]> posiciones) {
        if (nodo == null) return;

        double[] posPadre = posiciones.get(nodo);
        if (posPadre == null) return;

        // Conexión con hijo izquierdo
        if (nodo.getHijoIzquierdo() != null) {
            double[] posHijo = posiciones.get(nodo.getHijoIzquierdo());
            if (posHijo != null) {
                gc.setStroke(Color.web("#444466"));
                gc.setLineWidth(2);
                gc.strokeLine(posPadre[0], posPadre[1], posHijo[0], posHijo[1]);
                
                // Etiqueta "NO"
                double midX = (posPadre[0] + posHijo[0]) / 2;
                double midY = (posPadre[1] + posHijo[1]) / 2;
                gc.setFill(Color.web("#888899"));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText("NO", midX - 15, midY - 5);
            }
            dibujarConexiones(gc, nodo.getHijoIzquierdo(), posiciones);
        }

        // Conexión con hijo derecho
        if (nodo.getHijoDerecho() != null) {
            double[] posHijo = posiciones.get(nodo.getHijoDerecho());
            if (posHijo != null) {
                gc.setStroke(Color.web("#444466"));
                gc.setLineWidth(2);
                gc.strokeLine(posPadre[0], posPadre[1], posHijo[0], posHijo[1]);
                
                // Etiqueta "SÍ"
                double midX = (posPadre[0] + posHijo[0]) / 2;
                double midY = (posPadre[1] + posHijo[1]) / 2;
                gc.setFill(Color.web("#888899"));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText("SÍ", midX + 15, midY - 5);
            }
            dibujarConexiones(gc, nodo.getHijoDerecho(), posiciones);
        }
    }

    private void dibujarNodos(GraphicsContext gc, NodoArbol nodo,
                             Map<NodoArbol, double[]> posiciones) {
        if (nodo == null) return;

        double[] pos = posiciones.get(nodo);
        if (pos == null) return;

        double x = pos[0];
        double y = pos[1];

        // Color según tipo
        Color color = nodo.isEsPersonaje() ? Color.web("#55ff77") : Color.web("#7c3aed");
        
        // Resaltar nodo actual
        if (nodo.equals(arbol.getNodoActual())) {
            gc.setFill(Color.web("#e0b0ff"));
            gc.fillOval(x - RADIO_NODO - 5, y - RADIO_NODO - 5,
                       (RADIO_NODO + 5) * 2, (RADIO_NODO + 5) * 2);
        }

        // Dibujar círculo del nodo
        gc.setFill(color);
        gc.fillOval(x - RADIO_NODO, y - RADIO_NODO,
                   RADIO_NODO * 2, RADIO_NODO * 2);

        // Borde
        gc.setStroke(Color.web("#e0b0ff"));
        gc.setLineWidth(2);
        gc.strokeOval(x - RADIO_NODO, y - RADIO_NODO,
                     RADIO_NODO * 2, RADIO_NODO * 2);

        // Texto del nodo (truncado si es muy largo)
        String contenido = nodo.getContenido();
        if (contenido.length() > 12) {
            contenido = contenido.substring(0, 10) + "...";
        }
        
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(javafx.scene.text.Font.font(10));
        
        String[] lineas = contenido.split(" ");
        int y_offset = -5;
        for (String linea : lineas) {
            gc.fillText(linea, x, y + y_offset);
            y_offset += 12;
        }

        // Recursivo para hijos
        if (nodo.getHijoIzquierdo() != null) {
            dibujarNodos(gc, nodo.getHijoIzquierdo(), posiciones);
        }
        if (nodo.getHijoDerecho() != null) {
            dibujarNodos(gc, nodo.getHijoDerecho(), posiciones);
        }
    }

    @FXML
    private void onVolver() {
        try {
            App.mostrarMenu();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onMenu() {
        try {
            App.mostrarMenu();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onJugar() {
        try {
            App.mostrarJuego();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarError(String msg) {
        javafx.scene.control.Alert alert = 
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
