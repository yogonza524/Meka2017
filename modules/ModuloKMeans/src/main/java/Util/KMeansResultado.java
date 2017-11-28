/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author pichon
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KMeansResultado {
    private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    private ArrayList<Punto> centroideClusterN;
//    private List<List<Cluster>> historial = new ArrayList<>();
    private Double ofv;
    private int cantCluster;
    private int cantIteraciones;
//    private ArrayList<ArrayList<Cluster>> historialCluster;

    public KMeansResultado(ArrayList<Cluster> clusters, Double ofv, int cantCluster) {
	super();
	this.ofv = ofv;
	this.clusters = clusters;
        this.cantCluster = cantCluster;
    }

    public int getCantIteraciones() {
        return cantIteraciones;
    }

    public void setCantIteraciones(int cantIteraciones) {
        this.cantIteraciones = cantIteraciones;
    }

    public ArrayList<Punto> getCentroideClusterN() {
        return centroideClusterN;
    }

    public void setCentroideClusterN(ArrayList<Punto> centroideClusterN) {
        this.centroideClusterN = centroideClusterN;
    }

//    public List<List<Cluster>> getHistorial() {
//        return historial;
//    }
//
//    public void setHistorial(List<List<Cluster>> historial) {
//        this.historial = historial;
//    }
    
    public ArrayList<Cluster> getClusters() {
	return clusters;
    }
    
    public ArrayList<Cluster> setClusters(ArrayList<Cluster> clusters) {
	return this.clusters = clusters;
    }

    public Double getOfv() {
	return ofv;
    }

    public int getCantCluster() {
        return cantCluster;
    }

//    public ArrayList<ArrayList<Cluster>> getHistorialCluster() {
//        return historialCluster;
//    }
//
//    public void setHistorialCluster(ArrayList<ArrayList<Cluster>> historialCluster) {
//        this.historialCluster = historialCluster;
//    }

    public String ClasificarPuntos(List<Punto> puntosClasificar) {
        String salida = "";
        double distancia;
        int numCluster = -1;
        for (int i = 0; i < puntosClasificar.size(); i++) {
            distancia = Double.POSITIVE_INFINITY;
            for (int j = 0; j < cantCluster; j++) {
                if (distancia > puntosClasificar.get(i).distanciaEuclideana(clusters.get(j).getCentroide())) {
                    distancia = puntosClasificar.get(i).distanciaEuclideana(clusters.get(j).getCentroide());
                    numCluster = j;
                }
            }
            salida = salida + "Punto: " + Arrays.toString(puntosClasificar.get(i).getData()) + " corresponde al cluster: " + numCluster + "\n";
        }
        return salida;
    }

}
