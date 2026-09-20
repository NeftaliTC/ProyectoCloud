package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.DetallePedido;

import java.util.List;

public interface DetallePedidoBL {
    DetallePedido create(DetallePedido d, Integer idPedido) throws BusinessLogicException;
    DetallePedido update(DetallePedido d, Integer idPedido) throws BusinessLogicException;
    void remove(DetallePedido d) throws BusinessLogicException;
    DetallePedido load(Integer id) throws BusinessLogicException;
    List<DetallePedido> listAll() throws BusinessLogicException;
    List<DetallePedido> listByPedidoId(Integer idPedido) throws BusinessLogicException;
}
