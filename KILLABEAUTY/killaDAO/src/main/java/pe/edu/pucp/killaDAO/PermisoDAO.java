package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Permiso;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;
public interface PermisoDAO extends BaseDAO<Permiso,Integer> {
    List<Permiso> listAll() throws SQLException;
    List<Permiso> listByNombre(String nombre) throws SQLException;
}