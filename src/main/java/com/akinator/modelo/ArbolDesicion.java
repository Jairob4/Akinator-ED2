package com.akinator.modelo;

import java.io.Serializable;
import java.util.*;

import com.akinator.ResultadoPartida;
import com.akinator.service.EstrategiaSeleccionPreguntas;
import com.akinator.service.impl.EstrategiaGreedy;
/**
 * Árbol binario de decisión del Akinator.
 *
 * TÉCNICAS IMPLEMENTADAS:
 * ┌─────────────────────────────────────────────────────────────┐
 * │ 1. BACKTRACKING  → pila de estados para retroceder         │
 * │ 2. GREEDY        → elegir pregunta que mejor divide        │
 * │ 3. PROG. DINÁMICA→ memoización de rutas por personaje      │
 * │ 4. RECURSIVIDAD  → recorridos, altura, búsqueda            │
 * └─────────────────────────────────────────────────────────────┘
 */

// n va a la profundidad del nodo.
public class ArbolDesicion implements Serializable{
    private final static long serialVersionUID = 1L;

    //Estado actual del juego
    private NodoArbol raiz; // Raíz del árbol de decisión
    private NodoArbol nodoActual; // Nodo actual en el recorrido

    //Para backtracking
    private Deque<NodoArbol> pilaEstados; // Pila para almacenar nodos visitados, esta será la solucion parcial (backtracking)
    private Deque<Boolean> pilaRespuestas; // Pila para almacenar respuestas (true/false) correspondientes a los nodos visitados

    //Para programación dinámica
    // la clave es el nombre del personaje y el valor es la lista de nodos (preguntas) que se deben seguir para llegar a ese personaje
    private Map<String, List<Boolean>> memoria; // Mapa para memoización de rutas por personaje

    //Metricas de una partida
    private int preguntasRealizadas = 0; 
    private int profundidadActual = 0;
    private long tiempoInicio; // Para medir el tiempo de la partida

    private String personajePartidaActual = ""; // Para almacenar el personaje que se está adivinando en la partida actual

    //inyectar estrategia de selección de preguntas (greedy)
    private transient EstrategiaSeleccionPreguntas estrategia;


    public ArbolDesicion() {
        pilaEstados = new ArrayDeque<>();
        pilaRespuestas = new ArrayDeque<>();
        memoria = new HashMap<>();
        construirArbolInicial();
        reiniciarPartida();
    }

    //aca ponemos algunas preguntas y personajes para que el juego tenga algo de contenido al iniciar, luego se pueden agregar más o modificar, aparte luego haremos que el usuario pueda agregar sus propias preguntas y personajes
    private void construirArbolInicial() {
        raiz = new NodoArbol("¿Es un humano real?"); // pregunta base

        // Personajes
        NodoArbol einstein = new NodoArbol("Albert Einstein", true);
        NodoArbol napoleon = new NodoArbol("Napoleon Bonaparte", true);
        NodoArbol goku = new NodoArbol("Goku", true);
        NodoArbol batman = new NodoArbol("Batman", true);

        // Nivel 1
        NodoArbol cientifico = new NodoArbol("¿Es o fue un científico?");
        cientifico.setHijoDerecho(einstein); // respuesta "sí" lleva a Einstein
        cientifico.setHijoIzquierdo(napoleon); // respuesta "no" lleva a Napoleon

        NodoArbol poderes = new NodoArbol("¿Tiene poderes sobrenaturales?");
        poderes.setHijoDerecho(goku); // respuesta "sí" lleva a Goku
        poderes.setHijoIzquierdo(batman); // respuesta "no" lleva a Batman

        // Nivel 0 (raíz)
        raiz.setHijoDerecho(cientifico); // respuesta "sí" lleva a científico
        raiz.setHijoIzquierdo(poderes); // respuesta "no" lleva a futbolista

        

    }

