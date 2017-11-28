package com.pichon.modulokmeans;

import Util.Axes;
import Util.Cluster;
import Util.KMeans;
import Util.KMeansResultado;
import Util.Plot;
import Util.Punto;
import Util.UtilKmeans;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

public class KmeansController implements Initializable {
    
    private String leyenda1 =   "Cada punto representa un conjunto de parámetros.\n" +
                                "Estos valores se ingresan separado por “,” y terminan\n" +
                                "con “;”. Por ejemplo:\n" +
                                "6,8,5;\n" +
                                "12,8,2;\n" +
                                "…";
    private String leyenda2 =   "Ingresamos los puntos a ser clasificados. Estos valores\n" +
                                "se ingresan con el mismo formato que el anterior.";
    private StackPane layout;
    ArrayList<StackPane> layoutLists = new ArrayList<>();
    private Axes axes;
    private int dim;
    KMeansResultado resul;
    @FXML private TextArea entradasClasificar;
    @FXML private AnchorPane canvas;
    @FXML private TextArea resultado;
    @FXML private ChoiceBox<Integer> cantCluster;
    @FXML private ChoiceBox<String> tipoInicio;
    @FXML private TextArea puntosEntrada;
    @FXML private ChoiceBox<Integer> dimension;
    @FXML private ScrollPane scrollPane;
    @FXML private Label errorEntradas;
    @FXML private Label errorClasificar;
    @FXML private Hyperlink pregunta1;
    @FXML private Hyperlink pregunta2;
    
    @FXML void clasificarEntradas(ActionEvent event) {
        try{
            //Clasificar puntos
            String entradaClasificar = entradasClasificar.getText();
            ArrayList<ArrayList<Double>> puntosClasificar = UtilKmeans.cargarPuntos(entradaClasificar, dimension.getValue());
            //Agregamos los puntos
            List<Punto> puntos = new ArrayList<>();
            for (int i = 0; i < puntosClasificar.size(); i++) {
                Punto p = new Punto(puntosClasificar.get(i));
                puntos.add(p);
            }
            String clasificacion = resul.ClasificarPuntos(puntos);
            resultado.setText(resultado.getText() + "\n" +  clasificacion);
            errorClasificar.setVisible(false);
        } catch (Exception e) {
            errorClasificar.setVisible(true);
        }

    }

    @FXML void generarCluster(ActionEvent event) {
        dim = dimension.getValue();
        resultado.setText("");
//        try{
            int cantClus = cantCluster.getValue();
            String datos = puntosEntrada.getText();
            ArrayList<ArrayList<Double>> datosPuntos = UtilKmeans.cargarPuntos(datos, dim);
            //Creo el kmeans y lo entreno
            KMeans kmeans = new KMeans();
            kmeans.addPuntos(datosPuntos);
            resul = kmeans.calcular(cantClus, tipoInicio.getValue());
        
            //Clasificacion final
            resultado.setText("Cantidad de iteraciones: "+resul.getCantIteraciones());
            resultado.setText( resultado.getText() + UtilKmeans.resultadoFinal(resul));
            
            //Mostrar colores de cada cluster
            String[] colores = UtilKmeans.getColorName();
            String colorCluster = "";
            for (int i = 0; i < resul.getCantCluster(); i++) {
                colorCluster = colorCluster + "Cluster" + i + " (" + colores[i] + ")\n";
            }
            resultado.setText(resultado.getText() + colorCluster + "\n");

            //Grafico los clusters
            if (dim == 2) {
                StackPane layout = new StackPane();
                layout = generarGrafico(datosPuntos); //Genero grafico de puntos de entrada
                
                //Imprimo las distintas posiciones que van tomando los centroides en cada iteracion
                ArrayList<Plot> puntosPlotCentroides = addHCluster(layout, resul.getCentroideClusterN());
                for (int i = 0; i < puntosPlotCentroides.size(); i++) {
                    layout.getChildren().add(puntosPlotCentroides.get(i));                 
                }
                
                //Imprimo todos los puntos del problema
                ArrayList<Plot> puntosPlot = addCluster2(layout, resul.getClusters());
                for (int i = 0; i < puntosPlot.size(); i++) {
                    layout.getChildren().add(puntosPlot.get(i));                 
                }

                //Agrego las lineas del paso de los centroides en cada iteracion
                ArrayList<Plot> lineasClusterCent = generarLineasUnionHistClusters(resul.getCentroideClusterN(), resul.getClusters().size());
                for (int i = 0; i < lineasClusterCent.size(); i++) {
                    layout.getChildren().add(lineasClusterCent.get(i));                 
                }

                //Agrego las lineas del cluster a los puntos
                ArrayList<Plot> lineasCluster = generarLineasUnion2(resul.getClusters());
                for (int i = 0; i < lineasCluster.size(); i++) {
                    layout.getChildren().add(lineasCluster.get(i));                 
                }
                
                canvas.getChildren().clear();
                canvas.getChildren().add(layout);

                //Agregar las etiquetas
                Rectangle rect = new Rectangle(40,20,135,(25*cantClus)+15);
                rect.setFill(Color.TRANSPARENT);
                rect.setStroke(Color.WHITE);
                rect.setStrokeWidth(3);
                canvas.getChildren().add(rect);
                int valorX = 50;
                int valorY = 50;
                Color[] color = UtilKmeans.getColores();
                for (int i = 0; i < cantClus; i++) {
                    Text t = drawText("CLUSTER "+i, valorX, valorY, color[i]);
                    canvas.getChildren().add(t);
                    valorY += 25;
                }
            
                scrollPane.setHvalue(scrollPane.getHmax()/2);
                scrollPane.setVvalue(scrollPane.getVmax()/2);
            }
            
            errorEntradas.setVisible(false);
//        } catch (Exception e) {
//            errorEntradas.setVisible(true);
//        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dimension.getItems().addAll(
            2, 3, 4, 5
        );
        tipoInicio.getItems().addAll(
            "Aleatoria", 
            "Forgy"
        );
        cantCluster.getItems().addAll(
            2, 3, 4, 5, 6
        );
        dimension.setValue(2);
        cantCluster.setValue(2);
        tipoInicio.setValue("Aleatoria");
        
