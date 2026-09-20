package pe.edu.pucp.killaBeauty.reporte;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import pe.edu.pucp.killaBeauty.reporte.DTO.FilaReporteVentasDTO;
import pe.edu.pucp.killaBeauty.reporte.DTO.ReporteVentasDTO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneradorReporteVentas {
    private static final DateTimeFormatter FECHA_ENTRADA = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter FECHA_SALIDA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generarReporteVentas(
            ReporteVentasDTO reporte,
            String fechaInicio,
            String fechaFin,
            String categoriaFiltro
    ) throws Exception {
        InputStream jasperStream = getClass()
                .getResourceAsStream("/reports/ReporteVentas.jasper");

        if (jasperStream == null) {
            throw new IllegalStateException("No se encontro la plantilla /reports/ReporteVentas.jasper");
        }

        Map<String, Object> parametros = construirParametros(
                reporte,
                fechaInicio,
                fechaFin,
                categoriaFiltro
        );

        JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(construirFilas(reporte));

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperStream,
                parametros,
                dataSource
        );

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private Map<String, Object> construirParametros(
            ReporteVentasDTO reporte,
            String fechaInicio,
            String fechaFin,
            String categoriaFiltro
    ) {
        ReporteVentasDTO.VentasKpisDTO kpis = reporte.getKpis() == null
                ? new ReporteVentasDTO.VentasKpisDTO()
                : reporte.getKpis();
        Map<String, Object> parametros = new HashMap<>();

        parametros.put("fechaGeneracion", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        parametros.put("fechaInicio", formatearFecha(fechaInicio));
        parametros.put("fechaFin", formatearFecha(fechaFin));
        parametros.put("categoriaFiltro", normalizarFiltro(categoriaFiltro));
        parametros.put("ingresosTotales", nvl(kpis.getIngresos()));
        parametros.put("pedidosTotales", kpis.getPedidosTotales());
        parametros.put("pedidoPromedio", nvl(kpis.getPedidoPromedio()));
        parametros.put("unidadesVendidas", kpis.getUnidadesVendidas());

        return parametros;
    }

    private List<FilaReporteVentasDTO> construirFilas(ReporteVentasDTO reporte) {
        List<ReporteVentasDTO.VentaDTO> pedidos = reporte.getPedidos() == null
                ? Collections.emptyList()
                : reporte.getPedidos();

        return pedidos.stream()
                .map(this::crearFila)
                .collect(Collectors.toList());
    }

    private FilaReporteVentasDTO crearFila(ReporteVentasDTO.VentaDTO venta) {
        FilaReporteVentasDTO fila = new FilaReporteVentasDTO();
        List<ReporteVentasDTO.ProductoVentaDTO> productosVenta = venta.getProductos() == null
                ? Collections.emptyList()
                : venta.getProductos();
        String categorias = productosVenta.stream()
                .map(ReporteVentasDTO.ProductoVentaDTO::getCategoria)
                .filter(categoria -> categoria != null && !categoria.isBlank())
                .distinct()
                .collect(Collectors.joining(", "));
        Integer productos = productosVenta.stream()
                .mapToInt(ReporteVentasDTO.ProductoVentaDTO::getCantidad)
                .sum();

        fila.setPedido(String.format("KIL-%06d", venta.getId()));
        fila.setFecha(formatearFecha(venta.getFecha()));
        fila.setCliente(venta.getCliente());
        fila.setCategorias(categorias);
        fila.setProductos(productos);
        fila.setTotal(nvl(venta.getTotal()));

        return fila;
    }

    private String normalizarFiltro(String filtro) {
        if (filtro == null || filtro.isBlank()) return "Todas";
        return filtro.trim();
    }

    private String formatearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) return "";
        try {
            return LocalDate.parse(fecha.trim(), FECHA_ENTRADA).format(FECHA_SALIDA);
        } catch (Exception ex) {
            return fecha;
        }
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
