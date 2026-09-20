package pe.edu.pucp.killaBeauty.killaModelo;

public class Subcategoria {
    private int id;
    private String descripcion;
    private Boolean activo;
    private Categoria categoria;

    public Subcategoria() {}

    public Subcategoria(int id, String descripcion, Boolean activo, Categoria categoria) {
        this.id = id;
        this.descripcion = descripcion;
        this.activo = activo;
        this.categoria = categoria;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
