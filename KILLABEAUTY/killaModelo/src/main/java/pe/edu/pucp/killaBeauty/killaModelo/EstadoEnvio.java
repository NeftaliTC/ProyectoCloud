package pe.edu.pucp.killaBeauty.killaModelo;

public enum EstadoEnvio {
    PENDIENTE(1, "PENDIENTE"),
    EN_CAMINO(2, "EN_CAMINO"),
    ENTREGADO(3, "ENTREGADO");

    private final int id;
    private final String nombre;

    EstadoEnvio(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public static EstadoEnvio fromId(int id) {
        for (EstadoEnvio estado : EstadoEnvio.values()) {
            if (estado.getId() == id) {
                return estado;
            }
        }
        throw new IllegalArgumentException("EstadoEnvio inválido con id: " + id);
    }
}