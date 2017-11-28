/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Util.Geneticos;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void test1(){
        Random r = new Random();
        int poblacion = 10;
        int cantVariables = 2;
        int generaciones = 1;
        double cotaInf = 0;
        double cotaSup = 1000;
        String funcion = "x^2+1/2*y=z";
        double[] apltitud = new double[poblacion];
        
        //Genero individuos aleatorios
        double[][] individuos = new double[poblacion][cantVariables];
        for (int i = 0; i < poblacion; i++) {
            double[] variables = new double[cantVariables];
            for (int j = 0; j < cantVariables; j++) {
                variables[j] = (r.nextDouble()*(cotaSup))+cotaInf;
            }
            individuos[i] = variables;
        }
        
        //Calculo las aptitudes(funcion) de ca da uno
        for (int i = 0; i < poblacion; i++) {
            double fun = individuos[i][0]*individuos[i][0]+(1/2)*individuos[i][1];
            apltitud[i] = fun;
        }
        
        //Comienzo algoritmo geneticos
        Geneticos.seleccionRanking(generaciones, poblacion, individuos, apltitud);
    }
}
