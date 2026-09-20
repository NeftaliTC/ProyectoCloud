package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Envio;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface EnvioDAO extends BaseDAO <Envio, Integer> {
    List<Envio> listAll() throws SQLException;
    Envio buscarPorIdPedido(Integer idPedido) throws SQLException;
}
