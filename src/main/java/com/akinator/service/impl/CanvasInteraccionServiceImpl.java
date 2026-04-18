package com.akinator.service.impl;

import com.akinator.service.CanvasInteraccionService;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;

public class CanvasInteraccionServiceImpl implements CanvasInteraccionService {
    private double dragStartX;
    private double dragStartY;

    @Override
    public void habilitarDrag(Canvas canvas, ScrollPane scrollPane){
        canvas.setOnMousePressed(e-> {
            dragStartX = e.getSceneX();
            dragStartY = e.getSceneY();
        });

        canvas.setOnMouseDragged(e->{
            double deltaX = e.getSceneX() - dragStartX;
            double deltaY = e.getSceneX() - dragStartY;

            double newH = scrollPane.getHvalue()-deltaX/(canvas.getWidth());
            double newV = scrollPane.getVvalue()-deltaY/(canvas.getHeight());

            scrollPane.setHvalue(Math.max(0, Math.min(1,newH)));
            scrollPane.setVvalue(Math.max(0, Math.min(1,newV)));

            dragStartX = e.getSceneX();
            dragStartY = e.getSceneY();
        });
    }

    @Override
    public void habilitarZoom(Canvas canvas) {
        canvas.setOnScroll(e -> {
            e.consume();
            double factor = e.getDeltaY() > 0 ? 1.1 : 0.9;
            double escalaActual = canvas.getScaleX();
            double nuevaEscala = Math.max(0.3, Math.min(3.0, escalaActual * factor));
            canvas.setScaleX(nuevaEscala);
            canvas.setScaleY(nuevaEscala);
        });
    }

    @Override
    public void centrarScroll(ScrollPane scrollPane){
        Platform.runLater(()->{
            scrollPane.setHvalue(0.5);
            scrollPane.setVvalue(0);
        });
    }

}
