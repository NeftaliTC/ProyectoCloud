package pe.edu.pucp.killaBeauty.killaModelo.Promocionales;

public class Campana {
    private int idCampana;
    private String nombre;
    private String descripcion;
    private boolean activo;
    public Campana() {}

    public int getIdCampana() { return idCampana; }
    public void setIdCampana(int idCampana) { this.idCampana = idCampana; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
