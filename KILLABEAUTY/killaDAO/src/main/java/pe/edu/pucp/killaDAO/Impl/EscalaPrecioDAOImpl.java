package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.EscalaPrecio;
import pe.edu.pucp.killaDAO.EscalaPrecioDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EscalaPrecioDAOImpl implements EscalaPrecioDAO {
    @Override
    public List<EscalaPrecio> listAll() throws SQLException {
        List<EscalaPrecio> escalas = new ArrayList<>();
        String sql = "SELECT id_escala, cantidad_minima, precio_unitario, activo, id_producto FROM EscalaPrecio";

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EscalaPrecio e = new EscalaPrecio();
                e.setId(rs.getInt("id_escala"));
                e.setCantidadMinima(rs.getInt("cantidad_minima"));
                e.setPrecioUnitario(rs.getDouble("precio_unitario"));
                e.setActivo(rs.getBoolean("activo"));
                e.getProducto().setId(rs.getInt("id_producto"));
                escalas.add(e);
            }
        }
        return escalas;
    }


    @Override
    public List<EscalaPrecio> listByProductoId(Integer idProducto) throws SQLException {
        List<EscalaPrecio> escalas = new ArrayList<>();
        String sql = "SELECT id_escala, cantidad_minima, precio_unitario, activo FROM EscalaPrecio WHERE id_producto = ?";

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EscalaPrecio e = new EscalaPrecio();
                    e.setId(rs.getInt("id_escala"));
                    e.setCantidadMinima(rs.getInt("cantidad_minima"));
                    e.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    e.setActivo(rs.getBoolean("activo"));
                    e.getProducto().setId(idProducto);
                    escalas.add(e);
                }
            }
        }
        return escalas;
    }

    public EscalaPrecio load(Integer id) throws SQLException {
        String sql = "SELECT id_escala, cantidad_minima, precio_unitario, activo, id_producto FROM EscalaPrecio WHERE id_escala = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EscalaPrecio e = new EscalaPrecio();
                    e.setId(rs.getInt("id_escala"));
                    e.setCantidadMinima(rs.getInt("cantidad_minima"));
                    e.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    e.setActivo(rs.getBoolean("activo"));
                    e.getProducto().setId(rs.getInt("id_producto"));
                    return e;
                }
            }
        }
        return null;
    }

    @Override
    public EscalaPrecio save(EscalaPrecio e) throws SQLException {
        String sql = "INSERT INTO EscalaPrecio (cantidad_minima, precio_unitario, activo, id_producto) VALUES (?, ?, ?, ?)";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getCantidadMinima());
            ps.setDouble(2, e.getPrecioUnitario());
            ps.setBoolean(3, e.getActivo());
            ps.setInt(4, e.getProducto().getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setId(keys.getInt(1));
            }
        }
        return e;
    }

    @Override
    public EscalaPrecio update(EscalaPrecio e) throws SQLException {
        String sql = "UPDATE EscalaPrecio SET cantidad_minima = ?, precio_unitario = ?, activo = ?, id_producto = ? WHERE id_escala = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, e.getCantidadMinima());
            ps.setDouble(2, e.getPrecioUnitario());
            ps.setBoolean(3, e.getActivo());
            ps.setInt(4, e.getProducto().getId());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        }
        return e;
    }

    @Override
    public void remove(EscalaPrecio e) throws SQLException {
        e.setActivo(false);
        String sql = "UPDATE EscalaPrecio SET activo = ? WHERE id_escala = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, e.getActivo());
            ps.setInt(2, e.getId());
            ps.executeUpdate();
        }
    }

}


