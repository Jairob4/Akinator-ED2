package com.akinator.repository.impl;

import com.akinator.modelo.ArbolDecision;
import com.akinator.repository.ArbolRepository;

import java.io.*;
import java.nio.file.*;

public class ArbolSerializer implements ArbolRepository {

    private final String ruta;

    public ArbolSerializer(){
        this.ruta = System.getProperty("user.home")
                + File.separator + "arbol.dat";
    }
        

    @Override
    public void guardar(ArbolDecision arbol) {
        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream(ruta))) {

            oos.writeObject(arbol);

        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    @Override
    public ArbolDecision cargar() {

        if (!Files.exists(Paths.get(ruta))) {
            return new ArbolDecision();
        }

        try (ObjectInputStream ois =
                 new ObjectInputStream(new FileInputStream(ruta))) {

            ArbolDecision arbol =
                    (ArbolDecision) ois.readObject();

            arbol.reiniciarPartida();
            return arbol;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar: " + e.getMessage());
            return new ArbolDecision();
        }
    }
}