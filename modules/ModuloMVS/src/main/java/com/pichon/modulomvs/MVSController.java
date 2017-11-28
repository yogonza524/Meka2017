package com.pichon.modulomvs;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Util.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.PopOver;


/**
 * FXML Controller class
 *
 * @author pichon
 */
public class MVSController implements Initializable {

    private String leyenda1 =   "Ingresos los puntos con su correspondiente clase\n" +
                                "con el formato “x,y,clase;”. Donde la clase puede \n" +
                                "ser [1,-1]. Por ejemplo:\n" +
                                "1,2,1;\n" +
                                "-1,0,-1;\n" +
                                "… ";
    private String leyenda2 =   "Los valores de los alfas deben ingresarse  en el\n" +
                                "orden correcto de las variables. Estos van \n" +
                                "separados por “;”, terminando también con \n" +
                                "punto y coma. Estos valores pueden ingresarse\n" +
                                "tanto en decimal como en fracción. Por ejemplo:\n" +
                                "4/9; 0; 2/9; 2/9; o\n" +
                                "0.44; 0; 0.22; 0.22;";
    private String leyenda3 =   "Ingreso los puntos a clasificar con el formato\n" +
                                "“x,y;” Por ejemplo:\n" +
                                "1,2;\n" +
                                "-2,-2;\n" +
                                "…";
    private SVM svm = new SVM();
    private double[] w;
    private double b;
    private SalidaAprendizaje procesoAprendizaje;
    private ArrayList<ArrayList<Double>> puntos;
    private StackPane layout;
    private Axes axes;
    @FXML private TextArea resultado;
    @FXML private TextField textStringMax;
    @FXML private TextArea ingresoEntradas;
    @FXML private TextField textAlfas;
    @FXML private TextArea textClasificar;
    @FXML private StackPane canvas;
    @FXML private Label errorFormatoAlfas;
    @FXML private Label errorFormatoPuntosEntrada;
    @FXML private Label errorFormatoClasificar;
    
    @FXML private Hyperlink pregunta1;
    @FXML private Hyperlink pregunta2;
    @FXML private Hyperlink pregunta3;
    
    @FXML
    public void generarStringMax(ActionEvent event) throws IOException{
        //Verifico si el formato de entrada es el correto
        try {
            String entrada = ingresoEntradas.getText();
            entrada = entrada.replace(" ", "");
            errorFormatoPuntosEntrada.setVisible(false);
            puntos = UtilSVM.cargarPuntosTexto(entrada);
            if (puntos.size()<5) {
                textStringMax.setText(svm.generarStringMaximizar(puntos));
            }else{
                textStringMax.setText("String muy largo");
                mostrarResultado(resultado,"String de maximizacion: \n" + svm.generarStringMaximizar(puntos));
            }
            //Generar ejer cartesianos
            axes = new Axes(
                    800, 800,
                    -8, 8, 1,
                    -8, 8, 1
            );

            //generamos el layout y llenamos con los puntos
            layout = new StackPane();
            ArrayList<Plot> puntosGraficar = UtilSVM.addPuntos(puntos, axes);
            for (int i = 0; i < puntosGraficar.size(); i++) {
                layout.getChildren().add(puntosGraficar.get(i));
            }
            layout.setPadding(new Insets(20));
            layout.setStyle("-fx-background-color: rgb(35, 39, 50);");
            canvas.getChildren().add(layout);
            
            errorFormatoPuntosEntrada.setVisible(false);
        } catch (Exception e) {
            errorFormatoPuntosEntrada.setVisible(true);
        }
    }
    
    @FXML
    public void comenzarAprendizaje(ActionEvent event){
        //Verificar que la entrada esta bien escrita
        try {
            String entrada = textAlfas.getText();
            entrada = entrada.replace(" ", "");
            errorFormatoAlfas.setVisible(false);
            double[] alfai = UtilSVM.getAlfas(entrada, puntos.size());
            procesoAprendizaje = svm.aprendizaje(alfai, puntos);
            w = procesoAprendizaje.getW();
            b = procesoAprendizaje.getB();
            mostrarResultado(resultado,procesoAprendizaje.getProceso());
            
            //Graficar las tres lineas.
            layout.getChildren().add(UtilSVM.graficarLinea(w, b, axes, 0, Color.AQUA));
            layout.getChildren().add(UtilSVM.graficarLinea(w, b, axes, 1, Color.YELLOW));
            layout.getChildren().add(UtilSVM.graficarLinea(w, b, axes, -1, Color.YELLOW));
        
            errorFormatoAlfas.setVisible(false);
        } catch (Exception e) {
            errorFormatoAlfas.setVisible(true);
        }
    }
    
    @FXML public void clasificarPuntos(ActionEvent event){
        try {
            String entrada = textClasificar.getText();
            entrada = entrada.replace(" ", "");
            errorFormatoClasificar.setVisible(false);
            ArrayList<ArrayList<Double>> puntosClas = UtilSVM.cargarPuntosClasif(entrada);
            mostrarResultado(resultado, "\n" + "Clasificacion de datos: \n" + svm.clasificarPuntos(puntosClas, w, b));
            ArrayList<Integer> clasificacion = svm.clasificarPuntosInt(puntosClas, w, b);
            ArrayList<Plot> puntosGraficar = UtilSVM.addPuntosClass(puntosClas, clasificacion, axes);
            for (int i = 0; i < puntosGraficar.size(); i++) {
                layout.getChildren().add(puntosGraficar.get(i));
            }
            errorFormatoClasificar.setVisible(false);
        } catch (Exception e) {
            errorFormatoClasificar.setVisible(true);
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorFormatoAlfas.setVisible(false);
        errorFormatoPuntosEntrada.setVisible(false);
        errorFormatoClasificar.setVisible(false);
    }    
    
    private void mostrarResultado(TextArea resul, String s){
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                resul.setText(resul.getText() + s);
//                resul.positionCaret(resul.getLength());
//            }
//        });
        resul.setText(resul.getText() + s);
    }
    
    @FXML
    void pregunta1OnAction(ActionEvent event) {
        PopOver pop = new PopOver();
        VBox vbox = new VBox();
        Label textLabel = new Label(leyenda1);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().add(textLabel);
        pop.setContentNode(vbox);
        pop.show(pregunta1);
    }
    
    @FXML
    void pregunta2OnAction(ActionEvent event) {
        PopOver pop = new PopOver();
        VBox vbox = new VBox();
        Label textLabel = new Label(leyenda2);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().add(textLabel);
        pop.setContentNode(vbox);
        pop.show(pregunta2);
    }
    
    @FXML
    void pregunta3OnAction(ActionEvent event) {
        PopOver pop = new PopOver();
        VBox vbox = new VBox();
        Label textLabel = new Label(leyenda3);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().add(textLabel);
        pop.setContentNode(vbox);
        pop.show(pregunta3);
    }
}
