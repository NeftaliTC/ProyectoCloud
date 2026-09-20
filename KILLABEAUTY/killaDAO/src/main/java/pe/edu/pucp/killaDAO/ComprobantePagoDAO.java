package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.ComprobantePago;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface ComprobantePagoDAO extends BaseDAO <ComprobantePago, Integer> {
    List<ComprobantePago> listAll() throws SQLException;
    ComprobantePago buscarPorIdPago(Integer idPago) throws SQLException;
}
