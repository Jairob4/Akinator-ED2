package com.akinator.service;

import java.util.List;

public interface MenuService {

    // Estadísticas del árbol
    int contarNodos();
    int contarPersonajes();
    int obtenerAltura();
    int getTamanioMemoria();

    // Gestión de personajes
    List<String> obtenerTodosPersonajes();

    // Reinicio
    void reiniciarArbol();

    // Visualización
    ArbolVisualService getVisualizationService();
    ArbolCapaService getArbolCapaService();
    CanvasInteraccionService getCanvasInteraccionService();
}
