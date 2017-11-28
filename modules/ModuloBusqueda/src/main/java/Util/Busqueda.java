/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author pichon
 */
public class Busqueda {
    
    public static ResultBusqueda busquedaAmplitud(Grafo g, String nodoInicioNombre, String nodoFinNombre){ //funcionna bien
        ResultBusqueda salida = new ResultBusqueda();
        String result = "\nAlgoritmo Primero en Amplitud";
        result += "\nCantidad de nodos: " + g.getNodos().size();
        result += "\nCantidad de aristas: " + g.getAristas().size();
        
        Queue<String> cola = new LinkedList<>();
        Queue<String> padresCola = new LinkedList<>();
        List<String> explorados = new ArrayList<>(); //LISTA-NODOS
        List<String> padresExplorados = new ArrayList<>();
        String nodoActual, nodoPadre;
        //Creo estructura de arbol para luego graficar
        NodoArbol root = new NodoArbol(nodoInicioNombre); //Inicializo la raiz del arbol
        ArrayList<NodoArbol> total = new ArrayList<>();
        ArrayList<NodoArbol> padresTotal = new ArrayList<>();
        NodoArbol actual = null;
        
        cola.add(nodoInicioNombre);
        padresCola.add("#");
        while(true){
            //Guardo el estado de la cola
            result += "\n" + cola;
            
            //Si la cola esta bacia, no se encontro el nodo final y termina
            if (cola.isEmpty()) {
                result += "\nNo se encontro el nodo destino";
                break;
            }
            nodoActual = cola.poll();
            nodoPadre = padresCola.poll();
            explorados.add(nodoActual);
            padresExplorados.add(nodoPadre);
            
            if (actual == null) {
                actual = root;
            }else {
                actual = total.get(0);
                actual.getParent().addChild(actual); 
                total.remove(0);
            }
            
            //Verifico si se alcanzo el nodo destino
            if (nodoActual.equals(nodoFinNombre)) {
                result += "\nNodo destino alcanzado"; //Mostrar camino
                String nodo = nodoActual;
                String secuenciaResultado = "";
                ArrayList<String> secuanciaResult = new ArrayList<>();
                while(!nodo.equals("#")){
                    secuanciaResult.add(nodo);
                    secuenciaResultado = nodo + " " + secuenciaResultado;
                    nodo = padresExplorados.get(explorados.indexOf(nodo));
                }
                result += "\nCamino solucion: " + secuenciaResultado;
                break;
            }
            List<String> vecinos = g.nodosVecinos(nodoActual);
            Iterator<String> i = vecinos.iterator();
            while(i.hasNext()){
                String nombre = i.next();
                if ( !explorados.contains(nombre) && !cola.contains(nombre)) {
                    //Creamos los nodos hijos del nodoArbol
                    NodoArbol hijo = new NodoArbol(nombre, actual);
                    total.add(hijo);
                    
                    cola.add(nombre);
                    padresCola.add(nodoActual);
                }
            }
        }
        salida.setRoot(root);
        salida.setProceso(result);
        return salida;
    }
    
