package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.ClienteCupon;

import java.util.List;

public interface ClienteCuponBL {
    ClienteCupon create(ClienteCupon clienteCupon) throws BusinessLogicException;
    ClienteCupon update(ClienteCupon clienteCupon) throws BusinessLogicException;
    void remove(ClienteCupon clienteCupon) throws BusinessLogicException;
    ClienteCupon load(Integer id) throws BusinessLogicException;
    List<ClienteCupon> listAll() throws BusinessLogicException;
    List<ClienteCupon> listByUsuarioId(Integer idUsuario) throws BusinessLogicException;
}