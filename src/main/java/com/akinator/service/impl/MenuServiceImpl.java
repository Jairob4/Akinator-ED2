package com.akinator.service.impl;

import java.io.File;
import java.util.List;

import com.akinator.modelo.ArbolDecision;
import com.akinator.repository.ArbolRepository;
import com.akinator.service.ArbolVisualService;
import com.akinator.service.CanvasInteraccionService;
import com.akinator.service.MenuService;
import com.akinator.service.ArbolCapaService;

public class MenuServiceImpl implements MenuService {

    private final ArbolDecision arbol;
    private final ArbolRepository repository;
    private final ArbolVisualService visualService;
    private final ArbolCapaService arbolCapaService;
    private final CanvasInteraccionService canvasInteraccionService;

    public MenuServiceImpl(ArbolDecision arbol,
                          ArbolRepository repository,
                          ArbolVisualService visualService) {
        this.arbol = arbol;
        this.repository = repository;
        this.visualService = visualService;
        this.arbolCapaService = new ArbolCapaServiceImpl();
        this.canvasInteraccionService = new CanvasInteraccionServiceImpl();
    }

    @Override
    public int contarNodos() {
        return arbol.contarNodos();
    }

    @Override
    public int contarPersonajes() {
        return arbol.contarPersonajes();
    }

    @Override
    public int obtenerAltura() {
        return arbol.obtenerAltura();
    }

    @Override
    public int getTamanioMemoria() {
        return arbol.getTamanioMemoria();
    }

    @Override
    public List<String> obtenerTodosPersonajes() {
        return arbol.obtenerTodosPersonajes();
    }

    @Override
    public void reiniciarArbol() {
        String rutaArchivo = System.getProperty("user.home")
                + File.separator + "arbol.dat";
        File f = new File(rutaArchivo);
        if (f.exists()) {
            f.delete();
        }
        
        arbol.reiniciarArbol();
    }

    @Override
    public ArbolVisualService getVisualizationService() {
        return visualService;
    }

    @Override
    public ArbolCapaService getArbolCapaService() {
        return arbolCapaService;
    }

    @Override
    public CanvasInteraccionService getCanvasInteraccionService(){
        return canvasInteraccionService;
    }
}
