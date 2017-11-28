/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Util.Regla;
import Util.ObjetoValor;
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
public class JUnitTestSBReglas {
    
    public JUnitTestSBReglas() {
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
    public void hello() {
        String objetosString = "A, B, C , D, M, L, P, R, W, Y, Z;";
        
        String reglasString =   "-X>Y;\n" +
                                "-A>Y;\n" +
                                "-B^C>W;\n" +
                                "D>M;\n" +
                                "A>Z;\n" +
                                "M>Z;\n" +
                                "W^-D>P;\n" +
                                "-P^-C>R;\n" +
                                "-R^-P>L;\n" +
                                "-Y^-P>L;";
       
        String valoresEntradaString =   "A:flase;"
                                        + "C:false;"
                                        + "M:false;"
                                        + "W:flase;";
        
        //Cargo los arreglos
        ArrayList<String> stringReglas = Util.UtilSBReglas.getReglas(reglasString);
        ArrayList<String> objetos = Util.UtilSBReglas.getObjetos(objetosString);
        System.out.println("Objetos: " + objetos);
        System.out.println("Reglas\n" + reglasString);
        
        //Creo las reglas 
        ArrayList<Regla> reglas = new ArrayList<>();
        for (int i = 0; i < stringReglas.size(); i++) {
            Regla regla = new Regla(stringReglas.get(i));
            System.out.println("\nRegla: "+stringReglas.get(i));
            System.out.println("Premisa: "+regla.getPremisa());
            System.out.println("Signo(true=negado): "+regla.getSignoPremisa());
            System.out.println("Consecuente: "+regla.getConseguente());
            reglas.add(regla);
        }
        
        ObjetoValor valoresEntrada = Util.UtilSBReglas.getValoresEntrada(valoresEntradaString);
        System.out.println("\n- - - - - - - - -\nValores de entrada");
        for (int i = 0; i < valoresEntrada.getObjetos().size(); i++) {
            System.out.println(valoresEntrada.getObjetos().get(i)+": "+valoresEntrada.getValores().get(i));
        }
        
        //Comienzo algoritmo
        System.out.println("\nComienza algoritmo");
        ArrayList<String> objetoEntrada = new ArrayList<>();
        ArrayList<Boolean> valorEntrada = new ArrayList<>();
        boolean huboError = false, reglaAplicada;
        ObjetoValor salidaRegla;
        
        //Guardo las entradas
        for (int i = 0; i < valoresEntrada.getObjetos().size(); i++) {
            objetoEntrada.add(valoresEntrada.getObjetos().get(i));
            valorEntrada.add(valoresEntrada.getValores().get(i));
            String objetoi = valoresEntrada.getObjetos().get(i);
            boolean valori = valoresEntrada.getValores().get(i);
            System.out.println("Entrada: " + objetoi + ": " + valori);
            System.out.println(objetoEntrada);
            System.out.println(valorEntrada);

            //Repito hasta que NO se puedan aplicar mas reglas
            while(true){
                

                reglaAplicada = false;
                while(true){ //Si aplico una regla sigue

                    //Aplico las reglas
                    for (int j = 0; j < reglas.size(); j++) {
                        
                        //Pregunto si la regla ya no fue aplicada
                        if(!reglas.get(j).isReglaAplicada()){
                            
                            //Aplico la regla
                            salidaRegla = reglas.get(j).aplicarRegla(objetoi, valori);

                            //Controlo que no se haya producido un error
                            if (salidaRegla.isErrorProducido()) {
                                System.out.println(salidaRegla.getMensajeError());
                                huboError = true;
                                break;
                            }

                            //pregunto si se aplico la regla
                            if (salidaRegla.isReglaAplicada()) {
                                System.out.println("Regla: "+reglas.get(j).getReglaString()+" aplicada \nValores inferidos");
                                for (int k = 0; k < salidaRegla.getObjetos().size(); k++) {
                                    System.out.println(salidaRegla.getObjetos().get(k)+": "+salidaRegla.getValores().get(k));

                                    //Pregunto si los valores inferidos ya no estan cargados en memoria
                                    if (!objetoEntrada.contains(salidaRegla.getObjetos().get(k))) {
                                        System.out.println("El valor "+ salidaRegla.getObjetos().get(k) +" no ha sido inferido anteriormente");
                                        objetoEntrada.add(salidaRegla.getObjetos().get(k));
                                        valorEntrada.add(salidaRegla.getValores().get(k));
                                        reglaAplicada = true;
                                        System.out.println(objetoEntrada);
                                        System.out.println(valorEntrada);
                                    }else{
                                        System.out.println("El valor "+salidaRegla.getObjetos().get(k)+" ya fue inferido antes");
                                    }
                                }
                                if (reglaAplicada) {
                                    break;
                                }
                            }
                        }
                    }

                    if (!reglaAplicada) {
                        break;
                    }else{
                        reglaAplicada = false;
                    }
                }

                System.out.println("--------------------------"
                            + "\nObtengo la siguiente entrada"
                            + "\n----------------------------");
                if (huboError) {
                    break;
                }
                if (!reglaAplicada) {
                    System.out.println("Ya no hay mas reglas para aplicar\n");
                    System.out.println("Estado final de objetos: ");
                    for (int j = 0; j < objetoEntrada.size(); j++) {
                        System.out.println(objetoEntrada.get(j)+": "+valorEntrada.get(j));
                    }
                    break; //No hay mas reglas para aplicar
                }
            }
            
            if (huboError) {
                break;
            }
        }
    
    }
}
