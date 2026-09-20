
package pe.edu.pucp.killaBeauty.killaModelo;

public enum EstadoPedido {
    PENDIENTE(1, "PENDIENTE"),
    PAGADO(2, "PAGADO"),
    EN_PREPARACION(3, "EN_PREPARACION"),
    ENVIADO(4, "ENVIADO"),
    ENTREGADO(5, "ENTREGADO"),
    CANCELADO(6, "CANCELADO");

    private final int id;
    private final String nombre;

    EstadoPedido(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public static EstadoPedido fromId(int id) {
        for (EstadoPedido estado : EstadoPedido.values()) {
            if (estado.getId() == id) {
                return estado;
            }
        }
        throw new IllegalArgumentException("EstadoPedido inválido con id: " + id);
    }
}
