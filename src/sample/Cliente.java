package sample;

public class Cliente {
    private static int cont;
    private int idCliente;
    private float random_productos;
    private int cantidad_productos;
    private float llegada_cliente;
    private String estado;
    private int mostrado = 0;

    public Cliente() {
        cont++;
        this.idCliente = cont;
    }

    public void setCantidad_productos() {
        this.random_productos = (float)Math.random();
        if (random_productos < 0.25){
            this.cantidad_productos = 1;
        } else {
            if (random_productos < 0.5){
                this.cantidad_productos = 2;
            } else {
                if (random_productos < 0.75){
                    this.cantidad_productos = 3;
                } else {
                    this.cantidad_productos = 4;
                }
            }
        }
    }

    @Override
    public String toString() {
        if (this.mostrado > 1) {
            String cadena =
                    "\t\t\t\t\t<td>" + " " + "</td>\n" +
                            "\t\t\t\t\t<td>" + " " + "</td>\n" +
                            "\t\t\t\t\t<td>" + " " + "</td>\n" +
                            "\t\t\t\t\t<td>" + " " + "</td>\n";
            return cadena;
        } else {
            String cadena =
                    "\t\t\t\t\t<td>" + idCliente + "</td>\n" +
                            "\t\t\t\t\t<td>" + estado + "</td>\n" +
                            "\t\t\t\t\t<td>" + random_productos + "</td>\n" +
                            "\t\t\t\t\t<td>" + cantidad_productos + "</td>\n";
            return cadena;
        }
    }

    public static int getCont() {
        return cont;
    }

    public static void setCont(int cont) {
        Cliente.cont = cont;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getCantidad_productos() {
        return cantidad_productos;
    }

    public void setCantidad_productos(int cantidad_productos) {
        this.cantidad_productos = cantidad_productos;
    }

    public float getLlegada_cliente() {
        return llegada_cliente;
    }

    public void setLlegada_cliente(float llegada_cliente) {
        this.llegada_cliente = llegada_cliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getMostrado() {
        return mostrado;
    }

    public void setMostrado(int mostrado) {
        this.mostrado = mostrado;
    }


    public float getRandom_productos() {
        return random_productos;
    }

    public void setRandom_productos(float random_productos) {
        this.random_productos = random_productos;
    }
}
 /*   @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", inicio_atencion=" + inicio_atencion +
                ", llegada_cliente=" + llegada_cliente +
                ", fin_atencion=" + fin_atencion +
                ", fin_espera=" + fin_espera +
                ", cantidad_productos=" + cantidad_productos +
                ", estado='" + estado + '\'' +
                ", mostrado=" + mostrado +
                '}';
    }*/
