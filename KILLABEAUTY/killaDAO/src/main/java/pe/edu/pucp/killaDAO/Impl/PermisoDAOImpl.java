package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Permiso;
import pe.edu.pucp.killaDAO.PermisoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermisoDAOImpl implements PermisoDAO {

    @Override
    public Permiso load(Integer id) throws SQLException {
        String sql = "SELECT * FROM Permiso WHERE id_permiso = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Permiso p = new Permiso();
                    p.setId(rs.getInt("id_permiso"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setActivo(rs.getBoolean("activo"));
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public Permiso save(Permiso p) throws SQLException {
        String sql = "INSERT INTO Permiso (nombre, descripcion, activo) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setBoolean(3, p.getActivo() != null ? p.getActivo() : true);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) p.setId(rs.getInt(1));
            }
        }
        return p;
    }

    @Override
    public Permiso update(Permiso p) throws SQLException {
        String sql = "UPDATE Permiso SET nombre = ?, descripcion = ?, activo = ? WHERE id_permiso = ?";
        try (PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setBoolean(3, p.getActivo());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
        return p;
    }

    @Override
    public void remove(Permiso p) throws SQLException {
        p.setActivo(false);
        String sql = "UPDATE Permiso SET activo = ? WHERE id_permiso = ?";
        try (PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(sql)) {
            ps.setBoolean(1, p.getActivo());
            ps.setInt(2, p.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Permiso> listAll() throws SQLException {
        List<Permiso> lista = new ArrayList<>();
        String sql = "SELECT * FROM Permiso";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                Permiso p = new Permiso();
                p.setId(rs.getInt("id_permiso"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setActivo(rs.getBoolean("activo"));
                lista.add(p);
            }
        }
        return lista;
    }

    @Override
    public List<Permiso> listByNombre(String nombre) throws SQLException {
        List<Permiso> lista = new ArrayList<>();
        String sql = "SELECT * FROM Permiso WHERE nombre LIKE ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Permiso p = new Permiso();
                    p.setId(rs.getInt("id_permiso"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setActivo(rs.getBoolean("activo"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}
