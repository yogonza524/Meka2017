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
public class UtilSBReglas {
    
    public static ArrayList<String> getObjetos(String objetos) {
        ArrayList<String> salida = new ArrayList<>();
        String aux;
        objetos = objetos.replaceAll(" ", "");
        objetos = objetos.replaceAll(";", ",");
        objetos += "#";
        while(!objetos.equals("#")) {
            aux = objetos.substring(0, objetos.indexOf(','));
            salida.add(aux);
            objetos = objetos.substring(objetos.indexOf(',')+1);
        }
        return salida;
    }
    
    public static ArrayList<String> getReglas(String reglas) {
        ArrayList<String> salida = new ArrayList<>();
        String aux;
        reglas = reglas.replaceAll(" ", "");
        reglas = reglas.replaceAll("\n", "");
        reglas += "#";
        while(!reglas.equals("#")){
            aux = reglas.substring(0, reglas.indexOf(';'));
            salida.add(aux);
            reglas = reglas.substring(reglas.indexOf(';')+1);
        }
        return salida;
    }

    public static ObjetoValor getValoresEntrada(String valoresEntrada) {
        ObjetoValor salida = new ObjetoValor();
        ArrayList<String> objetos = new ArrayList<>();
        ArrayList<Boolean> valor = new ArrayList<>();
        String linea, obj, val;
        valoresEntrada = valoresEntrada.replaceAll(" ", "");
        valoresEntrada = valoresEntrada.replaceAll("\n", "");
        valoresEntrada += "#";
        while(!valoresEntrada.equals("#")) {
            linea = valoresEntrada.substring(0, valoresEntrada.indexOf(';'));
            obj = linea.substring(0, linea.indexOf(":"));
            objetos.add(obj);
            val = linea.substring(linea.indexOf(":")+1);
            if ((val.contains("f")) || (val.contains("F"))) {
                valor.add(false);
            }else{
                valor.add(true);
            }
            valoresEntrada = valoresEntrada.substring(valoresEntrada.indexOf(';')+1);
        }
        salida.setObjetos(objetos);
        salida.setValores(valor);
        return salida;
    }

}
