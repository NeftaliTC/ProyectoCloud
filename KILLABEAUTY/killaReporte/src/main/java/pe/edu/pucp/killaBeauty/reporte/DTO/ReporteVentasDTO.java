package pe.edu.pucp.killaBeauty.reporte.DTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReporteVentasDTO {
    private VentasKpisDTO kpis = new VentasKpisDTO();
    private List<VentaPorDiaDTO> ventasPorDia = new ArrayList<>();
    private List<VentaPorCategoriaDTO> ventasPorCategoria = new ArrayList<>();
    private List<TopProductoDTO> productosMasVendidos = new ArrayList<>();
    private List<TopClienteDTO> clientesTop = new ArrayList<>();
    private List<DetalleVentaDTO> detalleVentas = new ArrayList<>();
    private List<VentaDTO> pedidos = new ArrayList<>();
    private List<String> categorias = new ArrayList<>();

    public VentasKpisDTO getKpis() { return kpis; }
    public void setKpis(VentasKpisDTO kpis) { this.kpis = kpis; }
    public List<VentaPorDiaDTO> getVentasPorDia() { return ventasPorDia; }
    public void setVentasPorDia(List<VentaPorDiaDTO> ventasPorDia) { this.ventasPorDia = ventasPorDia; }
    public List<VentaPorCategoriaDTO> getVentasPorCategoria() { return ventasPorCategoria; }
    public void setVentasPorCategoria(List<VentaPorCategoriaDTO> ventasPorCategoria) { this.ventasPorCategoria = ventasPorCategoria; }
    public List<TopProductoDTO> getProductosMasVendidos() { return productosMasVendidos; }
    public void setProductosMasVendidos(List<TopProductoDTO> productosMasVendidos) { this.productosMasVendidos = productosMasVendidos; }
    public List<TopClienteDTO> getClientesTop() { return clientesTop; }
    public void setClientesTop(List<TopClienteDTO> clientesTop) { this.clientesTop = clientesTop; }
    public List<DetalleVentaDTO> getDetalleVentas() { return detalleVentas; }
    public void setDetalleVentas(List<DetalleVentaDTO> detalleVentas) { this.detalleVentas = detalleVentas; }
    public List<VentaDTO> getPedidos() { return pedidos; }
    public void setPedidos(List<VentaDTO> pedidos) { this.pedidos = pedidos; }
    public List<String> getCategorias() { return categorias; }
    public void setCategorias(List<String> categorias) { this.categorias = categorias; }

    public static class VentasKpisDTO {
        private BigDecimal ingresos = BigDecimal.ZERO;
        private int pedidosTotales;
        private BigDecimal pedidoPromedio = BigDecimal.ZERO;
        private int unidadesVendidas;

        public BigDecimal getIngresos() { return ingresos; }
        public void setIngresos(BigDecimal ingresos) { this.ingresos = ingresos; }
        public int getPedidosTotales() { return pedidosTotales; }
        public void setPedidosTotales(int pedidosTotales) { this.pedidosTotales = pedidosTotales; }
        public BigDecimal getPedidoPromedio() { return pedidoPromedio; }
        public void setPedidoPromedio(BigDecimal pedidoPromedio) { this.pedidoPromedio = pedidoPromedio; }
        public int getUnidadesVendidas() { return unidadesVendidas; }
        public void setUnidadesVendidas(int unidadesVendidas) { this.unidadesVendidas = unidadesVendidas; }
    }

    public static class VentaPorDiaDTO {
        private String fecha;
        private BigDecimal total = BigDecimal.ZERO;

        public VentaPorDiaDTO() {}
        public VentaPorDiaDTO(String fecha, BigDecimal total) {
            this.fecha = fecha;
            this.total = total;
        }
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
    }

    public static class VentaPorCategoriaDTO {
        private String categoria;
        private BigDecimal total = BigDecimal.ZERO;

        public VentaPorCategoriaDTO() {}
        public VentaPorCategoriaDTO(String categoria, BigDecimal total) {
            this.categoria = categoria;
            this.total = total;
        }
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
    }

    public static class TopProductoDTO {
        private String nombre;
        private String marca;
        private String categoria;
        private int cantidadVendida;
        private BigDecimal precioUnitario = BigDecimal.ZERO;
        private String imagenUrl;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        public int getCantidadVendida() { return cantidadVendida; }
        public void setCantidadVendida(int cantidadVendida) { this.cantidadVendida = cantidadVendida; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
        public String getImagenUrl() { return imagenUrl; }
        public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    }

    public static class TopClienteDTO {
        private String nombre;
        private int pedidos;
        private int unidades;
        private BigDecimal totalComprado = BigDecimal.ZERO;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public int getPedidos() { return pedidos; }
        public void setPedidos(int pedidos) { this.pedidos = pedidos; }
        public int getUnidades() { return unidades; }
        public void setUnidades(int unidades) { this.unidades = unidades; }
        public BigDecimal getTotalComprado() { return totalComprado; }
        public void setTotalComprado(BigDecimal totalComprado) { this.totalComprado = totalComprado; }
    }

    public static class DetalleVentaDTO {
        private int pedidoId;
        private String fecha;
        private String cliente;
        private String producto;
        private String marca;
        private String categoria;
        private int cantidad;
        private BigDecimal precioUnitario = BigDecimal.ZERO;
        private BigDecimal totalLinea = BigDecimal.ZERO;

        public int getPedidoId() { return pedidoId; }
        public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        public String getProducto() { return producto; }
        public void setProducto(String producto) { this.producto = producto; }
        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
        public BigDecimal getTotalLinea() { return totalLinea; }
        public void setTotalLinea(BigDecimal totalLinea) { this.totalLinea = totalLinea; }
    }

    public static class VentaDTO {
        private int id;
        private String fecha;
        private String cliente;
        private String estado;
        private BigDecimal total = BigDecimal.ZERO;
        private List<ProductoVentaDTO> productos = new ArrayList<>();

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
        public List<ProductoVentaDTO> getProductos() { return productos; }
        public void setProductos(List<ProductoVentaDTO> productos) { this.productos = productos; }
    }

    public static class ProductoVentaDTO {
        private int productoId;
        private String nombre;
        private String marca;
        private String categoria;
        private int cantidad;
        private BigDecimal precioUnitario = BigDecimal.ZERO;
        private String imagenUrl;

        public int getProductoId() { return productoId; }
        public void setProductoId(int productoId) { this.productoId = productoId; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
        public String getImagenUrl() { return imagenUrl; }
        public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    }
}
