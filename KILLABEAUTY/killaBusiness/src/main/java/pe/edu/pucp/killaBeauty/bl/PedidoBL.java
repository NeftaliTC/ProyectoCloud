package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.*;

import java.util.List;

public interface PedidoBL {
    Pedido create(Pedido p) throws BusinessLogicException;
    Pedido createFromCart(Integer idCliente, Integer idDireccion, Integer idCupon, List<DetallePedido> detalles) throws BusinessLogicException;
    Pedido cancel(Integer id) throws BusinessLogicException;
    Pedido update(Pedido p) throws BusinessLogicException;
    void remove(Pedido p) throws BusinessLogicException;
    Pedido load(Integer id) throws BusinessLogicException;
    List<Pedido> listAll() throws BusinessLogicException;
    List<Pedido> listByCliente(Integer idCliente) throws BusinessLogicException;
    void actualizarTotal(Integer idPedido, double nuevoTotal) throws BusinessLogicException;
    void actualizarEstado(Integer idPedido, EstadoPedido estadoPedido) throws BusinessLogicException;
}
