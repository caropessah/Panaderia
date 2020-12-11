package sample;

import java.util.ArrayList;

public class Fila {
    private Fila filaAnterior;
    private int nroFila;
    private float reloj;
    private String evento;
    private String proximo_evento;
    private float proximo_reloj;

    private float random_llegada_cliente;
    private float tiempo_entre_llegadas;
    private float proxima_llegada_cliente;

    private float fin_atencion;
    private float random_fin_atencion;
    private float tiempo_entre_atencion;

    private int stock;

    private int productos_cocinar;
    private float tiempo_entre_productos;
    private float fin_coccion;
    private float fin_espera_horno;

    private String estado_empleado = "Libre";
    private int cola;

    private String estado_horno = "Ocupado";
    private float inicio_coccion;

    private Cliente cliente_actual;
    private Cliente proximo_cliente;

    private ArrayList<Cliente> clientes;

    //Estadisticas
    private int contador_clientes_no_atendidos;
    private int contador_clientes_atendidos;
    private float acumulador_tiempo_horno_trabajando;
    private float porcentaje_clientes_no_atendidos;
    private float acumulador_ocupacion_empleado;
    private float acumulador_esperando_producto_empleado;
    private int contador_clientes_cola;
    private float porcentaje_clientes_atendidos;
    private float porcentaje_ac_empleado_ocupado;
    private float porcentaje_ac_empleado_esperando_producto;
    private float porcentaje_ac_horno_ocupado;

    public Fila(){
        this.proximo_reloj = 0;
        this.proximo_evento = "Llegada cliente";
        this.proxima_llegada_cliente = 0;
        this.nroFila = -1;
        this.cola = 0;
        this.clientes = new ArrayList<>();
        this.stock = 0;
        this.contador_clientes_atendidos = 0;
        this.contador_clientes_no_atendidos = 0;
        this.acumulador_ocupacion_empleado = 0;
        this.acumulador_esperando_producto_empleado = 0;
        this.acumulador_tiempo_horno_trabajando = 0;
        this.porcentaje_clientes_atendidos = 0;
        this.porcentaje_clientes_no_atendidos = 0;
        this.porcentaje_ac_empleado_esperando_producto = 0;
        this.porcentaje_ac_empleado_ocupado = 0;
        this.porcentaje_ac_horno_ocupado = 0;
        this.contador_clientes_cola = 0;

    }

    public Fila(Fila filaAnterior){
        this.nroFila = filaAnterior.getNroFila() + 1;
        this.filaAnterior = filaAnterior;
        this.cola = filaAnterior.getCola();
        this.evento = filaAnterior.proximo_evento;
        this.reloj  = filaAnterior.proximo_reloj;
        this.estado_empleado = filaAnterior.estado_empleado;
        this.proxima_llegada_cliente = filaAnterior.proxima_llegada_cliente;
        this.cliente_actual = filaAnterior.proximo_cliente;
        this.clientes = filaAnterior.clientes;
        this.fin_atencion = filaAnterior.getFin_atencion();
        this.fin_coccion = filaAnterior.fin_coccion;
        this.fin_espera_horno = filaAnterior.fin_espera_horno;
        this.inicio_coccion = filaAnterior.inicio_coccion;
        this.stock = filaAnterior.stock;
        this.estado_horno = filaAnterior.estado_horno;
        this.productos_cocinar = filaAnterior.productos_cocinar;
        this.tiempo_entre_productos = filaAnterior.getTiempo_entre_productos();
        this.contador_clientes_no_atendidos = filaAnterior.getContador_clientes_no_atendidos();
        this.contador_clientes_atendidos = filaAnterior.getContador_clientes_atendidos();
        this.porcentaje_clientes_no_atendidos = filaAnterior.getPorcentaje_clientes_no_atendidos();
        this.porcentaje_clientes_atendidos = filaAnterior.getPorcentaje_clientes_atendidos();
        this.acumulador_tiempo_horno_trabajando = filaAnterior.getAcumulador_tiempo_horno_trabajando();
        this.acumulador_ocupacion_empleado = filaAnterior.getAcumulador_ocupacion_empleado();
        this.acumulador_esperando_producto_empleado = filaAnterior.getAcumulador_esperando_producto_empleado();
        this.porcentaje_ac_empleado_ocupado = filaAnterior.getPorcentaje_ac_empleado_ocupado();
        this.porcentaje_ac_empleado_esperando_producto = filaAnterior.getPorcentaje_ac_empleado_esperando_producto();
        this.porcentaje_ac_horno_ocupado = filaAnterior.getPorcentaje_ac_horno_ocupado();
        this.contador_clientes_cola = filaAnterior.getContador_clientes_cola();

        if(nroFila == 0){
        this.evento = "Inicializacion";
        generarFila(evento);
        } else{
        generarFila(filaAnterior.proximo_evento);
        }
    }

