package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.ClienteCupon;
import pe.edu.pucp.killaBeauty.killaModelo.Pedido;
import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;
import pe.edu.pucp.killaDAO.ClienteCuponDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteCuponDAOImpl implements ClienteCuponDAO {

    @Override
    public List<ClienteCupon> listAll() throws SQLException {
        List<ClienteCupon> lista = new ArrayList<>();
        String sql = "SELECT id_cliente_cupon, fecha_uso, usado, id_pedido, id_cupon, id_usuario FROM ClienteCupon";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public List<ClienteCupon> listByUsuarioId(Integer idUsuario) throws SQLException {
        List<ClienteCupon> lista = new ArrayList<>();
        String sql = "SELECT id_cliente_cupon, fecha_uso, usado, id_pedido, id_cupon, id_usuario FROM ClienteCupon WHERE id_usuario = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    @Override
    public ClienteCupon load(Integer id) throws SQLException {
        String sql = "SELECT id_cliente_cupon, fecha_uso, usado, id_pedido, id_cupon, id_usuario FROM ClienteCupon WHERE id_cliente_cupon = ?";
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
    public ClienteCupon save(ClienteCupon clienteCupon) throws SQLException {
        String sql = "INSERT INTO ClienteCupon (fecha_uso, usado, id_pedido, id_cupon, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, clienteCupon);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) clienteCupon.setId(keys.getInt(1));
            }
        }
        return clienteCupon;
    }

    @Override
    public ClienteCupon update(ClienteCupon clienteCupon) throws SQLException {
        String sql = "UPDATE ClienteCupon SET fecha_uso = ?, usado = ?, id_pedido = ?, id_cupon = ?, id_usuario = ? WHERE id_cliente_cupon = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            setParams(ps, clienteCupon);
            ps.setInt(6, clienteCupon.getId());
            ps.executeUpdate();
        }
        return clienteCupon;
    }

    @Override
    public void remove(ClienteCupon clienteCupon) throws SQLException {
        String sql = "DELETE FROM ClienteCupon WHERE id_cliente_cupon = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, clienteCupon.getId());
            ps.executeUpdate();
        }
    }

    private void setParams(PreparedStatement ps, ClienteCupon clienteCupon) throws SQLException {
        if (clienteCupon.getFechaUso() == null) ps.setNull(1, Types.TIMESTAMP);
        else ps.setTimestamp(1, new Timestamp(clienteCupon.getFechaUso().getTime()));
        ps.setBoolean(2, Boolean.TRUE.equals(clienteCupon.getUsado()));
        if (clienteCupon.getPedido() != null && clienteCupon.getPedido().getId() > 0) ps.setInt(3, clienteCupon.getPedido().getId());
        else ps.setNull(3, Types.INTEGER);
        ps.setInt(4, clienteCupon.getCupon().getId());
        ps.setInt(5, clienteCupon.getUsuario().getId());
    }

    private ClienteCupon mapRow(ResultSet rs) throws SQLException {
        ClienteCupon clienteCupon = new ClienteCupon();
        clienteCupon.setId(rs.getInt("id_cliente_cupon"));
        clienteCupon.setFechaUso(rs.getTimestamp("fecha_uso"));
        clienteCupon.setUsado(rs.getBoolean("usado"));

        int idPedido = rs.getInt("id_pedido");
        if (!rs.wasNull()) {
            Pedido pedido = new Pedido();
            pedido.setId(idPedido);
            clienteCupon.setPedido(pedido);
        }

        Cupon cupon = new Cupon();
        cupon.setId(rs.getInt("id_cupon"));
        clienteCupon.setCupon(cupon);

        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id_usuario"));
        clienteCupon.setUsuario(usuario);
        return clienteCupon;
    }
}
