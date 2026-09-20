package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.CarritoDeCompras;

import java.util.List;

public interface CarritoDeComprasBL {
    CarritoDeCompras create(CarritoDeCompras carrito) throws BusinessLogicException;
    CarritoDeCompras update(CarritoDeCompras carrito) throws BusinessLogicException;
    void remove(CarritoDeCompras carrito) throws BusinessLogicException;
    CarritoDeCompras load(int id) throws BusinessLogicException;
    List<CarritoDeCompras> listByUsuarioId(int idUsuario) throws BusinessLogicException;
}