    private void generarFila(String eventoFila) {
        switch (eventoFila) {
            case "Inicializacion":
                generarProximaLlegadaCliente();
                if (stock == 0){
                    generarProximoFinCoccion();
                } else {
                    generarFinEsperaHorno();
                }
                this.proximo_evento = "Llegada cliente";
                this.proximo_reloj = proxima_llegada_cliente;
                break;

            case "Llegada cliente":
                this.tiempo_entre_productos = 0;
                generarProximaLlegadaCliente();
                asignacionCliente();
                calcularOcupacion();
                revisarStock();
                calcularEstadisticas(" ");
                elegirProximoEvento();
                break;

            case "Fin espera horno":
                this.tiempo_entre_productos = 0;
                limpiarFinEsperaHorno();
                generarProximoFinCoccion();
                calcularOcupacion();
                revisarStock();
                calcularEstadisticas(" ");
                elegirProximoEvento();
                break;

            case "Fin coccion":
                this.tiempo_entre_productos = 0;
                limpiarFinCoccion();
                revisarColaClientes();
                revisarEsperandoProducto();
                generarFinEsperaHorno();
                calcularOcupacion();
                revisarStock();
                calcularEstadisticas(" ");
                elegirProximoEvento();
                break;

            case "Fin atencion":
                this.fin_atencion = 0;
                this.tiempo_entre_productos = 0;
                actualizarStock();
                destruirCliente();
                revisarColaClientes();
                calcularOcupacion();
                compararStockHorno();
                revisarStock();
                calcularEstadisticas("Fin atencion");
                elegirProximoEvento();
                break;
        }
        noMostrarDestruidos();
    }


    private void compararStockHorno() {
        if (stock == 0 && estado_horno.equals("Libre")){
            this.fin_espera_horno = 0;
            this.fin_coccion = reloj + 68;
            this.productos_cocinar = 45;
            this.tiempo_entre_productos = 68;
            this.estado_horno = "Cocinando";
            this.estado_empleado = "Esperando Producto";
        }
    }

    private void revisarEsperandoProducto() {
        for (Cliente cliente : clientes){
            if (cliente.getEstado().equals("Esperando Producto")){
                cliente.setEstado("Esperando atencion");
            }
        }
    }

    private void limpiarFinEsperaHorno() {
        this.fin_espera_horno = 0;
        this.estado_horno = "Cocinando";
    }

    //Si mientras un cliente esta esperando atencion el stock se queda en 0, entonces se destruye. Este metodo iria en todos los case salvo en inicializacion
    private void revisarStock(){
        if (stock == 0){
            for (Cliente cliente : clientes){
                if ((cliente.getEstado().equals("Esperando atencion") || cliente.getEstado().equals("Esperando Producto") || cliente.getEstado().equals("Siendo atendido")) && fin_coccion>reloj+5) {
                    if (cliente.getEstado().equals("Esperando atencion")) { cliente.setCantidad_productos(0);}
                    cliente.setEstado("Destruido");
                    cola--;
                    contador_clientes_no_atendidos++;
                }
                if (cliente.getEstado().equals("Siendo atendido") && fin_coccion<reloj+5){
                    cliente.setEstado("Esperando Producto");
                }
            }
        }
    }

    private void calcularOcupacion() {
        if (this.filaAnterior.getEstado_empleado().equals("Ocupado"))
        {
            this.acumulador_ocupacion_empleado = acumulador_ocupacion_empleado + (reloj - filaAnterior.getReloj());
        }
        if (this.filaAnterior.getEstado_empleado().equals("Esperando Producto"))
        {
            this.acumulador_esperando_producto_empleado = acumulador_esperando_producto_empleado + (reloj - filaAnterior.getReloj());
        }
        if (this.filaAnterior.getEstado_horno().equals("Cocinando"))
        {
            this.acumulador_tiempo_horno_trabajando = acumulador_tiempo_horno_trabajando + (reloj - filaAnterior.getReloj());
        }
    }

