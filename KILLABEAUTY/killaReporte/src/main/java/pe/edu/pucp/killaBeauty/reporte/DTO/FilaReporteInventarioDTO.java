package pe.edu.pucp.killaBeauty.reporte.DTO;

import java.math.BigDecimal;

public class FilaReporteInventarioDTO {
    private String sku;
    private String nombre;
    private String marca;
    private String categoria;
    private String subcategoria;
    private Integer stockActual;
    private String stockEstado;
    private BigDecimal precioUnitario;
    private BigDecimal valorInventario;

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getSubcategoria() { return subcategoria; }
    public void setSubcategoria(String subcategoria) { this.subcategoria = subcategoria; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public String getStockEstado() { return stockEstado; }
    public void setStockEstado(String stockEstado) { this.stockEstado = stockEstado; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getValorInventario() { return valorInventario; }
    public void setValorInventario(BigDecimal valorInventario) { this.valorInventario = valorInventario; }
}
