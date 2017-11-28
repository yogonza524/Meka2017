/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author pichon
 */
public class SVM {
    
    public String generarStringMaximizar( ArrayList<ArrayList<Double>> puntos) throws IOException {
        String letras = "zyxwvutsr";    //Variables
        String stringCalculos = "";
        int cantPuntos, dimensiones;
        double cal;
        String auxString;
        
        cantPuntos = puntos.size();
        dimensiones = puntos.get(0).size()-1;
        
        //Aca basicamente se hace el cualculo y se arma el string a maxizar
        for (int i = 0; i < cantPuntos; i++) {
            for (int j = 0; j < cantPuntos; j++) {
                auxString = "";
                cal = 0;
                for (int k = 0; k < dimensiones; k++) {
                    cal = cal + puntos.get(i).get(k) * puntos.get(j).get(k);
                }
                if (cal != 0) {
                    cal = cal * puntos.get(i).get(dimensiones)*puntos.get(j).get(dimensiones); //obtiene el signo
                    auxString = Double.toString(cal);
                    if(i==j){
                        auxString = String.valueOf(letras.charAt((cantPuntos-1)-i)) +"^2"+"*"+ auxString;
                    }else{
                        auxString = String.valueOf(letras.charAt((cantPuntos-1)-i)) +"*"+ String.valueOf(letras.charAt((cantPuntos-1)-j)) +"*"+ auxString;
                    }
                    stringCalculos = stringCalculos + "+" + auxString;
                }
            }
        }
        
        //Agrego un par de boludeces mas, esto es para darle el formato del maximizador wolfram
        stringCalculos = stringCalculos.substring(0, stringCalculos.length()-1);
        stringCalculos = "-1/2*("+stringCalculos+")";
        for (int i = 0; i < cantPuntos; i++) {
            stringCalculos = stringCalculos + "+" + String.valueOf(letras.charAt(i));
        }
        stringCalculos = "maximize {" + stringCalculos + "} on ";
        
        //Agrego las condiciones de optimalidad (que la sumatoria de cero y que sean mayor o igual a cero)
        for (int i = 0; i < cantPuntos; i++) {
            if (puntos.get(i).get(dimensiones) < 0) {
                stringCalculos = stringCalculos + "-" + String.valueOf(letras.charAt(i));
            }else{
                stringCalculos = stringCalculos + "+" + String.valueOf(letras.charAt(i));
            }
        }
        stringCalculos = stringCalculos + " = 0 and ";
        
        for (int i = 0; i < cantPuntos; i++) {
            stringCalculos = stringCalculos + String.valueOf(letras.charAt(i)) + ">=0";
            if (i != cantPuntos-1) {
                stringCalculos = stringCalculos + " and ";
            }
        }
        return stringCalculos;
    }
    
    //Aca una vez que obtengo los alfas del problema, paso a obtener los valores de 
    //los pesos "ws" y de "b" para trazar la recta que mejor clasefique
    public SalidaAprendizaje aprendizaje(double[] alfai, ArrayList<ArrayList<Double>> puntos){
        
        String salidaString = "";
        double[] yi = new double[puntos.size()]; 
        double[][] xi = new double[puntos.size()][2];
        double[] w;
        double b = 0;
        double aux;
        
        for (int i = 0; i < puntos.size(); i++) {
            xi[i][0] = puntos.get(i).get(0);
            xi[i][1] = puntos.get(i).get(1);
            yi[i] = puntos.get(i).get(2);
        }
        SalidaAprendizaje salida = new SalidaAprendizaje(xi[0].length);
        
        salidaString = salidaString + "Aprendizaje: \n";
        salidaString = salidaString + "Alfas: " + Arrays.toString(alfai) + "\n";
        salidaString = salidaString + "Xs: ";
        for (int i = 0; i < xi.length; i++) {
            salidaString = salidaString + Arrays.toString(xi[i]) + " ";
        }
        salidaString = salidaString + "\n";
        salidaString = salidaString + "Ys: " + Arrays.toString(yi) + "\n";
        salidaString = salidaString + "Calcular ws: \n";
        w = new double[xi[0].length];
        
        // aca calculo cada uno de los ws, con la formula w=Sum(yi+alfai+xi)
        for (int i = 0; i < xi[0].length; i++) {
            w[i] = 0;
            salidaString = salidaString + "w"+i+" =";
            for (int j = 0; j < xi.length; j++) {
                salidaString = salidaString + " + " + yi[j]+" * "+alfai[j]+" * "+xi[j][i];
                w[i] = w[i] + (yi[j]*alfai[j]*xi[j][i]);
            }
            salidaString = salidaString + " = " + w[i] + "\n";
        }
        
        salidaString = salidaString + "W = "+Arrays.toString(w) + "\n";
        
        // aca calculo el valor de b despejando de la siguiente formula: yi*(Wt*xi+b)-1=0 --> b=1/yi-Wt*xi
        for (int i = 0; i < xi.length; i++) {
            if (alfai[i] != 0) {
                aux = 0;
                for (int j = 0; j < xi[0].length; j++) {
                    aux = aux + (w[j]*xi[i][j]);
                }
                salidaString = salidaString + "b"+(i+1)+" = " + (yi[i]-(aux)) + "\n";
                b = b + (yi[i]-(aux));
            } 
        }
        b = b/3;
        salidaString = salidaString + "b = " + b + "\n";
        salida.setW(w);
        salida.setB(b);
        salida.setProceso(salidaString);
        return salida;
    }
    
    //Este metoro basicamente clasifica los puntos que le tires
    public String clasificarPuntos(ArrayList<ArrayList<Double>> puntos, double[] w, double b){
        String salida = "";
        for (int i = 0; i < puntos.size(); i++) {
            salida = salida + "Punto: "+ (puntos.get(i)) +" es de clase: "+ clasificar(puntos.get(i), w, b) + "\n";
        }
        return salida;
    }
    
    public ArrayList<Integer> clasificarPuntosInt(ArrayList<ArrayList<Double>> puntos, double[] w, double b){
        ArrayList<Integer> salida = new ArrayList<>();
        for (int i = 0; i < puntos.size(); i++) {
            salida.add(clasificar(puntos.get(i), w, b));
        }
        return salida;
    }
    
    private int clasificar(ArrayList<Double> punto, double[] w, double b){
        // NET=(Wt+x)+b.
        double aux = 0;
        for (int i = 0; i < w.length; i++) {
            aux = aux + w[i]*punto.get(i);
        }
        aux = aux + b;
        if (aux > 0) {
            return 1;
        } else {
            return -1;
        }
    }
    
}