    private void calcularEstadisticas(String evento) {
        if (evento.equals("Fin atencion"))
        {
            this.contador_clientes_atendidos++;
        }
        if ((contador_clientes_no_atendidos+contador_clientes_atendidos) != 0)
        {
            this.porcentaje_clientes_atendidos = ((float)contador_clientes_atendidos / (contador_clientes_atendidos+contador_clientes_no_atendidos)) * 100;
            this.porcentaje_clientes_no_atendidos = ((float)contador_clientes_no_atendidos / (contador_clientes_no_atendidos+contador_clientes_atendidos)) * 100;
        }
        if (contador_clientes_cola < filaAnterior.getCola()){
            contador_clientes_cola = filaAnterior.getCola();
        }
        this.porcentaje_ac_empleado_esperando_producto = (acumulador_esperando_producto_empleado / reloj * 100);
        this.porcentaje_ac_empleado_ocupado = (acumulador_ocupacion_empleado / reloj * 100);
        this.porcentaje_ac_horno_ocupado = (acumulador_tiempo_horno_trabajando / reloj * 100);
    }

    private void destruirCliente() {
        for (Cliente cliente : clientes){
            if (cliente.getEstado().equals("Siendo atendido")){
                cliente.setEstado("Destruido");
            }
        }
    }

   private void revisarColaClientes() {
        if (this.cola > 0 && stock!=0){
            for (Cliente cliente : this.clientes) {
                if (cliente.getEstado().equals("Esperando Producto")) {
                    cliente.setEstado("Siendo atendido");
                    this.cola--;
                    calcularFinAtencion(reloj);
                    this.estado_empleado = "Ocupado";
                    return;
                } else {
                    if (cliente.getEstado().equals("Siendo atendido")){ return; }
                    if (cliente.getEstado().equals("Esperando atencion")) {
                        cliente.setEstado("Siendo atendido");
                        this.cola--;
                        calcularFinAtencion(reloj);
                        this.estado_empleado = "Ocupado";
                        return;
                        }
                    }
                }
             }

        if (stock == 0 && estado_horno.equals("Cocinando")){
        this.estado_empleado = "Esperando Producto";}
        else {
            if (stock!=0 && estado_empleado.equals("Ocupado") && fin_atencion>reloj)
            {
                this.estado_empleado = "Ocupado";
            } else {
                this.estado_empleado = "Libre";
            }
        }
    }

    private void limpiarFinCoccion() {
        this.stock = this.stock + productos_cocinar;
        this.fin_coccion = 0;
        this.productos_cocinar = 0;
        this.tiempo_entre_productos = 0;
    }

    private void generarFinEsperaHorno() {
        this.fin_espera_horno = reloj + 35;
        this.estado_horno = "Libre";
    }

    private void generarProximoFinCoccion() {
        if (stock == 0){
            this.fin_coccion = reloj + 68;
            this.productos_cocinar = 45;
            this.tiempo_entre_productos = 68;
            this.estado_empleado = "Esperando Producto";
            this.estado_horno = "Cocinando";
        } else {
            this.fin_coccion = reloj + 41;
            this.productos_cocinar = 30;
            this.tiempo_entre_productos = 41;
            this.estado_horno = "Cocinando";
        }
    }

    private void actualizarStock() {
        for (Cliente cliente : this.clientes) {
            if (cliente.getEstado().equals("Siendo atendido") && stock!=0) {
                if ((stock - cliente.getCantidad_productos()) >= 0){
                    this.stock = stock - cliente.getCantidad_productos();
                } else {
                    cliente.setCantidad_productos(stock);
                    this.stock = 0;
                }
            }
        }
    }

    private void calcularFinAtencion (float fin_atencion){
        this.random_fin_atencion = (float)Math.random();
        this.tiempo_entre_atencion = (float)(0.5 + random_fin_atencion*1);
        this.fin_atencion = fin_atencion + tiempo_entre_atencion;
    }


