package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.killaModelo.CarritoDeCompras;
import pe.edu.pucp.killaBeauty.killaModelo.DetalleCarrito;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;
import pe.edu.pucp.killaDAO.DetalleCarritoDAO;
import pe.edu.pucp.killaDAO.ImagenProductoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleCarritoDAOImpl implements DetalleCarritoDAO {

    private ImagenProductoDAO imagenDAO = new ImagenProductoDAOImpl();

    @Override
    public DetalleCarrito load(Integer id) throws SQLException {
        String sql = "SELECT id_detalle_carrito, cantidad, id_producto, id_carrito FROM DetalleCarrito WHERE id_detalle_carrito = ?";
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
    public DetalleCarrito save(DetalleCarrito d) throws SQLException {
        validarDetalle(d);
        String sql = "INSERT INTO DetalleCarrito (cantidad, id_producto, id_carrito) VALUES (?, ?, ?)";
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, d.getCantidad());
            ps.setInt(2, d.getProducto().getId());
            ps.setInt(3, d.getCarritoDeCompras().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setId(rs.getInt(1));
            }
        }
        return d;
    }

    @Override
    public DetalleCarrito update(DetalleCarrito d) throws SQLException {
        validarDetalle(d);
        String sql = "UPDATE DetalleCarrito SET cantidad = ?, id_producto = ?, id_carrito = ? WHERE id_detalle_carrito = ?";
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, d.getCantidad());
            ps.setInt(2, d.getProducto().getId());
            ps.setInt(3, d.getCarritoDeCompras().getId());
            ps.setInt(4, d.getId());
            ps.executeUpdate();
        }
        return d;
    }

    @Override
    public void remove(DetalleCarrito d) throws SQLException {
        String sql = "DELETE FROM DetalleCarrito WHERE id_detalle_carrito = ?";
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, d.getId());
            ps.executeUpdate();
        }
    }

    private DetalleCarrito mapRowConProducto(ResultSet rs) throws SQLException {
        DetalleCarrito d = new DetalleCarrito();

        d.setId(rs.getInt("id_detalle_carrito"));
        d.setCantidad(rs.getInt("cantidad"));

        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecioBase(rs.getDouble("precio_base"));
        p.setStock(rs.getInt("stock"));
        p.setImagenes(imagenDAO.listByProductoId(p.getId()));
        d.setProducto(p);

        CarritoDeCompras carrito = new CarritoDeCompras();
        carrito.setId(rs.getInt("id_carrito"));
        d.setCarritoDeCompras(carrito);

        return d;
    }

    @Override
    public List<DetalleCarrito> listByCarritoId(int idCarrito) throws SQLException {
        List<DetalleCarrito> lista = new ArrayList<>();

        String sql = """
        SELECT
            dc.id_detalle_carrito,
            dc.cantidad,
            dc.id_carrito,
            p.id_producto,
            p.nombre,
            p.precio_base,
            p.stock
        FROM DetalleCarrito dc
        INNER JOIN Producto p
            ON dc.id_producto = p.id_producto
        WHERE dc.id_carrito = ?
    """;

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idCarrito);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRowConProducto(rs));
                }
            }
        }

        return lista;
    }

    private DetalleCarrito mapRow(ResultSet rs) throws SQLException {
        DetalleCarrito d = new DetalleCarrito();
        d.setId(rs.getInt("id_detalle_carrito"));
        d.setCantidad(rs.getInt("cantidad"));

        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        d.setProducto(p);

        CarritoDeCompras carrito = new CarritoDeCompras();
        carrito.setId(rs.getInt("id_carrito"));
        d.setCarritoDeCompras(carrito);
        return d;
    }

    private void validarDetalle(DetalleCarrito d) throws SQLException {
        if (d.getProducto() == null || d.getProducto().getId() <= 0) {
            throw new SQLException("DetalleCarrito: producto invalido");
        }
        if (d.getCarritoDeCompras() == null || d.getCarritoDeCompras().getId() <= 0) {
            throw new SQLException("DetalleCarrito: carrito invalido");
        }
    }
}
