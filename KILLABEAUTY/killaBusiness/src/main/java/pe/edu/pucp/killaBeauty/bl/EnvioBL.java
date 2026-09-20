package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Envio;

import java.util.List;

public interface EnvioBL {
    Envio create(Envio e) throws BusinessLogicException;
    Envio update(Envio e) throws BusinessLogicException;
    void cancel(Envio e) throws BusinessLogicException;
    Envio load(Integer id) throws BusinessLogicException;
    List<Envio> listAll() throws BusinessLogicException;
    Envio obtenerPorIdPedido(Integer idPedido) throws BusinessLogicException;
}
