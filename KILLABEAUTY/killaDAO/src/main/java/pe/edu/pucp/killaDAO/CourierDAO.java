package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Courier;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface CourierDAO extends BaseDAO<Courier, Integer> {
    List<Courier> listAll() throws SQLException;
    boolean existeDato(String columna, String valor) throws SQLException;
    boolean existeDatoExcluyendoId(String columna, String valor, Integer id) throws SQLException;
    Courier buscarAsignado() throws SQLException;
}
