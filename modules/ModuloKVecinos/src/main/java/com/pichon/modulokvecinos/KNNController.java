package com.pichon.modulokvecinos;

import Util.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

public class KNNController implements Initializable {
    
    private String leyenda1 =   "Cada punto representa un conjunto de parámetros, \n" +
                                "siendo el ultimo valor correspondiente a la clase.\n" +
                                "Estos valores se ingresan separado por “,” y terminan\n" +
                                "con “;”. Por ejemplo:\n" +
                                "6,8,1;		(clase 1)\n" +
                                "12,8,2;		(clase 2)\n" +
                                "…";
    private String leyenda2 =   "Ingresamos los puntos a ser clasificados, solamente\n" +
                                "los parámetros, sin la clase con el mismo formato.";
    private StackPane layout;
    private Axes axes;
    private Knn knn;
    private int dim;
    @FXML private TextArea entradasClasificar;
    @FXML private ChoiceBox<String> funcionDistancia;
    @FXML private ChoiceBox<Integer> cantVecinos;
    @FXML private StackPane canvas;
    @FXML private TextArea resultado;
    @FXML private ScrollPane scrollPane;
    @FXML private TextArea puntosEntrada;
    @FXML private ChoiceBox<Integer> dimension;
    @FXML private Label errorEntradas;
    @FXML private Label errorClasificar;
    @FXML private Hyperlink pregunta1;
    @FXML private Hyperlink pregunta2;

    @FXML void clasificarEntradas(ActionEvent event) {
        try{
            String entradaClasificar = entradasClasificar.getText();
            ArrayList<ArrayList<Double>> puntosClasificar = UtilKnn.cargarPuntos(entradaClasificar, dimension.getValue());

            //Agregamos los puntosClass
            ArrayList<Punto> puntosClass = new ArrayList<>();
            for (int i = 0; i < puntosClasificar.size(); i++) {
                Punto p = new Punto(puntosClasificar.get(i));
                puntosClass.add(p);
            }
            int cantPuntosVecinos = cantVecinos.getValue();
            AlmacenDatos almacenDatos = knn.getPuntosVecinos(puntosClass, cantPuntosVecinos);
            String salidaClasificacion = knn.clasificarPuntos(puntosClass, almacenDatos.getPuntosVecinos());
            resultado.setText(resultado.getText() + almacenDatos.getDatosProceso() + "\n" + salidaClasificacion);
            
            if (dim == 2) {
                //Graficar punto
                ArrayList<ArrayList<Plot>> puntosPlot= addPuntosClass(puntosClasificar);
                for (int i = 0; i < puntosPlot.size(); i++) {
                    for (int j = 0; j < puntosPlot.get(i).size(); j++) {
                        layout.getChildren().add(puntosPlot.get(i).get(j));                 
                    }
                }
                //Graficar lineas
                ArrayList<Plot> lineasCluster = generarLineasUnion(puntosClass, almacenDatos.getPuntosVecinos());
                for (int i = 0; i < lineasCluster.size(); i++) {
                    layout.getChildren().add(lineasCluster.get(i));
                }
            }
            errorClasificar.setVisible(false);
        
        } catch (Exception e) {
            errorClasificar.setVisible(true);
        }
        
    }

    @FXML void generarRed(ActionEvent event) {
        dim = dimension.getValue();
        resultado.setText("");
        
        try{
            String datos = puntosEntrada.getText();
            ArrayList<ArrayList<Double>> datosPuntos = UtilKnn.cargarPuntos(datos, (dim+1));

            //Creo el knn y agrego los puntosClass
            knn = new Knn();
            knn.addPuntos(datosPuntos);
            
            if (dim == 2) {
                //Grafico los clusters
                layout = generarGrafico(datosPuntos); //Genero grafico de puntosClass
                canvas.getChildren().clear();
                canvas.getChildren().add(layout);
                scrollPane.setHvalue(scrollPane.getHmax()/2);
                scrollPane.setVvalue(scrollPane.getVmax()/2);
            }
            errorEntradas.setVisible(false);
        } catch (Exception e) {
            errorEntradas.setVisible(true);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dimension.getItems().addAll(
            2, 3, 4, 5
        );
        dimension.setValue(2);
        cantVecinos.getItems().addAll(
            3, 4, 5, 6, 7
        );
        cantVecinos.setValue(3);
        errorEntradas.setVisible(false);
        errorClasificar.setVisible(false);
    }

    private StackPane generarGrafico(ArrayList<ArrayList<Double>> datosPuntos){
        //Generar ejes cartesianos
        axes = new Axes(
                800, 800,
                -20, 20, 2,
                -20, 20, 2
        );
        //generamos el layoutAux
        StackPane layoutAux = new StackPane();
        layoutAux.setPadding(new Insets(20));
        layoutAux.setStyle("-fx-background-color: rgb(0, 0, 0);");
        layoutAux.setPrefSize(canvas.getWidth() -1, canvas.getHeight() -1);
        canvas.getChildren().add(layoutAux);
        //Agregamos los puntosClass
        ArrayList<Plot> puntosGraficar = UtilKnn.addPuntosGrafic(datosPuntos, axes);
        for (int i = 0; i < puntosGraficar.size(); i++) {
            layoutAux.getChildren().add(puntosGraficar.get(i));
        }
        return layoutAux;
    }
    
    private ArrayList<ArrayList<Plot>> addPuntosClass(ArrayList<ArrayList<Double>> puntosClass){
        ArrayList<ArrayList<Plot>> salida = new ArrayList<>();
        for (int i = 0; i < puntosClass.size(); i++) {
            ArrayList<Plot> puntosGraficar = UtilKnn.addPuntosClassGrafic(puntosClass, axes);
            salida.add(puntosGraficar);
        }
        return salida;
    }
    
    private ArrayList<Plot> generarLineasUnion(ArrayList<Punto> puntos, ArrayList<ArrayList<Punto>> nVecinos){
        ArrayList<Plot> salida = new ArrayList<>();
        for (int i = 0; i < puntos.size(); i++) {
            for (int j = 0; j < nVecinos.get(0).size(); j++) {
                salida.add(UtilKnn.graficarLineaACluster(puntos.get(i), nVecinos.get(i).get(j), axes));
            }
        }
        return salida;
    }
    
    @FXML void pregunta1OnAction(ActionEvent event) {
        PopOver pop = new PopOver();
        VBox vbox = new VBox();
        Label textLabel = new Label(leyenda1);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().add(textLabel);
        pop.setContentNode(vbox);
        pop.show(pregunta1);
    }
    
    @FXML void pregunta2OnAction(ActionEvent event) {
        PopOver pop = new PopOver();
        VBox vbox = new VBox();
        Label textLabel = new Label(leyenda2);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().add(textLabel);
        pop.setContentNode(vbox);
        pop.show(pregunta2);
    }
}
