package pe.edu.pucp.killaBeauty.killaModelo;

import java.util.Date;

public class Pago {
    private int idPago;
    private double montoPagado;
    private Date fechaHoraPago;
    private boolean estado;
    private Pedido pedido;
    private MetodoPago metodoPago;

    public Pago() {}

    public Pago(int idPago, double montoPagado, Date fechaHoraPago, boolean estado, Pedido pedido, MetodoPago metodoPago) {
        this.idPago = idPago;
        this.montoPagado = montoPagado;
        this.fechaHoraPago = fechaHoraPago;
        this.estado = estado;
        this.pedido = pedido;
        this.metodoPago = metodoPago;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public Date getFechaHoraPago() {
        return fechaHoraPago;
    }

    public void setFechaHoraPago(Date fechaHoraPago) {
        this.fechaHoraPago = fechaHoraPago;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }
}
