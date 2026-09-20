package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.ImagenProductoBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.ImagenProducto;
import pe.edu.pucp.killaDAO.ImagenProductoDAO;
import pe.edu.pucp.killaDAO.Impl.ImagenProductoDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class ImagenProductoBLImpl implements ImagenProductoBL {
    private ImagenProductoDAO imagenDAO = new ImagenProductoDAOImpl();

    @Override
    public ImagenProducto create(ImagenProducto imagen) throws BusinessLogicException {
        validarImagen(imagen);
        try {
            return imagenDAO.save(imagen);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public ImagenProducto update(ImagenProducto imagen) throws BusinessLogicException {
        validarImagen(imagen);
        try {
            return imagenDAO.update(imagen);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(ImagenProducto imagen) throws BusinessLogicException {
        try {
            imagenDAO.remove(imagen);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public ImagenProducto load(Integer id) throws BusinessLogicException {
        try {
            return imagenDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<ImagenProducto> listAll() throws BusinessLogicException {
        try {
            return imagenDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<ImagenProducto> listByProductoId(Integer idProducto) throws BusinessLogicException {
        try {
            return imagenDAO.listByProductoId(idProducto);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    private void validarImagen(ImagenProducto imagen) throws BusinessLogicException {
        if (imagen == null) throw new BusinessLogicException("La imagen no puede ser nula.");
        if (imagen.getUrl() == null || imagen.getUrl().trim().isEmpty()) throw new BusinessLogicException("La imagen debe tener una URL.");
        if (imagen.getProducto() == null || imagen.getProducto().getId() <= 0) throw new BusinessLogicException("La imagen debe pertenecer a un producto valido.");
    }
}