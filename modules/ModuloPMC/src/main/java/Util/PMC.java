/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author pichon
 */
public class PMC {

    private ArrayList<ArrayList<Double>> valorEntradas;
    private ArrayList<ArrayList<Double>> valorSalida;
    private int cantEntradas;
    private int cantCapasOculta;
    private int cantNeuronasCapasOcultaI;
    private int cantSalida;
    private double errorAceptable;
    private double alfa;
    private String funcion; //funcion sigmoidal y lineal
    
    private final Random  rnd = new Random();
    private final Random  rnd2 = new Random();
    private Perceptron[] capaOculta;
    private Perceptron[] capaSalida;
    private double[] salida;

    public Perceptron[] getCapaOculta() {
        return capaOculta;
    }

    public void setCapaOculta(Perceptron[] capaOculta) {
        this.capaOculta = capaOculta;
    }

    public Perceptron[] getCapaSalida() {
        return capaSalida;
    }

    public void setCapaSalida(Perceptron[] capaSalida) {
        this.capaSalida = capaSalida;
    }
    
    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public ArrayList<ArrayList<Double>> getValorEntradas() {
        return valorEntradas;
    }

    public void setValorEntradas(ArrayList<ArrayList<Double>> valorEntradas) {
        this.valorEntradas = valorEntradas;
    }

    public ArrayList<ArrayList<Double>> getValorSalida() {
        return valorSalida;
    }

    public void setValorSalida(ArrayList<ArrayList<Double>> valorSalida) {
        this.valorSalida = valorSalida;
    }

    public int getCantEntradas() {
        return cantEntradas;
    }

    public void setCantEntradas(int cantEntradas) {
        this.cantEntradas = cantEntradas;
    }

    public int getCantCapasOculta() {
        return cantCapasOculta;
    }

    public void setCantCapasOculta(int cantCapasOculta) {
        this.cantCapasOculta = cantCapasOculta;
    }

    public int getCantNeuronasCapasOcultaI() {
        return cantNeuronasCapasOcultaI;
    }

    public void setCantNeuronasCapasOcultaI(int cantNeuronasCapasOcultaI) {
        this.cantNeuronasCapasOcultaI = cantNeuronasCapasOcultaI;
    }

    public int getCantSalida() {
        return cantSalida;
    }

    public void setCantSalida(int cantSalida) {
        this.cantSalida = cantSalida;
    }

    public double getErrorAceptable() {
        return errorAceptable;
    }

    public void setErrorAceptable(double errorAceptable) {
        this.errorAceptable = errorAceptable;
    }

    public double[] getSalida() {
        return salida;
    }

    public void setSalida(double[] salida) {
        this.salida = salida;
    }
    
    public void setAlfa(double valorAlfa){
        alfa = valorAlfa;
    }
    
    public double getAlfa(){
        return alfa;
    }
    
    public void initValues(){
        capaOculta = new Perceptron[cantCapasOculta * cantNeuronasCapasOcultaI];
        capaSalida = new Perceptron[cantSalida];
        salida = new double[cantSalida];
    }
    
