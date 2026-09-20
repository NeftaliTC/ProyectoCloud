package pe.edu.pucp.killaBeauty.reporte;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import pe.edu.pucp.killaBeauty.reporte.DTO.ReporteInventarioDTO;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GeneradorReporteInventario {
    private static final Locale LOCALE_PE = new Locale("es", "PE");

    public byte[] generarReporteInventario(
            ReporteInventarioDTO reporte,
            String estadoFiltro,
            String categoriaFiltro,
            String subcategoriaFiltro,
            String ordenFiltro
    ) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 28, 28, 28, 28);
        PdfWriter.getInstance(document, output);

        document.open();
        agregarTitulo(document);
        agregarFiltros(document, estadoFiltro, categoriaFiltro, subcategoriaFiltro, ordenFiltro);
        agregarResumen(document, reporte);
        agregarTabla(document, reporte);
        document.close();

        return output.toByteArray();
    }

    private void agregarTitulo(Document document) throws Exception {
        Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph paragraph = new Paragraph("Reporte de inventario", titulo);
        paragraph.setSpacingAfter(8);
        document.add(paragraph);

        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 9);
        document.add(new Paragraph(
                "Generado: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),
                normal
        ));
    }

    private void agregarFiltros(
            Document document,
            String estadoFiltro,
            String categoriaFiltro,
            String subcategoriaFiltro,
            String ordenFiltro
    ) throws Exception {
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 9);
        Paragraph paragraph = new Paragraph(
                "Filtros - Estado: " + normalizarFiltro(estadoFiltro)
                        + " | Categoria: " + normalizarFiltro(categoriaFiltro)
                        + " | Subcategoria: " + normalizarFiltro(subcategoriaFiltro)
                        + " | Orden: " + normalizarFiltro(ordenFiltro),
                normal
        );
        paragraph.setSpacingAfter(12);
        document.add(paragraph);
    }

    private void agregarResumen(Document document, ReporteInventarioDTO reporte) throws Exception {
        ReporteInventarioDTO.InventarioKpisDTO kpis = reporte.getKpis() == null
                ? new ReporteInventarioDTO.InventarioKpisDTO()
                : reporte.getKpis();

        Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Paragraph paragraph = new Paragraph(
                "Valor inventario: " + moneda(kpis.getValorTotal())
                        + " | Unidades totales: " + valorEntero(kpis.getUnidadesTotales())
                        + " | Stock critico: " + valorEntero(kpis.getStockCritico())
                        + " | Agotados: " + valorEntero(kpis.getAgotados()),
                bold
        );
        paragraph.setSpacingAfter(12);
        document.add(paragraph);
    }

    private void agregarTabla(Document document, ReporteInventarioDTO reporte) throws Exception {
        PdfPTable table = new PdfPTable(new float[]{1.1f, 3.2f, 1.6f, 1.7f, 1.8f, 1.0f, 1.2f, 1.2f, 1.3f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(4);

        agregarCabecera(table, "SKU");
        agregarCabecera(table, "Producto");
        agregarCabecera(table, "Marca");
        agregarCabecera(table, "Categoria");
        agregarCabecera(table, "Subcategoria");
        agregarCabecera(table, "Stock");
        agregarCabecera(table, "Estado");
        agregarCabecera(table, "Precio");
        agregarCabecera(table, "Valor");

        for (ReporteInventarioDTO.ProductoInventarioDTO producto : productos(reporte)) {
            agregarCelda(table, producto.getSku(), Element.ALIGN_LEFT);
            agregarCelda(table, producto.getNombre(), Element.ALIGN_LEFT);
            agregarCelda(table, producto.getMarca(), Element.ALIGN_LEFT);
            agregarCelda(table, producto.getCategoria(), Element.ALIGN_LEFT);
            agregarCelda(table, producto.getSubcategoria(), Element.ALIGN_LEFT);
            agregarCelda(table, String.valueOf(valorEntero(producto.getStockActual())), Element.ALIGN_RIGHT);
            agregarCelda(table, producto.getStockEstado(), Element.ALIGN_LEFT);
            agregarCelda(table, moneda(producto.getPrecioUnitario()), Element.ALIGN_RIGHT);
            agregarCelda(table, moneda(producto.getValorInventario()), Element.ALIGN_RIGHT);
        }

        document.add(table);
    }

    private void agregarCabecera(PdfPTable table, String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(244, 201, 219));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void agregarCelda(PdfPTable table, String text, int alignment) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(4);
        table.addCell(cell);
    }

    private List<ReporteInventarioDTO.ProductoInventarioDTO> productos(ReporteInventarioDTO reporte) {
        if (reporte == null || reporte.getProductos() == null) {
            return Collections.emptyList();
        }
        return reporte.getProductos();
    }

    private String normalizarFiltro(String filtro) {
        if (filtro == null || filtro.isBlank()) return "Todos";
        return filtro.trim();
    }

    private int valorEntero(Integer valor) {
        return valor == null ? 0 : valor;
    }

    private String moneda(BigDecimal value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(LOCALE_PE);
        return format.format(value == null ? BigDecimal.ZERO : value);
    }
}
