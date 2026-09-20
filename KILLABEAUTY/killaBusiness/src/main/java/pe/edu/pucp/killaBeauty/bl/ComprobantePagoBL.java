package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.ComprobantePago;

import java.util.List;

public interface ComprobantePagoBL {
    ComprobantePago create(ComprobantePago c) throws BusinessLogicException;
    ComprobantePago load(Integer id) throws BusinessLogicException;
    List<ComprobantePago> listAll() throws BusinessLogicException;
    ComprobantePago obtenerPorIdPago(Integer idPago) throws BusinessLogicException;
}