    public void inicializarRedNeuronal(){
        
        Perceptron.setValorAlfa(alfa);
        int inicio, inicioE, fin, finE, cont = 0;
        initValues();
        
        //Inicializamos los perceptrones de la primera capa oculta. Funciona bien
        for (int i = 0; i < cantNeuronasCapasOcultaI; i++) {
            Perceptron perceptronCapaOculta = new Perceptron();
            perceptronCapaOculta.setFuncion(funcion);
            perceptronCapaOculta.setConexionesEntrada(new Conexion[cantEntradas]);
            if (cantCapasOculta == 1) {
                perceptronCapaOculta.setConexionesSalida(new Conexion[cantSalida]);
            }else{
                perceptronCapaOculta.setConexionesSalida(new Conexion[cantNeuronasCapasOcultaI]);
            }
            for (int j = 0; j < cantEntradas; j++) {
                Conexion conexion = new Conexion();
                conexion.setNombre("wE"+j+"-"+i);
                conexion.setPerceptronEntrada(null);
                conexion.setPerceptronSalida(perceptronCapaOculta);
                conexion.setPeso((rnd.nextDouble()*2)-1);
                perceptronCapaOculta.getConexionesEntrada()[j] = conexion;
                cont++;
            }
            capaOculta[i] = perceptronCapaOculta;
        }
        
        
        if (cantCapasOculta != 1) {
            
            //Inicializamos los perceptrones las demas capas ocultas. funciona bien
            if (cantCapasOculta > 1) {
                int cantConexionesSalidas;
                for (int w = 1; w < cantCapasOculta; w++) {
                    inicio = w*cantNeuronasCapasOcultaI;
                    fin = (w + 1)*cantNeuronasCapasOcultaI;
                    inicioE = (w-1)*cantNeuronasCapasOcultaI;                    
                    finE = w*cantNeuronasCapasOcultaI;
                    if (w == cantCapasOculta-1 ) {
                        cantConexionesSalidas = cantSalida;
                    } else {
                        cantConexionesSalidas = cantNeuronasCapasOcultaI;
                    }
                    cont = 0;
                    for (int i = inicio; i < fin; i++) {
                        Perceptron perceptronCapaOculta = new Perceptron();
                        perceptronCapaOculta.setFuncion(funcion);
                        perceptronCapaOculta.setConexionesEntrada(new Conexion[cantNeuronasCapasOcultaI]);
                        perceptronCapaOculta.setConexionesSalida(new Conexion[cantConexionesSalidas]);

                        //se conectan con las neuronas anterirores. Funciona bien
                        for (int j = 0; j < cantNeuronasCapasOcultaI; j++) {
                            Conexion conexion = new Conexion();
                            conexion.setNombre("w"+(inicioE+j)+"-"+(finE+cont));
                            conexion.setPerceptronEntrada(capaOculta[(inicio-cantNeuronasCapasOcultaI)+j]);
                            conexion.setPerceptronSalida(perceptronCapaOculta);
                            conexion.setPeso((rnd.nextDouble()*2)-1);
                            perceptronCapaOculta.getConexionesEntrada()[j] = conexion;
                            capaOculta[(inicio-cantNeuronasCapasOcultaI)+j].getConexionesSalida()[i-inicio] = conexion;
                        }
                        cont++;
                        capaOculta[i] = perceptronCapaOculta;
                    }
                    
                }

            }
        }
        

        //Inicializamos los perceptrones de la capa conexionesSalida. Creo que si funciona
        inicio = (cantCapasOculta-1) * cantNeuronasCapasOcultaI;
        for (int i = 0; i < cantSalida; i++) {
            Perceptron perceptronSalida = new Perceptron();
            perceptronSalida.setFuncion(funcion);
            perceptronSalida.setConexionesEntrada(new Conexion[cantNeuronasCapasOcultaI]);
            for (int j = 0; j < cantNeuronasCapasOcultaI; j++) {
                Conexion conexion = new Conexion();
                conexion.setNombre("w"+(((cantCapasOculta-1)*cantNeuronasCapasOcultaI)+j)+"-"+((cantCapasOculta*cantNeuronasCapasOcultaI)+i));
                conexion.setPerceptronEntrada(capaOculta[inicio + j]);
                conexion.setPerceptronSalida(perceptronSalida);
                conexion.setPeso((rnd.nextDouble()*2)-1);
                capaOculta[inicio + j].getConexionesSalida()[i] = conexion;
                perceptronSalida.getConexionesEntrada()[j] = conexion;
                cont++;
            }
            capaSalida[i] = perceptronSalida;
        }

    }
    
