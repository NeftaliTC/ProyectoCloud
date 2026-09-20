package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.DetalleCarrito;

import java.util.List;

public interface DetalleCarritoBL {
    DetalleCarrito create(DetalleCarrito detalle) throws BusinessLogicException;
    DetalleCarrito update(DetalleCarrito detalle) throws BusinessLogicException;
    void remove(DetalleCarrito detalle) throws BusinessLogicException;
    DetalleCarrito load(int id) throws BusinessLogicException;
    List<DetalleCarrito> listByCarritoId(int idCarrito) throws BusinessLogicException;
}