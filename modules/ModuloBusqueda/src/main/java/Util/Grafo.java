/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author pichon
 */
public class Grafo implements Serializable{
    
    private String expresion;
    private String nombre;
    private Map<String,Nodo> nodos = new HashMap<>();
    private Map<String,Arista> aristas = new HashMap<>();
    private ArrayList<String> nombreNodos = new ArrayList<>();
    
    public Nodo buscarNodo(String nombre){
        return nodos.get(nombre);
    }

    public ArrayList<String> getNombreNodos() {
        return nombreNodos;
    }
    
    public boolean agregarNodo(Nodo n){
        boolean result = false;
        if (n != null && !n.getNombre().isEmpty()) {
            nodos.put(n.getNombre(), n);
            nombreNodos.add(n.getNombre());
            result = nodos.get(n.getNombre()) != null;
        }
        return result;
    }
    
    public boolean agregarArista(Arista a){
        boolean result = false;
        if (a != null && !a.getNombre().isEmpty()) {
            aristas.put(a.getNombre(), a);
            result = aristas.get(a.getNombre()) != null;
        }
        return result;
    }
    
    public Arista buscarArista(String nombreArista){
        String[] par = nombreArista.split("-");
        String origen = par[0];
        String destino = par[1];
        return (aristas.get(origen + "-" + destino)!= null? aristas.get(origen + "-" + destino) : aristas.get(destino + "-" + origen)) ;
    }
    
    public List<Arista> nodosVecinosDistancia(String nombreNodo){
        List<Arista> result = new ArrayList<>();
        for(Map.Entry<String,Arista> entry : this.aristas.entrySet()){
            String[] par = entry.getKey().split("-");
            String nodo;

            if (par[0].equals(nombreNodo) || par[1].equals(nombreNodo)) {
                Nodo origen = null;
                Nodo destino = null;
                if (par[0].equals(nombreNodo)) {
                    nodo = par[1];
                    origen = this.nodos.get(nombreNodo);
                    destino = this.nodos.get(par[1]);
                }else{
                    nodo = par[0];
                    origen = this.nodos.get(par[1]);
                    destino = this.nodos.get(nombreNodo);
                }
                Arista a = new Arista(nodo, entry.getValue().getValor());
                a.setOrigen(origen);
                a.setDestino(destino);
                result.add(a);
            }
        }
        return result;
    }
    
    public ArrayList<String> nodosVecinos(String nombreNodo){
        ArrayList<String> result = new ArrayList<>();
        for(Map.Entry<String,Arista> entry : this.aristas.entrySet()){
            String[] par = entry.getKey().split("-");
            String nodo;

            if (par[0].equals(nombreNodo) || par[1].equals(nombreNodo)) {
                if (par[0].equals(nombreNodo)) {
                    nodo = par[1];
                }else{
                    nodo = par[0];
                }
                result.add(nodo);
            }
        }
        return result;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Map<String, Nodo> getNodos() {
        return nodos;
    }

    public void setNodos(Map<String, Nodo> nodos) {
        this.nodos = nodos;
    }

    public Map<String, Arista> getAristas() {
        return aristas;
    }

    public void setAristas(Map<String, Arista> aristas) {
        this.aristas = aristas;
    }

    public String getExpresion() {
        this.expresion = "GRAFO " + nombre + "{\n";
        
        Map<String, Nodo> treeMap = new TreeMap<>(
                new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        treeMap.putAll(this.nodos);
        
        Map<String,Arista> treeAristas = ordenarAristas(this.aristas);
        
        for(Map.Entry<String,Nodo> entry : treeMap.entrySet()){
            expresion += entry.getKey() + "(" + entry.getValue().getValor() + ");\n";
        }
        
        for(Map.Entry<String,Arista> entry : treeAristas.entrySet()){
            expresion += entry.getKey() + "(" + entry.getValue().getValor() + ");\n";
        }
        
        expresion = expresion.substring(0, expresion.length() - 1) + "\n}";
        
        return expresion;
    }

    public static Map<String, Arista> ordenarAristas(Map<String, Arista> aristas) {
        Map<String, Arista> treeAristas = new TreeMap<>(
                new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        treeAristas.putAll(aristas);
        return treeAristas;
    }
    
    public static Map<String, Nodo> ordenarNodos(Map<String, Nodo> nodos) {
        Map<String, Nodo> treeNodos = new TreeMap<>(
                new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        treeNodos.putAll(nodos);
        return treeNodos;
    }
    
//    public static class GrafoBuilder{
//        
//        private String nombre;
//        private Map<String,Nodo> nodos;
//        private Map<String,Arista> aristas;
//        private ArrayList<String> nombreNodos;
//        
//        private Grafo grafo;
//        
//        public GrafoBuilder(){
//            this.grafo = new Grafo();
//            this.nodos = new HashMap<>();
//            this.aristas = new HashMap<>();
//            this.nombreNodos = new ArrayList<>();
//        }
//        
//        public GrafoBuilder agregar(String nombre, Nodo nuevo){
//            this.nodos.put(nombre, nuevo);
//            return this;
//        }
//        
//        public GrafoBuilder agregar(String nombre, Arista nueva){
//            this.aristas.put(nombre, nueva);
//            return this;
//        }
//        
//        public GrafoBuilder nombre(String nombre){
//            this.nombre = nombre;
//            return this;
//        }
//        
//        public Grafo crear(){
//            grafo.setNodos(nodos);
//            grafo.setAristas(aristas);
//            grafo.setNombre(nombre);
//            return this.grafo;
//        }
//    }
}
