package com.akinator.modelo.dto;

public class NodoVistaDTO {

    private final int id;
    private final int idPadre;
    private final String contenido;
    private final boolean esPersonaje;
    private final boolean esNodoActual;
    private final boolean esDerecho;

    public NodoVistaDTO(int id,
                        int idPadre,
                        String contenido,
                        boolean esPersonaje,
                        boolean esNodoActual,
                        boolean esDerecho) {
        this.id = id;
        this.idPadre = idPadre;
        this.contenido = contenido;
        this.esPersonaje = esPersonaje;
        this.esNodoActual = esNodoActual;
        this.esDerecho = esDerecho;
    }

    public int getId() { return id; }
    public int getIdPadre() { return idPadre; }
    public String getContenido() { return contenido; }
    public boolean isEsPersonaje() { return esPersonaje; }
    public boolean isEsNodoActual() { return esNodoActual; }
    public boolean isEsDerecho() { return esDerecho; }
}