package pe.edu.pucp.killaBeauty.bl.Promocionales;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;

import java.util.List;

public interface CuponBL {
    Cupon create(Cupon c) throws BusinessLogicException;
    Cupon update(Cupon c) throws BusinessLogicException;
    void remove(Cupon c) throws BusinessLogicException;
    Cupon load(Integer id) throws BusinessLogicException;
    List<Cupon> listAll() throws BusinessLogicException;
}
