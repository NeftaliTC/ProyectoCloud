package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.TokenRecuperacionBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.TokenRecuperacion;
import pe.edu.pucp.killaDAO.Impl.TokenRecuperacionDAOImpl;
import pe.edu.pucp.killaDAO.TokenRecuperacionDAO;

import java.sql.SQLException;
import java.util.List;

public class TokenRecuperacionBLImpl implements TokenRecuperacionBL {
    private TokenRecuperacionDAO tokenDAO = new TokenRecuperacionDAOImpl();

    @Override
    public TokenRecuperacion create(TokenRecuperacion token) throws BusinessLogicException {
        if(token.getUsuario() == null)
            throw new BusinessLogicException("El token debe pertenecer a un usuario");
        try {
            return tokenDAO.save(token);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public TokenRecuperacion update(TokenRecuperacion token) throws BusinessLogicException {
        try {
            return tokenDAO.update(token);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(TokenRecuperacion token) throws BusinessLogicException {
        try {
            tokenDAO.remove(token);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public TokenRecuperacion getValidToken(int usuarioId, String token) throws BusinessLogicException {
        try {
            return tokenDAO.getValidToken(usuarioId, token);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<TokenRecuperacion> listByUsuarioId(int usuarioId) throws BusinessLogicException {
        try {
            return tokenDAO.listByUsuarioId(usuarioId);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }
}
