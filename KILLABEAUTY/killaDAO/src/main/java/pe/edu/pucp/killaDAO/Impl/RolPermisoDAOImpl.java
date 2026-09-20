package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Permiso;
import pe.edu.pucp.killaBeauty.killaModelo.RolPermiso;
import pe.edu.pucp.killaBeauty.killaModelo.TipoUsuario;
import pe.edu.pucp.killaDAO.RolPermisoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolPermisoDAOImpl implements RolPermisoDAO {

    @Override
    public RolPermiso load(Integer id) throws SQLException {
        return null;
    }

    @Override
    public RolPermiso save(RolPermiso rp) throws SQLException {
        String sql = "INSERT INTO RolPermiso (tipo_usuario, id_permiso) VALUES (?, ?)";
        try (PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, rp.getTipoUsuario().name());
            ps.setInt(2, rp.getPermiso().getId());
            ps.executeUpdate();
        }
        return rp;
    }

    @Override
    public RolPermiso update(RolPermiso rp) throws SQLException {
        // Normalmente no se actualiza un rol_permiso, se elimina y se inserta de nuevo
        return rp;
    }

    @Override
    public void remove(RolPermiso rp) throws SQLException {
        String sql = "DELETE FROM RolPermiso WHERE tipo_usuario = ? AND id_permiso = ?";
        try (PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, rp.getTipoUsuario().name());
            ps.setInt(2, rp.getPermiso().getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<RolPermiso> listByTipoUsuario(int idTipoUsuario) throws SQLException {
        List<RolPermiso> lista = new ArrayList<>();
        String sql = "SELECT * FROM RolPermiso WHERE tipo_usuario = ?";
        TipoUsuario tipoUsuarioEnum = TipoUsuario.values()[idTipoUsuario];
        try (PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, tipoUsuarioEnum.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RolPermiso rp = new RolPermiso();
                    TipoUsuario tu = TipoUsuario.valueOf(rs.getString("tipo_usuario"));
                    Permiso p = new Permiso();
                    p.setId(rs.getInt("id_permiso"));
                    rp.setTipoUsuario(tu);
                    rp.setPermiso(p);
                    lista.add(rp);
                }
            }
        }
        return lista;
    }

    @Override
    public List<RolPermiso> listAll() throws SQLException {
        List<RolPermiso> lista = new ArrayList<>();
        String sql = "SELECT * FROM RolPermiso";
        try (PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RolPermiso rp = new RolPermiso();
                TipoUsuario tu = TipoUsuario.valueOf(rs.getString("tipo_usuario"));
                Permiso p = new Permiso();
                p.setId(rs.getInt("id_permiso"));
                rp.setTipoUsuario(tu);
                rp.setPermiso(p);
                lista.add(rp);
            }
        }
        return lista;
    }
}
