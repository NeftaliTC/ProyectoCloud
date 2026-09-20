package pe.edu.pucp.killaBeauty.reporte.DTO;

import java.math.BigDecimal;

/**
 * Dato plano obtenido por el DAO para armar el reporte de inventario.
 * No representa una tabla propia de la base de datos.
 */
public class InventarioReporteData {
    private String sku;
    private int productoId;
    private String nombre;
    private String marca;
    private String categoria;
    private String subcategoria;
    private int stock;
    private BigDecimal precioUnitario;
    private String imagenUrl;

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getSubcategoria() { return subcategoria; }
    public void setSubcategoria(String subcategoria) { this.subcategoria = subcategoria; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}

