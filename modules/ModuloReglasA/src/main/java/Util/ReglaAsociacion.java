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
public class ReglaAsociacion {
    
    public static String generarReglas(String entrada, double minSoporte, double minConfianza){
        String procesoSalida = "";
        ArrayList<ArrayList<String>> transac = Util.UtilAReglas.getTransac(entrada);
        double totalTransac = transac.size();
        procesoSalida += "Comienzo del algoritmo";
        
        ArrayList<String> productos = getProd(transac);     //Obtengo los productos del string de entrada
        productos = ordenarProd(productos);     //Ordeno los productos en orden alfabetico
        ArrayList<Integer> cantVeces = getCantVeces(transac, productos);    //Cuento la cantidad de veces que aparece cada producto
                                                                            //en las transacciones de entrada
        
        // Muestro las veces que aparece cada producto
        procesoSalida += "\nElementos | Cantidad";
        for (int i = 0; i < productos.size(); i++) {
            procesoSalida += "\n "+productos.get(i)+" ("+cantVeces.get(i)+")";
        }
        
        //Descarto los producto por debajo del minSoporte
        ArrayList<String> prodAux = new ArrayList<>();
        for (int i = 0; i < productos.size(); i++) {
            double porcentaje = cantVeces.get(i)/totalTransac;
            if (porcentaje < (minSoporte)) {
                procesoSalida += "\n"+productos.get(i)+" no cumple con el minimo de soporte ("+porcentaje+")";
            }else{
            prodAux.add(productos.get(i));
            }
        }
        productos = prodAux;
        
        //Inicializo el vector
        ArrayList<ArrayList<String>> inicial = new ArrayList<>();
        ArrayList<ArrayList<String>> entradai;
        for (int i = 0; i < productos.size(); i++) {
            ArrayList<String> aux = new ArrayList<>();
            aux.add("#");
            aux.add(productos.get(i));
            inicial.add(aux);
        }
        
        //
        procesoSalida += "\n\n---------------------------\n"
                            + "--- Algoritmo a priori ---\n"
                            + "---------------------------\n";
        entradai = inicial;
        ArrayList<ArrayList<ArrayList<String>>> proc = new ArrayList<>();
        int iteracion = 1;
        while (true) {            
            ArrayList<ArrayList<String>> conjuntoReglas = new ArrayList<>();  //conjunto de reglas por iteracion
            //Con estos dos for lo que hago es el producto cartesiano entre los elementos del conjunto
            for (int i = 0; i < entradai.size(); i++) {
                ArrayList<ArrayList<String>> auxRule = new ArrayList<>();
                for (int j = i; j < entradai.size(); j++) {
                    //Aca pregunto que no sea el mismo elemento, es decir i=j
                    if (i!=j) {
                        boolean bandera = true;
                        for (int k = 0; k < iteracion; k++) {
                            //Aca pregunto para sacar los elemento (j,i) si ya existe (i,j) 
                            if(!entradai.get(i).get(k).equals(entradai.get(j).get(k))){
                                bandera = false;
                            }
                        }
                        if (bandera) {
                            //Agrego a la lista de reglas auxiliares
                            ArrayList<String> aux = new ArrayList<>();
                            for (int k = 0; k < iteracion; k++) {
                                aux.add(entradai.get(i).get(k));
                            }
                            //Agrego las reglas, tanto A->B, como B->A
                            aux.add(entradai.get(i).get(iteracion));    //A->B
                            aux.add(entradai.get(j).get(iteracion));    //B->A
                            conjuntoReglas.add(aux);      //Agrego estas reglas al conjunto de reglas
                            auxRule.add(aux);
                        }
                    }
                }
                //Pregunto si la lista no esta bacia, y la muestro
                if (!auxRule.isEmpty()) {
                    procesoSalida += "\n"+auxRule.toString();
                }
            }
            procesoSalida += "\n";
            
            //Debo descartar los que no cumplen con el minimo de soporte
            //Para ello obtengo la cantidad de ocurrencia de cada regla
            cantVeces = getCantSubConjunto(transac, conjuntoReglas);
            
            //Descarto los producto por debajo del minSoporte
            ArrayList<ArrayList<String>> cAux = new ArrayList<>();
            for (int i = 0; i < conjuntoReglas.size(); i++) {
                double porcentaje = cantVeces.get(i)/totalTransac;
                procesoSalida += "\n "+conjuntoReglas.get(i)+" ("+cantVeces.get(i)+") --> soporte: "+porcentaje;
                if (porcentaje < (minSoporte)) {
                    procesoSalida += "  Descarto";
                }else{
                    cAux.add(conjuntoReglas.get(i));
                }
            }
            conjuntoReglas = cAux;
            proc.add(conjuntoReglas);
            
            //Si ya no quedan elemento o reglas para seguir iterando, salgo del WHILE
            if (conjuntoReglas.size() <= 1) {
                break;
            }
            entradai = conjuntoReglas;
            iteracion++;
            procesoSalida += "\n------------------------------------\n";
        }
        procesoSalida += "\n\n------------------------------------";
        procesoSalida += "\n"+"Aca finaliza el algoritmo a priori\n";
        
        
        //Comienza algoritmo a posteriori, donde debo ver aquellos que cumplen
        //con el minimo de confianza, el cual lo calculo confianza = cont(X U Y)/cont(X)
        ArrayList<ResultRegla> totalReglas = new ArrayList<>();
        for (int i = 0; i < proc.size(); i++) {
            for (int j = 0; j < proc.get(i).size(); j++) {
                proc.get(i).get(j).remove(0);
                ArrayList<ResultRegla> reglas = generarRegla(proc.get(i).get(j), transac);
                totalReglas.addAll(reglas);
            }
        }
        
        //Muestro resultados
        for (int i = 0; i < totalReglas.size(); i++) {
            ResultRegla regla = totalReglas.get(i);
            procesoSalida += "\n"+regla.getPremisa()+"->"+regla.getConsecuente()+" confianza: "+regla.getConfianza();
            if (regla.getConfianza() < minConfianza) {
                procesoSalida += "    Descarto";
            }
        }
        
        //Muestro las reglas que cumplen con el minimo soporte y confianza
        procesoSalida += "\n\nSalida final";
        for (int i = 0; i < totalReglas.size(); i++) {
            ResultRegla regla = totalReglas.get(i);
            if (regla.getConfianza() < minConfianza) {
                procesoSalida += "\n"+regla.getPremisa()+"->"+regla.getConsecuente();
            }
        }
        
        procesoSalida += "\n------------------------------------\n";
        return procesoSalida;
    }
    
