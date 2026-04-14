package com.akinator.service.impl;

import java.io.File;
import java.util.List;

import com.akinator.modelo.ArbolDesicion;
import com.akinator.repository.ArbolRepository;
import com.akinator.service.ArbolVisualService;
import com.akinator.service.MenuService;

public class MenuServiceImpl implements MenuService {

    private final ArbolDesicion arbol;
    private final ArbolRepository repository;
    private final ArbolVisualService visualService;

    public MenuServiceImpl(ArbolDesicion arbol,
                          ArbolRepository repository,
                          ArbolVisualService visualService) {
        this.arbol = arbol;
        this.repository = repository;
        this.visualService = visualService;
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
        // Borrar archivo persistido
        String rutaArchivo = System.getProperty("user.home")
                + File.separator + "arbol.dat";
        File f = new File(rutaArchivo);
        if (f.exists()) {
            f.delete();
        }
        
        // No recrear aquí, dejamos que App lo haga. Solo notificamos limpieza.
    }

    @Override
    public ArbolVisualService getVisualizationService() {
        return visualService;
    }
}
