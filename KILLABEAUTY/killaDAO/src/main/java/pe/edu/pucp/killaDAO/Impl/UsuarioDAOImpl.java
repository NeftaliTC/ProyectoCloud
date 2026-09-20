package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.TipoUsuario;
import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
import pe.edu.pucp.killaDAO.UsuarioDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario load(Integer id) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE id_usuario = ?";
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
    public Usuario save(Usuario u) throws SQLException {
        String sql = """
                INSERT INTO Usuario
                (nombre, apellido_paterno, apellido_materno, correo_electronico, fecha_nacimiento,
                 genero, fecha_inscripcion, contrasena, telefono, activo, id_tipo_usuario, ultimo_acceso, dni)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setUsuarioParams(ps, u);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setId(keys.getInt(1));
            }
        }
        return u;
    }

    @Override
    public Usuario update(Usuario u) throws SQLException {
        String sql = """
                UPDATE Usuario
                SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, correo_electronico = ?,
                    fecha_nacimiento = ?, genero = ?, fecha_inscripcion = ?, contrasena = ?, telefono = ?,
                    activo = ?, id_tipo_usuario = ?, ultimo_acceso = ?, dni = ?
                WHERE id_usuario = ?
                """;
        try (Connection cn = DBManager.getInstance().getConnection();
            PreparedStatement ps = cn.prepareStatement(sql)) {
            setUsuarioParams(ps, u);
            ps.setInt(14, u.getId());
            ps.executeUpdate();
        }
        return u;
    }

    @Override
    public void remove(Usuario u) throws SQLException {
        u.setActivo(false);
        String sql = "UPDATE Usuario SET activo = ? WHERE id_usuario = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, u.getActivo());
            ps.setInt(2, u.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Usuario> listByTipoUsuario(int idTipoUsuario) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario WHERE id_tipo_usuario = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idTipoUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    @Override
    public void actualizarUltimoAcceso(int idUsuario) throws SQLException {
        String sql = "UPDATE Usuario SET ultimo_acceso = NOW() WHERE id_usuario = ?";

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    @Override
    public Usuario loadByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE correo_electronico = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    private void setUsuarioParams(PreparedStatement ps, Usuario u) throws SQLException {
        ps.setString(1, u.getNombre());
        ps.setString(2, u.getApellidoPaterno());
        ps.setString(3, u.getApellidoMaterno());
        ps.setString(4, u.getCorreoElectronico());
        ps.setObject( 5, u.getFechaNacimiento());
        ps.setString(6, normalizarGenero(u.getGenero()));
        setTimestamp(ps, 7, u.getFechaDeInscripcion());
        ps.setString(8, u.getContrasena());
        ps.setString(9, u.getTelefono());
        ps.setBoolean(10, Boolean.TRUE.equals(u.getActivo()));
        if (u.getTipoUsuario() != null) ps.setInt(11, u.getTipoUsuario().getId());
        else ps.setNull(11, Types.INTEGER);
        setTimestamp(ps, 12, u.getUltimoAcceso());
        ps.setString(13, u.getDni());
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setApellidoPaterno(rs.getString("apellido_paterno"));
        u.setApellidoMaterno(rs.getString("apellido_materno"));
        u.setCorreoElectronico(rs.getString("correo_electronico"));
        u.setFechaNacimiento(rs.getObject("fecha_nacimiento", LocalDate.class));
        u.setGenero(rs.getString("genero"));
        u.setFechaDeInscripcion(rs.getTimestamp("fecha_inscripcion"));
        u.setContrasena(rs.getString("contrasena"));
        u.setTelefono(rs.getString("telefono"));
        u.setActivo(rs.getBoolean("activo"));
        u.setTipoUsuario(TipoUsuario.fromId(rs.getInt("id_tipo_usuario")));
        u.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));
        u.setDni(rs.getString("dni"));
        return u;
    }

    private void setDate(PreparedStatement ps, int index, java.util.Date value) throws SQLException {
        if (value == null) ps.setNull(index, Types.DATE);
        else ps.setDate(index, new java.sql.Date(value.getTime()));
    }

    private void setTimestamp(PreparedStatement ps, int index, java.util.Date value) throws SQLException {
        if (value == null) ps.setNull(index, Types.TIMESTAMP);
        else ps.setTimestamp(index, new Timestamp(value.getTime()));
    }

    private String normalizarGenero(String genero) {
        if (genero == null || genero.isBlank() || genero.equalsIgnoreCase("Selecciona")) {
            return null;
        }
        return genero;
    }
}
