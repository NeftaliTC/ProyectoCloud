package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.EscalaPrecio;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;
public interface EscalaPrecioDAO extends BaseDAO<EscalaPrecio,Integer> {
    List<EscalaPrecio> listAll() throws SQLException;
    List<EscalaPrecio> listByProductoId(Integer idProducto) throws SQLException;
}