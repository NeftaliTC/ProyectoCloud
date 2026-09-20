package pe.edu.pucp.killaBeauty.killaModelo;

// CORREGIDA
public enum TipoUsuario {
    ADMINISTRADOR(1, "ADMINISTRADOR"),
    CLIENTE(2, "CLIENTE"),
    TRABAJADOR(3, "TRABAJADOR");

    private final int id;
    private final String nombre;

    TipoUsuario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public static TipoUsuario fromId(int id) {
        for (TipoUsuario tipo : TipoUsuario.values()) {
            if (tipo.id == id) return tipo;
        }
        return null;
    }

}
