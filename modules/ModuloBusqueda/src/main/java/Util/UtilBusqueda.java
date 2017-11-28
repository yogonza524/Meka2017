/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author pichon
 */

public class UtilBusqueda {
    
    public static Grafo cargarGrafo(String stringEntrada) throws FileNotFoundException, IOException {
        Grafo g = new Grafo();
        String sCadena, nombre, valor, nombreA, valorArista;
        String entrada = stringEntrada.substring(stringEntrada.indexOf("\n")+1)+"\n";
        
        while (!(sCadena = entrada.substring(0,entrada.indexOf("\n"))).contains("}")) {
            
            if (!sCadena.contains("-")) { //en esta parte leo los nodos
                nombre = sCadena.substring(0,sCadena.indexOf('('));
                valor = sCadena.substring(sCadena.indexOf('(')+1,sCadena.indexOf(')'));
   
                //Aca debo crear un nuevo nodo y setearle los valores.
                Nodo n = new Nodo(nombre, Integer.parseInt(valor));
                g.agregarNodo(n);
            }
            if (sCadena.contains("-")) {
                nombreA = sCadena.substring(0,sCadena.indexOf('('));
                valorArista = sCadena.substring(sCadena.indexOf('(')+1,sCadena.indexOf(')'));

                //Aca creo la conexion entro los dos nodos con los valores obtenidos
                Arista a = new Arista(nombreA, Integer.parseInt(valorArista));
                g.agregarArista(a);
            }
            entrada = entrada.substring(entrada.indexOf("\n")+1);
        }
        return g;
    }
}
