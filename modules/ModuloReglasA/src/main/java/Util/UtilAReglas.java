/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;

/**
 *
 * @author pichon
 */
public class UtilAReglas {
    
    public static ArrayList<ArrayList<String>> getTransac(String reglas) {
        ArrayList<ArrayList<String>> salida = new ArrayList<>();
        String transacI;
        reglas = reglas.replaceAll(" ", "");
        reglas = reglas.replaceAll("\n", "");
        reglas += "#";
        while(!reglas.equals("#")){
            transacI = reglas.substring(0, reglas.indexOf(';'));
            transacI = transacI.replaceAll(" ", "");
            transacI += ",#";
            String prod;
            ArrayList<String> unaTransac = new ArrayList<>();
            while(!transacI.equals("#")) {
                prod = transacI.substring(0, transacI.indexOf(','));
                unaTransac.add(prod);
                transacI = transacI.substring(transacI.indexOf(',')+1);
            }
            salida.add(unaTransac);
            reglas = reglas.substring(reglas.indexOf(';')+1);
        }
        return salida;
    }
}
