package pe.edu.pucp.killaBeauty.killaModelo.Promocionales;

public enum TipoDescuento {
    PORCENTAJE(1, "Porcentaje"),
    MONTO_FIJO(2, "Monto Fijo");

    private final int id;
    private final String descripcion;

    TipoDescuento(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
