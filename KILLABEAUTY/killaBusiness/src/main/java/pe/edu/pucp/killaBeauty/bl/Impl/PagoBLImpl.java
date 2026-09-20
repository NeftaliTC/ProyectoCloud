package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.PagoBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Pago;
import pe.edu.pucp.killaDAO.Impl.PagoDAOImpl;
import pe.edu.pucp.killaDAO.PagoDAO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PagoBLImpl implements PagoBL {
    private PagoDAO pagoDAO = new PagoDAOImpl();

    @Override
    public Pago create(Pago pago) throws BusinessLogicException {
        validarPago(pago);
        if (pago.getFechaHoraPago() == null) pago.setFechaHoraPago(new Date());
        try {
            return pagoDAO.save(pago);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Pago update(Pago pago) throws BusinessLogicException {
        validarPago(pago);
        try {
            return pagoDAO.update(pago);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Pago load(Integer id) throws BusinessLogicException {
        try {
            return pagoDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Pago> listAll() throws BusinessLogicException {
        try {
            return pagoDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Pago obtenerPorIdPedido(Integer idPedido) throws BusinessLogicException{
        try {
            return pagoDAO.buscarPorIdPedido(idPedido);
        } catch (Exception ex) {
            System.err.println("Error al obtener pago: " + ex.getMessage());
            return null;
        }
    }

    private void validarPago(Pago pago) throws BusinessLogicException {
        if (pago == null) throw new BusinessLogicException("El pago no puede ser nulo.");
        if (pago.getMontoPagado() <= 0) throw new BusinessLogicException("El monto pagado debe ser mayor a cero.");
        if (pago.getPedido() == null || pago.getPedido().getId() <= 0) throw new BusinessLogicException("El pago debe estar asociado a un pedido valido.");
        if (pago.getMetodoPago() == null) throw new BusinessLogicException("El pago debe tener un metodo de pago.");
    }
}