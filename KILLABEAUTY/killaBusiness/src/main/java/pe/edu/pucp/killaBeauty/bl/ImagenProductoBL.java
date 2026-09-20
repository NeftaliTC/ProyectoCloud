package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.ImagenProducto;

import java.util.List;

public interface ImagenProductoBL {
    ImagenProducto create(ImagenProducto imagen) throws BusinessLogicException;
    ImagenProducto update(ImagenProducto imagen) throws BusinessLogicException;
    void remove(ImagenProducto imagen) throws BusinessLogicException;
    ImagenProducto load(Integer id) throws BusinessLogicException;
    List<ImagenProducto> listAll() throws BusinessLogicException;
    List<ImagenProducto> listByProductoId(Integer idProducto) throws BusinessLogicException;
}