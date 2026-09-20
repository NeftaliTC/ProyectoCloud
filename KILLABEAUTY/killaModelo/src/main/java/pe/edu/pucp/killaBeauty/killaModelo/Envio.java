package pe.edu.pucp.killaBeauty.killaModelo;

import java.util.Date;

public class Envio {
    private int id;
    private String descripcion;
    private double costoEnvio;
    private Date fechaEnvio;
    private EstadoEnvio estadoEnvio;
    private Pedido pedido;
    private Courier courier;
    private String numeroSeguimiento;

    public Envio() {}

    public Envio(int id, String descripcion, double costoEnvio, Date fechaEnvio, EstadoEnvio estadoEnvio, Pedido pedido, Courier courier, String numeroSeguimiento) {
        this.id = id;
        this.descripcion = descripcion;
        this.costoEnvio = costoEnvio;
        this.fechaEnvio = fechaEnvio;
        this.estadoEnvio = estadoEnvio;
        this.pedido = pedido;
        this.courier = courier;
        this.numeroSeguimiento = numeroSeguimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public EstadoEnvio getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(EstadoEnvio estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public String getNumeroSeguimiento() {
        return numeroSeguimiento;
    }

    public void setNumeroSeguimiento(String numeroSeguimiento) {
        this.numeroSeguimiento = numeroSeguimiento;
    }
}
