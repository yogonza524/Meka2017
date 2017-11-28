/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author pichon
 */

import java.util.ArrayList;
import java.util.List;

public class Punto {
    private Float[] data;
    private int clase;

    public Punto(ArrayList<Double> datos) {
	super();
	List<Float> puntos = new ArrayList<Float>();
	for (int i = 0; i < datos.size(); i++) {
            float x = datos.get(i).floatValue();
	    puntos.add(x);
	}
        this.clase = datos.get(datos.size()-1).intValue();
	this.data = puntos.toArray(new Float[datos.size()]);
    }

    public Punto(Float[] data) {
	this.data = data;
    }

    public float get(int dimension) {
	return data[dimension];
    }

    public int getGrado() {
	return data.length;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(data[0]);
	for (int i = 1; i < data.length; i++) {
	    sb.append(", ");
	    sb.append(data[i]);
	}
	return sb.toString();
    }
    
    public double distanciaEuclideana(Punto destino) {
        double d = 0.0f;
	for (int i = 0; i < data.length; i++) { //data es de mi punto
	    d += Math.pow(data[i] - destino.get(i), 2);
	}
	return Math.sqrt(d);
    }

    @Override
    public boolean equals(Object obj) {
	Punto other = (Punto) obj;
	for (int i = 0; i < data.length; i++) {
	    if (data[i] != other.get(i)) {
		return false;
	    }
	}
	return true;
    }

    public Float[] getData() {
        return data;
    }

    public int getClase() {
        return clase;
    }
    
}
