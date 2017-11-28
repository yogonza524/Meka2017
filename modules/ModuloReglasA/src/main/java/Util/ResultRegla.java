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
public class ResultRegla {
    
    ArrayList<String> premisa;
    ArrayList<String> consecuente;
    double confianza;

    public ArrayList<String> getPremisa() {
        return premisa;
    }

    public void setPremisa(ArrayList<String> premisa) {
        this.premisa = premisa;
    }

    public ArrayList<String> getConsecuente() {
        return consecuente;
    }

    public void setConsecuente(ArrayList<String> consecuente) {
        this.consecuente = consecuente;
    }

    public double getConfianza() {
        return confianza;
    }

    public void setConfianza(double confianza) {
        this.confianza = confianza;
    }
    
}
