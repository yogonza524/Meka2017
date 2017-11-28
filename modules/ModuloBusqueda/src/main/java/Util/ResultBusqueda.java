/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author pichon
 */
public class ResultBusqueda {
    
    private String proceso;
    private NodoArbol root;
    private boolean conHeuristica;

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public NodoArbol getRoot() {
        return root;
    }

    public void setRoot(NodoArbol root) {
        this.root = root;
    }

    public boolean isConHeuristica() {
        return conHeuristica;
    }

    public void setConHeuristica(boolean conHeuristica) {
        this.conHeuristica = conHeuristica;
    }
    
    
}
