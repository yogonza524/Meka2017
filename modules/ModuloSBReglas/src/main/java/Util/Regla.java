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
public class Regla {
    private ArrayList<String> premisa;
    private ArrayList<Boolean> valoresPremisa; 
    private ArrayList<Boolean> signoPremisa; //true si esta negado
    private String conseguente;
    private String reglaString;
    private Boolean valorConsecuente;
    private boolean reglaAplicada = false;
    
    //Recibe la regla: -P^-C>R;
    public Regla(String regla) {
        premisa = new ArrayList<>();
        valoresPremisa = new ArrayList<>(); 
        signoPremisa = new ArrayList<>();
        String aux, consec;
        reglaString = regla;
        String stringRegla = regla;
        consec = stringRegla.substring(stringRegla.indexOf('>')+1);
        stringRegla = stringRegla.substring(0, stringRegla.indexOf('>'));
        stringRegla += "^#";
        
        while(!stringRegla.equals("#")){
            aux = stringRegla.substring(0, stringRegla.indexOf('^'));
            if (aux.contains("-")) {
                signoPremisa.add(true);
                aux = aux.replace("-", "");
            }else{
                signoPremisa.add(false);
            }
            premisa.add(aux);
            valoresPremisa.add(null);
            stringRegla = stringRegla.substring(stringRegla.indexOf('^')+1);
        }
        conseguente = consec;
    }
    
    public ObjetoValor aplicarRegla(String objeto, boolean valor){
        String procesoString = "";
        ObjetoValor salida = new ObjetoValor();
        ArrayList<String> objetos = new ArrayList<>();
        ArrayList<Boolean> valores = new ArrayList<>();
        
//        procesoString += "\nRegla: "+reglaString;
//        procesoString += "\nPremisa: "+premisa;
//        procesoString += "\nValores: "+valoresPremisa;
//        procesoString += "\nConsecuente: "+conseguente+": "+valorConsecuente;
        
        //Ingreso los valores de entrada
        boolean initValores = true;
        if (conseguente.equals(objeto)) {
            valorConsecuente = valor;
            initValores = false;
        } else {
            for (int i = 0; i < premisa.size(); i++) {
                if (premisa.get(i).equals(objeto)) {
                    valoresPremisa.set(i, valor^(signoPremisa.get(i))); //Me fijo el signo del objeto
                    initValores = false;
                    break;
                }
            }
        }
        
        if (initValores) {
            salida.setProceso(procesoString);
            return salida;
        }
        
//        procesoString += "\n\nIngreso los valores de entrada";
//        procesoString += "\nPremisa: "+premisa;
//        procesoString += "\nValores: "+valoresPremisa;
//        procesoString += "\nConsecuente: "+conseguente+": "+valorConsecuente;
        
        //Pregunto por el consecuente
        if(valorConsecuente!=null){
            //el consecuente tiene un valor
            if (valorConsecuente == false) { //Modus tollens
                //Si solo hay un elemento its izi
                if (premisa.size() == 1) {
                    if (valoresPremisa.get(0) == null) {
                        objetos.add(premisa.get(0));
                        valoresPremisa.set(0, false);
                        valores.add(false);
                    }else{
                        if (valoresPremisa.get(0)) {
                            procesoString += "\nDatos inconsistentes. Variable: "+ premisa.get(0);
                            salida.setErrorProducido(true);
                            salida.setProceso(procesoString);
                            return salida;
                        }
                    }
                    salida.setObjetos(objetos);
                    salida.setValores(valores);
                    salida.setReglaAplicada(true);
                    salida.setErrorProducido(false);
                    reglaAplicada = true;

//                    procesoString += "\n\nValores despues de aplicar regla";
//                    procesoString += "\nPremisa: "+premisa;
//                    procesoString += "\nValores: "+valoresPremisa;
//                    procesoString += "\nConsecuente: "+conseguente+": "+valorConsecuente;
//                    procesoString += "\n- - - - - - - - - - - - - - - - - - - >";
                    salida.setProceso(procesoString);
                    return salida;
                }
                
                //Si hay mas de una premisa debo ver que onda (no puedo aplicar nada)
            }
        }
        
        //Pregunto por los las premisas
        boolean bandera = true;
        for (int i = 0; i < premisa.size(); i++) {
            if (valoresPremisa.get(i)==null) {
                //no puedo concluir nada aun. Faltan valores
                bandera = false;
                break;
            }
            if (valoresPremisa.get(i)==false) {
                bandera = false;
                break;
            }
        }
        
        //Todas las premisas son verdaderas. Aplico modus ponens
        if (bandera) { 
            if (valorConsecuente == null) {
                valorConsecuente = true;
                valores.add(true);
                objetos.add(conseguente);
                salida.setObjetos(objetos);
                salida.setValores(valores);
                salida.setReglaAplicada(true);
                salida.setErrorProducido(false);
                reglaAplicada = true;
                
//                procesoString += "\n\nValores despues de aplicar regla";
//                procesoString += "\nPremisa: "+premisa;
//                procesoString += "\nValores: "+valoresPremisa;
//                procesoString += "\nConsecuente: "+conseguente+": "+valorConsecuente;
//                procesoString += "\n- - - - - - - - - - - - - - - - - - - >";
                salida.setProceso(procesoString);
                return salida;
            }else{
                if (valorConsecuente == false) {
                    procesoString += "\nDatos inconsistentes. Variable: "+ conseguente;
                    salida.setErrorProducido(true);
                    salida.setProceso(procesoString);
                    return salida;
                }                
            }
        }
        //
//        procesoString += "\nNo se concluyo nada";
        salida.setReglaAplicada(false);
        salida.setErrorProducido(false);
        salida.setProceso(procesoString);
        return salida;
    }

    //Geter y Seter
    public ArrayList<String> getPremisa() {
        return premisa;
    }

    public void setPremisa(ArrayList<String> premisa) {
        this.premisa = premisa;
    }

    public ArrayList<Boolean> getValoresPremisa() {
        return valoresPremisa;
    }

    public void setValoresPremisa(ArrayList<Boolean> valoresPremisa) {
        this.valoresPremisa = valoresPremisa;
    }

    public ArrayList<Boolean> getSignoPremisa() {
        return signoPremisa;
    }

    public void setSignoPremisa(ArrayList<Boolean> signoPremisa) {
        this.signoPremisa = signoPremisa;
    }

    public String getConseguente() {
        return conseguente;
    }

    public void setConseguente(String conseguente) {
        this.conseguente = conseguente;
    }

    public boolean isValorConsecuente() {
        return valorConsecuente;
    }

    public void setValorConsecuente(boolean valorConsecuente) {
        this.valorConsecuente = valorConsecuente;
    }

    public String getReglaString() {
        return reglaString;
    }

    public void setReglaString(String reglaString) {
        this.reglaString = reglaString;
    }

    public boolean isReglaAplicada() {
        return reglaAplicada;
    }

    public void setReglaAplicada(boolean reglaAplicada) {
        this.reglaAplicada = reglaAplicada;
    }

}
