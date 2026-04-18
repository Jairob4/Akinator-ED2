package com.akinator.service.impl;

import com.akinator.ResultadoPartida;
import com.akinator.modelo.ArbolDecision;
import com.akinator.repository.ArbolRepository;
import com.akinator.service.JuegoService;

public class JuegoServiceImpl implements JuegoService {

    private final ArbolDecision arbol;
    private final ArbolRepository repository;

    public JuegoServiceImpl(ArbolDecision arbol,
                            ArbolRepository repository) {
        this.arbol = arbol;
        this.repository = repository;
    }

    @Override
    public void responder(boolean esSi) {
        arbol.responder(esSi);
    }

    @Override
    public boolean retroceder() {
        return arbol.retroceder();
    }

    @Override
    public boolean puedoRetroceder() {
        return arbol.puedoRetroceder();
    }

    @Override
    public boolean juegoTerminado() {
        return arbol.juegoTerminado();
    }

    @Override
    public String getPreguntaActual() {
        return arbol.getPreguntaActual();
    }

    @Override
    public void aprender(String personaje, String pregunta, boolean respuestaEsSi) {
        arbol.aprenderPersonaje(personaje, pregunta, respuestaEsSi);
        repository.guardar(arbol);
    }

    @Override
    public ResultadoPartida generarResultado(boolean gano, boolean aprendio) {
        return arbol.generarResultado(gano, aprendio);
    }

    @Override
    public void reiniciar() {
        arbol.reiniciarPartida();
    }

    @Override
    public int getPreguntasRealizadas() {
        return arbol.getPreguntasRealizadas();
    }

    @Override
    public long getTiempoPartida() {
        return arbol.getTiempoPartida();
    }

    @Override
    public int getTamanioMemoria() {
        return arbol.getTamanioMemoria();
    }
}