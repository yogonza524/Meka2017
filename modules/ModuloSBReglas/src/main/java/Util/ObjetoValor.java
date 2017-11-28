/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;

/**
 *
 * @author pichon
 */
public class ObjetoValor {
    ArrayList<String> objetos;
    ArrayList<Boolean> valores;
    boolean reglaAplicada = false;
    boolean errorProducido = false;
    String mensajeError;
    String proceso;

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public ArrayList<String> getObjetos() {
        return objetos;
    }

    public void setObjetos(ArrayList<String> objetos) {
        this.objetos = objetos;
    }

    public ArrayList<Boolean> getValores() {
        return valores;
    }

    public void setValores(ArrayList<Boolean> valores) {
        this.valores = valores;
    }

    public boolean isReglaAplicada() {
        return reglaAplicada;
    }

    public void setReglaAplicada(boolean reglaAplicada) {
        this.reglaAplicada = reglaAplicada;
    }

    public boolean isErrorProducido() {
        return errorProducido;
    }

    public void setErrorProducido(boolean errorProducido) {
        this.errorProducido = errorProducido;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
    
    
}