    public static ResultBusqueda busquedaProfundidad(Grafo g, String nodoInicioNombre, String nodoFinNombre){  //funcionna bien
        ResultBusqueda salida = new ResultBusqueda();
        String result = "\nAlgoritmo Primero en Profundidad";
        result += "\nCantidad de nodos: " + g.getNodos().size();
        result += "\nCantidad de aristas: " + g.getAristas().size();
        
        ArrayDeque<String> pila = new ArrayDeque<>();
        ArrayDeque<String> padresPila = new ArrayDeque<>();
        List<String> explorados = new ArrayList<>();
        List<String> padresExplorados = new ArrayList<>();
        String nodoActual, nodoPadre;
        
        //Inicializo el arbol
        NodoArbol root = new NodoArbol(nodoInicioNombre);
        ArrayList<NodoArbol> total = new ArrayList<>();
        NodoArbol actual = null;
        
        pila.push(nodoInicioNombre);
        padresPila.push("#");
        while(true){
            //Guardo el estado de la pila
            result += "\n" + pila;
            
            //Si la cola esta bacia, no se encontro el nodo final y termina
            if (pila.isEmpty()) {
                result += "\nNo se encontro el nodo destino";
                break;
            }
            
            nodoActual = pila.pop();
            if (actual == null) {
                //La primera vez acgrego la raiz
                actual = root;
            }else {
                //Tomo el primer elemento de la lista (Primero en profuncidad).
                actual = total.get(0);
                actual.getParent().addChild(actual);
                total.remove(0);
            }
            nodoPadre = padresPila.pop();
            explorados.add(nodoActual);
            padresExplorados.add(nodoPadre);

            //Pregunto si es el nodo final
            if (nodoActual.equals(nodoFinNombre)) {
                result += "\nNodo destino alcanzado"; //Mostrar camino
                String nodo = nodoActual;
                String secuenciaResultado = "";
                ArrayList<String> secuanciaResult = new ArrayList<>();
                while(!nodo.equals("#")){
                    secuanciaResult.add(nodo);
                    secuenciaResultado = nodo + " " + secuenciaResultado;
                    nodo = padresExplorados.get(explorados.indexOf(nodo));
                }
                result += "\nCamino solucion: " + secuenciaResultado;
                break;
            }
            //Agrego los vecinos del nodo que no hayan sido explorados
            ArrayList<String> vecinos = g.nodosVecinos(nodoActual);
            ArrayDeque<NodoArbol> aux = new ArrayDeque<>();
            for (int i = vecinos.size()-1; i >= 0; i--) {
                String nombre = vecinos.get(i);
                if ( !explorados.contains(nombre)) {
                    //Agrego los hijos al nodoArbol
                    NodoArbol hijo = new NodoArbol(nombre, actual);
                    aux.push(hijo);
                        
                    if (pila.contains(nombre)) {
                        pila.remove(nombre);
                        padresPila.remove(nodoActual);
                    }
                    pila.push(nombre);
                    padresPila.push(nodoActual);
                }
            }
            total.addAll(0, aux);
        }
        salida.setRoot(root);
        salida.setProceso(result);
        return salida;
    }
   
    public static ResultBusqueda busquedaEscaladaSimple(Grafo g, String nodoInicioNombre, String nodoFinNombre){
        ResultBusqueda salida = new ResultBusqueda();
        String result = "\nAlgoritmo Primero en Profundidad";
        result += "\nCantidad de nodos: " + g.getNodos().size();
        result += "\nCantidad de aristas: " + g.getAristas().size();
        
        ArrayList<String> explorados = new ArrayList<>();
        ArrayList<String> padresExplorados = new ArrayList<>();
        
        //Inicializo el arbol
        NodoArbol root = new NodoArbol(nodoInicioNombre);
        root.setValue(g.buscarNodo(nodoInicioNombre).getValor());
        NodoArbol actual = root;
        
        String nodoActual = nodoInicioNombre;
        explorados.add(nodoActual);
        padresExplorados.add("#");
        boolean bandera;
        while(true){
            int valorNodoActual = g.buscarNodo(nodoActual).getValor();
            int valorNodoProx;
            result += "\n" + nodoActual+"("+g.buscarNodo(nodoActual).getValor()+")";
            
            //Verifico si el nodo es el nodoFinal
            if (nodoActual.equals(nodoFinNombre)) {
                result += "\nSe alcanzo el nodo destino";
                break;
            }
            
            ArrayList<String> vecinos = g.nodosVecinos(nodoActual);
            ArrayList<String> vecinosAux = new ArrayList<>();
            //Elimino los nodo ya explorados
            for (int i = 0; i < vecinos.size(); i++) {
                if(!explorados.contains(vecinos.get(i))){
                    vecinosAux.add(vecinos.get(i));
                }
            }
            vecinos = vecinosAux;
            
            //Si no hay vecinos termino el algoritmo
            if (vecinos.isEmpty()) {
                result += "\nNo se alcanzo el nodo destino";
                break;
            }
            bandera = true;
            
            //Verifico si existe un nodo que mejore el valor actual
            for (int i = 0; i < vecinos.size(); i++) {
                valorNodoProx = g.buscarNodo(vecinos.get(i)).getValor();
                result += "\nVecino: "+vecinos.get(i)+"("+g.buscarNodo(vecinos.get(i)).getValor()+")";
                
                //Creo un nuevo nodo
                NodoArbol hijo = new NodoArbol(vecinos.get(i), actual);
                hijo.setValue(valorNodoProx);
                actual.addChild(hijo);
                
                //Si lo encuentro avanzo a ese nodo
                if(valorNodoActual > valorNodoProx){
                    result += "-> Entro";
                    padresExplorados.add(nodoActual);   //Almaceno el padre
                    nodoActual = vecinos.get(i);    //Avanzo al siguiente nodo
                    explorados.add(nodoActual);
                    bandera = false;
                    actual = hijo;
                    break;
                }
                explorados.add(nodoActual);
            }
            if (bandera) {
                result += "\nNo se alcanzo el nodo destino";
                break;
            }
        }
        salida.setRoot(root);
        salida.setProceso(result);
        return salida;
    }
    
