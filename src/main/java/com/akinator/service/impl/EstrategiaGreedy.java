package com.akinator.service.impl;

import com.akinator.modelo.ArbolDesicion;
import com.akinator.modelo.NodoArbol;
import com.akinator.service.EstrategiaSeleccionPreguntas;

public class EstrategiaGreedy implements EstrategiaSeleccionPreguntas {
    @Override
    public NodoArbol seleccionarSiguiente(NodoArbol actual, boolean respuesta) {

        return respuesta
                ? actual.getHijoDerecho()
                : actual.getHijoIzquierdo();
    }
}
