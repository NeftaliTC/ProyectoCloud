package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.RolPermiso;

import java.util.List;

public interface RolPermisoBL {
    RolPermiso create(RolPermiso rolPermiso) throws BusinessLogicException;
    void remove(RolPermiso rolPermiso) throws BusinessLogicException;
    List<RolPermiso> listByTipoUsuario(int tipoUsuario) throws BusinessLogicException;
    List<RolPermiso> listAll() throws BusinessLogicException;
}
