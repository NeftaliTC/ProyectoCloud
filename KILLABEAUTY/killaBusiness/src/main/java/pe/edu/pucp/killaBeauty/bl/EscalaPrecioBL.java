package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.EscalaPrecio;

import java.util.List;

public interface EscalaPrecioBL {
    EscalaPrecio create(EscalaPrecio escala) throws BusinessLogicException;
    EscalaPrecio update(EscalaPrecio escala) throws BusinessLogicException;
    void remove(EscalaPrecio escala) throws BusinessLogicException;
    List<EscalaPrecio> listAll() throws BusinessLogicException;
    List<EscalaPrecio> listByProductoId(int idProducto) throws BusinessLogicException;
    EscalaPrecio load(int id) throws BusinessLogicException;
}
