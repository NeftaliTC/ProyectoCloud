package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.ImagenProducto;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface ImagenProductoDAO extends BaseDAO<ImagenProducto, Integer> {
    List<ImagenProducto> listAll() throws SQLException;
    List<ImagenProducto> listByProductoId(Integer idProducto) throws SQLException;
    void quitarPrincipalPorProducto(Integer idProducto) throws SQLException;
}
