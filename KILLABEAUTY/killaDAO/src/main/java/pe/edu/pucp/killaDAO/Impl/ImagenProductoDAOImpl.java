package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.ImagenProducto;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;
import pe.edu.pucp.killaDAO.ImagenProductoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImagenProductoDAOImpl implements ImagenProductoDAO {

    @Override
    public List<ImagenProducto> listAll() throws SQLException {
        List<ImagenProducto> lista = new ArrayList<>();
        String sql = "SELECT id_imagen, url, titulo, orden, principal, activo, id_producto FROM ImagenProducto";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public List<ImagenProducto> listByProductoId(Integer idProducto) throws SQLException {
        List<ImagenProducto> lista = new ArrayList<>();
        String sql = "SELECT id_imagen, url, titulo, orden, principal, activo, id_producto FROM ImagenProducto WHERE id_producto = ? ORDER BY orden";
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
    public ImagenProducto load(Integer id) throws SQLException {
        String sql = "SELECT id_imagen, url, titulo, orden, principal, activo, id_producto FROM ImagenProducto WHERE id_imagen = ?";
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
    public ImagenProducto save(ImagenProducto imagen) throws SQLException {
        String sql = "INSERT INTO ImagenProducto (url, titulo, orden, principal, activo, id_producto) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, imagen);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) imagen.setId(keys.getInt(1));
            }
        }
        return imagen;
    }

    @Override
    public ImagenProducto update(ImagenProducto imagen) throws SQLException {
        String sql = "UPDATE ImagenProducto SET url = ?, titulo = ?, orden = ?, principal = ?, activo = ?, id_producto = ? WHERE id_imagen = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            setParams(ps, imagen);
            ps.setInt(7, imagen.getId());
            ps.executeUpdate();
        }
        return imagen;
    }

    @Override
    public void remove(ImagenProducto imagen) throws SQLException {
        imagen.setActivo(false);
        String sql = "UPDATE ImagenProducto SET activo = ? WHERE id_imagen = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, imagen.getActivo());
            ps.setInt(2, imagen.getId());
            ps.executeUpdate();
        }
    }

    private void setParams(PreparedStatement ps, ImagenProducto imagen) throws SQLException {
        ps.setString(1, imagen.getUrl());
        ps.setString(2, imagen.getTitulo());
        ps.setInt(3, imagen.getOrden());
        ps.setBoolean(4, Boolean.TRUE.equals(imagen.getPrincipal()));
        ps.setBoolean(5, Boolean.TRUE.equals(imagen.getActivo()));
        ps.setInt(6, imagen.getProducto().getId());
    }

    private ImagenProducto mapRow(ResultSet rs) throws SQLException {
        ImagenProducto imagen = new ImagenProducto();
        imagen.setId(rs.getInt("id_imagen"));
        imagen.setUrl(rs.getString("url"));
        imagen.setTitulo(rs.getString("titulo"));
        imagen.setOrden(rs.getInt("orden"));
        imagen.setPrincipal(rs.getBoolean("principal"));
        imagen.setActivo(rs.getBoolean("activo"));

        Producto producto = new Producto();
        producto.setId(rs.getInt("id_producto"));
        imagen.setProducto(producto);
        return imagen;
    }

    public ImagenProducto getPrincipalByProductoId(Integer idProducto) throws SQLException {
        String sql = """
        SELECT id_imagen, url, titulo, orden, principal, activo, id_producto
        FROM ImagenProducto
        WHERE id_producto = ?
          AND principal = 1
          AND activo = 1
        LIMIT 1
    """;

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }

        return null;
    }

    @Override
    public void quitarPrincipalPorProducto(Integer idProducto) throws SQLException {
        String sql = "UPDATE ImagenProducto SET principal = 0 WHERE id_producto = ?";

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.executeUpdate();
        }
    }
}
