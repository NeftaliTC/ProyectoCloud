package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.reporte.DTO.InventarioReporteData;
import pe.edu.pucp.killaBeauty.reporte.DTO.VentaReporteData;
import pe.edu.pucp.killaDAO.ReporteDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAOImpl implements ReporteDAO {
    @Override
    public List<VentaReporteData> listarVentas(LocalDate desde, LocalDate hasta) throws SQLException {
        List<VentaReporteData> filas = new ArrayList<>();
        String sql = """
                SELECT p.id_pedido, p.fecha_pedido, p.total,
                       ep.nombre AS estado,
                       CONCAT_WS(' ', u.nombre, u.apellido_paterno, u.apellido_materno) AS cliente,
                       pr.id_producto, pr.nombre AS producto, dp.cantidad,
                       dp.precio_unitario_aplicado,
                       m.descripcion AS marca,
                       c.descripcion AS categoria,
                       img.url AS imagen_url
                FROM Pedido p
                INNER JOIN EstadoPedido ep ON p.id_estado_pedido = ep.id_estado_pedido
                INNER JOIN Usuario u ON p.id_usuario = u.id_usuario
                INNER JOIN DetallePedido dp ON p.id_pedido = dp.id_pedido
                INNER JOIN Producto pr ON dp.id_producto = pr.id_producto
                INNER JOIN Marca m ON pr.id_marca = m.id_marca
                INNER JOIN Subcategoria s ON pr.id_subcategoria = s.id_subcategoria
                INNER JOIN Categoria c ON s.id_categoria = c.id_categoria
                LEFT JOIN ImagenProducto img ON pr.id_producto = img.id_producto
                    AND img.activo = 1 AND img.principal = 1
                WHERE DATE(p.fecha_pedido) BETWEEN ? AND ?
                ORDER BY p.fecha_pedido ASC, p.id_pedido ASC
                """;

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(mapVenta(rs));
                }
            }
        }
        return filas;
    }

    @Override
    public List<InventarioReporteData> listarInventario() throws SQLException {
        List<InventarioReporteData> filas = new ArrayList<>();
        String sql = """
                SELECT p.id_producto, p.nombre, p.precio_base, p.stock,
                       m.descripcion AS marca,
                       s.descripcion AS subcategoria,
                       c.descripcion AS categoria,
                       img.url AS imagen_url
                FROM Producto p
                INNER JOIN Marca m ON p.id_marca = m.id_marca
                INNER JOIN Subcategoria s ON p.id_subcategoria = s.id_subcategoria
                INNER JOIN Categoria c ON s.id_categoria = c.id_categoria
                LEFT JOIN ImagenProducto img ON p.id_producto = img.id_producto
                    AND img.activo = 1 AND img.principal = 1
                WHERE p.activo = 1
                ORDER BY p.nombre ASC
                """;

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(mapInventario(rs));
            }
        }
        return filas;
    }

    private VentaReporteData mapVenta(ResultSet rs) throws SQLException {
        VentaReporteData row = new VentaReporteData();
        row.setPedidoId(rs.getInt("id_pedido"));
        row.setFechaPedido(rs.getTimestamp("fecha_pedido").toLocalDateTime());
        row.setEstado(rs.getString("estado"));
        row.setCliente(rs.getString("cliente"));
        row.setTotalPedido(rs.getBigDecimal("total"));
        row.setProductoId(rs.getInt("id_producto"));
        row.setProducto(rs.getString("producto"));
        row.setMarca(rs.getString("marca"));
        row.setCategoria(rs.getString("categoria"));
        row.setCantidad(rs.getInt("cantidad"));
        row.setPrecioUnitario(rs.getBigDecimal("precio_unitario_aplicado"));
        row.setImagenUrl(rs.getString("imagen_url"));
        return row;
    }

    private InventarioReporteData mapInventario(ResultSet rs) throws SQLException {
        InventarioReporteData row = new InventarioReporteData();
        int idProducto = rs.getInt("id_producto");
        row.setProductoId(idProducto);
        row.setSku(String.format("KB-%06d", idProducto));
        row.setNombre(rs.getString("nombre"));
        row.setMarca(rs.getString("marca"));
        row.setCategoria(rs.getString("categoria"));
        row.setSubcategoria(rs.getString("subcategoria"));
        row.setStock(rs.getInt("stock"));
        row.setPrecioUnitario(rs.getBigDecimal("precio_base"));
        row.setImagenUrl(rs.getString("imagen_url"));
        return row;
    }
}
