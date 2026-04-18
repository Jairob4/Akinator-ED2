package com.akinator.service.impl;

import com.akinator.modelo.dto.*;
import com.akinator.modelo.ArbolDecision;
import com.akinator.modelo.NodoArbol;
import com.akinator.service.ArbolVisualService;

import java.util.*;

public class ArbolVisualServiceImpl implements ArbolVisualService {
    private final ArbolDecision arbol;
    public ArbolVisualServiceImpl(ArbolDecision arbol) {
        this.arbol = arbol;
    }

    @Override
    public List<NodoVistaDTO> exportar() {

        List<NodoVistaDTO> lista = new ArrayList<>();
        NodoArbol raiz = arbol.getRaiz();

        if (raiz == null) return lista;

        Deque<NodoArbol> cola = new ArrayDeque<>();
        Deque<Integer> colaId = new ArrayDeque<>();
        Deque<Integer> colaPadre = new ArrayDeque<>();
        Deque<Boolean> colaDer = new ArrayDeque<>();

        cola.add(raiz);
        colaId.add(0);
        colaPadre.add(-1);
        colaDer.add(true);

        int idActual = 0;

        while (!cola.isEmpty()) {

            NodoArbol nodo = cola.poll();
            int id = colaId.poll();
            int idPadre = colaPadre.poll();
            boolean esDer = colaDer.poll();

            boolean esActual =
                    nodo == arbol.getNodoActual();

            lista.add(new NodoVistaDTO(
                    id,
                    idPadre,
                    nodo.getContenido(),
                    nodo.isEsPersonaje(),
                    esActual,
                    esDer
            ));

            if (nodo.getHijoDerecho() != null) {
                cola.add(nodo.getHijoDerecho());
                colaId.add(++idActual);
                colaPadre.add(id);
                colaDer.add(true);
            }

            if (nodo.getHijoIzquierdo() != null) {
                cola.add(nodo.getHijoIzquierdo());
                colaId.add(++idActual);
                colaPadre.add(id);
                colaDer.add(false);
            }
        }

        return lista;
    }
}