        errorEntradas.setVisible(false);
        errorClasificar.setVisible(false);
    }
    
    private StackPane generarGrafico(ArrayList<ArrayList<Double>> datosPuntos){
        //Generar ejes cartesianos
        axes = new Axes(
                1200, 1200,
                -30, 30, 2,
                -30, 30, 2
        );
        //generamos el layoutAux
        StackPane layoutAux = new StackPane();
        layoutAux.setPadding(new Insets(20));
        layoutAux.setStyle("-fx-background-color: rgb(0, 0, 0);"); //(35, 39, 50)
        layoutAux.setPrefSize(canvas.getWidth() -1, canvas.getHeight() -1);
        canvas.getChildren().add(layoutAux);
        
        //Agregamos los puntos
        ArrayList<Plot> puntosGraficar = UtilKmeans.addPuntosGrafic(datosPuntos, axes);
        for (int i = 0; i < puntosGraficar.size(); i++) {
            layoutAux.getChildren().add(puntosGraficar.get(i));
        }
        return layoutAux;
    }
    
    private ArrayList<ArrayList<Plot>> addCluster(StackPane layout, ArrayList<ArrayList<Cluster>> Clusters){
        ArrayList<ArrayList<Plot>> salida = new ArrayList<>();
        for (int i = 0; i < Clusters.size(); i++) {
            ArrayList<ArrayList<Double>> cluster = UtilKmeans.getCoordenadasCluster(Clusters.get(i));
            ArrayList<Plot> puntosGraficar = UtilKmeans.addClustGrafic(cluster, axes);
            salida.add(puntosGraficar);
        }
        return salida;
    }
    
    private ArrayList<Plot> addCluster2(StackPane layout, ArrayList<Cluster> clusters){
        ArrayList<Plot> salida = new ArrayList<>();
        for (int i = 0; i < clusters.size(); i++) {
            ArrayList<Double> cluster = UtilKmeans.getCoordenadasCluster2(clusters.get(i));
            Plot puntosGraficar = UtilKmeans.addClustGrafic2(cluster, axes, clusters.size());
            salida.add(puntosGraficar);
        }
        return salida;
    }
    
    private ArrayList<Plot> generarLineasUnion(KMeansResultado resultado){
        ArrayList<Plot> salida = new ArrayList<>();
        for (int i = 0; i < resultado.getClusters().size(); i++) {
            List<Punto> puntos = resultado.getClusters().get(i).getPuntos();
            for (int j = 0; j < puntos.size(); j++) {
                salida.add(UtilKmeans.graficarLineaACluster(puntos.get(j), resultado.getClusters().get(i).getCentroide(), axes, i));
            }
        }
        return salida;
    }
    
    private ArrayList<Plot> generarLineasUnion2(ArrayList<Cluster> clusters){
        ArrayList<Plot> salida = new ArrayList<>();
        for (int i = 0; i < clusters.size(); i++) {
            List<Punto> puntos = clusters.get(i).getPuntos();
            for (int j = 0; j < puntos.size(); j++) {
                salida.add(UtilKmeans.graficarLineaACluster(puntos.get(j), clusters.get(i).getCentroide(), axes, i));
            }
        }
        return salida;
    }
    
    private ArrayList<Plot> generarLineasUnionHistClusters(ArrayList<Punto> centroides, int cantCluster){
        ArrayList<Plot> salida = new ArrayList<>();
        for (int i = 0; i < centroides.size()-cantCluster; i = i+cantCluster) {
            for (int j = 0; j < cantCluster; j++) {
                salida.add(UtilKmeans.graficarLineaHistCluster(centroides.get(i+j), centroides.get(i+j+cantCluster), axes, i));
            }
        }
        return salida;
    }
    
    private Text drawText(String nombre, int x, int y, Color color){
        Text t = new Text();
        t.setFill(color);
        t.setText(nombre);
        t.setX(x);
        t.setY(y);
        t.setId("textNegrita");
        return t;
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

    private ArrayList<Plot> addHCluster(StackPane layout, ArrayList<Punto> centroideClusterN) {
        ArrayList<Plot> salida = new ArrayList<>();
        
        for (int i = 0; i < centroideClusterN.size(); i++) {
            Plot puntosGraficar = UtilKmeans.addHistClustGrafic(centroideClusterN.get(i), axes);
            salida.add(puntosGraficar);
        }
        
        return salida;
    }
}
