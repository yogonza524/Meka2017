package com.pichon.modulosbreglas;

import Util.ObjetoValor;
import Util.Regla;
import java.net.URL;
import java.util.ArrayList;
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

public class FXMLControllerSBR implements Initializable {
    
    private String leyenda1 =   "Ingresar los elementos del problema separados \n" +
                                "por “,” y con “;” para finalizar. Por ejemplo\n" +
                                "A, B, C;";
    private String leyenda2 =   "Ingreso las reglas, una por cada línea y finalizando\n" +
                                "con “;”. Para armar las reglas se usan los objetos \n" +
                                "definidos arriba y los operadores: negación “-”;\n" +
                                "conjunción “^”; e implicación “>”. Por ejemplo\n" +
                                "-X>Y;\n" +
                                "-A>Y;\n" +
                                "…";
    private String leyenda3 =   "Ingreso las evidencias del problema para\n" +
                                "cada elemento, con el siguiente formato:\n" +
                                "A:flase;\n" +
                                "C:true;\n" +
                                "…";
    private ArrayList<Regla> reglas;
    private String inicio;
    @FXML private TextArea reglasString;
    @FXML private TextArea resultados;
    @FXML private TextArea proceso;
    @FXML private Label errorEntradas;
    @FXML private TextArea entradasString;
    @FXML private TextField objetosString;
    @FXML private Label errorReglas;
    
    @FXML private Hyperlink pregunta1;
    @FXML private Hyperlink pregunta3;
    @FXML private Hyperlink pregunta2;

    @FXML void generarMotorInferencia(ActionEvent event) {
        String salida = "";
        ArrayList<String> stringReglas;
        ArrayList<String> objetos;
        //Cargo los arreglos
        try{
            stringReglas = Util.UtilSBReglas.getReglas(reglasString.getText());
            objetos = Util.UtilSBReglas.getObjetos(objetosString.getText());
            errorReglas.setVisible(false);
        } catch (Exception e) {
            errorReglas.setVisible(true);
            return;
        }
        salida += "Objetos: " + objetos;
        salida += "\n\nReglas\n" + reglasString.getText();
        
        //Creo las reglas 
        reglas = new ArrayList<>();
        for (int i = 0; i < stringReglas.size(); i++) {
            Regla regla = new Regla(stringReglas.get(i));
//            salida += "\n\nRegla: "+stringReglas.get(i);
//            salida += "\nPremisa: "+regla.getPremisa();
//            salida += "\nSigno(true=negado): "+regla.getSignoPremisa();
//            salida += "\nConsecuente: "+regla.getConseguente();
            reglas.add(regla);
        }
        salida += "\n\nReglas generadas correctamente\n";
        inicio = salida;
        proceso.setText(salida);
    }

    @FXML void inferir(ActionEvent event) {
        String salida = "";
        String salida2;
        ObjetoValor valoresEntrada = new ObjetoValor();
        
        try{
            valoresEntrada = Util.UtilSBReglas.getValoresEntrada(entradasString.getText());
            errorEntradas.setVisible(false);
        } catch (Exception e) {
            errorEntradas.setVisible(true);
            return;
        }
        salida += "- - - - - - - - - - - - - - - \n\nValores de entrada";
        for (int i = 0; i < valoresEntrada.getObjetos().size(); i++) {
            salida += "\n"+valoresEntrada.getObjetos().get(i)+": "+valoresEntrada.getValores().get(i);
        }
        
        //Comienzo algoritmo
        salida += "\n\n-------------------------\n"
                    + "| Comienza algoritmo |\n"
                + "-------------------------";
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
            salida += "\nEntrada: " + objetoi + ": " + valori;
            salida += "\n"+objetoEntrada;
            salida += "\n"+valorEntrada;

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
                            salida += salidaRegla.getProceso();
                            
                            //Controlo que no se haya producido un error
                            if (salidaRegla.isErrorProducido()) {
                                salida += "\n"+salidaRegla.getMensajeError();
                                huboError = true;
                                break;
                            }

                            //pregunto si se aplico la regla
                            if (salidaRegla.isReglaAplicada()) {
                                salida += "\n\nRegla aplicada: "+reglas.get(j).getReglaString()+"\nValores inferidos: ";
                                for (int k = 0; k < salidaRegla.getObjetos().size(); k++) {
                                    salida += salidaRegla.getObjetos().get(k)+": "+salidaRegla.getValores().get(k);

                                    //Pregunto si los valores inferidos ya no estan cargados en memoria
                                    if (!objetoEntrada.contains(salidaRegla.getObjetos().get(k))) {
//                                        salida += "\nEl valor "+ salidaRegla.getObjetos().get(k) +" no ha sido inferido anteriormente";
                                        objetoEntrada.add(salidaRegla.getObjetos().get(k));
                                        valorEntrada.add(salidaRegla.getValores().get(k));
                                        reglaAplicada = true;
                                        salida += "\n"+objetoEntrada;
                                        salida += "\n"+valorEntrada;
                                    }else{
                                        salida += "\nEl valor "+salidaRegla.getObjetos().get(k)+" ya fue inferido anteriormente";
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
                if (huboError) {
                    break;
                }
                if (!reglaAplicada) {
                    salida += "\n\nYa no hay mas reglas para aplicar";
                    salida += "\n\nObtengo la siguiente entrada"
                            + "\n----------------------------";
                    break; //No hay mas reglas para aplicar
                }
            }
            if (huboError) {
                break;
            }
        }
        salida2 = "Conclusiones finales:";
        for (int j = 0; j < objetoEntrada.size(); j++) {
            salida2 += "\n"+objetoEntrada.get(j)+": "+valorEntrada.get(j).toString();
        }
        proceso.setText(inicio + salida);
        resultados.setText(salida2);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorEntradas.setVisible(false);
        errorReglas.setVisible(false);
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

    @FXML void pregunta3OnAction(ActionEvent event) {
        PopOver pop = new PopOver();
        VBox vbox = new VBox();
        Label textLabel = new Label(leyenda3);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().add(textLabel);
        pop.setContentNode(vbox);
        pop.show(pregunta3);
    }
}
