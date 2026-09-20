package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.TarifaEnvio;

import java.util.List;

public interface TarifaEnvioBL {
    TarifaEnvio create(TarifaEnvio tarifa) throws BusinessLogicException;
    TarifaEnvio update(TarifaEnvio tarifa) throws BusinessLogicException;
    void remove(TarifaEnvio tarifa) throws BusinessLogicException;
    TarifaEnvio load(Integer id) throws BusinessLogicException;
    List<TarifaEnvio> listAll() throws BusinessLogicException;
    List<TarifaEnvio> listByCourierId(Integer idCourier) throws BusinessLogicException;
}