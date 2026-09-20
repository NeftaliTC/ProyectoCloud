package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Subcategoria;
import pe.edu.pucp.killaDAO.CategoriaDAO;
import pe.edu.pucp.killaDAO.SubCategoriaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubCategoriaDAOImpl implements SubCategoriaDAO {

    private CategoriaDAO categoriaDAO = new CategoriaDAOImpl();

    @Override
    public List<Subcategoria> listAll() throws SQLException {
        List<Subcategoria> lista = new ArrayList<>();
        String sql = "SELECT id_subcategoria, descripcion, activo, id_categoria FROM Subcategoria";

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public List<Subcategoria> listByCategoriaId(Integer idCategoria) throws SQLException {
        List<Subcategoria> lista = new ArrayList<>();
        String sql = "SELECT id_subcategoria, descripcion, activo, id_categoria FROM Subcategoria WHERE id_categoria = ?";

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    @Override
    public Subcategoria load(Integer id) throws SQLException {
        String sql = "SELECT id_subcategoria, descripcion, activo, id_categoria FROM Subcategoria WHERE id_subcategoria = ?";

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
    public Subcategoria save(Subcategoria s) throws SQLException {
        String sql = "INSERT INTO Subcategoria (descripcion, activo, id_categoria) VALUES (?, ?, ?)";

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getDescripcion());
            ps.setBoolean(2, Boolean.TRUE.equals(s.getActivo()));
            ps.setInt(3, s.getCategoria().getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) s.setId(keys.getInt(1));
            }
        }
        return load(s.getId());
    }

    @Override
    public Subcategoria update(Subcategoria s) throws SQLException {
        String sql = "UPDATE Subcategoria SET descripcion = ?, activo = ?, id_categoria = ? WHERE id_subcategoria = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, s.getDescripcion());
            ps.setBoolean(2, Boolean.TRUE.equals(s.getActivo()));
            ps.setInt(3, s.getCategoria().getId());
            ps.setInt(4, s.getId());
            ps.executeUpdate();
        }
        return load(s.getId());
    }

    @Override
    public void remove(Subcategoria s) throws SQLException {
        s.setActivo(false);
        String sql = "UPDATE Subcategoria SET activo = ? WHERE id_subcategoria = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, s.getActivo());
            ps.setInt(2, s.getId());
            ps.executeUpdate();
        }
    }

    private Subcategoria mapRow(ResultSet rs) throws SQLException {
        Subcategoria s = new Subcategoria();
        s.setId(rs.getInt("id_subcategoria"));
        s.setDescripcion(rs.getString("descripcion"));
        s.setActivo(rs.getBoolean("activo"));
        s.setCategoria(categoriaDAO.load(rs.getInt("id_categoria")));
        return s;
    }
}
