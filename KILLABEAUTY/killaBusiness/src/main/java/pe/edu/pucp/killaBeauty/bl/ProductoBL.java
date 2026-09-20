package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.ImagenProducto;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;

import java.util.List;

public interface ProductoBL {
    Producto create(Producto producto) throws BusinessLogicException;
    Producto update(Producto producto) throws BusinessLogicException;
    void remove(Producto producto) throws BusinessLogicException;
    List<Producto> listAll() throws BusinessLogicException;
    Producto load(int id) throws BusinessLogicException;
    Producto createConImagenes(Producto p, List<ImagenProducto> imagenes) throws BusinessLogicException;
    public Producto updateConImagenes(Producto p, List<ImagenProducto> nuevasImagenes) throws BusinessLogicException;
}