    public void reiniciarPartida() {
        nodoActual = raiz;
        preguntasRealizadas = 0;
        profundidadActual = 0;
        personajePartidaActual = "";
        tiempoInicio = System.nanoTime();
        pilaEstados.clear();
        pilaRespuestas.clear();
    }

    public void reiniciarArbol(){
        memoria.clear();
        pilaEstados.clear();
        pilaRespuestas.clear();
        construirArbolInicial();
        reiniciarPartida();
    }

    public String getPreguntaActual(){
        return nodoActual != null ? nodoActual.getContenido() : "";
    }

    public boolean getPersonajeActual(){
        return nodoActual != null && nodoActual.isEsPersonaje();
    }

    // hay mas preguntas para hacer o el nodo actual es un personaje
    public boolean juegoTerminado(){
        return nodoActual == null || nodoActual.isEsPersonaje();
    }

    /*Backtracking:  
        avanza en el arbol según la respuesta del usuario, guardando el estado actual para poder retroceder si es necesario
        O(1) para avanzar
    */

    public void responder(boolean esSi){
        if (nodoActual == null || nodoActual.isEsPersonaje()) return; // No hay nodo actual, no se puede responder

        // ingresar estado actual en la solucion parcial (backtracking)
        pilaEstados.push(nodoActual);
        pilaRespuestas.push(esSi);

        // Avanzar según la estrategia de selección de preguntas
        if(estrategia != null) {
            nodoActual = estrategia.seleccionarSiguiente(nodoActual, esSi);
        } else {
            // Si no hay estrategia, avanzar de forma predeterminada (hijo derecho para "sí", hijo izquierdo para "no")
            nodoActual = esSi ? nodoActual.getHijoDerecho() : nodoActual.getHijoIzquierdo();
        }

        preguntasRealizadas++;
        profundidadActual++;
    }

    /*Retroceder:  
        Backtrack:
        recupera el último estado visitado y actualiza el nodo actual
        O(1) para retroceder
    */
    public boolean retroceder(){
        if (!puedoRetroceder()) return false; // No hay estados para retroceder

        nodoActual = pilaEstados.pop(); // Recuperar el último nodo visitado
        pilaRespuestas.pop(); // Eliminar la respuesta correspondiente

        preguntasRealizadas--;
        profundidadActual--;
        return true;
    }

    public boolean puedoRetroceder() { // Esta seria el isValid de este backtracking, se puede retroceder si hay estados en la pila
        return !pilaEstados.isEmpty(); // Se puede retroceder si hay estados en la pila
    }

    /* Programación dinámica:
        memoiza la ruta (secuencia de preguntas) para cada personaje adivinado, para acelerar futuras partidas
        O(1) para recuperar la ruta memorizada, O(n) para almacenar una nueva ruta

        cuando nuestro akinator falla, va a aprender un personaje nuevo, entonces va a guardar la ruta (secuencia de preguntas) que llevó a ese personaje,
        para que si en el futuro alguien juega y piensa en ese mismo personaje, el akinator pueda recuperar esa ruta memorizada y adivinarlo más rápido en O(1) 
        O(n) para almacenar la nueva ruta, donde n es la profundidad del nodo del personaje nuevo
    */

