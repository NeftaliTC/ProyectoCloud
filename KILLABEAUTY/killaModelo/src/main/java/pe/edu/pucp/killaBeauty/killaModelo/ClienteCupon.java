package pe.edu.pucp.killaBeauty.killaModelo;

import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;

import java.util.Date;

public class ClienteCupon {
    private int id;
    private Date fechaUso;
    private Boolean usado;
    private Pedido pedido;
    private Cupon cupon;
    private Usuario usuario;

    public ClienteCupon() {
    }

    public ClienteCupon(int id, Date fechaUso, Boolean usado, Pedido pedido, Cupon cupon, Usuario usuario) {
        this.id = id;
        this.fechaUso = fechaUso;
        this.usado = usado;
        this.pedido = pedido;
        this.cupon = cupon;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaUso() {
        return fechaUso;
    }

    public void setFechaUso(Date fechaUso) {
        this.fechaUso = fechaUso;
    }

    public Boolean getUsado() {
        return usado;
    }

    public void setUsado(Boolean usado) {
        this.usado = usado;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Cupon getCupon() {
        return cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
