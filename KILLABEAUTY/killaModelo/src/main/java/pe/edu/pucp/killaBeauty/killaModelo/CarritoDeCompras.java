package pe.edu.pucp.killaBeauty.killaModelo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class CarritoDeCompras {
    private int id; // redundante
    private Date fechaDeCreacion;
    private EstadoCarrito estado;

    private Usuario usuario;
    private List<DetalleCarrito> detalleCarritoList;

    public CarritoDeCompras() {
    }

    public CarritoDeCompras(int id, Date fechaDeCreacion, EstadoCarrito estado, Usuario usuario, List<DetalleCarrito> detalleCarritoList) {
        this.id = id;
        this.fechaDeCreacion = fechaDeCreacion;
        this.estado = estado;
        this.usuario = usuario;
        this.detalleCarritoList = detalleCarritoList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(Date fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public EstadoCarrito getEstado() {
        return estado;
    }

    public void setEstado(EstadoCarrito estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleCarrito> getDetalleCarritoList() {
        return detalleCarritoList;
    }

    public void setDetalleCarritoList(List<DetalleCarrito> detalleCarritoList) {
        this.detalleCarritoList = detalleCarritoList;
    }
}