    public void aprenderPersonaje(String nuevoPersonaje, String nuevaPregunta, boolean respuestaEsSi){
        NodoArbol personajeViejo = nodoActual; // Nodo del personaje que el Akinator no pudo adivinar
        String nombrePersonajeViejo = personajeViejo.getContenido();

        personajePartidaActual = nuevoPersonaje; // Guardar el personaje que se está aprendiendo en la partida actual

        if(nuevoPersonaje.equalsIgnoreCase(nombrePersonajeViejo) || existePersonaje(nuevoPersonaje)) {
            // Si el nuevo personaje es el mismo que el viejo, no hay nada que aprender
            return;
        }

        // convertir el nodo en una nueva pregunta, para expandir el arbol
        personajeViejo.setContenido(nuevaPregunta);
        personajeViejo.setEsPersonaje(false); 

        NodoArbol nuevoNodo = new NodoArbol(nuevoPersonaje, respuestaEsSi); // Crear nodo para el nuevo personaje
        NodoArbol nodoViejo = new NodoArbol(nombrePersonajeViejo, true); // Crear nodo para el personaje viejo

        if(respuestaEsSi){
            personajeViejo.setHijoDerecho(nuevoNodo); // Respuesta "sí" lleva al nuevo personaje
            personajeViejo.setHijoIzquierdo(nodoViejo); // Respuesta "no" lleva al personaje viejo
        } else {
            personajeViejo.setHijoIzquierdo(nuevoNodo); // Respuesta "no" lleva al nuevo personaje
            personajeViejo.setHijoDerecho(nodoViejo); // Respuesta "sí" lleva al personaje viejo
        }

        nodoActual = personajeViejo; // Actualizar el nodo actual al nuevo nodo de pregunta

        // eliminar la ruta memorizada del personaje viejo, ya que ahora ese nodo es una pregunta
        memoria.remove(nombrePersonajeViejo);

        List<Boolean> rutaBase = reconstruirRutaDesdePila(); // Reconstruir la ruta base desde la raíz hasta el nodo actual

        // almacenar la nueva ruta para el nuevo personaje y hasta el nuevo nodo del personaje viejo con programacion dinamica
        // reconstruimos la ruta a traves de la solucion parcial guardada en la pila
        List<Boolean> respuestasPartida = new ArrayList<>(pilaRespuestas);
        Collections.reverse(respuestasPartida); // Invertir la lista para obtener la ruta correcta desde la raíz

        // ruta del personaje viejo
        List<Boolean> rutaPersonajeViejo = new ArrayList<>(respuestasPartida);
        rutaPersonajeViejo.add(!respuestaEsSi); // La respuesta opuesta para llegar al personaje viejo
        memoria.put(nombrePersonajeViejo, rutaPersonajeViejo); // Guardar la ruta del personaje viejo

        // ruta del nuevo personaje
        List<Boolean> rutaNuevoPersonaje = new ArrayList<>(respuestasPartida);
        rutaNuevoPersonaje.add(respuestaEsSi); // La respuesta para llegar al nuevo personaje
        memoria.put(nuevoPersonaje, rutaNuevoPersonaje); // Guardar la ruta del nuevo personaje

    }

    public boolean existePersonaje(String personaje) {
        return memoria.containsKey(personaje) || buscarPersonajeRec(raiz, personaje);
    }

    private boolean buscarPersonajeRec(NodoArbol nodo, String personaje) {
        if (nodo == null) return false;
        if (nodo.isEsPersonaje() && nodo.getContenido().equalsIgnoreCase(personaje)) {
            return true; // Personaje encontrado
        }
        // Buscar en ambos subárboles
        return buscarPersonajeRec(nodo.getHijoDerecho(), personaje) ||
               buscarPersonajeRec(nodo.getHijoIzquierdo(), personaje);
    }

    private List<Boolean> reconstruirRutaDesdePila() {
        List<Boolean> ruta = new ArrayList<>(pilaRespuestas);
        Collections.reverse(ruta); // Invertir para obtener la ruta desde la raíz
        return ruta;
    }

     /* Estrategia Greedy:
        calcula qué tan buena es una pregunta para dividir el árbol, y elige la mejor pregunta según esa métrica
        O(n) para evaluar todas las preguntas disponibles en el nodo actual, donde n es el número de preguntas hijas del nodo actual
     */

    /**
        GREEDY: calcula qué tan buena es una pregunta para dividir el árbol. 
        Score = 0 es perfecto (divide 50/50).

        si una pregunta es muy buena para dividir el árbol, entonces va a tener un score cercano a 0, lo que significa que divide el árbol en partes iguales (50/50).
     
        Complejidad: O(n) donde n = personajes en el árbol
     */

