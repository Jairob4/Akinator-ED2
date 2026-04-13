package com.akinator.repository.impl;

import com.akinator.modelo.ArbolDesicion;
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
    public void guardar(ArbolDesicion arbol) {
        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream(ruta))) {

            oos.writeObject(arbol);

        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    @Override
    public ArbolDesicion cargar() {

        if (!Files.exists(Paths.get(ruta))) {
            return new ArbolDesicion();
        }

        try (ObjectInputStream ois =
                 new ObjectInputStream(new FileInputStream(ruta))) {

            ArbolDesicion arbol =
                    (ArbolDesicion) ois.readObject();

            arbol.reiniciarPartida();
            return arbol;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar: " + e.getMessage());
            return new ArbolDesicion();
        }
    }
}