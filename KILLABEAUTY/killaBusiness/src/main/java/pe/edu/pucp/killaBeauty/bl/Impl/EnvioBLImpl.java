package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.bl.EnvioBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Envio;
import pe.edu.pucp.killaDAO.EnvioDAO;
import pe.edu.pucp.killaDAO.Impl.EnvioDAOImpl;

import java.util.List;

public class EnvioBLImpl implements EnvioBL {

    private EnvioDAO envioDAO = new EnvioDAOImpl();

    @Override
    public Envio create(Envio e) throws BusinessLogicException {
        try {
            if (e.getPedido() == null || e.getCourier() == null) {
                throw new BusinessLogicException("El envío debe tener un pedido y un courier asignado.");
            }

            TransactionContext.getConnection();
            Envio guardado = envioDAO.save(e);
            TransactionContext.commit();
            return guardado;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new BusinessLogicException("Error al registrar el envío: " + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Envio update(Envio e) throws BusinessLogicException {
        try {
            return envioDAO.update(e);
        } catch (Exception ex) {
            throw new BusinessLogicException("Error al actualizar el envío: " + ex.getMessage());
        }
    }

    @Override
    public void cancel(Envio e) throws BusinessLogicException {
        try {
            // El DAO ya maneja el cambio a estado CANCELADO
            envioDAO.remove(e);
        } catch (Exception ex) {
            throw new BusinessLogicException("Error al cancelar el envío: " + ex.getMessage());
        }
    }

    @Override
    public Envio load(Integer id) throws BusinessLogicException {
        try {
            return envioDAO.load(id);
        } catch (Exception e) {
            throw new BusinessLogicException("Error al cargar el envío: " + e.getMessage());
        }
    }

    @Override
    public List<Envio> listAll() throws BusinessLogicException {
        try {
            return envioDAO.listAll();
        } catch (Exception e) {
            throw new BusinessLogicException("Error al listar los envíos: " + e.getMessage());
        }
    }

    @Override
    public Envio obtenerPorIdPedido(Integer idPedido) throws BusinessLogicException{
        try {
            return envioDAO.buscarPorIdPedido(idPedido);
        } catch (Exception ex) {
            System.err.println("Error al obtener envío: " + ex.getMessage());
            return null;
        }
    }
}