    private void noMostrarDestruidos() {
        for (Cliente cliente : this.clientes) {
            if (cliente.getEstado().equals("Destruido")){
                cliente.setMostrado(cliente.getMostrado() + 1);
            }
        }
    }

    private void elegirProximoEvento() {
        proximo_evento = "Llegada cliente";
        proximo_reloj = proxima_llegada_cliente;
        if (fin_coccion <= proximo_reloj && fin_coccion>reloj){
            proximo_evento = "Fin coccion";
            proximo_reloj = fin_coccion;
        }
        if(fin_atencion <= proximo_reloj && fin_atencion>reloj){
            proximo_evento = "Fin atencion";
            proximo_reloj = fin_atencion;
        }
        if(inicio_coccion <= proximo_reloj && inicio_coccion>reloj){
            proximo_evento = "Inicio coccion";
            proximo_reloj = inicio_coccion;
        }
        if(fin_espera_horno <= proximo_reloj && fin_espera_horno>reloj)
        {
            proximo_evento = "Fin espera horno";
            proximo_reloj = fin_espera_horno;
        }
    }

    private void asignacionCliente() {
        if (estado_empleado.equals("Libre"))
        {
            if (stock != 0) {
                Cliente cliente = new Cliente();
                cliente.setEstado("Siendo atendido");
                this.estado_empleado = "Ocupado";
                //this.cola = filaAnterior.cola;
                cliente.setCantidad_productos();
                calcularFinAtencion(reloj);
                clientes.add(cliente);
            } else {
                if (fin_coccion > reloj+5) {
                    Cliente cliente = new Cliente();
                    cliente.setEstado("Destruido");
                    contador_clientes_no_atendidos++;
                    this.estado_empleado = "Esperando Producto";
                    clientes.add(cliente);
                } else {
                    Cliente cliente = new Cliente();
                    cliente.setEstado("Esperando Producto");
                    this.estado_empleado = "Esperando Producto";
                    clientes.add(cliente);
                    cliente.setCantidad_productos();
                    //cliente.setLlegada_cliente(reloj);
                    cola++;
                }
            }
        } else {
            if (estado_empleado.equals("Ocupado") && stock != 0) {
                Cliente cliente = new Cliente();
                cliente.setEstado("Esperando atencion");
                cliente.setCantidad_productos();
                //cliente.setLlegada_cliente(reloj);
                clientes.add(cliente);
                cola++;
            } else {
                if (estado_empleado.equals("Ocupado") && stock == 0) {
                    if (fin_coccion > reloj + 5) {
                        Cliente cliente = new Cliente();
                        cliente.setEstado("Destruido");
                        contador_clientes_no_atendidos++;
                        clientes.add(cliente);
                    } else {
                        Cliente cliente = new Cliente();
                        cliente.setEstado("Esperando Producto");
                        cliente.setCantidad_productos();
                        clientes.add(cliente);
                        cola++;
                    }
                } else {
                    if (estado_empleado.equals("Esperando Producto")) {
                        if (fin_coccion > reloj + 5) {
                            Cliente cliente = new Cliente();
                            cliente.setEstado("Destruido");
                            contador_clientes_no_atendidos++;
                            //cliente.setLlegada_cliente(reloj);
                            clientes.add(cliente);
                        } else {
                            Cliente cliente = new Cliente();
                            cliente.setEstado("Esperando Producto");
                            cliente.setCantidad_productos();
                            clientes.add(cliente);
                            //cliente.setLlegada_cliente(reloj);
                            cola++;
                        }
                    }
                }
            }
        }
    }



    private void generarProximaLlegadaCliente() {
        // A partir de un RND se genera la proxima llegada del cliente
        this.random_llegada_cliente = (float) Math.random();
        this.tiempo_entre_llegadas = (float) ((-3) * Math.log(1 - random_llegada_cliente));
        this.proxima_llegada_cliente = tiempo_entre_llegadas + reloj;
    }


    @Override
    public String toString() {
        return String.format("|%-8s", nroFila) + clientes.toString() + "\n";
    }

