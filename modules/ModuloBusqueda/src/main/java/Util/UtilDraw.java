/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.EllipseBuilder;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author pichon
 */
public class UtilDraw {
    
    int radioNodos = 20;
    int correc = 5;    
    
    public Group dibujarGrafo(Grafo grafo){
        Group root = new Group();
        int distanciaNodos = 100;
        int longitudPane;
        Random r =  new Random();
        
        ArrayList<String> nombreNodos = grafo.getNombreNodos();
        Map<String, Nodo> nodos = grafo.getNodos();
        ArrayList<int[]> posicion = new ArrayList<>();
        longitudPane = nodos.size()*80;
        
        //Creamos las posiciones de cada nodo
        for (int i = 0; i < nodos.size(); i++) {
            int[] posXY = new int[2];
            while(true){
                boolean bandera = true;
                posXY[0] = r.nextInt(longitudPane-60)+50;
                posXY[1] = r.nextInt(longitudPane-60)+50;
                for (int j = 0; j < posicion.size(); j++) {
                    if (!distanciaCorrecta(posicion.get(j), posXY, distanciaNodos)) {
                        bandera = false;
                        break;
                    }
                }
                if (bandera) {
                    posicion.add(posXY);
                    break;
                }
            }
        }
        
        //Dibujamos las lineas
        Map<String, Arista> aristas = grafo.getAristas();
        for (int i = 0; i < nodos.size(); i++) {
            for (int j = 0; j < nodos.size(); j++) {
                Arista a = aristas.get(nombreNodos.get(i)+"-"+nombreNodos.get(j));
                if(a != null){
                    int[] p1 = posicion.get(i);
                    int[] p2 = posicion.get(j);
                    Line l = drawLine(p1[0], p1[1], p2[0], p2[1]);
                    int x = (p1[0]+p2[0])/2;
                    int y = (p1[1]+p2[1])/2;
                    String valor = String.valueOf(a.getValor());
                    Text t = drawText(valor, x, y, 16);
                    root.getChildren().add(l);
                    root.getChildren().add(t);
                }
            }
        }
        
        //Creamo cada uno de los nodos
        for (int i = 0; i < nodos.size(); i++) {
            int[] posXY = posicion.get(i);
            
            //Creamos el nodo centroX, centroY, radio
            Ellipse c = drawCircle(posXY, radioNodos);
            root.getChildren().add(c);

            //Creamos el texto de nombre
            Text t = drawText(nombreNodos.get(i), posXY[0]-correc, posXY[1]+correc, 22);
            root.getChildren().add(t);
            
            //Creamos la heuristica del nodo
            String valor = String.valueOf(nodos.get(nombreNodos.get(i)).getValor());
            int x = posXY[0]+radioNodos;
            Text t2 = drawText(valor, x, posXY[1]+correc, 18);
            root.getChildren().add(t2);
        }
        
        return root;
    }
    
    private Text drawText(String nombre, int x, int y, int tamano){
        Text t = new Text();
        t.setFill(Color.BLACK);
        t.setText(nombre);
        t.setX(x);
        t.setY(y);
        t.setFont(Font.font ("Verdana", tamano));
        return t;
    }
    
    private Line drawLine(int x1, int y1, int x2, int y2){
        Line line = new Line(x1, y1, x2, y2); 
        line.setStrokeWidth(2);
        line.setStrokeLineJoin(StrokeLineJoin.MITER);
        return line;
    }
    
    private Ellipse drawCircle(int[] posXY, int radioNodos){
        Ellipse c = EllipseBuilder.create()
             .centerX(posXY[0])
             .centerY(posXY[1])
             .radiusX(radioNodos)
             .radiusY(radioNodos)
             .strokeWidth(3)
             .stroke(Color.BLACK)
             .fill(Color.WHITE)
             .build();
        return c;
    }

    private boolean distanciaCorrecta(int[] posXY, int[] posXYNueva, int distancia) {
        double x = Math.abs(posXY[0] - posXYNueva[0]);
        double y = Math.abs(posXY[1] - posXYNueva[1]);
        double dis = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        if(dis > distancia){
            return true;
        }else{
            return false;
        }
    }
    
    //Dibujo el arbol de expansion
    public Group dibujarArbol(NodoArbol root, boolean conHeuristica){
        Group grafico = new Group();
        int xInicial = 500;
        int yInicial = 50;
        int separacionX = 150;
        int separacionY = 80;
        grafico.getChildren().add(drawLines(root, xInicial, yInicial, xInicial, yInicial, separacionX, separacionY));
        grafico.getChildren().add(drawNodo(root, xInicial, yInicial, xInicial, yInicial, separacionX, separacionY, conHeuristica));
        return grafico;
    }
    
    private Group drawLines(NodoArbol n, int x, int y, int xp, int yp, int sepX, int sepY){
        Group grafico = new Group();

        //Dibujamos hijos
        List<NodoArbol> hijos = n.getChildren();
        if (hijos==null) {
            return null;
        }
        int yHijo = y + sepY;
        int xHijo;
        if (hijos.size()==1) {
            xHijo = x;
        }else{
            xHijo = x-((hijos.size()*sepX)/2);
        }
        for (int i = 0; i < hijos.size(); i++) {
            //Creamos la arista
            Line l = drawLine(x, y, xHijo, yHijo);
            grafico.getChildren().add(l);
            grafico.getChildren().add(drawLines(hijos.get(i), xHijo, yHijo, x, y, (int)(sepX-(sepX*0.3)), sepY));
            xHijo += sepX;
        }
        return grafico;
    }
    
    private Group drawNodo(NodoArbol n, int x, int y, int xp, int yp, int sepX, int sepY, boolean conHeuristica){
        Group grafico = new Group();
        
        //Creamos el nodo centroX, centroY, radio
        int[] posXY = {x, y};
        Ellipse c = drawCircle(posXY, radioNodos);
        grafico.getChildren().add(c);

        //Creamos el texto de nombre
        Text t = drawText(n.getData().toString(), posXY[0]-correc, posXY[1]+correc, 22);
        grafico.getChildren().add(t);
        
        //Creamos el texto de valor de funcion del nodo
        if (conHeuristica) {
            Text tv = drawText(String.valueOf(n.getValue()), posXY[0]+radioNodos+5, posXY[1]+correc, 16);
            grafico.getChildren().add(tv);
        }

        //Dibujamos hijos
        List<NodoArbol> hijos = n.getChildren();
        if (hijos==null) {
            return null;
        }
        int yHijo = y + sepY;
        int xHijo;
        if (hijos.size()==1) {
            xHijo = x;
        }else{
            xHijo = x-((hijos.size()*sepX)/2);
        }
        for (int i = 0; i < hijos.size(); i++) {
            //Creamos la arista
            grafico.getChildren().add(drawNodo(hijos.get(i), xHijo, yHijo, x, y, (int)(sepX-(sepX*0.3)), sepY, conHeuristica));
            xHijo += sepX;
        }
        return grafico;
    }
    
}
