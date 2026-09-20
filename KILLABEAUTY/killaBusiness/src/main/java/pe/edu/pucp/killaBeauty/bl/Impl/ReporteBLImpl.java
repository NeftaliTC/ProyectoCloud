package pe.edu.pucp.killaBeauty.bl.Impl;
import pe.edu.pucp.killaBeauty.bl.ReporteBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.reporte.DTO.InventarioReporteData;
import pe.edu.pucp.killaBeauty.reporte.DTO.ReporteInventarioDTO;
import pe.edu.pucp.killaBeauty.reporte.DTO.ReporteVentasDTO;
import pe.edu.pucp.killaBeauty.reporte.DTO.VentaReporteData;
import pe.edu.pucp.killaDAO.Impl.ReporteDAOImpl;
import pe.edu.pucp.killaDAO.ReporteDAO;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ReporteBLImpl implements ReporteBL {
    private static final Set<String> ESTADOS_VENTA = Set.of("PAGADO", "EN_PREPARACION", "ENVIADO", "ENTREGADO");
    private static final Set<String> ESTADOS_INVENTARIO = Set.of("Agotado", "Critico", "Saludable");
    private static final Set<String> ORDENES_INVENTARIO = Set.of("Estado", "MenorStock", "MayorValor", "Categoria");
    private static final int UMBRAL_STOCK_CRITICO = 10;
    private static final String IMAGEN_FALLBACK = "/Images/Logo.png";

    private final ReporteDAO reporteDAO = new ReporteDAOImpl();

    @Override
    public ReporteVentasDTO obtenerReporteVentas(String desde, String hasta, String categoria)
            throws BusinessLogicException {
        LocalDate fechaDesde = parseFecha(desde, "La fecha de inicio del reporte no es valida.");
        LocalDate fechaHasta = parseFecha(hasta, "La fecha de fin del reporte no es valida.");
        if (fechaDesde.isAfter(fechaHasta)) {
            throw new BusinessLogicException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        String filtroCategoria = normalizarFiltro(categoria, "Todos", "Todas");

        try {
            List<VentaReporteData> filasBase = reporteDAO.listarVentas(fechaDesde, fechaHasta).stream()
                    .filter(row -> ESTADOS_VENTA.contains(valor(row.getEstado())))
                    .toList();

            ReporteVentasDTO reporte = new ReporteVentasDTO();
            reporte.setCategorias(distintos(filasBase.stream().map(VentaReporteData::getCategoria).toList()));

            List<VentaReporteData> filas = filasBase.stream()
                    .filter(row -> filtroCategoria == null
                            || valor(row.getCategoria()).equalsIgnoreCase(filtroCategoria))
                    .toList();

            List<ReporteVentasDTO.VentaDTO> pedidos = construirPedidos(filas);
            reporte.setDetalleVentas(construirDetalleVentas(filas));
            reporte.setPedidos(pedidos);
            reporte.setKpis(calcularVentasKpis(pedidos));
            reporte.setVentasPorDia(calcularVentasPorDia(pedidos));
            reporte.setVentasPorCategoria(calcularVentasPorCategoria(pedidos));
            reporte.setProductosMasVendidos(calcularProductosMasVendidos(pedidos));
            reporte.setClientesTop(calcularClientesTop(pedidos));
            return reporte;
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public ReporteInventarioDTO obtenerReporteInventario(String estado, String categoria, String subcategoria, String orden)
            throws BusinessLogicException {
        String filtroEstado = normalizarFiltro(estado, "Todos", "Todas");
        String filtroCategoria = normalizarFiltro(categoria, "Todos", "Todas");
        String filtroSubcategoria = normalizarFiltro(subcategoria, "Todos", "Todas");
        String ordenNormalizado = normalizarFiltro(orden, "Todos", "Todas");
        if (ordenNormalizado == null) ordenNormalizado = "Estado";

        if (filtroEstado != null && !ESTADOS_INVENTARIO.contains(filtroEstado)) {
            throw new BusinessLogicException("El estado de inventario no es valido.");
        }
        if (!ORDENES_INVENTARIO.contains(ordenNormalizado)) {
            throw new BusinessLogicException("El orden de inventario no es valido.");
        }

        try {
            List<ReporteInventarioDTO.ProductoInventarioDTO> productosBase = reporteDAO.listarInventario().stream()
                    .map(this::mapInventario)
                    .toList();

            ReporteInventarioDTO reporte = new ReporteInventarioDTO();
            reporte.setCategorias(distintos(productosBase.stream()
                    .map(ReporteInventarioDTO.ProductoInventarioDTO::getCategoria).toList()));
            reporte.setSubcategorias(distintos(productosBase.stream()
                    .filter(p -> filtroCategoria == null || p.getCategoria().equalsIgnoreCase(filtroCategoria))
                    .map(ReporteInventarioDTO.ProductoInventarioDTO::getSubcategoria).toList()));

            List<ReporteInventarioDTO.ProductoInventarioDTO> productos = productosBase.stream()
                    .filter(p -> filtroEstado == null || p.getStockEstado().equalsIgnoreCase(filtroEstado))
                    .filter(p -> filtroCategoria == null || p.getCategoria().equalsIgnoreCase(filtroCategoria))
                    .filter(p -> filtroSubcategoria == null || p.getSubcategoria().equalsIgnoreCase(filtroSubcategoria))
                    .collect(Collectors.toCollection(ArrayList::new));

            ordenarInventario(productos, ordenNormalizado);
            reporte.setProductos(productos);
            reporte.setKpis(calcularInventarioKpis(productos));
            reporte.setStockPorCategoria(calcularStockPorCategoria(productos));
            reporte.setResumenStock(calcularResumenStock(productos));
            return reporte;
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    private LocalDate parseFecha(String fecha, String mensaje) throws BusinessLogicException {
        if (fecha == null || fecha.isBlank()) {
            throw new BusinessLogicException(mensaje);
        }
        try {
            return LocalDate.parse(fecha.trim());
        } catch (DateTimeParseException ex) {
            throw new BusinessLogicException(mensaje);
        }
    }

    private String normalizarFiltro(String valor, String... comodines) {
        if (valor == null || valor.isBlank()) return null;
        String limpio = valor.trim();
        for (String comodin : comodines) {
            if (limpio.equalsIgnoreCase(comodin)) return null;
        }
        return limpio;
    }

    private List<ReporteVentasDTO.VentaDTO> construirPedidos(List<VentaReporteData> filas) {
        Map<Integer, ReporteVentasDTO.VentaDTO> pedidos = new LinkedHashMap<>();
        for (VentaReporteData row : filas) {
            ReporteVentasDTO.VentaDTO venta = pedidos.computeIfAbsent(row.getPedidoId(), id -> {
                ReporteVentasDTO.VentaDTO dto = new ReporteVentasDTO.VentaDTO();
                dto.setId(id);
                dto.setFecha(row.getFechaPedido().toLocalDate().toString());
                dto.setCliente(valor(row.getCliente()));
                dto.setEstado(valor(row.getEstado()));
                dto.setTotal(nvl(row.getTotalPedido()));
                return dto;
            });
            venta.getProductos().add(mapProductoVenta(row));
        }
        return pedidos.values().stream()
                .sorted(Comparator.comparing(ReporteVentasDTO.VentaDTO::getFecha))
                .toList();
    }

    private List<ReporteVentasDTO.DetalleVentaDTO> construirDetalleVentas(List<VentaReporteData> filas) {
        return filas.stream()
                .map(row -> {
                    ReporteVentasDTO.DetalleVentaDTO dto = new ReporteVentasDTO.DetalleVentaDTO();
                    dto.setPedidoId(row.getPedidoId());
                    dto.setFecha(row.getFechaPedido().toLocalDate().toString());
                    dto.setCliente(valor(row.getCliente()));
                    dto.setProducto(valor(row.getProducto()));
                    dto.setMarca(valor(row.getMarca()));
                    dto.setCategoria(valor(row.getCategoria()));
                    dto.setCantidad(row.getCantidad());
                    dto.setPrecioUnitario(nvl(row.getPrecioUnitario()));
                    dto.setTotalLinea(nvl(row.getPrecioUnitario()).multiply(BigDecimal.valueOf(row.getCantidad())));
                    return dto;
                })
                .toList();
    }

    private ReporteVentasDTO.ProductoVentaDTO mapProductoVenta(VentaReporteData row) {
        ReporteVentasDTO.ProductoVentaDTO dto = new ReporteVentasDTO.ProductoVentaDTO();
        dto.setProductoId(row.getProductoId());
        dto.setNombre(valor(row.getProducto()));
        dto.setMarca(valor(row.getMarca()));
        dto.setCategoria(valor(row.getCategoria()));
        dto.setCantidad(row.getCantidad());
        dto.setPrecioUnitario(nvl(row.getPrecioUnitario()));
        dto.setImagenUrl(normalizarImagen(row.getImagenUrl()));
        return dto;
    }

    private ReporteVentasDTO.VentasKpisDTO calcularVentasKpis(List<ReporteVentasDTO.VentaDTO> pedidos) {
        ReporteVentasDTO.VentasKpisDTO kpis = new ReporteVentasDTO.VentasKpisDTO();
        BigDecimal ingresos = pedidos.stream()
                .map(ReporteVentasDTO.VentaDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int unidades = pedidos.stream()
                .flatMap(p -> p.getProductos().stream())
                .mapToInt(ReporteVentasDTO.ProductoVentaDTO::getCantidad)
                .sum();

        kpis.setIngresos(ingresos);
        kpis.setPedidosTotales(pedidos.size());
        kpis.setUnidadesVendidas(unidades);
        kpis.setPedidoPromedio(pedidos.isEmpty()
                ? BigDecimal.ZERO
                : ingresos.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP));
        return kpis;
    }

    private List<ReporteVentasDTO.VentaPorDiaDTO> calcularVentasPorDia(List<ReporteVentasDTO.VentaDTO> pedidos) {
        return pedidos.stream()
                .collect(Collectors.groupingBy(ReporteVentasDTO.VentaDTO::getFecha, TreeMap::new,
                        Collectors.mapping(ReporteVentasDTO.VentaDTO::getTotal,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet().stream()
                .map(e -> new ReporteVentasDTO.VentaPorDiaDTO(e.getKey(), e.getValue()))
                .toList();
    }

    private List<ReporteVentasDTO.VentaPorCategoriaDTO> calcularVentasPorCategoria(List<ReporteVentasDTO.VentaDTO> pedidos) {
        Map<String, BigDecimal> totales = new LinkedHashMap<>();
        pedidos.stream().flatMap(p -> p.getProductos().stream()).forEach(producto -> {
            BigDecimal total = producto.getPrecioUnitario().multiply(BigDecimal.valueOf(producto.getCantidad()));
            totales.merge(producto.getCategoria(), total, BigDecimal::add);
        });
        return totales.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .map(e -> new ReporteVentasDTO.VentaPorCategoriaDTO(e.getKey(), e.getValue()))
                .toList();
    }

    private List<ReporteVentasDTO.TopProductoDTO> calcularProductosMasVendidos(List<ReporteVentasDTO.VentaDTO> pedidos) {
        Map<Integer, ReporteVentasDTO.TopProductoDTO> top = new LinkedHashMap<>();
        pedidos.stream().flatMap(p -> p.getProductos().stream()).forEach(producto -> {
            ReporteVentasDTO.TopProductoDTO dto = top.computeIfAbsent(producto.getProductoId(), id -> {
                ReporteVentasDTO.TopProductoDTO nuevo = new ReporteVentasDTO.TopProductoDTO();
                nuevo.setNombre(producto.getNombre());
                nuevo.setMarca(producto.getMarca());
                nuevo.setCategoria(producto.getCategoria());
                nuevo.setPrecioUnitario(producto.getPrecioUnitario());
                nuevo.setImagenUrl(producto.getImagenUrl());
                return nuevo;
            });
            dto.setCantidadVendida(dto.getCantidadVendida() + producto.getCantidad());
        });
        return top.values().stream()
                .sorted(Comparator.comparing(ReporteVentasDTO.TopProductoDTO::getCantidadVendida).reversed()
                        .thenComparing(ReporteVentasDTO.TopProductoDTO::getNombre))
                .limit(5)
                .toList();
    }

    private List<ReporteVentasDTO.TopClienteDTO> calcularClientesTop(List<ReporteVentasDTO.VentaDTO> pedidos) {
        Map<String, ReporteVentasDTO.TopClienteDTO> top = new LinkedHashMap<>();
        for (ReporteVentasDTO.VentaDTO pedido : pedidos) {
            ReporteVentasDTO.TopClienteDTO dto = top.computeIfAbsent(pedido.getCliente(), nombre -> {
                ReporteVentasDTO.TopClienteDTO nuevo = new ReporteVentasDTO.TopClienteDTO();
                nuevo.setNombre(nombre);
                return nuevo;
            });
            dto.setPedidos(dto.getPedidos() + 1);
            dto.setUnidades(dto.getUnidades() + pedido.getProductos().stream()
                    .mapToInt(ReporteVentasDTO.ProductoVentaDTO::getCantidad).sum());
            dto.setTotalComprado(dto.getTotalComprado().add(pedido.getTotal()));
        }
        return top.values().stream()
                .sorted(Comparator.comparing(ReporteVentasDTO.TopClienteDTO::getTotalComprado).reversed()
                        .thenComparing(ReporteVentasDTO.TopClienteDTO::getNombre))
                .limit(5)
                .toList();
    }

    private ReporteInventarioDTO.ProductoInventarioDTO mapInventario(InventarioReporteData row) {
        ReporteInventarioDTO.ProductoInventarioDTO dto = new ReporteInventarioDTO.ProductoInventarioDTO();
        BigDecimal precio = nvl(row.getPrecioUnitario());
        int stock = Math.max(0, row.getStock());
        dto.setSku(row.getSku());
        dto.setNombre(valor(row.getNombre()));
        dto.setMarca(valor(row.getMarca()));
        dto.setCategoria(valor(row.getCategoria()));
        dto.setSubcategoria(valor(row.getSubcategoria()));
        dto.setStockActual(stock);
        dto.setPrecioUnitario(precio);
        dto.setValorInventario(precio.multiply(BigDecimal.valueOf(stock)));
        dto.setStockEstado(calcularEstadoStock(stock));
        dto.setImagenUrl(normalizarImagen(row.getImagenUrl()));
        return dto;
    }

    private void ordenarInventario(List<ReporteInventarioDTO.ProductoInventarioDTO> productos, String orden) {
        Comparator<ReporteInventarioDTO.ProductoInventarioDTO> comparator = switch (orden) {
            case "MenorStock" -> Comparator.comparing(ReporteInventarioDTO.ProductoInventarioDTO::getStockActual)
                    .thenComparing(ReporteInventarioDTO.ProductoInventarioDTO::getNombre);
            case "MayorValor" -> Comparator.comparing(ReporteInventarioDTO.ProductoInventarioDTO::getValorInventario)
                    .reversed().thenComparing(ReporteInventarioDTO.ProductoInventarioDTO::getNombre);
            case "Categoria" -> Comparator.comparing(ReporteInventarioDTO.ProductoInventarioDTO::getCategoria)
                    .thenComparing(ReporteInventarioDTO.ProductoInventarioDTO::getSubcategoria)
                    .thenComparing(ReporteInventarioDTO.ProductoInventarioDTO::getNombre);
            default -> Comparator.comparingInt((ReporteInventarioDTO.ProductoInventarioDTO p) -> prioridadEstado(p.getStockEstado()))
                    .thenComparing(ReporteInventarioDTO.ProductoInventarioDTO::getStockActual)
                    .thenComparing(ReporteInventarioDTO.ProductoInventarioDTO::getNombre);
        };
        productos.sort(comparator);
    }

    private ReporteInventarioDTO.InventarioKpisDTO calcularInventarioKpis(List<ReporteInventarioDTO.ProductoInventarioDTO> productos) {
        ReporteInventarioDTO.InventarioKpisDTO kpis = new ReporteInventarioDTO.InventarioKpisDTO();
        kpis.setValorTotal(productos.stream()
                .map(ReporteInventarioDTO.ProductoInventarioDTO::getValorInventario)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        kpis.setStockCritico((int) productos.stream().filter(p -> p.getStockEstado().equals("Critico")).count());
        kpis.setAgotados((int) productos.stream().filter(p -> p.getStockEstado().equals("Agotado")).count());
        kpis.setUnidadesTotales(productos.stream()
                .mapToInt(ReporteInventarioDTO.ProductoInventarioDTO::getStockActual).sum());
        return kpis;
    }

    private List<ReporteInventarioDTO.CategoriaStockDTO> calcularStockPorCategoria(List<ReporteInventarioDTO.ProductoInventarioDTO> productos) {
        Map<String, Integer> totales = productos.stream()
                .collect(Collectors.groupingBy(ReporteInventarioDTO.ProductoInventarioDTO::getCategoria,
                        Collectors.summingInt(ReporteInventarioDTO.ProductoInventarioDTO::getStockActual)));
        int maximo = Math.max(1, totales.values().stream().mapToInt(Integer::intValue).max().orElse(1));
        return totales.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(e -> new ReporteInventarioDTO.CategoriaStockDTO(e.getKey(), e.getValue(),
                        Math.max(5, e.getValue() * 100 / maximo)))
                .toList();
    }

    private ReporteInventarioDTO.ResumenStockDTO calcularResumenStock(List<ReporteInventarioDTO.ProductoInventarioDTO> productos) {
        ReporteInventarioDTO.ResumenStockDTO resumen = new ReporteInventarioDTO.ResumenStockDTO();
        resumen.setAgotado((int) productos.stream().filter(p -> p.getStockEstado().equals("Agotado")).count());
        resumen.setCritico((int) productos.stream().filter(p -> p.getStockEstado().equals("Critico")).count());
        resumen.setSaludable((int) productos.stream().filter(p -> p.getStockEstado().equals("Saludable")).count());
        return resumen;
    }

    private String calcularEstadoStock(int stock) {
        if (stock <= 0) return "Agotado";
        if (stock <= UMBRAL_STOCK_CRITICO) return "Critico";
        return "Saludable";
    }

    private int prioridadEstado(String estado) {
        return switch (estado) {
            case "Agotado" -> 0;
            case "Critico" -> 1;
            default -> 2;
        };
    }

    private List<String> distintos(List<String> valores) {
        return valores.stream()
                .map(this::valor)
                .filter(v -> !v.isBlank())
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    private String normalizarImagen(String imagenUrl) {
        if (imagenUrl == null || imagenUrl.isBlank()) return IMAGEN_FALLBACK;
        String limpia = imagenUrl.trim().replace("\\", "/");
        return limpia.startsWith("/") || limpia.startsWith("http") ? limpia : "/" + limpia;
    }

    private String valor(String value) {
        return value == null ? "" : value.trim();
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