    public String toString2() {
        String cadena = "";
        cadena = "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td>" + nroFila + "</td>\n" +
                "\t\t\t\t\t<td>" + evento + "</td>\n" +
                "\t\t\t\t\t<td>" + reloj + "</td>\n" +
                "\t\t\t\t\t<td>" + random_llegada_cliente + "</td>\n" +
                "\t\t\t\t\t<td>" + tiempo_entre_llegadas + "</td>\n" +
                "\t\t\t\t\t<td class='colorEvento'>" + proxima_llegada_cliente + "</td>\n" +
                "\t\t\t\t\t<td>" + stock + "</td>\n" +
                "\t\t\t\t\t<td>" + productos_cocinar + "</td>\n" +
                "\t\t\t\t\t<td>" + tiempo_entre_productos + "</td>\n" +
                "\t\t\t\t\t<td class='colorEvento'>" + fin_coccion + "</td>\n" +
                "\t\t\t\t\t<td class='colorEvento'>" + fin_espera_horno + "</td>\n" +
                "\t\t\t\t\t<td>" + random_fin_atencion + "</td>\n" +
                "\t\t\t\t\t<td>" + tiempo_entre_atencion + "</td>\n" +
                "\t\t\t\t\t<td class='colorEvento'>" + fin_atencion + "</td>\n" +
                "\t\t\t\t\t<td>" + estado_empleado + "</td>\n" +
                "\t\t\t\t\t<td>" + cola + "</td>\n" +
                "\t\t\t\t\t<td>" + estado_horno + "</td>\n" +
                "\t\t\t\t\t<td>" + contador_clientes_atendidos + "</td>\n" +
                "\t\t\t\t\t<td>" + contador_clientes_no_atendidos + "</td>\n" +
                "\t\t\t\t\t<td>" + porcentaje_clientes_atendidos + "</td>\n" +
                "\t\t\t\t\t<td>" + porcentaje_clientes_no_atendidos + "</td>\n" +
                "\t\t\t\t\t<td>" + acumulador_esperando_producto_empleado + "</td>\n" +
                "\t\t\t\t\t<td>" + acumulador_ocupacion_empleado + "</td>\n" +
                "\t\t\t\t\t<td>" + acumulador_tiempo_horno_trabajando + "</td>\n" +
                "\t\t\t\t\t<td>" + porcentaje_ac_empleado_esperando_producto + "</td>\n" +
                "\t\t\t\t\t<td>" + porcentaje_ac_empleado_ocupado + "</td>\n" +
                "\t\t\t\t\t<td>" + porcentaje_ac_horno_ocupado + "</td>\n" +
                "\t\t\t\t\t<td>" + contador_clientes_cola + "</td>\n" +
                toStringClientes() + "\n" +
                "\t\t\t\t</tr>\n";
        return cadena;
    }

    public String toString3(){
        String cadena = "\t\t\t<tfoot>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td>" + nroFila + "</td>\n" +
                "\t\t\t\t\t<td>" + evento + "</td>\n" +
                "\t\t\t\t\t<td>" + reloj + "</td>\n" +
                "\t\t\t\t\t<td>" + random_llegada_cliente + "</td>\n" +
                "\t\t\t\t\t<td>" + tiempo_entre_llegadas + "</td>\n" +
                "\t\t\t\t\t<td class='colorEvento'>" + proxima_llegada_cliente + "</td>\n" +
                "\t\t\t\t\t<td>" + stock + "</td>\n" +
                "\t\t\t\t\t<td>" + productos_cocinar + "</td>\n" +
                "\t\t\t\t\t<td>" + tiempo_entre_productos + "</td>\n" +
                "\t\t\t\t\t<td class='colorEvento'>" + fin_coccion + "</td>\n" +
                "\t\t\t\t\t<td class='colorEvento'>" + fin_espera_horno + "</td>\n" +
                "\t\t\t\t\t<td>" + random_fin_atencion + "</td>\n" +
                "\t\t\t\t\t<td>" + tiempo_entre_atencion + "</td>\n" +
                "\t\t\t\t\t<td class='colorEvento'>" + fin_atencion + "</td>\n" +
                "\t\t\t\t\t<td>" + estado_empleado + "</td>\n" +
                "\t\t\t\t\t<td>" + cola + "</td>\n" +
                "\t\t\t\t\t<td>" + estado_horno + "</td>\n" +
                "\t\t\t\t\t<td>" + contador_clientes_atendidos + "</td>\n" +
                "\t\t\t\t\t<td>" + contador_clientes_no_atendidos + "</td>\n" +
                "\t\t\t\t\t<td>" + porcentaje_clientes_atendidos + "</td>\n" +
                "\t\t\t\t\t<td>" + porcentaje_clientes_no_atendidos + "</td>\n" +
                "\t\t\t\t\t<td>" + acumulador_esperando_producto_empleado + "</td>\n" +
                "\t\t\t\t\t<td>" + acumulador_ocupacion_empleado + "</td>\n" +
                "\t\t\t\t\t<td>" + acumulador_tiempo_horno_trabajando + "</td>\n" +
                "\t\t\t\t\t<td>" + porcentaje_ac_empleado_esperando_producto + "</td>\n" +
                "\t\t\t\t\t<td>" + porcentaje_ac_empleado_ocupado + "</td>\n" +
                "\t\t\t\t\t<td>" + porcentaje_ac_horno_ocupado + "</td>\n" +
                "\t\t\t\t\t<td>" + contador_clientes_cola + "</td>\n" +
                toStringClientes() + "\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</tfoot>\n";
        return cadena;
    }

