package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.killaModelo.CarritoDeCompras;
import pe.edu.pucp.killaBeauty.killaModelo.EstadoCarrito;
import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
import pe.edu.pucp.killaDAO.CarritoDeComprasDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarritoDeComprasDAOImpl implements CarritoDeComprasDAO {

    @Override
    public CarritoDeCompras load(Integer id) throws SQLException {
        String sql = "SELECT id_carrito, fecha_creacion, id_estado_carrito, id_usuario FROM CarritoDeCompras WHERE id_carrito = ?";
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
    public CarritoDeCompras save(CarritoDeCompras carrito) throws SQLException {
        validarCarrito(carrito);
        String sql = "INSERT INTO CarritoDeCompras (fecha_creacion, id_estado_carrito, id_usuario) VALUES (?, ?, ?)";
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setTimestamp(ps, 1, carrito.getFechaDeCreacion());
            ps.setInt(2, carrito.getEstado().getId());
            ps.setInt(3, carrito.getUsuario().getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) carrito.setId(keys.getInt(1));
            }
        }
        return carrito;
    }

    @Override
    public CarritoDeCompras update(CarritoDeCompras carrito) throws SQLException {
        validarCarrito(carrito);
        String sql = "UPDATE CarritoDeCompras SET fecha_creacion = ?, id_estado_carrito = ?, id_usuario = ? WHERE id_carrito = ?";
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            setTimestamp(ps, 1, carrito.getFechaDeCreacion());
            ps.setInt(2, carrito.getEstado().getId());
            ps.setInt(3, carrito.getUsuario().getId());
            ps.setInt(4, carrito.getId());
            ps.executeUpdate();
        }
        return carrito;
    }

    @Override
    public void remove(CarritoDeCompras carrito) throws SQLException {
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM DetalleCarrito WHERE id_carrito = ?")) {
            ps.setInt(1, carrito.getId());
            ps.executeUpdate();
        }
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM CarritoDeCompras WHERE id_carrito = ?")) {
            ps.setInt(1, carrito.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<CarritoDeCompras> listAll() throws SQLException {
        List<CarritoDeCompras> lista = new ArrayList<>();
        String sql = "SELECT id_carrito, fecha_creacion, id_estado_carrito, id_usuario FROM CarritoDeCompras";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public List<CarritoDeCompras> listByUsuarioId(int idUsuario) throws SQLException {
        List<CarritoDeCompras> lista = new ArrayList<>();
        String sql = "SELECT id_carrito, fecha_creacion, id_estado_carrito, id_usuario FROM CarritoDeCompras WHERE id_usuario = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    private CarritoDeCompras mapRow(ResultSet rs) throws SQLException {
        CarritoDeCompras c = new CarritoDeCompras();
        c.setId(rs.getInt("id_carrito"));
        c.setFechaDeCreacion(rs.getTimestamp("fecha_creacion"));
        c.setEstado(EstadoCarrito.fromId(rs.getInt("id_estado_carrito")));

        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        c.setUsuario(u);
        return c;
    }

    private void validarCarrito(CarritoDeCompras carrito) throws SQLException {
        if (carrito.getUsuario() == null || carrito.getUsuario().getId() <= 0) {
            throw new SQLException("Carrito: usuario invalido");
        }
        if (carrito.getEstado() == null) {
            throw new SQLException("Carrito: estado invalido");
        }
    }

    private void setTimestamp(PreparedStatement ps, int index, java.util.Date value) throws SQLException {
        if (value == null) ps.setTimestamp(index, new Timestamp(System.currentTimeMillis()));
        else ps.setTimestamp(index, new Timestamp(value.getTime()));
    }
}
