package pe.edu.pucp.killaBeauty.killaModelo;

public enum MetodoPago {
    YAPE_PLIN(1, "YAPE_PLIN"),
    TARJETA(2, "TARJETA");

    private final int id;
    private final String nombre;

    MetodoPago(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public static MetodoPago fromId(int id) {
        for (MetodoPago metodo : MetodoPago.values()) {
            if (metodo.getId() == id) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Método de pago inválido con id: " + id);
    }}
