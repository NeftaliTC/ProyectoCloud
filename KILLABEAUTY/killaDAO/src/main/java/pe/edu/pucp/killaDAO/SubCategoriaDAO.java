package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Subcategoria;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface SubCategoriaDAO extends BaseDAO<Subcategoria, Integer> {
    List<Subcategoria> listAll() throws SQLException;

    List<Subcategoria> listByCategoriaId(Integer idCategoria) throws SQLException;
}