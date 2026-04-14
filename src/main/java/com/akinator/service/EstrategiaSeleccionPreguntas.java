package com.akinator.service;

import com.akinator.modelo.ArbolDesicion;
import com.akinator.modelo.NodoArbol;

public interface EstrategiaSeleccionPreguntas {

    // el criterio de seleccion se aplicará por polimorfismo, cada estrategia implementará su propio criterio
    NodoArbol seleccionarSiguiente(NodoArbol actual, boolean respuesta );
}
