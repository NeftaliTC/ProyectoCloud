package pe.edu.pucp.killaBeauty.killaModelo;

import java.util.Date;

public class Boleta extends ComprobantePago {
    private int idBoleta;
    private String dni;

    public Boleta() {
        super();
    }

    public Boleta(int idComprobante, Date fechaEmision, String serie, String numeroCorrelativo, Pago pago, TipoComprobante tipoComprobante, int idBoleta, String dni) {
        super(idComprobante, fechaEmision, serie, numeroCorrelativo, pago, tipoComprobante);
        this.idBoleta = idBoleta;
        this.dni = dni;
    }

    public int getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(int idBoleta) {
        this.idBoleta = idBoleta;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
