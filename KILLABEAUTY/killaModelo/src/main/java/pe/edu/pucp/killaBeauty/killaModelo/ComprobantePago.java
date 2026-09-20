package pe.edu.pucp.killaBeauty.killaModelo;

import java.util.Date;

public class ComprobantePago {
    private int idComprobante;
    private Date fechaEmision;
    private String serie;
    private String numeroCorrelativo;
    private Pago pago;
    private TipoComprobante tipoComprobante;

    public ComprobantePago() {}

    public ComprobantePago(int idComprobante, Date fechaEmision, String serie, String numeroCorrelativo, Pago pago, TipoComprobante tipoComprobante) {
        this.idComprobante = idComprobante;
        this.fechaEmision = fechaEmision;
        this.serie = serie;
        this.numeroCorrelativo = numeroCorrelativo;
        this.pago = pago;
        this.tipoComprobante = tipoComprobante;
    }

    public int getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(int idComprobante) {
        this.idComprobante = idComprobante;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumeroCorrelativo() {
        return numeroCorrelativo;
    }

    public void setNumeroCorrelativo(String numeroCorrelativo) {
        this.numeroCorrelativo = numeroCorrelativo;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public TipoComprobante getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }
}
