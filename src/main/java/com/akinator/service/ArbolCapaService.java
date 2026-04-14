package com.akinator.service;

import java.util.List;
import java.util.Map;

import com.akinator.modelo.dto.NodoVistaDTO;

public interface ArbolCapaService {

    Map<Integer, double[]> calcularPosiciones( List<NodoVistaDTO> nodos );
    
}
