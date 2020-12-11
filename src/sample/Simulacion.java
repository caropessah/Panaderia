package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class Simulacion {
    public int cantSimulaciones;
    public int contador;
    public int desde;    // MostrarDesde
    public int hasta;    // MostrarHasta
    private Fila fila;
    private ArrayList<Fila> filasMostrar;
    private Fila ultimaFila;
    private String html;

    public Simulacion(int cantSimulaciones, int contador, int desde, int cantFilas) throws IOException {
        this.cantSimulaciones = cantSimulaciones;
        this.contador = contador;
        this.desde = desde;
        this.hasta = cantFilas;
        this.filasMostrar = new ArrayList<Fila>();
        this.html = "";
        generarSimulacion();
    }

        //OJO AGREGAR LO QUE FALTAAA PARA EL HTML!!!!
    public void generarSimulacion() throws IOException {
        for (int i = 0; i < cantSimulaciones; i++) {
            Fila filaAnterior;
            if (i == 0){
                filaAnterior = new Fila();
                this.fila = new Fila(filaAnterior);

            } else {
                filaAnterior = fila;
                this.fila = new Fila(filaAnterior);

            }
            if (i >= desde && i <= hasta){
                filasMostrar.add(fila);
                this.html = html + fila.toString2();

            }

            if (i == cantSimulaciones - 1){
                this.ultimaFila = fila;

            }
        }
        this.html = html + ultimaFila.toString3();
        crearHTML();
    }

    public void crearHTML() throws IOException {
        Writer wr2 = new FileWriter("src/sample/prueba.html");
        String encabezados_clientes = "";
        for (int i = 0; i < ultimaFila.getClientes().size(); i++) {
            encabezados_clientes = encabezados_clientes + htmlClientes();
        }
        wr2.write(html1(encabezados_clientes));
        wr2.write(html);
        wr2.flush();
        wr2.close();
    }

    public String html1(String encabezados_clientes){
        String cadena = "<html>\n" +
                "\t<head>\n" +
                "\t\t<title>Panaderia</title>\n" +
                "\t\t <link href=\"Style.css\" type=\"text/css\" rel=\"stylesheet\" media=\"\">\n" +
                "\t\t <link rel=\"shortcut icon\" href=\"bread.png\">" +
                "\t</head>\n" +
                "\t\n" +
                "\t<body>\n" +
                "\n" +
                "\t\t<table class=\"tabla71\">\n" +
                "\t\t\t<thead>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<th>Nro Fila</th>\n" +
                "\t\t\t\t\t<th>Evento</th>\n" +
                "\t\t\t\t\t<th>Reloj</th>\n" +
                "\t\t\t\t\t<th>RND llegada cliente</th>\n" +
                "\t\t\t\t\t<th>Tiempo entre llegadas</th>\n" +
                "\t\t\t\t\t<th class='colorEvento'>Proxima llegada cliente</th>\n" +
                "\t\t\t\t\t<th>Stock</th>\n" +
                "\t\t\t\t\t<th>Productos a cocinar</th>\n" +
                "\t\t\t\t\t<th>Tiempo entre productos</th>\n" +
                "\t\t\t\t\t<th class='colorEvento'>Fin coccion</th>\n" +
                "\t\t\t\t\t<th class='colorEvento'>Fin espera horno</th>\n" +
                "\t\t\t\t\t<th>RND Fin atencion</th>\n" +
                "\t\t\t\t\t<th>Tiempo entre atencion</th>\n" +
                "\t\t\t\t\t<th class='colorEvento'>Fin atencion</th>\n" +
                "\t\t\t\t\t<th>Estado Empleado</th>\n" +
                "\t\t\t\t\t<th>Cola</th>\n" +
                "\t\t\t\t\t<th>Estado Horno</th>\n" +
                "\t\t\t\t\t<th>Contador clientes atendidos</th>\n" +
                "\t\t\t\t\t<th>Contador clientes no atendidos</th>\n" +
                "\t\t\t\t\t<th>% Clientes Atendidos</th>\n" +
                "\t\t\t\t\t<th>% Clientes NO atendidos</th>\n" +
                "\t\t\t\t\t<th>AC Tiempo esperando prod empleado</th>\n" +
                "\t\t\t\t\t<th>AC Tiempo ocupado empleado</th>\n" +
                "\t\t\t\t\t<th>AC Tiempo ocupado horno</th>\n" +
                "\t\t\t\t\t<th>% Tiempo esperando prod empleado</th>\n" +
                "\t\t\t\t\t<th>% Tiempo ocupado empleado</th>\n" +
                "\t\t\t\t\t<th>% Tiempo horno ocupado</th>\n" +
                "\t\t\t\t\t<th>MAX clientes cola</th>\n" +
                encabezados_clientes +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</thead>\n" +
                "\t\t\t\n" +
                "\t\t\t<tbody>\n";
        return cadena;
    }


    public String htmlClientes(){
        String cadena = "\t\t\t\t\t<th>Id Cliente</th>\n" +
                "\t\t\t\t\t<th>Estado</th>\n" +
                "\t\t\t\t\t<th>RND Productos</th>\n" +
                "\t\t\t\t\t<th>Cantidad productos</th>\n";
        return cadena;
    }
}