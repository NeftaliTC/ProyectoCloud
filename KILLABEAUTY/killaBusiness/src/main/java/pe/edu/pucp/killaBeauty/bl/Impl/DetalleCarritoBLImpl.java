package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.bl.DetalleCarritoBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.DetalleCarrito;
import pe.edu.pucp.killaDAO.DetalleCarritoDAO;
import pe.edu.pucp.killaDAO.Impl.DetalleCarritoDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class DetalleCarritoBLImpl implements DetalleCarritoBL {

    private DetalleCarritoDAO detalleDAO = new DetalleCarritoDAOImpl();

    @Override
    public DetalleCarrito create(DetalleCarrito detalle) throws BusinessLogicException {
        validarDetalle(detalle);
        try {
            TransactionContext.getConnection();
            DetalleCarrito guardado = detalleDAO.save(detalle);
            TransactionContext.commit();
            return guardado;
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public DetalleCarrito update(DetalleCarrito detalle) throws BusinessLogicException {
        validarDetalle(detalle);
        try {
            TransactionContext.getConnection();
            DetalleCarrito actualizado = detalleDAO.update(detalle);
            TransactionContext.commit();
            return actualizado;
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void remove(DetalleCarrito detalle) throws BusinessLogicException {
        if (detalle == null || detalle.getId() <= 0) throw new BusinessLogicException("Debe indicar un detalle valido.");
        try {
            TransactionContext.getConnection();
            detalleDAO.remove(detalle);
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public DetalleCarrito load(int id) throws BusinessLogicException {
        try {
            return detalleDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<DetalleCarrito> listByCarritoId(int idCarrito) throws BusinessLogicException {
        try {
            return detalleDAO.listByCarritoId(idCarrito);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    private void validarDetalle(DetalleCarrito detalle) throws BusinessLogicException {
        if (detalle == null) throw new BusinessLogicException("El detalle no puede ser nulo.");
        if (detalle.getProducto() == null || detalle.getProducto().getId() <= 0) throw new BusinessLogicException("El detalle debe tener un producto valido.");
        if (detalle.getCantidad() <= 0) throw new BusinessLogicException("La cantidad debe ser mayor a cero.");
        if (detalle.getCarritoDeCompras() == null || detalle.getCarritoDeCompras().getId() <= 0) throw new BusinessLogicException("El detalle debe pertenecer a un carrito valido.");
    }
}