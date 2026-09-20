package pe.edu.pucp.killaBeauty.killaModelo;


public enum EstadoCarrito {
    ACTIVO(1, "ACTIVO"),
    CONVERTIDO(2, "CONVERTIDO"),
    ABANDONADO(3, "ABANDONADO");

    private final int id;
    private final String nombre;

    EstadoCarrito(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public static EstadoCarrito fromId(int id) {
        for (EstadoCarrito estado : EstadoCarrito.values()) {
            if (estado.getId() == id) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Id de estado carrito no válido: " + id);
    }
}