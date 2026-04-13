package com.akinator.modelo;

import java.io.Serializable;

public class NodoArbol implements Serializable {
    private final static long serialVersionUID = 1L;

    private static int contadorId = 0; // Contador para asignar IDs únicos a cada nodo
    private int id; // ID único del nodo 

    private String contenido; //aca se guarda la pregunta o el personaje
    private boolean esPersonaje; // false si es pregunta, true si es personaje
    private NodoArbol hijoIzquierdo; // hijo izquierdo para la respuesta "no"
    private NodoArbol hijoDerecho; // hijo derecho para la respuesta "si"

    // Constructor para crear un nodo de tipo personaje
    public NodoArbol(String personaje, boolean esPersonaje) {
        this.id = ++contadorId; // Asignar ID único
        this.contenido = personaje;
        this.esPersonaje = esPersonaje;
        this.hijoIzquierdo = null; // referencia nula para el hijo izquierdo
        this.hijoDerecho = null; //referencia nula para el hijo derecho
    }

    // Constructor para crear un nodo de tipo pregunta
    public NodoArbol(String pregunta) {
        this.id = ++contadorId; // Asignar ID único
        this.contenido = pregunta;
        this.esPersonaje = false; // es una pregunta
        this.hijoIzquierdo = null; // referencia nula para el hijo izquierdo
        this.hijoDerecho = null; // referencia nula para el hijo derecho
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isEsPersonaje() {
        return esPersonaje;
    }

    public void setEsPersonaje(boolean esPersonaje) {
        this.esPersonaje = esPersonaje;
    }

    public NodoArbol getHijoIzquierdo() {
        return hijoIzquierdo;
    }

    public void setHijoIzquierdo(NodoArbol hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    public NodoArbol getHijoDerecho() {
        return hijoDerecho;
    }

    public void setHijoDerecho(NodoArbol hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }

    @Override
    public String toString() {
        return (esPersonaje ? "Personaje: " : "Pregunta: ") + contenido;
    }


}
