package com.akinator.repository;

import com.akinator.modelo.ArbolDecision;

public interface ArbolRepository {

    void guardar(ArbolDecision arbol);

    ArbolDecision cargar();
}