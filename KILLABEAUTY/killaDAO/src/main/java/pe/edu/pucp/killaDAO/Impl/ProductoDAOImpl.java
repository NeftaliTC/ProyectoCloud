package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Categoria;
import pe.edu.pucp.killaBeauty.killaModelo.Marca;
import pe.edu.pucp.killaBeauty.killaModelo.Pais;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;
import pe.edu.pucp.killaBeauty.killaModelo.Subcategoria;
import pe.edu.pucp.killaDAO.ImagenProductoDAO;
import pe.edu.pucp.killaDAO.MarcaDAO;
import pe.edu.pucp.killaDAO.ProductoDAO;
import pe.edu.pucp.killaDAO.SubCategoriaDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoDAOImpl implements ProductoDAO {

    ImagenProductoDAO imagenDAO = new ImagenProductoDAOImpl();
    private MarcaDAO marcaDAO = new MarcaDAOImpl();
    private SubCategoriaDAO subCategoriaDAO = new SubCategoriaDAOImpl();

    @Override
    public List<Producto> listAll() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = """
                SELECT p.id_producto, p.nombre, p.precio_base, p.stock, p.disponible, p.promocion, p.activo,
                       m.id_marca, m.descripcion AS marca_descripcion, m.id_pais, m.activo AS marca_activo,
                       s.id_subcategoria, s.descripcion AS subcategoria_descripcion, s.activo AS subcategoria_activo,
                       c.id_categoria, c.descripcion AS categoria_descripcion, c.activo AS categoria_activo
                FROM Producto p
                INNER JOIN Marca m ON p.id_marca = m.id_marca
                INNER JOIN Subcategoria s ON p.id_subcategoria = s.id_subcategoria
                INNER JOIN Categoria c ON s.id_categoria = c.id_categoria
                """;

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = mapRowConCatalogos(rs);
                p.setImagenes(imagenDAO.listByProductoId(p.getId()));
                lista.add(p);
            }
        }
        return lista;
    }

    @Override
    public Producto load(Integer id) throws SQLException {
        String sql = "SELECT id_producto, nombre, precio_base, stock, disponible, promocion, activo, id_marca, id_subcategoria FROM Producto WHERE id_producto = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs, new HashMap<>(), new HashMap<>(), true);
                }
            }
        }
        return null;
    }

    @Override
    public Producto save(Producto p) throws SQLException {
        String sql = "INSERT INTO Producto (nombre, precio_base, stock, disponible, promocion, activo, id_marca, id_subcategoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecioBase());
            ps.setInt(3, p.getStock());
            ps.setBoolean(4, p.getDisponible());
            ps.setBoolean(5, p.getPromocion());
            ps.setBoolean(6, p.getActivo());
            ps.setInt(7, p.getMarca().getId());
            ps.setInt(8, p.getSubcategoria().getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setId(keys.getInt(1));
            }
        }
        return load(p.getId());
    }

    @Override
    public Producto update(Producto p) throws SQLException {
        String sql = "UPDATE Producto SET nombre = ?, precio_base = ?, stock = ?, disponible = ?, promocion = ?, activo = ?, id_marca = ?, id_subcategoria = ? WHERE id_producto = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecioBase());
            ps.setInt(3, p.getStock());
            ps.setBoolean(4, p.getDisponible());
            ps.setBoolean(5, p.getPromocion());
            ps.setBoolean(6, p.getActivo());
            ps.setInt(7, p.getMarca().getId());
            ps.setInt(8, p.getSubcategoria().getId());
            ps.setInt(9, p.getId());
            ps.executeUpdate();
        }
        return load(p.getId());
    }

    @Override
    public void remove(Producto p) throws SQLException {
        p.setActivo(false);
        String sql = "UPDATE Producto SET activo = ? WHERE id_producto = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, p.getActivo());
            ps.setInt(2, p.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void descontarStock(Integer idProducto, Integer cantidad) throws SQLException {
        String sql = """
                UPDATE Producto
                SET stock = stock - ?,
                    disponible = CASE WHEN stock - ? > 0 THEN disponible ELSE 0 END
                WHERE id_producto = ? AND stock >= ?
                """;
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, cantidad);
            ps.setInt(3, idProducto);
            ps.setInt(4, cantidad);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No hay stock suficiente para el producto " + idProducto + ".");
            }
        }
    }

    private Producto mapRow(ResultSet rs, Map<Integer, Marca> marcas, Map<Integer, Subcategoria> subcategorias,
                            boolean cargarImagenes) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecioBase(rs.getDouble("precio_base"));
        p.setStock(rs.getInt("stock"));
        p.setDisponible(rs.getBoolean("disponible"));
        p.setPromocion(rs.getBoolean("promocion"));
        p.setActivo(rs.getBoolean("activo"));

        int idMarca = rs.getInt("id_marca");
        Marca marca = marcas.get(idMarca);
        if (marca == null) {
            marca = marcaDAO.load(idMarca);
            marcas.put(idMarca, marca);
        }
        p.setMarca(marca);

        int idSubcategoria = rs.getInt("id_subcategoria");
        Subcategoria subcategoria = subcategorias.get(idSubcategoria);
        if (subcategoria == null) {
            subcategoria = subCategoriaDAO.load(idSubcategoria);
            subcategorias.put(idSubcategoria, subcategoria);
        }
        p.setSubcategoria(subcategoria);

        if (cargarImagenes) {
            p.setImagenes(imagenDAO.listByProductoId(p.getId()));
        }
        return p;
    }

    private Producto mapRowConCatalogos(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecioBase(rs.getDouble("precio_base"));
        p.setStock(rs.getInt("stock"));
        p.setDisponible(rs.getBoolean("disponible"));
        p.setPromocion(rs.getBoolean("promocion"));
        p.setActivo(rs.getBoolean("activo"));

        Marca marca = new Marca();
        marca.setId(rs.getInt("id_marca"));
        marca.setDescripcion(rs.getString("marca_descripcion"));
        marca.setPais(paisFromId(rs.getInt("id_pais")));
        marca.setActivo(rs.getBoolean("marca_activo"));
        p.setMarca(marca);

        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id_categoria"));
        categoria.setDescripcion(rs.getString("categoria_descripcion"));
        categoria.setActivo(rs.getBoolean("categoria_activo"));

        Subcategoria subcategoria = new Subcategoria();
        subcategoria.setId(rs.getInt("id_subcategoria"));
        subcategoria.setDescripcion(rs.getString("subcategoria_descripcion"));
        subcategoria.setActivo(rs.getBoolean("subcategoria_activo"));
        subcategoria.setCategoria(categoria);
        p.setSubcategoria(subcategoria);

        return p;
    }

    private Pais paisFromId(int id) throws SQLException {
        for (Pais pais : Pais.values()) {
            if (pais.getId() == id) return pais;
        }
        throw new SQLException("Pais no reconocido con id: " + id);
    }
}
