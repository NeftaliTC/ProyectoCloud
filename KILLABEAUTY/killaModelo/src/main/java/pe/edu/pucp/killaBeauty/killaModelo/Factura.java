package pe.edu.pucp.killaBeauty.killaModelo;

import java.util.Date;

public class Factura extends ComprobantePago {
    private int idFactura;
    private String ruc;
    private String razonSocial;
    private String direccionFiscal;

    public Factura() {
        super();
    }

    public Factura(int idComprobante, Date fechaEmision, String serie, String numeroCorrelativo, Pago pago, TipoComprobante tipoComprobante, int idFactura, String ruc, String razonSocial, String direccionFiscal) {
        super(idComprobante, fechaEmision, serie, numeroCorrelativo, pago, tipoComprobante);
        this.idFactura = idFactura;
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.direccionFiscal = direccionFiscal;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccionFiscal() {
        return direccionFiscal;
    }

    public void setDireccionFiscal(String direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
    }
}
