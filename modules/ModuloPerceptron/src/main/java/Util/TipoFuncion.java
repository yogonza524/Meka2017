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
public class TipoFuncion {
    private static final double c = 1;
    
    public static double getTipoFuncion(String tipoFuncion, double net){
        double salida = 0;
        if (tipoFuncion.equals("Lineal")) {
            salida = lineal(net);
        }
        if (tipoFuncion.equals("Escalera")) {
            salida = Escalera(net);
        }
        if (tipoFuncion.equals("Sigmoidal")) {
            salida = sigmoid(net);
        }
        return salida;
    }
    
    private static double sigmoid(double net) {
        return (1/( 1 + Math.pow(Math.E,(-net))));
    }
    
    private static double lineal(double net) {
        double fNet;
        if (net < (-c)) {
            fNet = 0;
        } else {
            if (net > c) {
                fNet = 1;
            } else {
                fNet = (net/(c*2))+0.5;
            }
        }
        return fNet;
    }
    
    private static double Escalera(double net){
        double fNet;
        if (net >= 0) {
            fNet = 1;
        }else{
            fNet = 0;
        }
        return fNet;
    }
    
}
