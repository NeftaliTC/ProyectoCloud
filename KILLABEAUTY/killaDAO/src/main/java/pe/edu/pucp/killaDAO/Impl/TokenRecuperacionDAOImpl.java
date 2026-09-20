package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.killaModelo.TokenRecuperacion;
import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
import pe.edu.pucp.killaDAO.TokenRecuperacionDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TokenRecuperacionDAOImpl implements TokenRecuperacionDAO {

    @Override
    public TokenRecuperacion load(Integer id) throws SQLException {
        String sql = "SELECT * FROM TokenRecuperacion WHERE id_token = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    TokenRecuperacion t = new TokenRecuperacion();
                    t.setId(rs.getInt("id_token"));
                    t.setToken(rs.getString("token"));
                    t.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    t.setFechaExpiracion(rs.getTimestamp("fecha_expiracion"));
                    t.setUsado(rs.getBoolean("usado"));
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    t.setUsuario(u);
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public TokenRecuperacion save(TokenRecuperacion t) throws SQLException {
        String sql = "INSERT INTO TokenRecuperacion (token, id_usuario, fecha_creacion, fecha_expiracion, usado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = TransactionContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getToken());
            ps.setInt(2, t.getUsuario().getId());
            ps.setTimestamp(3, new Timestamp(t.getFechaCreacion().getTime()));
            ps.setTimestamp(4, new Timestamp(t.getFechaExpiracion().getTime()));
            ps.setBoolean(5, t.getUsado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) t.setId(rs.getInt(1));
            }
        }
        return t;
    }

    @Override
    public TokenRecuperacion update(TokenRecuperacion t) throws SQLException {
        String sql = "UPDATE TokenRecuperacion SET token = ?, id_usuario = ?, fecha_creacion = ?, fecha_expiracion = ?, usado = ? WHERE id_token = ?";
        try (PreparedStatement ps = TransactionContext.getConnection().prepareStatement(sql)) {
            ps.setString(1, t.getToken());
            ps.setInt(2, t.getUsuario().getId());
            ps.setTimestamp(3, new Timestamp(t.getFechaCreacion().getTime()));
            ps.setTimestamp(4, new Timestamp(t.getFechaExpiracion().getTime()));
            ps.setBoolean(5, t.getUsado());
            ps.setInt(6, t.getId());
            ps.executeUpdate();
        }
        return t;
    }

    @Override
    public void remove(TokenRecuperacion t) throws SQLException {
        String sql = "DELETE FROM TokenRecuperacion WHERE id_token = ?";
        try (PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, t.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public TokenRecuperacion getValidToken(int usuarioId, String token) throws SQLException {
        String sql = "SELECT * FROM TokenRecuperacion WHERE id_usuario = ? AND token = ? AND usado = FALSE AND fecha_expiracion >= NOW()";
        try (PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, token);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    TokenRecuperacion t = new TokenRecuperacion();
                    t.setId(rs.getInt("id_token"));
                    t.setToken(rs.getString("token"));
                    t.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    t.setFechaExpiracion(rs.getTimestamp("fecha_expiracion"));
                    t.setUsado(rs.getBoolean("usado"));
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    t.setUsuario(u);
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public List<TokenRecuperacion> listByUsuarioId(int usuarioId) throws SQLException {
        List<TokenRecuperacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM TokenRecuperacion WHERE id_usuario = ?";
        try (PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    TokenRecuperacion t = new TokenRecuperacion();
                    t.setId(rs.getInt("id_token"));
                    t.setToken(rs.getString("token"));
                    t.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    t.setFechaExpiracion(rs.getTimestamp("fecha_expiracion"));
                    t.setUsado(rs.getBoolean("usado"));
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    t.setUsuario(u);
                    lista.add(t);
                }
            }
        }
        return lista;
    }
}
