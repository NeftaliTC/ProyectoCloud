package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.DetallePedido;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;
public interface DetallePedidoDAO extends BaseDAO<DetallePedido, Integer> {
    List<DetallePedido> listAll() throws SQLException;
    List<DetallePedido> listByPedidoId(Integer idPedido) throws SQLException;

    DetallePedido save(DetallePedido detalle, Integer idPedido) throws SQLException;
    DetallePedido update(DetallePedido detalle, Integer idPedido) throws SQLException;
}