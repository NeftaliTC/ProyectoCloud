package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.bl.CourierBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Courier;
import pe.edu.pucp.killaDAO.CourierDAO;
import pe.edu.pucp.killaDAO.Impl.CourierDAOImpl;
import pe.edu.pucp.killaBeauty.bl.utils.FormatHelper;

import java.sql.SQLException;
import java.util.List;

public class CourierBLImpl implements CourierBL {

    private CourierDAO courierDAO = new CourierDAOImpl();

    @Override
    public Courier create(Courier c) throws BusinessLogicException {
        try {
            TransactionContext.getConnection();
            // Capitalizacion de nombre de courier
            c.setNombre(FormatHelper.capitalizarTexto(c.getNombre()));
            if (c.getCorreo() != null) {
                c.setCorreo(c.getCorreo().trim().toLowerCase());
            }

            // Validar que no se repitan nombre, correo y ruc de un courier
            if (courierDAO.existeDato("nombre", c.getNombre())) {
                throw new BusinessLogicException("Ya existe un courier registrado con este nombre.");
            }
            if (c.getCorreo() != null && !c.getCorreo().isEmpty() && courierDAO.existeDato("correo", c.getCorreo())) {
                throw new BusinessLogicException("El correo electrónico ya está asignado a otro courier.");
            }
            if (c.getRuc() != null && !c.getRuc().isEmpty() && courierDAO.existeDato("ruc", c.getRuc())) {
                throw new BusinessLogicException("El número de RUC ya se encuentra registrado.");
            }

            Courier creado = courierDAO.save(c);
            TransactionContext.commit();
            return creado;
        } catch (Exception e) {
            TransactionContext.rollback();
            throw new BusinessLogicException("Error al registrar el courier: " + e.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Courier update(Courier c) throws BusinessLogicException {
        try {
            // Se usa transacción porque el DAO realiza múltiples updates (el cambio de activo)
            TransactionContext.getConnection();

            // Capitalizacion previa a la actualización
            c.setNombre(FormatHelper.capitalizarTexto(c.getNombre()));
            if (c.getCorreo() != null) {
                c.setCorreo(c.getCorreo().trim().toLowerCase());
            }

            // Validaciones al actualizar (asegurando no chocar con otros registros existentes)
            if (courierDAO.existeDatoExcluyendoId("nombre", c.getNombre(), c.getId())) {
                throw new BusinessLogicException("Ya existe otro courier registrado con este nombre.");
            }
            if (c.getCorreo() != null && !c.getCorreo().isEmpty() && courierDAO.existeDatoExcluyendoId("correo", c.getCorreo(), c.getId())) {
                throw new BusinessLogicException("El correo electrónico ya está asignado a otro courier.");
            }
            if (c.getRuc() != null && !c.getRuc().isEmpty() && courierDAO.existeDatoExcluyendoId("ruc", c.getRuc(), c.getId())) {
                throw new BusinessLogicException("El número de RUC ya se encuentra registrado por otro courier.");
            }

            Courier actualizado = courierDAO.update(c);
            TransactionContext.commit();
            return actualizado;
        } catch (Exception e) {
            TransactionContext.rollback();
            throw new BusinessLogicException("Error al actualizar el courier: " + e.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Courier load(Integer id) throws BusinessLogicException {
        try {
            return courierDAO.load(id);
        } catch (Exception e) {
            throw new BusinessLogicException("Error al cargar el courier: " + e.getMessage());
        }
    }

    @Override
    public List<Courier> listAll() throws BusinessLogicException {
        try {
            return courierDAO.listAll();
        } catch (Exception e) {
            throw new BusinessLogicException("Error al listar los couriers: " + e.getMessage());
        }
    }

    @Override
    public void remove(Courier courier) throws BusinessLogicException {
        try {
            // Validación: No permitir eliminar si está asignado
            if (courier.isEsAsignado()) {
                throw new BusinessLogicException("No puedes eliminar el courier que está actualmente asignado. Primero asigna otro diferente.");
            }

            TransactionContext.getConnection();
            courierDAO.remove(courier);
            TransactionContext.commit();
        } catch (Exception e) {
            TransactionContext.rollback();
            throw new BusinessLogicException("Error al eliminar: " + e.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Courier buscarAsignado() throws BusinessLogicException {
        try {
            return courierDAO.buscarAsignado();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }
}
