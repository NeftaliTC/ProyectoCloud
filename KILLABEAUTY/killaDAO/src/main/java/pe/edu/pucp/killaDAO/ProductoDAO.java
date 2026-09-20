package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Producto;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;
public interface ProductoDAO extends BaseDAO<Producto,Integer> {
    List<Producto> listAll() throws SQLException;
    void descontarStock(Integer idProducto, Integer cantidad) throws SQLException;
}
