package com.akinator.service;


import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;

public interface CanvasInteraccionService {
    void habilitarDrag(Canvas canvas, ScrollPane scrollpane);
    void habilitarZoom(Canvas canvas);
    void centrarScroll(ScrollPane scrollpane);
} 
