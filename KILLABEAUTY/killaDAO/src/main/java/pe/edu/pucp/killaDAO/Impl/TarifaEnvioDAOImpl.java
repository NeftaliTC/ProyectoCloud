package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Courier;
import pe.edu.pucp.killaBeauty.killaModelo.TarifaEnvio;
import pe.edu.pucp.killaDAO.TarifaEnvioDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarifaEnvioDAOImpl implements TarifaEnvioDAO {

    @Override
    public List<TarifaEnvio> listAll() throws SQLException {
        List<TarifaEnvio> lista = new ArrayList<>();
        String sql = "SELECT id_tarifa, id_courier, nombre_distrito, costo, activo FROM TarifaEnvio";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public List<TarifaEnvio> listByCourierId(Integer idCourier) throws SQLException {
        List<TarifaEnvio> lista = new ArrayList<>();
        String sql = "SELECT id_tarifa, id_courier, nombre_distrito, costo, activo FROM TarifaEnvio WHERE id_courier = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCourier);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    @Override
    public TarifaEnvio load(Integer id) throws SQLException {
        String sql = "SELECT id_tarifa, id_courier, nombre_distrito, costo, activo FROM TarifaEnvio WHERE id_tarifa = ?";
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
    public TarifaEnvio save(TarifaEnvio tarifa) throws SQLException {
        String sql = "INSERT INTO TarifaEnvio (id_courier, nombre_distrito, costo, activo) VALUES (?, ?, ?, ?)";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, tarifa);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) tarifa.setId(keys.getInt(1));
            }
        }
        return tarifa;
    }

    @Override
    public TarifaEnvio update(TarifaEnvio tarifa) throws SQLException {
        String sql = "UPDATE TarifaEnvio SET id_courier = ?, nombre_distrito = ?, costo = ?, activo = ? WHERE id_tarifa = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            setParams(ps, tarifa);
            ps.setInt(5, tarifa.getId());
            ps.executeUpdate();
        }
        return tarifa;
    }

    @Override
    public void remove(TarifaEnvio tarifa) throws SQLException {
        tarifa.setActivo(false);
        String sql = "UPDATE TarifaEnvio SET activo = ? WHERE id_tarifa = ?";
        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, tarifa.getActivo());
            ps.setInt(2, tarifa.getId());
            ps.executeUpdate();
        }
    }

    private void setParams(PreparedStatement ps, TarifaEnvio tarifa) throws SQLException {
        ps.setInt(1, tarifa.getCourier().getId());
        ps.setString(2, tarifa.getNombreDistrito());
        if (tarifa.getCosto() == null) ps.setNull(3, Types.DOUBLE);
        else ps.setDouble(3, tarifa.getCosto());
        ps.setBoolean(4, Boolean.TRUE.equals(tarifa.getActivo()));
    }

    private TarifaEnvio mapRow(ResultSet rs) throws SQLException {
        TarifaEnvio tarifa = new TarifaEnvio();
        tarifa.setId(rs.getInt("id_tarifa"));
        tarifa.setNombreDistrito(rs.getString("nombre_distrito"));
        double costo = rs.getDouble("costo");
        tarifa.setCosto(rs.wasNull() ? null : costo);
        tarifa.setActivo(rs.getBoolean("activo"));

        Courier courier = new Courier();
        courier.setId(rs.getInt("id_courier"));
        tarifa.setCourier(courier);
        return tarifa;
    }
}
