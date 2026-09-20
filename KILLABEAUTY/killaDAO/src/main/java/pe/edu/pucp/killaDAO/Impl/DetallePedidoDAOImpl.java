package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.killaModelo.DetallePedido;
import pe.edu.pucp.killaBeauty.killaModelo.Pedido;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;
import pe.edu.pucp.killaDAO.DetallePedidoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoDAOImpl implements DetallePedidoDAO {

    @Override
    public List<DetallePedido> listAll() throws SQLException {
        List<DetallePedido> lista = new ArrayList<>();
        String sql = """
                SELECT id_detalle_pedido, cantidad, precio_unitario_aplicado, subtotal, id_pedido, id_producto
                FROM DetallePedido
                """;
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public List<DetallePedido> listByPedidoId(Integer idPedido) throws SQLException {
        List<DetallePedido> lista = new ArrayList<>();
        String sql = """
                SELECT id_detalle_pedido, cantidad, precio_unitario_aplicado, subtotal, id_pedido, id_producto
                FROM DetallePedido
                WHERE id_pedido = ?
                """;
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    @Override
    public DetallePedido load(Integer id) throws SQLException {
        String sql = """
                SELECT id_detalle_pedido, cantidad, precio_unitario_aplicado, subtotal, id_pedido, id_producto
                FROM DetallePedido
                WHERE id_detalle_pedido = ?
                """;
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public DetallePedido save(DetallePedido detalle) throws SQLException {
        if (detalle.getPedido() == null || detalle.getPedido().getId() <= 0) {
            throw new SQLException("Use save(detalle, idPedido) o asigne un Pedido valido");
        }
        return save(detalle, detalle.getPedido().getId());
    }

    @Override
    public DetallePedido save(DetallePedido detalle, Integer idPedido) throws SQLException {
        validarDetalle(detalle);
        String sql = """
                INSERT INTO DetallePedido
                (cantidad, precio_unitario_aplicado, subtotal, id_pedido, id_producto)
                VALUES (?, ?, ?, ?, ?)
                """;
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getPrecioAplicado());
            ps.setDouble(3, detalle.calcularSubtotal());
            ps.setInt(4, idPedido);
            ps.setInt(5, detalle.getProducto().getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) detalle.setIdDetallePedido(keys.getInt(1));
            }
        }
        return detalle;
    }

    @Override
    public DetallePedido update(DetallePedido detalle) throws SQLException {
        if (detalle.getPedido() == null || detalle.getPedido().getId() <= 0) {
            throw new SQLException("Use update(detalle, idPedido) o asigne un Pedido valido");
        }
        return update(detalle, detalle.getPedido().getId());
    }

    @Override
    public DetallePedido update(DetallePedido detalle, Integer idPedido) throws SQLException {
        validarDetalle(detalle);
        String sql = """
                UPDATE DetallePedido
                SET cantidad = ?, precio_unitario_aplicado = ?, subtotal = ?, id_pedido = ?, id_producto = ?
                WHERE id_detalle_pedido = ?
                """;
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getPrecioAplicado());
            ps.setDouble(3, detalle.calcularSubtotal());
            ps.setInt(4, idPedido);
            ps.setInt(5, detalle.getProducto().getId());
            ps.setInt(6, detalle.getIdDetallePedido());
            ps.executeUpdate();
        }
        return detalle;
    }

    @Override
    public void remove(DetallePedido detalle) throws SQLException {
        String sql = "DELETE FROM DetallePedido WHERE id_detalle_pedido = ?";
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, detalle.getIdDetallePedido());
            ps.executeUpdate();
        }
    }

    private DetallePedido mapRow(ResultSet rs) throws SQLException {
        DetallePedido d = new DetallePedido();
        d.setIdDetallePedido(rs.getInt("id_detalle_pedido"));
        d.setCantidad(rs.getInt("cantidad"));
        d.setPrecioAplicado(rs.getDouble("precio_unitario_aplicado"));
        d.setSubtotal(rs.getDouble("subtotal"));

        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id_pedido"));
        d.setPedido(pedido);

        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        d.setProducto(p);
        return d;
    }

    private void validarDetalle(DetallePedido detalle) throws SQLException {
        if (detalle.getProducto() == null || detalle.getProducto().getId() <= 0) {
            throw new SQLException("Producto invalido en DetallePedido");
        }
    }
}
