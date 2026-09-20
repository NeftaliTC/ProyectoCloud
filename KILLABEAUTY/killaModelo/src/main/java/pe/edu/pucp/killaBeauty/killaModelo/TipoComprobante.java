package pe.edu.pucp.killaBeauty.killaModelo;

public enum TipoComprobante {
    BOLETA(1, "Boleta de Venta"),
    FACTURA(2, "Factura Comercial");

    private final int id;
    private final String nombre;

    TipoComprobante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getnombre() {
        return nombre;
    }

    public static TipoComprobante getById(int id) {
        for (TipoComprobante tipo : TipoComprobante.values()) {
            if (tipo.getId() == id) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("ID de comprobante inválido: " + id);
    }
}
