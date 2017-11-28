/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pichon
 */
public class NodoArbol<T> {
    
    private List<NodoArbol<T>> children = new ArrayList<NodoArbol<T>>();
    private NodoArbol<T> parent = null;
    private T data = null;
    private int value;

    public NodoArbol(T data) {
        this.data = data;
    }

    public NodoArbol(T data, NodoArbol<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    public List<NodoArbol<T>> getChildren() {
        return children;
    }

    public void setParent(NodoArbol<T> parent) {
        this.parent = parent;
    }

    public NodoArbol getParent(){
        return this.parent;
    }

    public void addChild(NodoArbol<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if(this.children.size() == 0) 
            return true;
        else 
            return false;
    }

    public void removeParent() {
        this.parent = null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    
}