    public ProcesoAprendizaje entrenarRedNeuronal(){
        ProcesoAprendizaje salidaProceso = new ProcesoAprendizaje();
        double[] salidaEsperada = new double[cantSalida];
        int numEntradas = valorEntradas.size();
        int epoca = 0;
        int ciclo = 0;
        double error;
        double errorPatron = 0;
//        double errorPatronAnterior = 1;
                
        salidaProceso.addProceso("Comenzando proceso de aprendizaje");
        while(epoca < numEntradas){
            //Pongo las entradas en cada una de las conexiones de entrada
            for (int i = 0; i < cantNeuronasCapasOcultaI; i++) {
                for (int j = 0; j < cantEntradas; j++) {
                    capaOculta[i].getConexionesEntrada()[j].setValor(valorEntradas.get(ciclo).get(j)); //[(ciclo*cantEntradas)+j]
                }
            }
            //Pongo cuales son las salidas esperadas en esta iteracion
            for (int i = 0; i < cantSalida; i++) {
                salidaEsperada[i] = valorSalida.get(ciclo).get(i); //[(ciclo*cantSalida)+i];
            }
            
            //Aqui comienza la magia papa!
            //En el metodo a continuacion lo que hace es, comenzando con las primera capa de la capa oculta, 
            //obtiene el net de acuerdo a las conexiones de entrada, y luego calcula el F(net) de salida de
            //cada neurona y lo setea en las conexiones de salida, y asi va haciendo. Esta asi por q esta mal 
            //hecho el metodo esta todo acoplado y sin cohesion.
            String procesoCalculoNet = "Calcular net \n";
            int fin = capaOculta.length;
            //Calculos el net y F(net) de los perceptrones de capa oculta
            for (int i = 0; i < fin; i++) {
                //Calculo el valor de net de acuerdo a los pesos y entradas de las conexiones
                capaOculta[i].calcularNet();
                //Obtengo la salida del perceptron haciendo F(net), de acuerdo al tipo de funcion que corresponda
                capaOculta[i].calcularSalida();
                procesoCalculoNet = procesoCalculoNet + "P"+i+": "+capaOculta[i].getNet()+"\n";
            }
            //Calculo el net de los perceptrones de capa de salida
            for (int i = 0; i < capaSalida.length; i++) {
                capaSalida[i].calcularNet();
                procesoCalculoNet = procesoCalculoNet + "S"+i+": "+capaOculta[i].getNet()+"\n";
            }
            salidaProceso.addProceso(procesoCalculoNet);
            
            //Este metodo obtiene la salida de la red, y la guarda en la lista "salida" (una por cada neurona de salida)
            salida = calcularSalida(capaSalida);
            
            //Aca calculo el error total de la red
            //ErrorTotal = Sumatoria(1/2)*(salida esperada - salidaReal)^2
            error = 0;
            for (int i = 0; i < salida.length; i++) {
                error = error + (Math.pow((salidaEsperada[i] - salida[i]),2));            
            }
            error = error/2;
            
            //Aca los que hago es el back propagation: Nuestro objetivo  es actualizar cada 
            //uno de los pesos en la red de modo que causen que la salida real sea más cercana la salida  
            //esperada, minimizando así el error para cada neurona de salida y la red en conjunto.
            
            //Lo que hacemos en calcular el error cometido por cada perceptron de adelante para atras
            for (int i = 0; i < salidaEsperada.length; i++) {
                //Seteo el error coetido para cada perceptron de salida
                double errorCometido =  (salidaEsperada[i] - capaSalida[i].getSalida())* TipoFuncion.getDerivadaTipoFuncion(capaSalida[i].getFuncion(), capaSalida[i].getNet());
                capaSalida[i].setError(errorCometido);
            }
            //Ahora de adelante para atras vamos calculando los errores de cada perceptron
            //de acuerdo con los errores de los perceptrones de la capa n+1
            //Todo esto sin actualizar los pesos todavia
            for (int i = (capaOculta.length-1) ; i > -1; i--) {
                Conexion[] conexSalida = capaOculta[i].getConexionesSalida();
                double errorCometido = 0;
                
                //Para capcular el error cometido por las neuronas de capa oculta se hace los siguiente
                //este error va a ser igual a la sumatoria de el peso de la conexion de salida,
                //multiplicada por el error de la capa de nivel superior con la q se conecta
                for (int j = 0; j < conexSalida.length; j++) {
                    errorCometido = errorCometido + (conexSalida[j].getPerceptronSalida().getError() * conexSalida[j].getPeso());
                }
                errorCometido = errorCometido * TipoFuncion.getDerivadaTipoFuncion(capaOculta[i].getFuncion(), capaOculta[i].getNet());
                capaOculta[i].setError(errorCometido);
            }
            
            //Ahora debo actualizar los pesos de la red, teniendo en cuenta el error cometido
            for (int i = 0; i < capaSalida.length; i++) {
                capaSalida[i].actualizarPeso();
            }
            for (int i = capaOculta.length-1; i > -1; i--) {
                capaOculta[i].actualizarPeso();
            }
            
            errorPatron = errorPatron + error;
//            System.out.println(error);
            
            salidaProceso.addProceso("Salida esperada: " + Arrays.toString(salidaEsperada));
            salidaProceso.addProceso("Salida obtenida: " + Arrays.toString(salida));
            salidaProceso.addProceso("Error cometido: " + error);
            
//            if (errorPatronAnterior < errorPatron) {
//                break;
//            }
            
            if (error < errorAceptable) {
                epoca++;
            } else {
                epoca = 0;
            }
            ciclo++;
            if (ciclo == numEntradas) {
                salidaProceso.addError(errorPatron);    //No debo dividir el errorPatron por la cantidad de sumandos ene?
//                System.out.println("EP: "+errorPatron);
                ciclo = 0;
//                errorPatronAnterior = errorPatron;
                errorPatron = 0;
            }
        }
        salidaProceso.addProceso("Aprendizaje completado!");
        return salidaProceso;
    }
    
