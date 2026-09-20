package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.TokenRecuperacion;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface TokenRecuperacionDAO extends BaseDAO<TokenRecuperacion, Integer> {
    TokenRecuperacion getValidToken(int usuarioId, String token) throws SQLException;
    List<TokenRecuperacion> listByUsuarioId(int usuarioId) throws SQLException;
}
