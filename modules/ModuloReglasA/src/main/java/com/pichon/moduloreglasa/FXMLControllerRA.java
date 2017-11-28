package com.pichon.moduloreglasa;

import Util.ReglaAsociacion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

public class FXMLControllerRA implements Initializable {
    
    String leyenda =    "Cada transacción termina con “;” y cada \n" +
                        "elemento va separado por “,” por ejemplo\n" +
                        "B,I,M;\n" +
                        "B,C;\n" +
                        "…";
    @FXML private TextField minSoporte;
    @FXML private TextArea proceso;
    @FXML private TextArea transacciones;
    @FXML private TextField minConfianza;
    @FXML private Label error;
    @FXML private Hyperlink pregunta;

    @FXML void procesar(ActionEvent event) {
        try{
            double minSup = Double.valueOf(minSoporte.getText());
            double minConf = Double.valueOf(minConfianza.getText());
            String e = transacciones.getText();
            String salidaProceso = ReglaAsociacion.generarReglas(e, minSup, minConf);
            proceso.setText(salidaProceso);
            error.setVisible(false);
        } catch (Exception e) {
            error.setVisible(true);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        error.setVisible(false);
    }

    @FXML void preguntaReglas(ActionEvent event) {
        PopOver pop = new PopOver();
        VBox vbox = new VBox();
        Label textLabel = new Label(leyenda);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().add(textLabel);
        pop.setContentNode(vbox);
        pop.show(pregunta);
    }
}