    public String runRedNeuronal(ArrayList<Double> entrada){
        String salidaProceso = "";
        double[] salidaObtenida;
        salidaProceso = salidaProceso + "valor entrada:" + entrada + "\n";
//        System.out.println("valor entrada:" + Arrays.toString(entrada));
        //Cargamos el valor de prueba a la entrada de la red.
        ArrayList<Double> valorEntradasAux = entrada; 
        for (int i = 0; i < cantNeuronasCapasOcultaI; i++) {
            for (int j = 0; j < cantEntradas; j++) {
                capaOculta[i].getConexionesEntrada()[j].setValor(valorEntradasAux.get(j));
            }
        }
        //Calculamos el net y obtenemos la conexionesSalida de la red.
        salidaObtenida = calcularSalida(capaSalida);
        salidaProceso = salidaProceso + "Salida:  ";
        for (int i = 0; i < salida.length; i++) {
            salidaProceso = salidaProceso + salidaObtenida[i] + "  ";
        }
        salidaProceso = salidaProceso + "\nSalida binaria:  ";
        for (int i = 0; i < salida.length; i++) {
            salidaProceso = salidaProceso + (int) Math.rint(salidaObtenida[i]) + " ";
        }
        salidaProceso = salidaProceso + "\n";
        return salidaProceso;
    }
    
    //Nice
    private double[] calcularSalida(Perceptron[] capaSalida){
        int cantSalidas = capaSalida.length;
        double[] salidaActual = new double[cantSalidas];
        for (int i = 0; i < cantSalidas; i++) {
            salidaActual[i] = capaSalida[i].getSalida();
        }
        return salidaActual;
    }
    

    //Este metodo muestra los datos de la topologia de la red
    public String informeRed(){
        String salidaProceso = "";
        salidaProceso = salidaProceso + "Cantidad entradas: " + this.getCantEntradas() + "\n";
        salidaProceso = salidaProceso + "Cantidad capas ocultas: " + this.getCantCapasOculta() + "\n";
        salidaProceso = salidaProceso + "Cantidad de neuronas por capa: " + this.getCantNeuronasCapasOcultaI() + "\n";
        salidaProceso = salidaProceso + "Cantidad de salidas: " + this.getCantSalida() + "\n";
        salidaProceso = salidaProceso + "Valor de alfa: " + this.getAlfa() + "\n";
        salidaProceso = salidaProceso + "Error aceptable: " + this.getErrorAceptable() + "\n";
        
        return salidaProceso;
    }
    
    //Obtener las conexiones con sus valores
    public ArrayList<ArrayList<Conexion>> getConexiones(){
        ArrayList<ArrayList<Conexion>> conexionesSalida = new ArrayList<>();
        for (int i = 0; i < capaOculta.length; i++) {
            ArrayList<Conexion> conexPerceptron = new ArrayList<>();
            for (int j = 0; j < capaOculta[i].getConexionesEntrada().length; j++) {
                conexPerceptron.add(capaOculta[i].getConexionesEntrada()[j]);
            }
            conexionesSalida.add(conexPerceptron);
        }
        for (int i = 0; i < capaSalida.length; i++) {
            ArrayList<Conexion> conexPerceptron = new ArrayList<>();
            for (int j = 0; j < capaSalida[i].getConexionesEntrada().length; j++) {
                conexPerceptron.add(capaSalida[i].getConexionesEntrada()[j]);
            }
            conexionesSalida.add(conexPerceptron);
        }
        return conexionesSalida;
    }
    
    public ArrayList<Perceptron> getPerceptrones(){
        ArrayList<Perceptron> salidaPercep = new ArrayList<>();
        for (int i = 0; i < capaOculta.length; i++) {
            salidaPercep.add(capaOculta[i]);
        }
        for (int i = 0; i < capaSalida.length; i++) {
            salidaPercep.add(capaSalida[i]);
        }
        return salidaPercep;
    }
    
    public void getEstadoPerceptrones(){
        ArrayList<Perceptron> p = getPerceptrones();
        for (int i = 0; i < p.size(); i++) {
            System.out.println(p.get(i).getFuncion());
        }
    }
}
