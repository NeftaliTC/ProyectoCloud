package pe.edu.pucp.killabeauty.killarest.dto;

public class DisponibilidadCuponDTO {
    private boolean disponible;
    private String motivo;

    public DisponibilidadCuponDTO(boolean disponible, String motivo) {
        this.disponible = disponible;
        this.motivo = motivo;
    }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
