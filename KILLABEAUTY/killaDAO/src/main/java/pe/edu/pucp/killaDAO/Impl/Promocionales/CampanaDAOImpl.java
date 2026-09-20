package pe.edu.pucp.killaDAO.Impl.Promocionales;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Campana;
import pe.edu.pucp.killaDAO.Promocionales.CampanaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CampanaDAOImpl implements CampanaDAO {
    //throws SQLException para que se lleve el error arriba
    @Override
    public List<Campana> listAll() throws SQLException {
        List<Campana> list = new ArrayList<>();
        String sql = "SELECT id_campana, nombre, descripcion, activo FROM Campana";
        //buscas conexion
        Connection connection = TransactionContext.getConnection();
        try(PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
            while (rs.next()) {
                Campana campana = new Campana();
                campana.setIdCampana(rs.getInt(1));
                campana.setNombre(rs.getString(2));
                campana.setDescripcion(rs.getString(3));
                campana.setActivo(rs.getBoolean(4));
                list.add(campana);
            }
            return list;
        }
    }

    @Override
    public Campana load(Integer id) throws SQLException {
        String sql = "SELECT id_campana, nombre, descripcion, activo FROM Campana WHERE id_campana = ?";
        Connection connection = TransactionContext.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Campana campana = new Campana();
                    campana.setIdCampana(rs.getInt("id_campana"));
                    campana.setNombre(rs.getString("nombre"));
                    campana.setDescripcion(rs.getString("descripcion"));
                    campana.setActivo(rs.getBoolean("activo"));
                    return campana;
                }
            }
        }
        return null;
    }

    @Override
    public Campana save(Campana campana) throws SQLException {
        String sql = "INSERT INTO Campana (nombre, descripcion, activo) VALUES (?, ?, ?)";
        Connection connection = TransactionContext.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, campana.getNombre());
            pstmt.setString(2, campana.getDescripcion());
            pstmt.setBoolean(3, campana.isActivo());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        campana.setIdCampana(keys.getInt(1));
                    }
                }
            }
        }
        return campana;
    }

    @Override
    public Campana update(Campana campana) throws SQLException {
        String sql = "UPDATE Campana SET nombre = ?, descripcion = ?, activo = ? WHERE id_campana = ?";
        Connection connection = TransactionContext.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, campana.getNombre());
            pstmt.setString(2, campana.getDescripcion());
            pstmt.setBoolean(3, campana.isActivo());
            pstmt.setInt(4, campana.getIdCampana());
            pstmt.executeUpdate();
            return campana;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Campana campana) throws SQLException {
        // logical removal
        campana.setActivo(false);
        String sql = "update Campana set activo = ? where id_campana = ?";
        Connection connection = TransactionContext.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setBoolean(1, campana.isActivo());
            pstmt.setInt(2, campana.getIdCampana());
            pstmt.executeUpdate();
        }
    }
}

