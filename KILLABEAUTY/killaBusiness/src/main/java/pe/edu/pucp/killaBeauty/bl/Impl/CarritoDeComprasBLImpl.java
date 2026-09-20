package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.bl.CarritoDeComprasBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.CarritoDeCompras;
import pe.edu.pucp.killaBeauty.killaModelo.DetalleCarrito;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;
import pe.edu.pucp.killaDAO.CarritoDeComprasDAO;
import pe.edu.pucp.killaDAO.DetalleCarritoDAO;
import pe.edu.pucp.killaDAO.Impl.CarritoDeComprasDAOImpl;
import pe.edu.pucp.killaDAO.Impl.DetalleCarritoDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class CarritoDeComprasBLImpl implements CarritoDeComprasBL {

    private CarritoDeComprasDAO carritoDAO = new CarritoDeComprasDAOImpl();
    private DetalleCarritoDAO detalleDAO = new DetalleCarritoDAOImpl();

    @Override
    public CarritoDeCompras create(CarritoDeCompras carrito) throws BusinessLogicException {
        validarCarrito(carrito, false);
        try {
            TransactionContext.getConnection();
            carritoDAO.save(carrito);
            if (carrito.getDetalleCarritoList() != null) {
                for (DetalleCarrito detalle : carrito.getDetalleCarritoList()) {
                    detalle.setCarritoDeCompras(carrito);
                    detalleDAO.save(detalle);
                }
            }
            TransactionContext.commit();
            return carrito;
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public CarritoDeCompras update(CarritoDeCompras carrito) throws BusinessLogicException {
        validarCarrito(carrito, false);
        try {
            TransactionContext.getConnection();
            carritoDAO.update(carrito);
            if (carrito.getDetalleCarritoList() != null) {
                for (DetalleCarrito detalle : carrito.getDetalleCarritoList()) {
                    detalle.setCarritoDeCompras(carrito);
                    detalleDAO.update(detalle);
                }
            }
            TransactionContext.commit();
            return carrito;
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void remove(CarritoDeCompras carrito) throws BusinessLogicException {
        if (carrito == null || carrito.getId() <= 0) throw new BusinessLogicException("Debe indicar un carrito valido.");
        try {
            CarritoDeCompras carritoExistente = carritoDAO.load(carrito.getId());
            if (carritoExistente == null) {
                throw new BusinessLogicException("El carrito no existe.");
            }
            TransactionContext.getConnection();
            carritoDAO.remove(carritoExistente);
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public CarritoDeCompras load(int id) throws BusinessLogicException {
        try {
            CarritoDeCompras c = carritoDAO.load(id);
            if (c != null) c.setDetalleCarritoList(detalleDAO.listByCarritoId(c.getId()));
            return c;
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<CarritoDeCompras> listByUsuarioId(int idUsuario) throws BusinessLogicException {
        try {
            List<CarritoDeCompras> carritos = carritoDAO.listByUsuarioId(idUsuario);
            for (CarritoDeCompras c : carritos) c.setDetalleCarritoList(detalleDAO.listByCarritoId(c.getId()));
            return carritos;
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    private void validarCarrito(CarritoDeCompras carrito, boolean exigirDetalles) throws BusinessLogicException {
        if (carrito == null) throw new BusinessLogicException("El carrito no puede ser nulo.");
        if (carrito.getUsuario() == null || carrito.getUsuario().getId() <= 0) throw new BusinessLogicException("El carrito debe tener un usuario valido.");
        if (carrito.getEstado() == null) throw new BusinessLogicException("El carrito debe tener un estado.");
        if (exigirDetalles && (carrito.getDetalleCarritoList() == null || carrito.getDetalleCarritoList().isEmpty())) {
            throw new BusinessLogicException("El carrito debe contener al menos un detalle.");
        }
        if (carrito.getDetalleCarritoList() != null) {
            for (DetalleCarrito detalle : carrito.getDetalleCarritoList()) validarDetalle(detalle);
        }
    }

    private void validarDetalle(DetalleCarrito detalle) throws BusinessLogicException {
        if (detalle == null) throw new BusinessLogicException("El detalle del carrito no puede ser nulo.");
        Producto producto = detalle.getProducto();
        if (producto == null || producto.getId() <= 0) throw new BusinessLogicException("Cada detalle debe tener un producto valido.");
        if (detalle.getCantidad() <= 0) throw new BusinessLogicException("La cantidad debe ser mayor a cero.");
    }
}