    public double scoreGreedy(String pregunta) {
        List<String> personajes = obtenerTodosPersonajes();
        int total = personajes.size();
        if (total == 0) return 1.0;

        // Simulamos cuántos personajes irían a SI y cuántos a NO
        // En una implementación real, tendriamos que usar una entropia (modelo de ML), pero aca usamos una heuristica
        // Aquí usamos heurística basada en longitud de pregunta
        int estimadoSi = total / 2;
        int estimadoNo = total - estimadoSi;
        return Math.abs(estimadoSi - estimadoNo) / (double) total;
        // 0.0 = división perfecta (óptimo greedy)
        // 1.0 = toda la carga en un lado (pésimo)
    }

     /* Metodo axuliliar para obtener todos los personajes del árbol — O(n) */
    public List<String> obtenerTodosPersonajes() {
        List<String> lista = new ArrayList<>();
        obtenerPersonajesRec(raiz, lista);
        return lista;
    }

    private void obtenerPersonajesRec(NodoArbol nodo, List<String> lista) {
        if (nodo == null) return;
        if (nodo.isEsPersonaje()) {
            lista.add(nodo.getContenido());
            return;
        }
        obtenerPersonajesRec(nodo.getHijoDerecho(),   lista);
        obtenerPersonajesRec(nodo.getHijoIzquierdo(), lista);
    }

    /*   programacion dinamica:
        busca en la memoria si el personaje ya tiene una ruta.
        si la tiene la retorna en 0(1) por lo que la complejidad computacional baja significativamente
    */

    public void memorizarAcierto(String personaje){
        if(nodoActual == null || !nodoActual.isEsPersonaje()) return; // No hay personaje actual para memorizar
        if(nodoActual.getContenido().equalsIgnoreCase(personaje) && !memoria.containsKey(personaje)) {
            List<Boolean> ruta = reconstruirRutaDesdePila(); // Reconstruir la ruta desde la pila
            memoria.put(personaje, ruta); // Guardar la ruta memorizada para el personaje
        }
    }

    public List<Boolean> obtenerRutaPersonaje(String personaje) {
        if(memoria.containsKey(personaje)) {
            return memoria.get(personaje); // Retornar la ruta memorizada en O(1)
        }

        List<Boolean> ruta = new ArrayList<>();
        boolean encontrado = buscarRutaRec(raiz, personaje, ruta);
        if(encontrado){
            memoria.put(personaje, new ArrayList<>(ruta)); // Guardar la ruta encontrada en la memoria
            return ruta;
        }
        return Collections.emptyList(); // Retornar lista vacía si no se encuentra el personaje
    }

    // Busqueda DFS con backtracking para encontrar la ruta O(n) donde n es el número de nodos en el árbol
    private boolean buscarRutaRec(NodoArbol nodo, String personaje, List<Boolean> ruta) {
        if(nodo == null) return false;
        if(nodo.isEsPersonaje()){
            return nodo.getContenido().equalsIgnoreCase(personaje); // Verificar si el nodo actual es el personaje buscado
        }
        // Buscar en el hijo derecho (respuesta "sí")
        ruta.add(true); // Agregar respuesta "sí" a la ruta parcial
        if(buscarRutaRec(nodo.getHijoDerecho(), personaje, ruta)) {
            return true; // Personaje encontrado en el subárbol derecho
        }
        ruta.remove(ruta.size() - 1); // Eliminar la última respuesta agregada (Backtracking)

        // Buscar en el hijo izquierdo (respuesta "no")
        ruta.add(false); // Agregar respuesta "no" a la ruta parcial
        if(buscarRutaRec(nodo.getHijoIzquierdo(), personaje, ruta)) {
            return true; // Personaje encontrado en el subárbol izquierdo
        }
        ruta.remove(ruta.size() - 1); // Eliminar la última respuesta agregada (Backtracking)
        return false; // Personaje no encontrado
    }

    //metodos recursivos O(n)

    public int contarNodos() {
        return contarNodosRec(raiz);
    }

