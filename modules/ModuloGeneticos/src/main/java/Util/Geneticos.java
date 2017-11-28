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
public class Geneticos {
    
    public static void seleccionRuleta(int generaciones, int poblacion, int cantVariables, double[][] individuos, double[] aptitud){
        double[] aptitudGeneracional = new double[generaciones];
        for (int w = 0; w < generaciones; w++) {
            double total = 0;
            for (int i = 0; i < poblacion; i++) {
                total += aptitud[i];
            }
            double[] prob = new double[poblacion];
            double[] cantidad = new double[poblacion];
            for (int i = 0; i < poblacion; i++) {
                prob[i] = aptitud[i]/total;
                cantidad[i] = Math.round(prob[i]*100);
            }
            //Muestro tabla
            int acumulado = (int) cantidad[0];
            System.out.println(" | Individuo | Aptitud  | C       | Cantidad | Intervalo | ");
            System.out.println(" | 0      | "+aptitud[0]+"    | "+prob[0]+"       | "+cantidad[0]+"      | 0-"+(cantidad[0]-1)+" | ");
            for (int i = 1; i < poblacion; i++) {
                System.out.println(" | "+i+"      | "+aptitud[i]+"      | "+prob[i]+"        | "+cantidad[i]+"        | "+acumulado+"-"+(cantidad[i]-1)+"    | ");
                acumulado += cantidad[i];
            }
            aptitudGeneracional[w] = total/poblacion;
            
            //Obtengo los que se reproducen
            
            //Obtengo nuevos individuos
            double[][] nuevosIndividuos = new double[poblacion][cantVariables];
            for (int i = 0; i < poblacion; i++) {
                
            }
        }
    }
    
    public static void seleccionCopiasSeparadas(int generaciones, int poblacion, double[][] individuos, double[] aptitud){
        double[] aptitudGeneracional = new double[generaciones];
        for (int w = 0; w < generaciones; w++) {
            double promedio, total = 0;
            for (int i = 0; i < poblacion; i++) {
                total += aptitud[i];
            }
            promedio = total/poblacion;
            
            double[] aux = new double[poblacion];
            double[] copiasE = new double[poblacion];
            double[] copiasA = new double[poblacion];
            
            for (int i = 0; i < poblacion; i++) {
                aux[i] = aptitud[i]/promedio;
                if (aux[i]>0) {
                    copiasE[i] = Math.round(aux[i]);
                }else{
                    copiasE[i] = 0;
                    if (aux[i]>0.5) {
                        copiasA[i] = 1;
                    }else{
                        copiasA[i] = 0;
                    }
                }
            }
            //Muestro tabla
            System.out.println(" | Individuo | Aptitud     | Promedio      | C        | Copias Esperadas | Copias Asignadas | ");
            for (int i = 1; i < poblacion; i++) {
                System.out.println(" | "+i+"      | "+aptitud[i]+"    | "+promedio+"       | "+aux[i]+"      | "+copiasE[i]+"  | "+copiasA[i]+"  |");
            }
            aptitudGeneracional[w] = total/poblacion;
            
            //Obtengo nuevos individuos
        }
    }
    
    public static void seleccionRanking(int generaciones, int poblacion, double[][] individuos, double[] aptitud){
        double[] aptitudGeneracional = new double[generaciones];
        for (int w = 0; w < generaciones; w++) {
            //Ordeno los individuos por su aptitud
            while (true){
                boolean bandera = true;
                for (int i = 0; i < poblacion-1; i++) {
                    if (aptitud[i] < aptitud[i+1]) {
                        double aux = aptitud[i];
                        aptitud[i] = aptitud[i+1];
                        aptitud[i+1] = aux;
                        bandera = false;
                    }
                }
                if (bandera) {
                    break;
                }
            }
            
            //Obtengo el total
            double total = 0;
            for (int i = 0; i < poblacion; i++) {
                total += aptitud[i];
            }
            
            //Calculo el C: Rmin + 2 ((n-i)*(1-Rmin)/(n-1))
            double rMin = 0;
            double[] copiasE = new double[poblacion];
            int[] copiasA = new int[poblacion];
            
            copiasE[0] = rMin + (2 * (1-rMin));
            if (copiasE[0] > 1.9999) {
                copiasA[0] = 2;
            }else{
                copiasA[0] = 1;
            }
            for (int i = 1; i < 10; i++) {
                copiasE[i] = rMin + (2 * ((poblacion-(i+1))*(1-rMin))/(poblacion-1));
                if (copiasE[i] > 1) {
                    copiasA[i] = 1;
                }
            }
            
            //Muestro tabla
            System.out.println(" | Individuo | Aptitud  | Copias esperadas | Copias asignadas | ");
            for (int i = 0; i < poblacion; i++) {
                System.out.println(" | "+i+"      | "+aptitud[i]+"      | "+copiasE[i]+"        | "+copiasA[i]+"        | ");
            }
            aptitudGeneracional[w] = total/poblacion;
            
            //Obtengo nuevos individuos
        }
    }
}
