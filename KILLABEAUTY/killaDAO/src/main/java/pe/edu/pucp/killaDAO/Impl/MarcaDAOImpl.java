package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Marca;
import pe.edu.pucp.killaBeauty.killaModelo.Pais;
import pe.edu.pucp.killaDAO.MarcaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAOImpl implements MarcaDAO {

    @Override
    public List<Marca> listAll() throws SQLException {
        List<Marca> lista = new ArrayList<>();
        String sql = "SELECT id_marca, descripcion, id_pais, activo FROM Marca";

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public Marca load(Integer id) throws SQLException {
        String sql = "SELECT id_marca, descripcion, id_pais, activo FROM Marca WHERE id_marca = ?";
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
    public Marca save(Marca m) throws SQLException {
        String sql = "INSERT INTO Marca (descripcion, id_pais, activo) VALUES (?, ?, ?)";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getDescripcion());
            ps.setInt(2, m.getPais().getId());
            ps.setBoolean(3, Boolean.TRUE.equals(m.getActivo()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) m.setId(keys.getInt(1));
            }
        }
        return m;
    }

    @Override
    public Marca update(Marca m) throws SQLException {
        String sql = "UPDATE Marca SET descripcion = ?, id_pais = ?, activo = ? WHERE id_marca = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, m.getDescripcion());
            ps.setInt(2, m.getPais().getId());
            ps.setBoolean(3, Boolean.TRUE.equals(m.getActivo()));
            ps.setInt(4, m.getId());
            ps.executeUpdate();
        }
        return m;
    }

    @Override
    public void remove(Marca m) throws SQLException {
        m.setActivo(false);
        String sql = "UPDATE Marca SET activo = ? WHERE id_marca = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, m.getActivo());
            ps.setInt(2, m.getId());
            ps.executeUpdate();
        }
    }

    private Marca mapRow(ResultSet rs) throws SQLException {
        Marca m = new Marca();
        m.setId(rs.getInt("id_marca"));
        m.setDescripcion(rs.getString("descripcion"));
        m.setPais(paisFromId(rs.getInt("id_pais")));
        m.setActivo(rs.getBoolean("activo"));
        return m;
    }

    private Pais paisFromId(int id) throws SQLException {
        for (Pais pais : Pais.values()) {
            if (pais.getId() == id) return pais;
        }
        throw new SQLException("Pais no reconocido con id: " + id);
    }
}
