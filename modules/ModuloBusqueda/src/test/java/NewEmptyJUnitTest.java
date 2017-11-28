/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Util.Busqueda;
import Util.Grafo;
import Util.ResultBusqueda;
import Util.UtilBusqueda;
import java.io.IOException;
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

    @Test
    @Ignore
    public void Test1() throws IOException{
        String inicio = "A";
        String fin = "K";
        String entrada =    "GRAFO graph1{\n" +
                            "A(22);\n" +
                            "B(14);\n" +
                            "C(21);\n" +
                            "D(16);\n" +
                            "E(18);\n" +
                            "F(7);\n" +
                            "G(8);\n" +
                            "H(16);\n" +
                            "I(16);\n" +
                            "J(14);\n" +
                            "K(0);\n" +
                            "A-B(12);\n" +
                            "A-C(15);\n" +
                            "A-E(43);\n" +
                            "A-D(23);\n" +
                            "B-I(17);\n" +
                            "B-F(9);\n" +
                            "B-C(10);\n" +
                            "C-G(2);\n" +
                            "E-G(32);\n" +
                            "E-H(12);\n" +
                            "D-H(6);\n" +
                            "I-J(17);\n" +
                            "J-K(34);\n" +
                            "F-K(26);\n" +
                            "}";

            Grafo g = UtilBusqueda.cargarGrafo(entrada);
            ResultBusqueda resultado;
            
            resultado = Busqueda.busquedaAmplitud(g, inicio, fin);
            System.out.println(resultado.getProceso());
            
            resultado = Busqueda.busquedaProfundidad(g, inicio, fin);
            System.out.println(resultado.getProceso());
            
            resultado = Busqueda.busquedaEscaladaSimple(g, inicio, fin);
            System.out.println(resultado.getProceso());
            
            resultado = Busqueda.busquedaEscaladaMaxima(g, inicio, fin);
            System.out.println(resultado.getProceso());
            
//            resultado = Busqueda.aEstrella(g, inicio, fin);
//            System.out.println(resultado.getProceso());
            
    }
}
