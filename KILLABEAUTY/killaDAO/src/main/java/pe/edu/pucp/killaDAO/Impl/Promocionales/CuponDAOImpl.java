package pe.edu.pucp.killaDAO.Impl.Promocionales;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Campana;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.TipoDescuento;
import pe.edu.pucp.killaDAO.Promocionales.CuponDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuponDAOImpl implements CuponDAO {

    @Override
    public List<Cupon> listAll() throws SQLException {
        List<Cupon> cupones = new ArrayList<>();
        String sql = """
                SELECT id_cupon, codigo, descripcion, valor_descuento, fecha_inicio, fecha_fin,
                       activo, monto_maximo_descuento, monto_minimo_compra, max_usos_generales,
                       id_tipo_descuento, id_campana
                FROM Cupon
                """;
        Connection connection = TransactionContext.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) cupones.add(mapRow(rs));
        }
        return cupones;
    }

    @Override
    public Cupon load(Integer id) throws SQLException {
        String sql = """
                SELECT id_cupon, codigo, descripcion, valor_descuento, fecha_inicio, fecha_fin,
                       activo, monto_maximo_descuento, monto_minimo_compra, max_usos_generales,
                       id_tipo_descuento, id_campana
                FROM Cupon WHERE id_cupon = ?
                """;
        Connection connection = TransactionContext.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public Cupon save(Cupon cupon) throws SQLException {
        String sql = """
                INSERT INTO Cupon
                (codigo, descripcion, valor_descuento, fecha_inicio, fecha_fin, activo,
                 monto_maximo_descuento, monto_minimo_compra, max_usos_generales,
                 id_tipo_descuento, id_campana)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        Connection connection = TransactionContext.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setCuponParams(pstmt, cupon);
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) cupon.setId(keys.getInt(1));
            }
        }
        return cupon;
    }

    @Override
    public Cupon update(Cupon cupon) throws SQLException {
        String sql = """
                UPDATE Cupon
                SET codigo = ?, descripcion = ?, valor_descuento = ?, fecha_inicio = ?, fecha_fin = ?,
                    activo = ?, monto_maximo_descuento = ?, monto_minimo_compra = ?,
                    max_usos_generales = ?, id_tipo_descuento = ?, id_campana = ?
                WHERE id_cupon = ?
                """;
        Connection connection = TransactionContext.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            setCuponParams(pstmt, cupon);
            pstmt.setInt(12, cupon.getId());
            pstmt.executeUpdate();
        }
        return cupon;
    }

    @Override
    public void remove(Cupon cupon) throws SQLException {
        String sql = "UPDATE Cupon SET activo = 0 WHERE id_cupon = ?";
        Connection connection = TransactionContext.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cupon.getId());
            pstmt.executeUpdate();
        }
    }

    private void setCuponParams(PreparedStatement pstmt, Cupon cupon) throws SQLException {
        pstmt.setString(1, cupon.getCodigo());
        pstmt.setString(2, cupon.getDescripcion());
        pstmt.setDouble(3, cupon.getValorDescuento());
        if (cupon.getFechaInicio() == null) pstmt.setNull(4, Types.DATE);
        else pstmt.setDate(4, Date.valueOf(cupon.getFechaInicio()));
        if (cupon.getFechaFin() == null) pstmt.setNull(5, Types.DATE);
        else pstmt.setDate(5, Date.valueOf(cupon.getFechaFin()));
        pstmt.setBoolean(6, cupon.isActivo());
        setDouble(pstmt, 7, cupon.getMontoMaximoDescuento());
        setDouble(pstmt, 8, cupon.getMontoMinimoCompra());
        setInteger(pstmt, 9, cupon.getMaxUsosGenerales());
        if (cupon.getTipoDescuento() != null) pstmt.setInt(10, cupon.getTipoDescuento().getId());
        else pstmt.setNull(10, Types.INTEGER);
        if (cupon.getCampana() != null && cupon.getCampana().getIdCampana() > 0) {
            pstmt.setInt(11, cupon.getCampana().getIdCampana());
        } else {
            pstmt.setNull(11, Types.INTEGER);
        }
    }

    private Cupon mapRow(ResultSet rs) throws SQLException {
        Cupon cupon = new Cupon();
        cupon.setId(rs.getInt("id_cupon"));
        cupon.setCodigo(rs.getString("codigo"));
        cupon.setDescripcion(rs.getString("descripcion"));
        cupon.setValorDescuento(rs.getDouble("valor_descuento"));
        cupon.setActivo(rs.getBoolean("activo"));

        Date fechaInicio = rs.getDate("fecha_inicio");
        if (fechaInicio != null) cupon.setFechaInicio(fechaInicio.toLocalDate());
        Date fechaFin = rs.getDate("fecha_fin");
        if (fechaFin != null) cupon.setFechaFin(fechaFin.toLocalDate());

        double montoMaximo = rs.getDouble("monto_maximo_descuento");
        cupon.setMontoMaximoDescuento(rs.wasNull() ? null : montoMaximo);
        double montoMinimo = rs.getDouble("monto_minimo_compra");
        cupon.setMontoMinimoCompra(rs.wasNull() ? null : montoMinimo);
        int maxUsos = rs.getInt("max_usos_generales");
        cupon.setMaxUsosGenerales(rs.wasNull() ? null : maxUsos);

        int idTipo = rs.getInt("id_tipo_descuento");
        if (!rs.wasNull()) cupon.setTipoDescuento(tipoDescuentoFromId(idTipo));

        int idCampana = rs.getInt("id_campana");
        if (!rs.wasNull()) {
            Campana campana = new Campana();
            campana.setIdCampana(idCampana);
            cupon.setCampana(campana);
        }
        return cupon;
    }

    private TipoDescuento tipoDescuentoFromId(int id) throws SQLException {
        for (TipoDescuento tipo : TipoDescuento.values()) {
            if (tipo.getId() == id) return tipo;
        }
        throw new SQLException("TipoDescuento no reconocido con id: " + id);
    }

    private void setDouble(PreparedStatement ps, int index, Double value) throws SQLException {
        if (value == null) ps.setNull(index, Types.DOUBLE);
        else ps.setDouble(index, value);
    }

    private void setInteger(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) ps.setNull(index, Types.INTEGER);
        else ps.setInt(index, value);
    }
}
