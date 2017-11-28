/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Util.ResultRegla;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author pichon
 */
public class NewEmptyJUnitTest {
    
    public NewEmptyJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    
    @Test
    @Ignore
    public void test1(){
        double minSoporte = 0.3;
        double minConfianza = 0.7;
        
        String entrada = 
                        "B,I,M;\n" +
                        "B,C;\n" +
                        "C,O;\n" +
                        "B,I,C,O;\n" +
                        "B,I,L,C,M;\n" +
                        "I,L,M;\n" +
                        "I,M,L;\n" +
                        "C,M,I,L,O;\n" +
                        "M,L,B;\n" +
                        "B,O,M;";
        
        ArrayList<ArrayList<String>> transac = Util.UtilAReglas.getTransac(entrada);
        System.out.println(transac);
        double totalTransac = transac.size();
        
        //Algoritmo a priori
        ArrayList<String> productos = getProd(transac);
        productos = ordenarProd(productos);
        System.out.println("\n"+productos);
        
        ArrayList<Integer> cantVeces = getCantVeces(transac, productos);
        System.out.println(cantVeces);
        
        //Descarto los producto por debajo del minSoporte
        ArrayList<String> prodAux = new ArrayList<>();
        for (int i = 0; i < productos.size(); i++) {
            double porcentaje = cantVeces.get(i)/totalTransac;
            if (porcentaje < (minSoporte)) {
                System.out.println("Descarto "+productos.get(i)+" por no cumplir con el minimo de soporte ("+porcentaje+")");
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
        entradai = inicial;
        ArrayList<ArrayList<ArrayList<String>>> proc = new ArrayList<>();
        int iteracion = 1;
        while (true) {            
            ArrayList<ArrayList<String>> c = new ArrayList<>();
            for (int i = 0; i < entradai.size(); i++) {
                for (int j = i; j < entradai.size(); j++) {
                    if (i!=j) {
                        boolean bandera = true;
                        for (int k = 0; k < iteracion; k++) {
                            if(entradai.get(i).get(k) != entradai.get(j).get(k)){
                                bandera = false;
                            }
                        }
                        if (bandera) {
                            ArrayList<String> aux = new ArrayList<>();
                            for (int k = 0; k < iteracion; k++) {
                                aux.add(entradai.get(i).get(k));
                            }
                            aux.add(entradai.get(i).get(iteracion));
                            aux.add(entradai.get(j).get(iteracion));
                            c.add(aux);
                        }
                    }
                }
            }
            System.out.println("\n"+c);
            
            //Debo descartar los que no cumplen con el minimo de soporte
            cantVeces = getCantSubConjunto(transac, c);
            System.out.println(cantVeces);
            
            //Descarto los producto por debajo del minSoporte
            ArrayList<ArrayList<String>> cAux = new ArrayList<>();
            for (int i = 0; i < c.size(); i++) {
                double porcentaje = cantVeces.get(i)/totalTransac;
                if (porcentaje < (minSoporte)) {
                    System.out.println("Descarto "+c.get(i)+" por no cumplir con el minimo de soporte ("+porcentaje+")");
                }else{
                    cAux.add(c.get(i));
                }
            }
            c = cAux;
            System.out.println("Sub conjunto: "+c);
            proc.add(c);
            
            if (c.size() <= 1) {
                break;
            }
            entradai = c;
            iteracion++;
        }
        System.out.println("----------------------\nAca finaliza el algoritmo a priori\n");
        
        
        //Comienza algoritmo a posteriori
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
            System.out.println(regla.getPremisa()+"->"+regla.getConsecuente()+" confienza: "+regla.getConfianza());
        }
      
    }
    
    public ArrayList<String> getProd(ArrayList<ArrayList<String>> entrada){
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
    
    public ArrayList<Integer> getCantVeces(ArrayList<ArrayList<String>> transac, ArrayList<String> prod){
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
    
    public ArrayList<Integer> getCantSubConjunto(ArrayList<ArrayList<String>> transac, ArrayList<ArrayList<String>> prod){
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
    
    public ArrayList<String> ordenarProd(ArrayList<String> entrada){
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
    
    public ArrayList<ResultRegla> generarRegla(ArrayList<String> entrada, ArrayList<ArrayList<String>> transac){
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
                reglai.setPremisa(premisa);
                reglai.setConsecuente(consecuente);
                reglai.setConfianza(confianza);
                salida.add(reglai);
            }
        }
        return salida;
    }
    
    public double calcularConfianza(ArrayList<ArrayList<String>> transac, ArrayList<String> premisa, ArrayList<String> consecuente){
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
