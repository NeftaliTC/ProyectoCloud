package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.ClienteCupon;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface ClienteCuponDAO extends BaseDAO<ClienteCupon, Integer> {
    List<ClienteCupon> listAll() throws SQLException;
    List<ClienteCupon> listByUsuarioId(Integer idUsuario) throws SQLException;
}
