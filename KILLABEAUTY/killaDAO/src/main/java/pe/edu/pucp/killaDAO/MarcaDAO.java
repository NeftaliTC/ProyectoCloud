package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Marca;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;
public interface MarcaDAO extends BaseDAO<Marca,Integer> {
    List<Marca> listAll() throws SQLException;
}