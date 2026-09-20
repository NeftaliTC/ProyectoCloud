package pe.edu.pucp.killaBeauty.killaModelo;

public enum Pais {
    EEUU(1, "Estados Unidos"),
    COREA(2, "Corea"),
    JAPON(3, "Japon"),
    PERU(4, "Peru"),
    CHILE(5, "Chile"),
    MEXICO(6, "Mexico"),
    COLOMBIA(7, "Colombia"),
    CHINA(8, "China");

    private final int id;
    private final String descripcion;

    Pais(int id, String descripcion) {
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
