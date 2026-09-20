package pe.edu.pucp.killaBeauty.killaModelo;

import java.util.Date;

public class TokenRecuperacion {
    private int id;
    private String token;
    private Date fechaCreacion;
    private Date fechaExpiracion;
    private Boolean usado;
    private Usuario usuario;

    public TokenRecuperacion() {
    }

    public TokenRecuperacion(int id, String token, Date fechaCreacion, Date fechaExpiracion, Boolean usado, Usuario usuario) {
        this.id = id;
        this.token = token;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.usado = usado;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Boolean getUsado() {
        return usado;
    }

    public void setUsado(Boolean usado) {
        this.usado = usado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
