package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Categoria;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;
public interface CategoriaDAO extends BaseDAO<Categoria,Integer> {
    List<Categoria> listAll() throws SQLException;
}