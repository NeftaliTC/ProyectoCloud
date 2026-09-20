package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Pago;

import java.util.List;

public interface PagoBL {
    Pago create(Pago pago) throws BusinessLogicException;
    Pago update(Pago pago) throws BusinessLogicException;
    Pago load(Integer id) throws BusinessLogicException;
    List<Pago> listAll() throws BusinessLogicException;
    Pago obtenerPorIdPedido(Integer idPedido) throws BusinessLogicException;
}