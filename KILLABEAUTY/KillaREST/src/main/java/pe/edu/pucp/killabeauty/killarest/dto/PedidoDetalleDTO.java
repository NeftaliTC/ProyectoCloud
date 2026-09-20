package pe.edu.pucp.killabeauty.killarest.dto;

import pe.edu.pucp.killaBeauty.killaModelo.*;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.TipoDescuento;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoDetalleDTO {
    private int id;
    private String fecha;
    private String estado;
    private double subtotal;
    private double igv;
    private double total;
    private int clienteId;
    private String cliente;
    private String correo;
    private String contacto;
    private int direccionId;
    private String direccion;
    private String referencia;
    private String distrito;
    private String provincia;
    private String departamento;
    private Integer cuponId;
    private double descuentoCupon;
    private String numeroSeguimiento;
    private double costoEnvio;
    private int courierId;
    private String courier;
    private String estadoEnvio;
    private String fechaEntrega;
    private boolean pagoAprobado;
    private String metodoPago;
    private String tipoComprobante;
    private String serieComprobante;
    private String numeroCorrelativo;
    private String documentoIdentidad;
    private String razonSocial;
    private List<DetalleItemDTO> productos;

    public PedidoDetalleDTO(Pedido p, Envio envio, Pago pago, ComprobantePago comprobante) {
        this.id = p.getId();
        this.fecha = p.getFechaPedido() != null
                ? new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(p.getFechaPedido())
                : "";
        this.estado = p.getEstadoPedido() != null ? p.getEstadoPedido().getNombre() : "PENDIENTE";
        this.subtotal = p.getSubtotal();
        this.igv = p.getIgv();
        this.total = p.getTotal();

        if (p.getCliente() != null) {
            this.clienteId = p.getCliente().getId();
            this.cliente = nombreCompleto(p);
            this.correo = p.getCliente().getCorreoElectronico();
            this.contacto = p.getCliente().getTelefono();
        }

        if (p.getDireccionEnvio() != null) {
            this.direccionId = p.getDireccionEnvio().getId();
            this.direccion = p.getDireccionEnvio().getDireccionDetalle();
            this.referencia = p.getDireccionEnvio().getReferencia();
            this.distrito = p.getDireccionEnvio().getDistrito();
            this.provincia = p.getDireccionEnvio().getProvincia();
            this.departamento = p.getDireccionEnvio().getDepartamento();
        }

        if (p.getCupon() != null && p.getCupon().getId() > 0) {
            this.cuponId = p.getCupon().getId();
            if (p.getCupon().getTipoDescuento() == TipoDescuento.PORCENTAJE) {
                double dscto = p.getSubtotal() * p.getCupon().getValorDescuento() / 100;
                // Respeta el maximo si existe
                if (p.getCupon().getMontoMaximoDescuento() != null && dscto > p.getCupon().getMontoMaximoDescuento()) {
                    dscto = p.getCupon().getMontoMaximoDescuento();
                }
                this.descuentoCupon = dscto;
            } else {
                this.descuentoCupon = p.getCupon().getValorDescuento();
            }
        }

        if (p.getDetalles() != null)
            this.productos = p.getDetalles().stream()
                    .map(DetalleItemDTO::new)
                    .collect(Collectors.toList());

        // Envio
        if (envio != null) {
            this.numeroSeguimiento = envio.getNumeroSeguimiento();
            this.costoEnvio = envio.getCostoEnvio();
            if (envio.getCourier() != null) {
                this.courierId = envio.getCourier().getId();
                this.courier = envio.getCourier().getNombre();
            }
            this.estadoEnvio = envio.getEstadoEnvio() != null ? envio.getEstadoEnvio().getNombre() : "";
            this.fechaEntrega = envio.getFechaEnvio() != null
                    ? new SimpleDateFormat("yyyy-MM-dd").format(envio.getFechaEnvio())
                    : "";
        }

        // Pago
        if (pago != null && pago.getMetodoPago() != null) {
            this.metodoPago = pago.getMetodoPago().getNombre();
            this.pagoAprobado = pago.isEstado();
        } else {
            this.metodoPago = "No registrado";
            this.pagoAprobado = false;
        }

        // Comprobante
        if (comprobante != null) {
            this.tipoComprobante = comprobante.getTipoComprobante() != null
                    ? comprobante.getTipoComprobante().getnombre() : "";
            this.serieComprobante = comprobante.getSerie();
            this.numeroCorrelativo = comprobante.getNumeroCorrelativo();

            if (comprobante instanceof Boleta) {
                this.documentoIdentidad = ((Boleta) comprobante).getDni();
            } else if (comprobante instanceof Factura) {
                this.documentoIdentidad = ((Factura) comprobante).getRuc();
                this.razonSocial = ((Factura) comprobante).getRazonSocial();
            }
        }
    }

    private String nombreCompleto(Pedido p) {
        StringBuilder nombre = new StringBuilder();
        appendIfPresent(nombre, p.getCliente().getNombre());
        appendIfPresent(nombre, p.getCliente().getApellidoPaterno());
        appendIfPresent(nombre, p.getCliente().getApellidoMaterno());
        return nombre.length() > 0 ? nombre.toString() : "Cliente " + p.getCliente().getId();
    }

    private void appendIfPresent(StringBuilder builder, String value) {
        if (value == null || value.isBlank()) return;
        if (builder.length() > 0) builder.append(" ");
        builder.append(value);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getIgv() { return igv; }
    public void setIgv(double igv) { this.igv = igv; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public int getDireccionId() { return direccionId; }
    public void setDireccionId(int direccionId) { this.direccionId = direccionId; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public Integer getCuponId() { return cuponId; }
    public void setCuponId(Integer cuponId) { this.cuponId = cuponId; }
    public double getDescuentoCupon() { return descuentoCupon; }
    public void setDescuentoCupon(double descuentoCupon) { this.descuentoCupon = descuentoCupon; }
    public String getNumeroSeguimiento() { return numeroSeguimiento; }
    public void setNumeroSeguimiento(String numeroSeguimiento) { this.numeroSeguimiento = numeroSeguimiento; }
    public double getCostoEnvio() { return costoEnvio; }
    public void setCostoEnvio(double costoEnvio) { this.costoEnvio = costoEnvio; }
    public int getCourierId() { return courierId; }
    public void setCourierId(int courierId) { this.courierId = courierId; }
    public String getCourier() { return courier; }
    public void setCourier(String courier) { this.courier = courier; }
    public String getEstadoEnvio() { return estadoEnvio; }
    public void setEstadoEnvio(String estadoEnvio) { this.estadoEnvio = estadoEnvio; }
    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public boolean isPagoAprobado() { return pagoAprobado; }
    public void setPagoAprobado(boolean pagoAprobado) { this.pagoAprobado = pagoAprobado; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }
    public String getSerieComprobante() { return serieComprobante; }
    public void setSerieComprobante(String serieComprobante) { this.serieComprobante = serieComprobante; }
    public String getNumeroCorrelativo() { return numeroCorrelativo; }
    public void setNumeroCorrelativo(String numeroCorrelativo) { this.numeroCorrelativo = numeroCorrelativo; }
    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public List<DetalleItemDTO> getProductos() { return productos; }
    public void setProductos(List<DetalleItemDTO> productos) { this.productos = productos; }
}
