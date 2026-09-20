package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Categoria;
import pe.edu.pucp.killaDAO.CategoriaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    @Override
    public List<Categoria> listAll() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, descripcion, activo FROM Categoria";

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) categorias.add(mapRow(rs));
        }
        return categorias;
    }

    @Override
    public Categoria load(Integer id) throws SQLException {
        String sql = "SELECT id_categoria, descripcion, activo FROM Categoria WHERE id_categoria = ?";
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
    public Categoria save(Categoria c) throws SQLException {
        String sql = "INSERT INTO Categoria (descripcion, activo) VALUES (?, ?)";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getDescripcion());
            ps.setBoolean(2, Boolean.TRUE.equals(c.getActivo()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setId(keys.getInt(1));
            }
        }
        return c;
    }

    @Override
    public Categoria update(Categoria c) throws SQLException {
        String sql = "UPDATE Categoria SET descripcion = ?, activo = ? WHERE id_categoria = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getDescripcion());
            ps.setBoolean(2, Boolean.TRUE.equals(c.getActivo()));
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        }
        return c;
    }

    @Override
    public void remove(Categoria c) throws SQLException {
        c.setActivo(false);
        String sql = "UPDATE Categoria SET activo = ? WHERE id_categoria = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, c.getActivo());
            ps.setInt(2, c.getId());
            ps.executeUpdate();
        }
    }

    private Categoria mapRow(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setId(rs.getInt("id_categoria"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setActivo(rs.getBoolean("activo"));
        return c;
    }
}
