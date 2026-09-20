package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Courier;
import pe.edu.pucp.killaBeauty.killaModelo.Envio;
import pe.edu.pucp.killaBeauty.killaModelo.EstadoEnvio;
import pe.edu.pucp.killaBeauty.killaModelo.Pedido;
import pe.edu.pucp.killaDAO.EnvioDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnvioDAOImpl implements EnvioDAO {
    @Override
    public List<Envio> listAll() throws SQLException {
        List<Envio> lista = new ArrayList<>();
        String sql = """
                SELECT id_envio, descripcion, costo_envio, fecha_envio, id_pedido,
                       id_estado_envio, id_courier, numero_seguimiento
                FROM Envio
                """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    private Envio mapRowConCourier(ResultSet rs) throws SQLException {
        Envio e = new Envio();
        e.setId(rs.getInt("id_envio"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setCostoEnvio(rs.getDouble("costo_envio"));
        e.setFechaEnvio(rs.getTimestamp("fecha_envio"));
        e.setNumeroSeguimiento(rs.getString("numero_seguimiento"));
        e.setEstadoEnvio(EstadoEnvio.fromId(rs.getInt("id_estado_envio")));

        Pedido p = new Pedido();
        p.setId(rs.getInt("id_pedido"));
        e.setPedido(p);

        Courier c = new Courier();
        c.setId(rs.getInt("id_courier"));
        c.setNombre(rs.getString("nombre_courier"));
        e.setCourier(c);
        return e;
    }

    @Override
    public Envio buscarPorIdPedido(Integer idPedido) throws SQLException {
        String sql = """
            SELECT e.id_envio, e.descripcion, e.costo_envio, e.fecha_envio, e.id_pedido,
                   e.id_estado_envio, e.numero_seguimiento,
                   c.id_courier, c.nombre AS nombre_courier
            FROM Envio e
            LEFT JOIN Courier c ON e.id_courier = c.id_courier
            WHERE e.id_pedido = ?
            """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowConCourier(rs);
            }
        }
        return null;
    }

    @Override
    public Envio load(Integer id) throws SQLException {
        String sql = """
                SELECT id_envio, descripcion, costo_envio, fecha_envio, id_pedido,
                       id_estado_envio, id_courier, numero_seguimiento
                FROM Envio WHERE id_envio = ?
                """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public Envio save(Envio e) throws SQLException {
        validarEnvio(e);
        String sql = """
                INSERT INTO Envio (descripcion, costo_envio, fecha_envio, id_pedido,
                                   id_estado_envio, id_courier, numero_seguimiento)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setEnvioParams(ps, e);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getInt(1));
            }
        }
        return e;
    }

    @Override
    public Envio update(Envio e) throws SQLException {
        validarEnvio(e);
        String sql = """
                UPDATE Envio
                SET descripcion = ?, costo_envio = ?, fecha_envio = ?, id_pedido = ?,
                    id_estado_envio = ?, id_courier = ?, numero_seguimiento = ?
                WHERE id_envio = ?
                """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            setEnvioParams(ps, e);
            ps.setInt(8, e.getId());
            ps.executeUpdate();
        }
        return e;
    }

    @Override
    public void remove(Envio e) throws SQLException {
        String sql = "DELETE FROM Envio WHERE id_envio = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, e.getId());
            ps.executeUpdate();
        }
    }

    private void setEnvioParams(PreparedStatement ps, Envio e) throws SQLException {
        ps.setString(1, e.getDescripcion());
        ps.setDouble(2, e.getCostoEnvio());
        if (e.getFechaEnvio() == null) ps.setNull(3, Types.TIMESTAMP);
        else ps.setTimestamp(3, new Timestamp(e.getFechaEnvio().getTime()));
        ps.setInt(4, e.getPedido().getId());
        ps.setInt(5, e.getEstadoEnvio().getId());
        ps.setInt(6, e.getCourier().getId());
        ps.setString(7, e.getNumeroSeguimiento());
    }

    private Envio mapRow(ResultSet rs) throws SQLException {
        Envio e = new Envio();
        e.setId(rs.getInt("id_envio"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setCostoEnvio(rs.getDouble("costo_envio"));
        e.setFechaEnvio(rs.getTimestamp("fecha_envio"));
        e.setNumeroSeguimiento(rs.getString("numero_seguimiento"));
        e.setEstadoEnvio(EstadoEnvio.fromId(rs.getInt("id_estado_envio")));

        Pedido p = new Pedido();
        p.setId(rs.getInt("id_pedido"));
        e.setPedido(p);

        Courier c = new Courier();
        c.setId(rs.getInt("id_courier"));
        e.setCourier(c);
        return e;
    }

    private void validarEnvio(Envio e) throws SQLException {
        if (e.getPedido() == null || e.getPedido().getId() <= 0) throw new SQLException("Envio: pedido invalido");
        if (e.getCourier() == null || e.getCourier().getId() <= 0) throw new SQLException("Envio: courier invalido");
        if (e.getEstadoEnvio() == null) throw new SQLException("Envio: estado invalido");
    }
}