    public static ResultBusqueda busquedaEscaladaMaxima(Grafo g, String nodoInicioNombre, String nodoFinNombre){
        ResultBusqueda salida = new ResultBusqueda();
        String result = "\nAlgoritmo Primero en Amplitud";
        result += "\nCantidad de nodos: " + g.getNodos().size();
        result += "\nCantidad de aristas: " + g.getAristas().size();
        
        List<String> explotados = new ArrayList<>();
        
        //Inicializo el arbol
        NodoArbol root = new NodoArbol(nodoInicioNombre);
        root.setValue(g.buscarNodo(nodoInicioNombre).getValor());
        NodoArbol actual = root;
        
        String nodoActual = nodoInicioNombre;
        explotados.add(nodoActual);
        boolean bandera;
        while(true){
            int valorNodoActual = g.buscarNodo(nodoActual).getValor();
            int valorNodoProx;
            result += "\n" + nodoActual+"("+g.buscarNodo(nodoActual).getValor()+")";
            
            //Si el nodo actual es igual al nodoFinal termino
            if (nodoActual.equals(nodoFinNombre)) {
                result += "\nSe alcanzo el nodo destino";
                break;
            }
            
            List<String> vecinos = g.nodosVecinos(nodoActual);
            //Elimino los nodos ya explorados
            for (int i = 0; i < vecinos.size(); i++) {
                if(explotados.contains(vecinos.get(i))){
                    vecinos.remove(i);
                }
            }
            //Si la lista de vecinos esta bacia termino
            if (vecinos.isEmpty()) {
                result += "\nNo se alcanzo el nodo destino";
                break;
            }
            result += "\nVecinos: ";
            for (int i = 0; i < vecinos.size(); i++) {
                result += vecinos.get(i)+"("+g.buscarNodo(vecinos.get(i)).getValor()+") ";
            }
            bandera = true;
            NodoArbol aux = null;
            for (int i = 0; i < vecinos.size(); i++) {
                valorNodoProx = g.buscarNodo(vecinos.get(i)).getValor();
                
                //Creo un nuevo nodo
                NodoArbol hijo = new NodoArbol(vecinos.get(i), actual);
                hijo.setValue(valorNodoProx);
                actual.addChild(hijo);
                
                if(valorNodoActual > valorNodoProx){
                    nodoActual = vecinos.get(i);
                    valorNodoActual = valorNodoProx;
                    aux = hijo;
                    bandera = false;
                }
            }
            if (bandera) {
                result += "\nNo se alcanzo el nodo destino";
                break;
            }else{
                actual = aux;
            }
            explotados.add(nodoActual);
        }
        salida.setRoot(root);
        salida.setProceso(result);
        return salida;
    }

