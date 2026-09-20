package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.MetodoPago;
import pe.edu.pucp.killaBeauty.killaModelo.Pago;
import pe.edu.pucp.killaBeauty.killaModelo.Pedido;
import pe.edu.pucp.killaDAO.PagoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAOImpl implements PagoDAO {
    @Override
    public List<Pago> listAll() throws SQLException {
        List<Pago> lista = new ArrayList<>();
        String sql = """
                SELECT p.id_pago, p.monto_pagado, p.fecha_hora_pago, p.estado, p.id_pedido,
                       p.id_metodo_pago, m.nombre AS nombre_metodo
                FROM Pago p
                JOIN MetodoPago m ON p.id_metodo_pago = m.id_metodo_pago
                """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public Pago buscarPorIdPedido(Integer idPedido) throws SQLException {
        String sql = """
                SELECT p.id_pago, p.monto_pagado, p.fecha_hora_pago, p.estado, p.id_pedido,
                       p.id_metodo_pago, m.nombre AS nombre_metodo
                FROM Pago p
                JOIN MetodoPago m ON p.id_metodo_pago = m.id_metodo_pago
                WHERE p.id_pedido = ?
                """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public Pago load(Integer id) throws SQLException {
        String sql = """
                SELECT p.id_pago, p.monto_pagado, p.fecha_hora_pago, p.estado, p.id_pedido,
                       p.id_metodo_pago, m.nombre AS nombre_metodo
                FROM Pago p
                JOIN MetodoPago m ON p.id_metodo_pago = m.id_metodo_pago
                WHERE p.id_pedido = ?
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
    public Pago save(Pago p) throws SQLException {
        String sql = """
                INSERT INTO Pago (monto_pagado, fecha_hora_pago, estado, id_pedido, id_metodo_pago)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, p.getMontoPagado());
            ps.setTimestamp(2, new Timestamp(p.getFechaHoraPago().getTime()));
            ps.setBoolean(3, p.isEstado());
            ps.setInt(4, p.getPedido().getId());
            ps.setInt(5, p.getMetodoPago().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setIdPago(rs.getInt(1));
            }
        }
        return p;
    }

    @Override
    public Pago update(Pago p) throws SQLException {
        String sql = """
                UPDATE Pago SET monto_pagado = ?, estado = ?, id_metodo_pago = ?
                WHERE id_pago = ?
                """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, p.getMontoPagado());
            ps.setBoolean(2, p.isEstado());
            ps.setInt(3, p.getMetodoPago().getId());
            ps.setInt(4, p.getIdPago());
            ps.executeUpdate();
        }
        return p;
    }

    @Override
    public void remove(Pago pago) throws SQLException {
        throw new UnsupportedOperationException("No se permite eliminar registros de pago por auditoría.");
    }

    private Pago mapRow(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.setIdPago(rs.getInt("id_pago"));
        p.setMontoPagado(rs.getDouble("monto_pagado"));
        p.setFechaHoraPago(rs.getTimestamp("fecha_hora_pago"));
        p.setEstado(rs.getBoolean("estado"));

        Pedido ped = new Pedido();
        ped.setId(rs.getInt("id_pedido"));
        p.setPedido(ped);

        int idMetodo = rs.getInt("id_metodo_pago");
        p.setMetodoPago(MetodoPago.fromId(idMetodo));
        return p;
    }
}
