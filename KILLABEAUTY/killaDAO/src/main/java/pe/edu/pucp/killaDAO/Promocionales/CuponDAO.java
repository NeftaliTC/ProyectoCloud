package pe.edu.pucp.killaDAO.Promocionales;

import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface CuponDAO extends BaseDAO<Cupon,Integer> {
    List<Cupon> listAll() throws SQLException;
}