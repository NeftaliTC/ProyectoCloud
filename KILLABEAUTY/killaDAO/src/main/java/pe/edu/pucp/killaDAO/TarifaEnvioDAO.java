package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.TarifaEnvio;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface TarifaEnvioDAO extends BaseDAO<TarifaEnvio, Integer> {
    List<TarifaEnvio> listAll() throws SQLException;
    List<TarifaEnvio> listByCourierId(Integer idCourier) throws SQLException;
}
