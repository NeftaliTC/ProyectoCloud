package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.killaModelo.Courier;
import pe.edu.pucp.killaDAO.CourierDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourierDAOImpl implements CourierDAO {
    @Override
    public List<Courier> listAll() throws SQLException {
        List<Courier> lista = new ArrayList<>();
        String sql = "SELECT id_courier, nombre, ruc, telefono, activo, correo, es_asignado FROM Courier WHERE activo = 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearCourier(rs));
            }
        }
        return lista;
    }

    @Override
    public Courier load(Integer id) throws SQLException {
        String sql = "SELECT id_courier, nombre, ruc, telefono, activo, correo, es_asignado FROM Courier WHERE id_courier = ? AND activo = 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCourier(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Courier save(Courier courier) throws SQLException {
        String sql = "INSERT INTO Courier (nombre, ruc, telefono, activo, correo, es_asignado) VALUES (?, ?, ?, 1, ?, ?)";
        Connection con = TransactionContext.getConnection();

        if (courier.isEsAsignado()) {
            try (Statement st = con.createStatement()) {
                st.executeUpdate("UPDATE Courier SET es_asignado = 0 WHERE activo = 1");
            }
        }

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, courier.getNombre());
            ps.setString(2, courier.getRuc());
            ps.setString(3, courier.getTelefono());
            ps.setString(4, courier.getCorreo());
            ps.setBoolean(5, courier.isEsAsignado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    courier.setId(rs.getInt(1));
                }
            }
        }

        return courier;
    }

    @Override
    public Courier update(Courier courier) throws SQLException {
        String sql = "UPDATE Courier SET nombre = ?, ruc = ?, telefono = ?, activo = 1, correo = ?, es_asignado = ? WHERE id_courier = ?";
        Connection con = TransactionContext.getConnection();

        if (courier.isEsAsignado()) {
            try (Statement st = con.createStatement()) {
                st.executeUpdate("UPDATE Courier SET es_asignado = 0 WHERE id_courier != " + courier.getId() + " AND activo = 1");
            }
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, courier.getNombre());
            ps.setString(2, courier.getRuc());
            ps.setString(3, courier.getTelefono());
            ps.setString(4, courier.getCorreo());
            ps.setBoolean(5, courier.isEsAsignado());
            ps.setInt(6, courier.getId());
            ps.executeUpdate();
        }

        return courier;
    }

    @Override
    public void remove(Courier courier) throws SQLException {
        String sql = "UPDATE Courier SET activo = 0, es_asignado = 0 WHERE id_courier = ?";
        Connection con = TransactionContext.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courier.getId());
            ps.executeUpdate();
        }
    }

    private Courier mapearCourier(ResultSet rs) throws SQLException {
        Courier c = new Courier();
        c.setId(rs.getInt("id_courier"));
        c.setNombre(rs.getString("nombre"));
        c.setRuc(rs.getString("ruc"));
        c.setTelefono(rs.getString("telefono"));
        c.setActivo(rs.getBoolean("activo"));
        c.setCorreo(rs.getString("correo"));
        c.setEsAsignado(rs.getBoolean("es_asignado"));
        return c;
    }

    @Override
    public boolean existeDato(String columna, String valor) throws SQLException {
        // Al usar "WHERE " + columna, dinámicamente buscará por nombre, correo o ruc
        String sql = "SELECT COUNT(*) FROM Courier WHERE " + columna + " = ? AND activo = 1";
        Connection con = TransactionContext.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    @Override
    public boolean existeDatoExcluyendoId(String columna, String valor, Integer id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Courier WHERE " + columna + " = ? AND id_courier != ? AND activo = 1";
        Connection con = TransactionContext.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, valor);
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    @Override
    public Courier buscarAsignado() throws SQLException {
        String sql = "SELECT id_courier, nombre, ruc, telefono, activo, correo, es_asignado FROM Courier WHERE es_asignado = 1 AND activo = 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return mapearCourier(rs);
        }
        return null;
    }
}