package pe.edu.pucp.killaDAO;


import pe.edu.pucp.killaBeauty.killaModelo.CarritoDeCompras;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface CarritoDeComprasDAO extends BaseDAO<CarritoDeCompras, Integer> {
    List<CarritoDeCompras> listAll() throws SQLException;
    List<CarritoDeCompras> listByUsuarioId(int idUsuario) throws SQLException;
}
