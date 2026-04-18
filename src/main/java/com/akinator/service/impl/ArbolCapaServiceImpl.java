package com.akinator.service.impl;

import java.util.*;

import com.akinator.modelo.ArbolDecision;
import com.akinator.modelo.dto.NodoVistaDTO;
import com.akinator.service.ArbolCapaService;

public class ArbolCapaServiceImpl implements ArbolCapaService {

    private static final double ESPACIO_HORIZONTAL = 120;
    private static final double ESPACIO_VERTICAL = 100;


    @Override
    public Map<Integer, double[]> calcularPosiciones(List<NodoVistaDTO> nodos) {

        Map<Integer, double[]> posiciones = new HashMap<>();

        // crear mapa de nodos por ID para acceso rápido
        Map<Integer, NodoVistaDTO> mapaNodos = new HashMap<>();
        for (NodoVistaDTO nodo : nodos) {
            mapaNodos.put(nodo.getId(), nodo);
        }

        //  Calcular nivel dinámicamente
        Map<Integer, Integer> niveles = new HashMap<>();

        for (NodoVistaDTO nodo : nodos) {
            calcularNivel(nodo, mapaNodos, niveles);
        }

        //  Agrupar por nivel
        Map<Integer, List<NodoVistaDTO>> porNivel = new TreeMap<>();

        for (NodoVistaDTO nodo : nodos) {
            int nivel = niveles.get(nodo.getId());
            porNivel
                .computeIfAbsent(nivel, k -> new ArrayList<>())
                .add(nodo);
        }

        //  Asignar posiciones

        double canvasWidth = 2000;
        for (Map.Entry<Integer, List<NodoVistaDTO>> entry : porNivel.entrySet()) {

            int nivel = entry.getKey();
            List<NodoVistaDTO> lista = entry.getValue();

            double anchoTotal = (lista.size()-1)*ESPACIO_HORIZONTAL;

            double inicioX = (canvasWidth-anchoTotal)/2;

            for (int i = 0; i < lista.size(); i++) {

                double x = inicioX + i * ESPACIO_HORIZONTAL;
                double y = nivel * ESPACIO_VERTICAL+60;

                posiciones.put(
                        lista.get(i).getId(),
                        new double[]{x, y}
                );
            }
        }

        return posiciones;
    }

    private int calcularNivel(NodoVistaDTO nodo,
                              Map<Integer, NodoVistaDTO> mapa,
                              Map<Integer, Integer> niveles) {

        if (niveles.containsKey(nodo.getId())) {
            return niveles.get(nodo.getId());
        }

        // Si no tiene padre - raíz
        if (nodo.getIdPadre() == -1) {
            niveles.put(nodo.getId(), 0);
            return 0;
        }

        NodoVistaDTO padre = mapa.get(nodo.getIdPadre());
        int nivelPadre = calcularNivel(padre, mapa, niveles);
        int nivel = nivelPadre + 1;

        niveles.put(nodo.getId(), nivel);
        return nivel;
    }
}