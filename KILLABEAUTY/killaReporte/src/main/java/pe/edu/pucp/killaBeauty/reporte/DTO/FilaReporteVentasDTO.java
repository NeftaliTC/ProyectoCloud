package pe.edu.pucp.killaBeauty.reporte.DTO;

import java.math.BigDecimal;

public class FilaReporteVentasDTO {
    private String pedido;
    private String fecha;
    private String cliente;
    private String categorias;
    private Integer productos;
    private BigDecimal total;

    public String getPedido() { return pedido; }
    public void setPedido(String pedido) { this.pedido = pedido; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getCategorias() { return categorias; }
    public void setCategorias(String categorias) { this.categorias = categorias; }

    public String getCategoria() { return categorias; }
    public void setCategoria(String categoria) { this.categorias = categoria; }

    public Integer getProductos() { return productos; }
    public void setProductos(Integer productos) { this.productos = productos; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