    private int contarNodosRec(NodoArbol nodo) {
        if (nodo == null) return 0;
        return 1 + contarNodosRec(nodo.getHijoDerecho()) + contarNodosRec(nodo.getHijoIzquierdo());
    }

    public int contarPersonajes() {
        return contarPersonajesRec(raiz);
    }

    private int contarPersonajesRec(NodoArbol nodo) {
        if (nodo == null)          return 0;
        if (nodo.isEsPersonaje())  return 1;
        return contarPersonajesRec(nodo.getHijoDerecho())
             + contarPersonajesRec(nodo.getHijoIzquierdo());
    }

    public int obtenerAltura() {
        return obtenerAlturaRec(raiz);
    }

    private int obtenerAlturaRec(NodoArbol nodo) {
        if (nodo == null) return 0;
        int alturaIzq = obtenerAlturaRec(nodo.getHijoIzquierdo());
        int alturaDer = obtenerAlturaRec(nodo.getHijoDerecho());
        return 1 + Math.max(alturaIzq, alturaDer);
    }

    // ahora vienen las metricas de la partida, complejidad practica y analisis de rendimiento
    public long getTiempoPartida() {
        return System.nanoTime() - tiempoInicio; // Tiempo transcurrido en nanosegundos
    }

    public long getMemoriaUsadaBytes() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory(); // Memoria usada en bytes
    }

    public String analizarComplejidad(){
        int n = contarNodos();
        double logN = n > 1 ? Math.log(n) / Math.log(2) : 1; // Logaritmo base 2 de n
        double ratio = profundidadActual/logN; // Ratio entre profundidad actual y log(n)
        if(ratio < 1.5) return "O(log n) - Excelente rendimiento, el árbol está bien balanceado.";
        else if(ratio < 3) return "O(n) - Rendimiento aceptable, el árbol tiene algunas ramas más largas.";
        else return "O(n) - Rendimiento pobre, el árbol está muy desbalanceado.";
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject(); // reconstruye los atributos serializados

        // Reinyectar estrategia (porque es transient)
        if (this.estrategia == null) {
            this.estrategia = new EstrategiaGreedy();
        }
    }

    // metricas generales de la partida
    public int getPreguntasRealizadas() {
        return preguntasRealizadas;
    }

    public int getProfundidadActual() {
        return profundidadActual;
    }

    public NodoArbol getNodoActual() {
        return nodoActual;
    }

    public NodoArbol getRaiz() {
        return raiz;
    }

    public int getTamanioMemoria() {
        return memoria.size();
    }

    public void setEstrategia(EstrategiaSeleccionPreguntas estrategia) {
        this.estrategia = estrategia;
    }

    public ResultadoPartida generarResultado(boolean gano, boolean aprendio) {
        ResultadoPartida r = new ResultadoPartida();

        //  Resultado del juego 
        r.setGanoAkinator(gano);
        r.setAprendioNuevo(aprendio);
        if (gano && nodoActual != null && nodoActual.isEsPersonaje()) {
            r.setPersonaje(nodoActual.getContenido());
        } else if (aprendio && !personajePartidaActual.isEmpty()) {
            r.setPersonaje(personajePartidaActual);
        } else {
            r.setPersonaje("Desconocido");
        }

        // Métricas de recorrido 
        r.setPreguntasHechas(preguntasRealizadas);
        r.setProfundidad(profundidadActual);

        // Métricas del árbol (recursivas)
        r.setTotalNodos(contarNodos());
        r.setTotalPersonajes(contarPersonajes());

        // Tiempo real con System.nanoTime() 
        r.setTiempoNanos(System.nanoTime() - tiempoInicio);

        // Memoria real con Runtime 
        Runtime rt = Runtime.getRuntime();
        r.setMemoriaBytes(rt.totalMemory() - rt.freeMemory());

        // Programación dinámica: rutas memorizadas 
        r.setTamanioMemo(memoria.size());

        return r;
    }

    
}
