package pe.edu.pucp.killaBeauty.killaModelo;

import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;

import java.util.Date;
import java.util.List;

public class Pedido {
    private int id;
    private Date fechaPedido;
    private EstadoPedido estadoPedido;
    private double subtotal;
    private double igv;
    private double total;

    private Usuario cliente;
    private Direccion direccionEnvio;
    private Cupon cupon;
    private List<DetallePedido> detalles;

    public Pedido() {
    }

    public Pedido(int id, Date fechaPedido, EstadoPedido estadoPedido, double subtotal, double igv, double total, Usuario cliente, Direccion direccionEnvio, Cupon cupon, List<DetallePedido> detalles) {
        this.id = id;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.cliente = cliente;
        this.direccionEnvio = direccionEnvio;
        this.cupon = cupon;
        this.detalles = detalles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Direccion getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(Direccion direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public Cupon getCupon() {
        return cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
}
