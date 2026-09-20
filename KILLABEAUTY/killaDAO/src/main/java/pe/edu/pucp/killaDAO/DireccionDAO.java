package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Direccion;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface DireccionDAO extends BaseDAO<Direccion,Integer> {
    List<Direccion> listAll() throws SQLException;
    List<Direccion> listarPorUsuario(Integer idUsuario) throws  SQLException;
    void resetearPredeterminadas(Integer idUsuario) throws SQLException;
}