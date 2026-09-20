package pe.edu.pucp.killaBeauty.killaModelo;

public class Courier {
    private int id;
    private String nombre;
    private String ruc;
    private String telefono;
    private boolean activo;
    private boolean esAsignado;
    private String correo;

    public Courier() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isEsAsignado() {
        return esAsignado;
    }

    public void setEsAsignado(boolean esAsignado) {
        this.esAsignado = esAsignado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
