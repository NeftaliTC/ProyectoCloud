package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.ProductoBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;
import pe.edu.pucp.killaDAO.Impl.ProductoDAOImpl;
import pe.edu.pucp.killaDAO.ProductoDAO;
import pe.edu.pucp.killaBeauty.killaModelo.ImagenProducto;
import pe.edu.pucp.killaDAO.ImagenProductoDAO;
import pe.edu.pucp.killaDAO.Impl.ImagenProductoDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class ProductoBLImpl implements ProductoBL {
    private ProductoDAO productoDAO = new ProductoDAOImpl();
    private ImagenProductoDAO imagenProductoDAO = new ImagenProductoDAOImpl();

    @Override
    public Producto create(Producto p) throws BusinessLogicException {
        try {
            if(p.getNombre() == null || p.getNombre().isEmpty())
                throw new BusinessLogicException("El nombre del producto no puede estar vacío");
            if(p.getSubcategoria() == null)
                throw new BusinessLogicException("El producto debe tener una subcategoría");
            if(p.getMarca() == null)
                throw new BusinessLogicException("El producto debe tener una marca");
            return productoDAO.save(p);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Producto update(Producto p) throws BusinessLogicException {
        try {
            return productoDAO.update(p);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(Producto p) throws BusinessLogicException {
        try {
            productoDAO.remove(p);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Producto> listAll() throws BusinessLogicException {
        try {
            return productoDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Producto load(int id) throws BusinessLogicException {
        try {
            return productoDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Producto createConImagenes(Producto p, List<ImagenProducto> imagenes) throws BusinessLogicException {
        try {
            if (p.getNombre() == null || p.getNombre().isEmpty())
                throw new BusinessLogicException("El nombre del producto no puede estar vacío");
            if (p.getSubcategoria() == null)
                throw new BusinessLogicException("El producto debe tener una subcategoría");
            if (p.getMarca() == null)
                throw new BusinessLogicException("El producto debe tener una marca");

            Producto productoGuardado = productoDAO.save(p);

            if (imagenes != null && !imagenes.isEmpty()) {
                for (int i = 0; i < imagenes.size(); i++) {
                    ImagenProducto img = imagenes.get(i);

                    if (img.getUrl() != null && !img.getUrl().isBlank()) {
                        img.setProducto(productoGuardado);
                        img.setActivo(true);
                        img.setOrden(i + 1);
                        img.setPrincipal(i == 0);

                        imagenProductoDAO.save(img);
                    }
                }
            }

            return productoGuardado;

        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Producto updateConImagenes(Producto p, List<ImagenProducto> nuevasImagenes) throws BusinessLogicException {
        try {
            Producto productoActualizado = productoDAO.update(p);

            if (nuevasImagenes != null && !nuevasImagenes.isEmpty()) {
                List<ImagenProducto> existentes =
                        imagenProductoDAO.listByProductoId(productoActualizado.getId());

                int ordenInicial = existentes.size() + 1;

                for (int i = 0; i < nuevasImagenes.size(); i++) {
                    ImagenProducto img = nuevasImagenes.get(i);

                    if (img.getUrl() != null && !img.getUrl().isBlank()) {
                        img.setProducto(productoActualizado);
                        img.setActivo(true);
                        img.setOrden(ordenInicial + i);

                        boolean noHayPrincipal = existentes.stream()
                                .noneMatch(x -> Boolean.TRUE.equals(x.getPrincipal()));

                        img.setPrincipal(noHayPrincipal && i == 0);

                        imagenProductoDAO.save(img);
                    }
                }
            }

            return productoActualizado;

        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }
}
