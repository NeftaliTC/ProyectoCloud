package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.RolPermiso;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;
public interface RolPermisoDAO extends BaseDAO<RolPermiso,Integer> {
    List<RolPermiso> listByTipoUsuario(int idTipoUsuario) throws SQLException;
    List<RolPermiso> listAll() throws SQLException;
}