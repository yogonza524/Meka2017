package com.pichon.modulobusqueda;

import Util.Busqueda;
import Util.Grafo;
import Util.ResultBusqueda;
import Util.UtilBusqueda;
import Util.UtilDraw;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

public class FXMLController implements Initializable {
    
    private String leyenda =    "Ingresamos el grafo del problema respetando \n" +
                                "el siguiente formato. Primero se definen los \n" +
                                "nodos con su nombre y su valor: Nodo1(valor);\n" +
                                "luego se definen las aristas y su valor: \n" +
                                "Nodo1-Nodo2(valor). Por ejemplo\n" +
                                "GRAFO{\n" +
                                "A(45);\n" +
                                "B(35);\n" +
                                "…\n" +
                                "A-B(12);\n" +
                                "A-C(15);\n" +
                                "…\n" +
                                "}";
    private Grafo grafo;
    private String stringGrafo = "";
    @FXML private StackPane arbolCanvas;
    @FXML private TextArea resultado;
    @FXML private TextArea valoresEntrada;
    @FXML private TextField nodoInicial;
    @FXML private TextField nodoFinal;
    @FXML private Label errorClasificar;
    @FXML private StackPane grafoCanvas;
    @FXML private Label errorEntradas;
    @FXML private ChoiceBox<String> algoritmo;
    @FXML private Tab grafoPestana;
    @FXML private Tab arbolPestana;
    
    @FXML private Hyperlink pregunta;

    @FXML void generarGrafo(ActionEvent event) throws IOException {
        try {
            String entrada = valoresEntrada.getText();
            if(!stringGrafo.equals(entrada)){
                grafo = UtilBusqueda.cargarGrafo(entrada);
                resultado.setText("Grafo generado correctamente");
            }
            
            //dibujar grafo
            grafoCanvas.getChildren().clear();
            grafoCanvas.getChildren().add(dibujarGrafo());
            errorEntradas.setVisible(false);
        } catch (Exception e) {
            errorEntradas.setVisible(true);
        }
    }
    
    @FXML void iniciarBusqueda(ActionEvent event) {
//        try{
            ResultBusqueda result;
            String inicio = nodoInicial.getText();
            String fin = nodoFinal.getText();

            result = busqueda(algoritmo.getValue(), grafo, inicio, fin);
            resultado.setText(result.getProceso());
            arbolPestana.selectedProperty();

            //Graficar arbol de busqueda
            UtilDraw dibujador = new UtilDraw();
            arbolCanvas.getChildren().clear();
            arbolCanvas.getChildren().add(dibujador.dibujarArbol(result.getRoot(), result.isConHeuristica()));
            
//            errorClasificar.setVisible(false);
//        } catch (Exception e) {
//            errorClasificar.setVisible(true);
//        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        algoritmo.getItems().addAll("Primero en amplitud",
                "Primero en profundidad",
                "Escalada simple",
                "Escalada maxima",
                "A*");
        errorEntradas.setVisible(false);
        errorClasificar.setVisible(false);
        
        grafoCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                grafoCanvas.getChildren().clear();
                grafoCanvas.getChildren().add(dibujarGrafo());
            }
        });
    }
    
    private Group dibujarGrafo(){
        //dibujar grafo
        UtilDraw dibujador = new UtilDraw();
        return dibujador.dibujarGrafo(grafo);
    }
    
    private ResultBusqueda busqueda(String algoritmo, Grafo g, String inicio, String fin){
        ResultBusqueda salida = null;
        if (algoritmo.equals("Primero en amplitud")) {
            salida = Busqueda.busquedaAmplitud(g, inicio, fin);
            salida.setConHeuristica(false);
        }
        if (algoritmo.equals("Primero en profundidad")) {
            salida = Busqueda.busquedaProfundidad(g, inicio, fin);
            salida.setConHeuristica(false);
        }
        if (algoritmo.equals("Escalada simple")) {
            salida = Busqueda.busquedaEscaladaSimple(g, inicio, fin);
            salida.setConHeuristica(true);
        }
        if (algoritmo.equals("Escalada maxima")) {
            salida = Busqueda.busquedaEscaladaMaxima(g, inicio, fin);
            salida.setConHeuristica(true);
        }
        if (algoritmo.equals("A*")) {
            salida = Busqueda.busquedaAEstrella(g, inicio, fin);
            salida.setConHeuristica(true);
        }
        return salida;
    }
    
    @FXML void preguntaOnAction(ActionEvent event) {
        PopOver pop = new PopOver();
        VBox vbox = new VBox();
        Label textLabel = new Label(leyenda);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().add(textLabel);
        pop.setContentNode(vbox);
        pop.show(pregunta);
    }

}
