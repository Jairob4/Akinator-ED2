package com.akinator.repository;

import com.akinator.modelo.ArbolDesicion;

public interface ArbolRepository {

    void guardar(ArbolDesicion arbol);

    ArbolDesicion cargar();
}