package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;
import pe.edu.pucp.killaBeauty.killaModelo.Resena;
import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
import pe.edu.pucp.killaDAO.ResenaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResenaDAOImpl implements ResenaDAO {

    @Override
    public Resena load(Integer id) throws SQLException {
        String sql = "SELECT id_resena, titulo, comentario, calificacion, verificado, fecha_publicacion, activo, id_usuario, id_producto FROM Resena WHERE id_resena = ?";
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
    public Resena save(Resena r) throws SQLException {
        String sql = """
                INSERT INTO Resena
                (titulo, comentario, calificacion, verificado, fecha_publicacion, activo, id_usuario, id_producto)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, r);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setId(keys.getInt(1));
            }
        }
        return r;
    }

    @Override
    public Resena update(Resena r) throws SQLException {
        String sql = """
                UPDATE Resena
                SET titulo = ?, comentario = ?, calificacion = ?, verificado = ?,
                    fecha_publicacion = ?, activo = ?, id_usuario = ?, id_producto = ?
                WHERE id_resena = ?
                """;
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            setParams(ps, r);
            ps.setInt(9, r.getId());
            ps.executeUpdate();
        }
        return r;
    }

    @Override
    public void remove(Resena r) throws SQLException {
        r.setActivo(false);
        String sql = "UPDATE Resena SET activo = ? WHERE id_resena = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, r.getActivo());
            ps.setInt(2, r.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Resena> listByProductoId(int idProducto) throws SQLException {
        List<Resena> lista = new ArrayList<>();
        String sql = """
            SELECT r.id_resena, r.titulo, r.comentario, r.calificacion, r.verificado,\s
                       r.fecha_publicacion, r.activo, r.id_usuario, r.id_producto,
                       u.nombre, u.apellido_paterno
                FROM Resena r
                INNER JOIN Usuario u ON r.id_usuario = u.id_usuario
                WHERE r.id_producto = ?
            """;
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Resena> listByUsuarioId(int idUsuario) throws SQLException {
        List<Resena> lista = new ArrayList<>();
        String sql = "SELECT id_resena, titulo, comentario, calificacion, verificado, fecha_publicacion, activo, id_usuario, id_producto FROM Resena WHERE id_usuario = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    private void setParams(PreparedStatement ps, Resena r) throws SQLException {
        ps.setString(1, r.getTitulo());
        ps.setString(2, r.getComentario());
        ps.setInt(3, r.getCalificacion());
        ps.setBoolean(4, r.isVerificado());
        if (r.getFechaPublicacion() == null) ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
        else ps.setTimestamp(5, new Timestamp(r.getFechaPublicacion().getTime()));
        ps.setBoolean(6, r.getActivo() != null ? r.getActivo() : true);
        ps.setInt(7, r.getCliente().getId());
        ps.setInt(8, r.getProducto().getId());
    }

    private Resena mapRow(ResultSet rs) throws SQLException {
        Resena r = new Resena();
        r.setId(rs.getInt("id_resena"));
        r.setTitulo(rs.getString("titulo"));
        r.setComentario(rs.getString("comentario"));
        r.setCalificacion(rs.getInt("calificacion"));
        r.setVerificado(rs.getBoolean("verificado"));
        r.setFechaPublicacion(rs.getTimestamp("fecha_publicacion"));
        r.setActivo(rs.getBoolean("activo"));

        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id_usuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellidoPaterno(rs.getString("apellido_paterno"));
        r.setCliente(usuario);

        Producto producto = new Producto();
        producto.setId(rs.getInt("id_producto"));
        r.setProducto(producto);
        return r;
    }
}