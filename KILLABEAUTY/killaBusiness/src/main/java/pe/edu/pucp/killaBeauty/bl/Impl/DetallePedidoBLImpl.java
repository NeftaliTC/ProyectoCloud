package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.bl.DetallePedidoBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.DetallePedido;
import pe.edu.pucp.killaDAO.DetallePedidoDAO;
import pe.edu.pucp.killaDAO.Impl.DetallePedidoDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class DetallePedidoBLImpl implements DetallePedidoBL {
    private DetallePedidoDAO detalleDAO = new DetallePedidoDAOImpl();

    @Override
    public DetallePedido create(DetallePedido detalle, Integer idPedido) throws BusinessLogicException {
        validarDetalle(detalle, idPedido);
        try {
            TransactionContext.getConnection();
            DetallePedido guardado = detalleDAO.save(detalle, idPedido);
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
    public DetallePedido update(DetallePedido detalle, Integer idPedido) throws BusinessLogicException {
        validarDetalle(detalle, idPedido);
        try {
            TransactionContext.getConnection();
            DetallePedido actualizado = detalleDAO.update(detalle, idPedido);
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
    public void remove(DetallePedido detalle) throws BusinessLogicException {
        if (detalle == null || detalle.getIdDetallePedido() <= 0) throw new BusinessLogicException("Debe indicar un detalle valido.");
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
    public DetallePedido load(Integer id) throws BusinessLogicException {
        try {
            return detalleDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<DetallePedido> listAll() throws BusinessLogicException {
        try {
            return detalleDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<DetallePedido> listByPedidoId(Integer idPedido) throws BusinessLogicException {
        try {
            return detalleDAO.listByPedidoId(idPedido);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    private void validarDetalle(DetallePedido detalle, Integer idPedido) throws BusinessLogicException {
        if (idPedido == null || idPedido <= 0) throw new BusinessLogicException("Debe indicar un pedido valido.");
        if (detalle == null) throw new BusinessLogicException("El detalle no puede ser nulo.");
        if (detalle.getProducto() == null || detalle.getProducto().getId() <= 0) throw new BusinessLogicException("El detalle debe tener un producto valido.");
        if (detalle.getCantidad() <= 0) throw new BusinessLogicException("La cantidad debe ser mayor a cero.");
        if (detalle.getPrecioAplicado() < 0) throw new BusinessLogicException("El precio aplicado no puede ser negativo.");
    }
}