    private static ArrayList<String> getProd(ArrayList<ArrayList<String>> entrada){
        ArrayList<String> salida = new ArrayList<>();
        for (int i = 0; i < entrada.size(); i++) {
            for (int j = 0; j < entrada.get(i).size(); j++) {
                if (!salida.contains(entrada.get(i).get(j))) {
                    salida.add(entrada.get(i).get(j));
                }
            }
        }
        return salida;
    }
    
    private static ArrayList<Integer> getCantVeces(ArrayList<ArrayList<String>> transac, ArrayList<String> prod){
        ArrayList<Integer> salida = new ArrayList<>();
        for (int i = 0; i < prod.size(); i++) {
            int cant = 0;
            for (int j = 0; j < transac.size(); j++) {
                if (transac.get(j).contains(prod.get(i))) {
                    cant++;
                }
            }
            salida.add(cant);
        }
        return salida;
    }
    
    private static ArrayList<Integer> getCantSubConjunto(ArrayList<ArrayList<String>> transac, ArrayList<ArrayList<String>> prod){
        ArrayList<Integer> salida = new ArrayList<>();
        for (int i = 0; i < prod.size(); i++) {
            int cant = 0;
            ArrayList<String> aux = new ArrayList<>(prod.get(i));
            aux.remove(0);
            for (int k = 0; k < transac.size(); k++) {
                if (transac.get(k).containsAll(aux)) {
                    cant++;
                }
            }
            salida.add(cant);
        }
        return salida;
    }
    
    private static ArrayList<String> ordenarProd(ArrayList<String> entrada){
        ArrayList<String> salida = entrada;
        
        while (true) {
            boolean bandera = true;
            for (int i = 1; i < entrada.size(); i++) {
                if ((entrada.get(i).compareTo(entrada.get(i-1))) < 0) {
                    String aux = entrada.get(i);
                    entrada.set(i,entrada.get(i-1));
                    entrada.set(i-1, aux);
                    bandera = false;
                }
            }
            if (bandera) {
                break;
            }
        }
        return salida;
    }
    
    private static ArrayList<ResultRegla> generarRegla(ArrayList<String> entrada, ArrayList<ArrayList<String>> transac){
        ArrayList<ResultRegla> salida = new ArrayList<>();
        for (int x = 0; x < entrada.size()-1; x++) {
            for (int i = 0; i < entrada.size(); i++) {
                ResultRegla reglai = new ResultRegla();

                //Obtenco la premisa y el cosecuente
                ArrayList<String> consecuente = new ArrayList<>();
                ArrayList<String> premisa = new ArrayList<>();
                for (int j = 0; j < entrada.size(); j++) {
                    int aux = i+x;
                    if (aux >= entrada.size()) {
                        aux = aux - entrada.size();
                        if (j < i && j > aux) {
                            premisa.add(entrada.get(j));
                        }else{
                            consecuente.add(entrada.get(j));
                        }
                    }else{
                        if (j >= i && j <= aux) {
                            consecuente.add(entrada.get(j));
                        }else{
                            premisa.add(entrada.get(j));
                        }
                    }
                }
                //Calculamos la confianza de la regla: X->Y
                double confianza = calcularConfianza(transac, premisa, consecuente);
                confianza = (Math.round(confianza*100))/Double.valueOf(100);
                reglai.setPremisa(premisa);
                reglai.setConsecuente(consecuente);
                reglai.setConfianza(confianza);
                salida.add(reglai);
            }
        }
        return salida;
    }
    
    private static double calcularConfianza(ArrayList<ArrayList<String>> transac, ArrayList<String> premisa, ArrayList<String> consecuente){
        double ocurrencePremisa = 0;
        double ocurrenceRegla = 0;
        for (int j = 0; j < transac.size(); j++) {
            if (transac.get(j).containsAll(premisa)) {
                // X count
                ocurrencePremisa++;
                if (transac.get(j).containsAll(consecuente)) {
                    // X U Y count
                    ocurrenceRegla++;
                }
            }
        }
        return ocurrenceRegla/ocurrencePremisa;
    }

}