    private String toStringClientes(){
        String cadena = "";
        for (Cliente cliente: clientes) {
            cadena = cadena + cliente.toString();
        }
        return cadena;
    }
    public Fila getFilaAnterior() {
        return filaAnterior;
    }

    public void setFilaAnterior(Fila filaAnterior) {
        this.filaAnterior = filaAnterior;
    }

    public int getNroFila() {
        return nroFila;
    }

    public void setNroFila(int nroFila) {
        this.nroFila = nroFila;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getProximo_evento() {
        return proximo_evento;
    }

    public void setProximo_evento(String proximo_evento) {
        this.proximo_evento = proximo_evento;
    }

    public float getReloj() {
        return reloj;
    }

    public void setReloj(float reloj) {
        this.reloj = reloj;
    }

    public float getProximo_reloj() {
        return proximo_reloj;
    }

    public void setProximo_reloj(float proximo_reloj) {
        this.proximo_reloj = proximo_reloj;
    }

    public float getRandom_llegada_cliente() {
        return random_llegada_cliente;
    }

    public void setRandom_llegada_cliente(float random_llegada_cliente) {
        this.random_llegada_cliente = random_llegada_cliente;
    }

    public float getTiempo_entre_llegadas() {
        return tiempo_entre_llegadas;
    }

    public void setTiempo_entre_llegadas(float tiempo_entre_llegadas) {
        this.tiempo_entre_llegadas = tiempo_entre_llegadas;
    }

    public float getProxima_llegada_cliente() {
        return proxima_llegada_cliente;
    }

    public void setProxima_llegada_cliente(float proxima_llegada_cliente) {
        this.proxima_llegada_cliente = proxima_llegada_cliente;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getProductos_cocinar() {
        return productos_cocinar;
    }

    public void setProductos_cocinar(int productos_cocinar) {
        this.productos_cocinar = productos_cocinar;
    }

    public float getTiempo_entre_productos() {
        return tiempo_entre_productos;
    }

    public void setTiempo_entre_productos(float tiempo_entre_productos) {
        this.tiempo_entre_productos = tiempo_entre_productos;
    }

    public float getFin_coccion() {
        return fin_coccion;
    }

    public void setFin_coccion(float fin_coccion) {
        this.fin_coccion = fin_coccion;
    }

    public String getEstado_empleado() {
        return estado_empleado;
    }

    public void setEstado_empleado(String estado_empleado) {
        this.estado_empleado = estado_empleado;
    }

    public int getCola() {
        return cola;
    }

    public void setCola(int cola) {
        this.cola = cola;
    }

    public String getEstado_horno() {
        return estado_horno;
    }

    public void setEstado_horno(String estado_horno) {
        this.estado_horno = estado_horno;
    }

    public float getInicio_coccion() {
        return inicio_coccion;
    }

    public void setInicio_coccion(float inicio_coccion) {
        this.inicio_coccion = inicio_coccion;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Cliente getCliente_actual() {
        return cliente_actual;
    }

    public void setCliente_actual(Cliente cliente_actual) {
        this.cliente_actual = cliente_actual;
    }

    public Cliente getProximo_cliente() {
        return proximo_cliente;
    }

    public void setProximo_cliente(Cliente proximo_cliente) {
        this.proximo_cliente = proximo_cliente;
    }

    public float getFin_atencion() {
        return fin_atencion;
    }

    public void setFin_atencion(float fin_atencion) {
        this.fin_atencion = fin_atencion;
    }

    public float getRandom_fin_atencion() {
        return random_fin_atencion;
    }

    public void setRandom_fin_atencion(float random_fin_atencion) {
        this.random_fin_atencion = random_fin_atencion;
    }

    public float getTiempo_entre_atencion() {
        return tiempo_entre_atencion;
    }

    public void setTiempo_entre_atencion(float tiempo_entre_atencion) {
        this.tiempo_entre_atencion = tiempo_entre_atencion;
    }

    public float getFin_espera_horno() {
        return fin_espera_horno;
    }

    public void setFin_espera_horno(float fin_espera_horno) {
        this.fin_espera_horno = fin_espera_horno;
    }

    public int getContador_clientes_no_atendidos() {
        return contador_clientes_no_atendidos;
    }

    public void setContador_clientes_no_atendidos(int contador_clientes_no_atendidos) {
        this.contador_clientes_no_atendidos = contador_clientes_no_atendidos;
    }

    public int getContador_clientes_atendidos() {
        return contador_clientes_atendidos;
    }

    public void setContador_clientes_atendidos(int contador_clientes_atendidos) {
        this.contador_clientes_atendidos = contador_clientes_atendidos;
    }

    public float getAcumulador_tiempo_horno_trabajando() {
        return acumulador_tiempo_horno_trabajando;
    }

    public void setAcumulador_tiempo_horno_trabajando(float acumulador_tiempo_horno_trabajando) {
        this.acumulador_tiempo_horno_trabajando = acumulador_tiempo_horno_trabajando;
    }

    public float getPorcentaje_clientes_no_atendidos() {
        return porcentaje_clientes_no_atendidos;
    }

    public void setPorcentaje_clientes_no_atendidos(float porcentaje_clientes_no_atendidos) {
        this.porcentaje_clientes_no_atendidos = porcentaje_clientes_no_atendidos;
    }

    public float getAcumulador_ocupacion_empleado() {
        return acumulador_ocupacion_empleado;
    }

    public void setAcumulador_ocupacion_empleado(float acumulador_ocupacion_empleado) {
        this.acumulador_ocupacion_empleado = acumulador_ocupacion_empleado;
    }

    public float getPorcentaje_clientes_atendidos() {
        return porcentaje_clientes_atendidos;
    }

    public void setPorcentaje_clientes_atendidos(float porcentaje_clientes_atendidos) {
        this.porcentaje_clientes_atendidos = porcentaje_clientes_atendidos;
    }

    public float getAcumulador_esperando_producto_empleado() {
        return acumulador_esperando_producto_empleado;
    }

    public void setAcumulador_esperando_producto_empleado(float acumulador_esperando_producto_empleado) {
        this.acumulador_esperando_producto_empleado = acumulador_esperando_producto_empleado;
    }

    public float getPorcentaje_ac_empleado_ocupado() {
        return porcentaje_ac_empleado_ocupado;
    }

    public void setPorcentaje_ac_empleado_ocupado(float porcentaje_ac_empleado_ocupado) {
        this.porcentaje_ac_empleado_ocupado = porcentaje_ac_empleado_ocupado;
    }

    public float getPorcentaje_ac_empleado_esperando_producto() {
        return porcentaje_ac_empleado_esperando_producto;
    }

    public void setPorcentaje_ac_empleado_esperando_producto(float porcentaje_ac_empleado_esperando_producto) {
        this.porcentaje_ac_empleado_esperando_producto = porcentaje_ac_empleado_esperando_producto;
    }

    public float getPorcentaje_ac_horno_ocupado() {
        return porcentaje_ac_horno_ocupado;
    }

    public void setPorcentaje_ac_horno_ocupado(float porcentaje_ac_horno_ocupado) {
        this.porcentaje_ac_horno_ocupado = porcentaje_ac_horno_ocupado;
    }

    public int getContador_clientes_cola() {
        return contador_clientes_cola;
    }

    public void setContador_clientes_cola(int contador_clientes_cola) {
        this.contador_clientes_cola = contador_clientes_cola;
    }
}
