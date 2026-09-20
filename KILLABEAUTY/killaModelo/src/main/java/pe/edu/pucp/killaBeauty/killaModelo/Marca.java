package pe.edu.pucp.killaBeauty.killaModelo;

public class Marca {
    private int id;
    private String descripcion;
    private Pais pais;
    private Boolean activo;

    public Marca() {
    }

    public Marca(int id, String descripcion, Pais pais, Boolean activo) {
        this.id = id;
        this.descripcion = descripcion;
        this.pais = pais;
        this.activo = activo;
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

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
