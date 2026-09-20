package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.TokenRecuperacion;

import java.util.List;

public interface TokenRecuperacionBL {
    TokenRecuperacion create(TokenRecuperacion token) throws BusinessLogicException;
    TokenRecuperacion update(TokenRecuperacion token) throws BusinessLogicException;
    void remove(TokenRecuperacion token) throws BusinessLogicException;
    TokenRecuperacion getValidToken(int usuarioId, String token) throws BusinessLogicException;
    List<TokenRecuperacion> listByUsuarioId(int usuarioId) throws BusinessLogicException;
}