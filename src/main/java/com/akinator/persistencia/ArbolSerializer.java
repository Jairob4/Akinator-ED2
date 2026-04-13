package com.akinator.persistencia;

import com.akinator.modelo.ArbolDesicion;
import java.io.*;
import java.nio.file.*;

public class ArbolSerializer {

    private static final String RUTA = "arbol.dat";

    // Guardar el árbol en disco
    public static void guardar(ArbolDesicion arbol) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(RUTA))) {
            oos.writeObject(arbol);
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    // Cargar el árbol desde disco (si no existe, crea uno nuevo)
    public static ArbolDesicion cargar() {
        if (!Files.exists(Paths.get(RUTA))) {
            return new ArbolDesicion();
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(RUTA))) {
            return (ArbolDesicion) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar: " + e.getMessage());
            return new ArbolDesicion();
        }
    }
}