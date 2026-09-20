package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Direccion;
import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
import pe.edu.pucp.killaDAO.DireccionDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DireccionDAOImpl implements DireccionDAO {

    @Override
    public List<Direccion> listAll() throws SQLException {
        List<Direccion> lista = new ArrayList<>();
        String sql = "SELECT id_direccion, alias, direccion_detalle, telefono, departamento, provincia, distrito, codigo_postal, referencia, activo, es_predeterminada, id_usuario FROM Direccion WHERE activo = 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Direccion d = new Direccion();
                d.setId(rs.getInt("id_direccion"));
                d.setAlias(rs.getString("alias"));
                d.setDireccionDetalle(rs.getString("direccion_detalle"));
                d.setTelefono(rs.getString("telefono"));
                d.setDepartamento(rs.getString("departamento"));
                d.setProvincia(rs.getString("provincia"));
                d.setDistrito(rs.getString("distrito"));
                d.setCodigoPostal(rs.getString("codigo_postal"));
                d.setReferencia(rs.getString("referencia"));
                d.setActivo(rs.getBoolean("activo"));
                d.setEsPredeterminada(rs.getBoolean("es_predeterminada"));

                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                d.setUsuario(u);

                lista.add(d);
            }
        }
        return lista;
    }

    @Override
    public List<Direccion> listarPorUsuario(Integer idUsuario) throws SQLException {
        List<Direccion> lista = new ArrayList<>();
        String sql = "SELECT id_direccion, alias, direccion_detalle, telefono, departamento, provincia, distrito, codigo_postal, referencia, activo, es_predeterminada, id_usuario FROM Direccion WHERE id_usuario = ? AND activo = 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Direccion d = new Direccion();
                    d.setId(rs.getInt("id_direccion"));
                    d.setAlias(rs.getString("alias"));
                    d.setDireccionDetalle(rs.getString("direccion_detalle"));
                    d.setTelefono(rs.getString("telefono"));
                    d.setDepartamento(rs.getString("departamento"));
                    d.setProvincia(rs.getString("provincia"));
                    d.setDistrito(rs.getString("distrito"));
                    d.setCodigoPostal(rs.getString("codigo_postal"));
                    d.setReferencia(rs.getString("referencia"));
                    d.setActivo(rs.getBoolean("activo"));
                    d.setEsPredeterminada(rs.getBoolean("es_predeterminada"));

                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    d.setUsuario(u);

                    lista.add(d);
                }
            }
        }
        return lista;
    }

    @Override
    public Direccion load(Integer id) throws SQLException {
        String sql = "SELECT id_direccion, alias, direccion_detalle, telefono, departamento, provincia, distrito, codigo_postal, referencia, activo, es_predeterminada, id_usuario FROM Direccion WHERE id_direccion = ? AND activo = 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Direccion d = new Direccion();
                    d.setId(rs.getInt("id_direccion"));
                    d.setAlias(rs.getString("alias"));
                    d.setDireccionDetalle(rs.getString("direccion_detalle"));
                    d.setTelefono(rs.getString("telefono"));
                    d.setDepartamento(rs.getString("departamento"));
                    d.setProvincia(rs.getString("provincia"));
                    d.setDistrito(rs.getString("distrito"));
                    d.setCodigoPostal(rs.getString("codigo_postal"));
                    d.setReferencia(rs.getString("referencia"));
                    d.setActivo(rs.getBoolean("activo"));
                    d.setEsPredeterminada(rs.getBoolean("es_predeterminada"));

                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    d.setUsuario(u);

                    return d;
                }
            }
        }
        return null;
    }

    @Override
    public Direccion save(Direccion d) throws SQLException {
        String sql = "INSERT INTO Direccion (alias, direccion_detalle, telefono, departamento, provincia, distrito, codigo_postal, referencia, activo, es_predeterminada, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getAlias());
            ps.setString(2, d.getDireccionDetalle());
            ps.setString(3, d.getTelefono());
            ps.setString(4, d.getDepartamento());
            ps.setString(5, d.getProvincia());
            ps.setString(6, d.getDistrito());
            ps.setString(7, d.getCodigoPostal());
            ps.setString(8, d.getReferencia());
            ps.setBoolean(9, d.getEsPredeterminada());
            ps.setInt(10, d.getUsuario().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setId(rs.getInt(1));
            }
        }
        return d;
    }

    @Override
    public Direccion update(Direccion d) throws SQLException {
        String sql = "UPDATE Direccion SET alias = ?, direccion_detalle = ?, telefono = ?, departamento = ?, provincia = ?, distrito = ?, codigo_postal = ?, referencia = ?, es_predeterminada = ? WHERE id_direccion = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getAlias());
            ps.setString(2, d.getDireccionDetalle());
            ps.setString(3, d.getTelefono());
            ps.setString(4, d.getDepartamento());
            ps.setString(5, d.getProvincia());
            ps.setString(6, d.getDistrito());
            ps.setString(7, d.getCodigoPostal());
            ps.setString(8, d.getReferencia());
            ps.setBoolean(9, d.getEsPredeterminada());
            ps.setInt(10, d.getId());
            ps.executeUpdate();
        }
        return d;
    }

    @Override
    public void remove(Direccion d) throws SQLException {
        String sql = "UPDATE Direccion SET activo = 0, es_predeterminada = 0 WHERE id_direccion = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, d.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void resetearPredeterminadas(Integer idUsuario) throws SQLException {
        String sql = "UPDATE Direccion SET es_predeterminada = 0 WHERE id_usuario = ? AND activo = 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }
}