package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.DetalleCarrito;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;
public interface DetalleCarritoDAO extends BaseDAO<DetalleCarrito,Integer> {
    List<DetalleCarrito> listByCarritoId(int idCarrito) throws SQLException;
}