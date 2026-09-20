package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Direccion;

import java.util.List;

public interface DireccionBL {
    Direccion create(Direccion d) throws BusinessLogicException;
    Direccion update(Direccion d) throws BusinessLogicException;
    void remove(Direccion d) throws BusinessLogicException;
    Direccion load(Integer id) throws BusinessLogicException;
    List<Direccion> listAll() throws BusinessLogicException;
    List<Direccion> listarPorUsuario(Integer idUsuario) throws BusinessLogicException;
}
