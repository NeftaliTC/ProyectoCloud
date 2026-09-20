package pe.edu.pucp.killaBeauty.reporte.DTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReporteInventarioDTO {
    private InventarioKpisDTO kpis = new InventarioKpisDTO();
    private List<CategoriaStockDTO> stockPorCategoria = new ArrayList<>();
    private ResumenStockDTO resumenStock = new ResumenStockDTO();
    private List<ProductoInventarioDTO> productos = new ArrayList<>();
    private List<String> categorias = new ArrayList<>();
    private List<String> subcategorias = new ArrayList<>();

    public InventarioKpisDTO getKpis() { return kpis; }
    public void setKpis(InventarioKpisDTO kpis) { this.kpis = kpis; }
    public List<CategoriaStockDTO> getStockPorCategoria() { return stockPorCategoria; }
    public void setStockPorCategoria(List<CategoriaStockDTO> stockPorCategoria) { this.stockPorCategoria = stockPorCategoria; }
    public ResumenStockDTO getResumenStock() { return resumenStock; }
    public void setResumenStock(ResumenStockDTO resumenStock) { this.resumenStock = resumenStock; }
    public List<ProductoInventarioDTO> getProductos() { return productos; }
    public void setProductos(List<ProductoInventarioDTO> productos) { this.productos = productos; }
    public List<String> getCategorias() { return categorias; }
    public void setCategorias(List<String> categorias) { this.categorias = categorias; }
    public List<String> getSubcategorias() { return subcategorias; }
    public void setSubcategorias(List<String> subcategorias) { this.subcategorias = subcategorias; }

    public static class InventarioKpisDTO {
        private BigDecimal valorTotal = BigDecimal.ZERO;
        private int stockCritico;
        private int agotados;
        private int unidadesTotales;

        public BigDecimal getValorTotal() { return valorTotal; }
        public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
        public int getStockCritico() { return stockCritico; }
        public void setStockCritico(int stockCritico) { this.stockCritico = stockCritico; }
        public int getAgotados() { return agotados; }
        public void setAgotados(int agotados) { this.agotados = agotados; }
        public int getUnidadesTotales() { return unidadesTotales; }
        public void setUnidadesTotales(int unidadesTotales) { this.unidadesTotales = unidadesTotales; }
    }

    public static class CategoriaStockDTO {
        private String categoria;
        private int unidades;
        private int porcentaje;

        public CategoriaStockDTO() {}
        public CategoriaStockDTO(String categoria, int unidades, int porcentaje) {
            this.categoria = categoria;
            this.unidades = unidades;
            this.porcentaje = porcentaje;
        }
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        public int getUnidades() { return unidades; }
        public void setUnidades(int unidades) { this.unidades = unidades; }
        public int getPorcentaje() { return porcentaje; }
        public void setPorcentaje(int porcentaje) { this.porcentaje = porcentaje; }
    }

    public static class ResumenStockDTO {
        private int agotado;
        private int critico;
        private int saludable;

        public int getAgotado() { return agotado; }
        public void setAgotado(int agotado) { this.agotado = agotado; }
        public int getCritico() { return critico; }
        public void setCritico(int critico) { this.critico = critico; }
        public int getSaludable() { return saludable; }
        public void setSaludable(int saludable) { this.saludable = saludable; }
    }

    public static class ProductoInventarioDTO {
        private String sku;
        private String nombre;
        private String marca;
        private String categoria;
        private String subcategoria;
        private int stockActual;
        private BigDecimal precioUnitario = BigDecimal.ZERO;
        private BigDecimal valorInventario = BigDecimal.ZERO;
        private String stockEstado;
        private String imagenUrl;

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
        public int getStockActual() { return stockActual; }
        public void setStockActual(int stockActual) { this.stockActual = stockActual; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
        public BigDecimal getValorInventario() { return valorInventario; }
        public void setValorInventario(BigDecimal valorInventario) { this.valorInventario = valorInventario; }
        public String getStockEstado() { return stockEstado; }
        public void setStockEstado(String stockEstado) { this.stockEstado = stockEstado; }
        public String getImagenUrl() { return imagenUrl; }
        public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    }
}
