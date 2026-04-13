package com.akinator;

public class ResultadoPartida {

    private boolean ganoAkinator;
    private String  personaje;
    private int     preguntasHechas;
    private int     profundidad;
    private int     totalNodos;
    private int     totalPersonajes;
    private long    tiempoNanos;
    private long    memoriaBytes;
    private boolean aprendioNuevo;
    private int     tamanioMemo;


    // ── Getters y Setters ─────────────────────────────────────────────────
    public boolean isGanoAkinator()              { return ganoAkinator; }
    public void    setGanoAkinator(boolean g)    { this.ganoAkinator = g; }
    public String  getPersonaje()                { return personaje; }
    public void    setPersonaje(String p)        { this.personaje = p; }
    public int     getPreguntasHechas()          { return preguntasHechas; }
    public void    setPreguntasHechas(int p)     { this.preguntasHechas = p; }
    public int     getProfundidad()              { return profundidad; }
    public void    setProfundidad(int p)         { this.profundidad = p; }
    public int     getTotalNodos()               { return totalNodos; }
    public void    setTotalNodos(int t)          { this.totalNodos = t; }
    public int     getTotalPersonajes()          { return totalPersonajes; }
    public void    setTotalPersonajes(int t)     { this.totalPersonajes = t; }
    public long    getTiempoNanos()              { return tiempoNanos; }
    public void    setTiempoNanos(long t)        { this.tiempoNanos = t; }
    public long    getMemoriaBytes()             { return memoriaBytes; }
    public void    setMemoriaBytes(long m)       { this.memoriaBytes = m; }
    public boolean isAprendioNuevo()             { return aprendioNuevo; }
    public void    setAprendioNuevo(boolean a)   { this.aprendioNuevo = a; }
    public int     getTamanioMemo()              { return tamanioMemo; }
    public void    setTamanioMemo(int t)         { this.tamanioMemo = t; }


    

    // ── Conversiones ──────────────────────────────────────────────────────
    public double getTiempoMs() {
        return tiempoNanos / 1000000.0;
    }

    public double getMemoriaKB() {
        return memoriaBytes / 1024.0;
    }

    // ── Análisis de complejidad ───────────────────────────────────────────
    public String getComplejidad() {
        if (totalPersonajes <= 1) return "O(1)";
        double logN  = Math.log(totalPersonajes) / Math.log(2);
        double ratio = profundidad / logN;
        if      (ratio <= 1.5) return "O(log n)  —  balanceado, los nodos se distribuyen bien";
        else if (ratio <= 3.0) return "O(log n)  —  moderado, desequilibrio algunos personajes están más profundos";
        else                   return "O(n)  —  degenerado , el árbol se comporta como una lista enlazada, con muchos personajes en un lado y pocos en el otro";
    }

    public String getResumenTexto() {
        return String.format(
            "Personaje: %s\n" +
            "Preguntas: %d\n" +
            "Profundidad: %d / %d nodos\n" +
            "Complejidad: %s\n" +
            "Tiempo: %.3f ms\n" +
            "Memoria: %.2f KB\n" +
            "Rutas memorizadas: %d\n" +
            "Aprendió personaje nuevo: %s",
            personaje, preguntasHechas,
            profundidad, totalNodos,
            getComplejidad(),
            getTiempoMs(),
            getMemoriaKB(),
            tamanioMemo,
            aprendioNuevo ? "Sí " : "No"
        );
    }

    
}