    public static ResultBusqueda busquedaAEstrella(Grafo g, String nodoInicioNombre, String nodoFinNombre){
        ResultBusqueda salida = new ResultBusqueda();
        String result2 = "\nAlgoritmo A*";
        result2 += "\nCantidad de nodos: " + g.getNodos().size();
        result2 += "\nCantidad de aristas: " + g.getAristas().size();
        
        List<String> abiertos = new ArrayList<>();
        List<Integer> valorFuncion = new ArrayList<>();
        List<Integer> valorG = new ArrayList<>();
        List<String> cerrados = new ArrayList<>();
        List<Integer> valorFuncionCerrados = new ArrayList<>();
        List<String> padresCerrados = new ArrayList<>();
        List<String> padresAbiertos = new ArrayList<>();
        List<String> sucesores = new ArrayList<>();
//        List<Integer> valorFuncionSucesores = new ArrayList<>();
        String mejorNodo, nodoActual;
        int index;
        
        //Inicializo el arbol
        NodoArbol root = new NodoArbol(nodoInicioNombre);
        root.setValue(g.buscarNodo(nodoInicioNombre).getValor());
        ArrayList<NodoArbol> totalAbiertos = new ArrayList<>();
        NodoArbol actual;
        
        padresAbiertos.add("#");
        abiertos.add(nodoInicioNombre);
        totalAbiertos.add(null);
        valorFuncion.add(g.buscarNodo(nodoInicioNombre).getValor()+0);
        valorG.add(0);
        while(true){
            String result = "\n\nPadres Abiertos: "+padresAbiertos+"\nAbiertos: "+abiertos+"\nConsto hasta nodo: "+valorG+"\nValor funcion: "+valorFuncion+"\nCerrados: "+cerrados+"\nPadres cerrados: "+padresCerrados+"\n";
            
            if (abiertos.isEmpty()) {
                result += "\nError";
                System.out.println(result);
                result2 += result;
                break;
            }
            
            if (abiertos.size() == 1) {
                result += "\nEntro la primera vez, mejor nodo= "+abiertos.get(0);
                mejorNodo = abiertos.get(0);
                actual = root;
            } else {
                //Buscamos el mejor nodo de la lista abiertos
                mejorNodo = getMejorNodo(abiertos, valorFuncion);
                result += "\nDe los abiertos el mejor nodo es: "+mejorNodo;
                actual = totalAbiertos.get(abiertos.indexOf(mejorNodo));
            }
            
            //Pregunto si es el nodo final
            if (mejorNodo.equals(nodoFinNombre)) {
                result += "\n"+mejorNodo+" es el nodo objetivo, finalizo la busqueda\nNodo encontrado";
                index = abiertos.indexOf(mejorNodo);
                cerrados.add(mejorNodo);
                padresCerrados.add(padresAbiertos.get(index));
                
                String nodo = mejorNodo;
                String secuenciaResultado = "";
                while(!nodo.equals("#")){
                    secuenciaResultado = nodo +" "+ secuenciaResultado;
                    nodo = padresCerrados.get(cerrados.indexOf(nodo));
                }
                result += "\nCamino solucion: [ "+secuenciaResultado+"]";
                System.out.println(result);
                result2 += result;
                break;
            }
            result += "\n"+mejorNodo+" No es el nodo final";
            
            int heuristicaNodo;
            int costoHastaMejorNodo;
            //Tratar mejorNodo, Exploro sus vecinos
            index = abiertos.indexOf(mejorNodo);
            //Paso a mejorNodo, de abiertos a cerrados
            cerrados.add(mejorNodo);
            valorFuncionCerrados.add(valorFuncion.get(index));
            padresCerrados.add(padresAbiertos.get(index));
            padresAbiertos.remove(index);
            //Calculo el costo hasta el nodo actual (mejorNodo)
            costoHastaMejorNodo = valorG.get(abiertos.indexOf(mejorNodo)); 
            abiertos.remove(index);
            totalAbiertos.remove(index);
            valorG.remove(index);
            valorFuncion.remove(index);
            result += "\nLo saco de abiertos y lo agrego a cerrados";
            
            //Genero los sucesores de mejor nodo
            sucesores = g.nodosVecinos(mejorNodo);
            result += "\nSucesores de mejorNodo: "+sucesores+"\nPara cada sucesor hago:";

            //Para cada sucesor 
            for (int i = 0; i < sucesores.size(); i++) {
                heuristicaNodo = g.buscarNodo(sucesores.get(i)).getValor();
                int costoHastaSucesorI = costoHastaMejorNodo+g.buscarArista(mejorNodo+"-"+sucesores.get(i)).getValor(); 
                int valorFuncionSucesores = costoHastaSucesorI + heuristicaNodo;
                
                //Si sucesor esta en abiertos, denominar Viejo a este
                if(abiertos.contains(sucesores.get(i))){
                    index = abiertos.indexOf(sucesores.get(i));
                    
                    //Comparo el valor de funcion de viejo nodo y el nuevo encontrado, para ver cual es mejor
                    if (valorFuncion.get(index) > valorFuncionSucesores) {
                        //ya esta en abiertos, pero este es mejor, asi que le coloco el nuevo padre
                        result += "\nSucesor: "+sucesores.get(i)+" ya esta en abiertos, pero este es mejor:"+valorFuncionSucesores;
                        valorFuncion.set(index, valorFuncionSucesores);
                        valorG.set(index, costoHastaSucesorI);
                        padresAbiertos.set(index, mejorNodo);
                        //Creo un nuevo nodo y lo agrego
                        NodoArbol n = new NodoArbol(sucesores.get(i), actual);
                        n.setValue(valorFuncionSucesores);
                        actual.addChild(n);
                        totalAbiertos.set(index,n);
                        
                    }else{
                        //ya esta en abiertos, y no es mejor, lo descarto
                        result += "\nSucesor: "+sucesores.get(i)+" ya esta en abiertos, y no es mejor";
                    }
                } else {
                    if (cerrados.contains(sucesores.get(i))) {
                        result += "\nSucesor: "+sucesores.get(i)+" ya se encuentra en cerrados";
                    } else {
                        //Si sucesor no estaba en abiertos ni cerrados, agregarlo a abiertos
                        result += "\nSucesor: "+sucesores.get(i)+" ("+valorFuncionSucesores+") no esta en abiertos ni cerrados, lo agrego a abiertos";
                        abiertos.add(sucesores.get(i));
                        padresAbiertos.add(mejorNodo);
                        valorFuncion.add(valorFuncionSucesores);
                        valorG.add(costoHastaSucesorI);
                        
                        NodoArbol n = new NodoArbol(sucesores.get(i), actual);
                        n.setValue(valorFuncionSucesores);
                        actual.addChild(n);
                        totalAbiertos.add(n);
                    }
                }
            } //fin for
            System.out.println(result);
            result2 += result;
        } //fin while
        salida.setRoot(root);
        salida.setProceso(result2);
        return salida;
    }
    
    private static String getMejorNodo(List<String> abiertos, List<Integer> valorFuncion){
        int min = valorFuncion.get(0);
        int posicion = 0;
        for (int i = 1; i < valorFuncion.size(); i++) {
            if (min > valorFuncion.get(i)) {
                min = valorFuncion.get(i);
                posicion = i;
            }
        }
        return abiertos.get(posicion);
    }
}
