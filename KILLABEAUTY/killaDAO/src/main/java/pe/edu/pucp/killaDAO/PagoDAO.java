package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Pago;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface PagoDAO extends BaseDAO<Pago, Integer> {
    List<Pago> listAll() throws SQLException;
    Pago buscarPorIdPedido(Integer idPedido) throws SQLException;
}
