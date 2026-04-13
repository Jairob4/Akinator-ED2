package com.akinator.service;

import com.akinator.ResultadoPartida;

public interface JuegoService {

    // Lógica de juego
    void responder(boolean esSi);
    boolean retroceder();
    boolean puedoRetroceder();
    boolean juegoTerminado();
    String getPreguntaActual();
    void aprender(String personaje, String pregunta, boolean respuestaEsSi);
    ResultadoPartida generarResultado(boolean gano, boolean aprendio);
    void reiniciar();

    // Métricas
    int getPreguntasRealizadas();
    long getTiempoPartida();
    int getTamanioMemoria();
}