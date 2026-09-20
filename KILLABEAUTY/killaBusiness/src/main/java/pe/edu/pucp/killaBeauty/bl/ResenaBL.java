package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Resena;

import java.util.List;

public interface ResenaBL {
    Resena create(Resena r) throws BusinessLogicException;
    Resena update(Resena r) throws BusinessLogicException;
    void remove(Resena r) throws BusinessLogicException;
    Resena load(Integer id) throws BusinessLogicException;
    List<Resena> listAll() throws BusinessLogicException;
    List<Resena> listByProductoId(int idProducto) throws BusinessLogicException;
    List<Resena> listByUsuarioId(int idUsuario) throws BusinessLogicException;
}
