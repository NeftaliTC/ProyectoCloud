package pe.edu.pucp.killabeauty.killarest.dto;

import java.util.ArrayList;
import java.util.List;

public class PedidoCheckoutDTO {
    private Integer usuarioId;
    private Integer direccionId;
    private Integer cuponId;
    private double costoEnvio;
    private double descuentoCupon;
    private String metodoPago;
    private String tipoComprobante;
    private String dni;
    private String ruc;
    private String razonSocial;
    private String direccionFiscal;
    private List<ItemCarritoDTO> items = new ArrayList<>();

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getDireccionId() { return direccionId; }
    public void setDireccionId(Integer direccionId) { this.direccionId = direccionId; }

    public Integer getCuponId() { return cuponId; }
    public void setCuponId(Integer cuponId) { this.cuponId = cuponId; }

    public double getCostoEnvio() { return costoEnvio; }
    public void setCostoEnvio(double costoEnvio) { this.costoEnvio = costoEnvio; }

    public double getDescuentoCupon() { return descuentoCupon; }
    public void setDescuentoCupon(double descuentoCupon) { this.descuentoCupon = descuentoCupon; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getDireccionFiscal() { return direccionFiscal; }
    public void setDireccionFiscal(String direccionFiscal) { this.direccionFiscal = direccionFiscal; }

    public List<ItemCarritoDTO> getItems() { return items; }
    public void setItems(List<ItemCarritoDTO> items) { this.items = items; }

    public static class ItemCarritoDTO {
        private Integer productoId;
        private Integer cantidad;
        private double precioAplicado;

        public Integer getProductoId() { return productoId; }
        public void setProductoId(Integer productoId) { this.productoId = productoId; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public double getPrecioAplicado() { return precioAplicado; }
        public void setPrecioAplicado(double precioAplicado) { this.precioAplicado = precioAplicado; }
    }
}