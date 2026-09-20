package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;
public interface UsuarioDAO extends BaseDAO<Usuario,Integer> {
    Usuario loadByEmail(String email) throws SQLException;
    List<Usuario> listByTipoUsuario(int idTipoUsuario) throws SQLException;
    void actualizarUltimoAcceso(int idUsuario) throws SQLException;
}