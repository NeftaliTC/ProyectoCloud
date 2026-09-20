package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Permiso;

import java.util.List;

public interface PermisoBL {
    Permiso create(Permiso permiso) throws BusinessLogicException;
    Permiso update(Permiso permiso) throws BusinessLogicException;
    void remove(Permiso permiso) throws BusinessLogicException;
    List<Permiso> listAll() throws BusinessLogicException;
    List<Permiso> listByNombre(String nombre) throws BusinessLogicException;
}
