package pe.edu.pucp.killaBeauty.bl.Impl.Promocionales;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.bl.Promocionales.CampanaBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Campana;
import pe.edu.pucp.killaDAO.Impl.Promocionales.CampanaDAOImpl;
import pe.edu.pucp.killaDAO.Promocionales.CampanaDAO;
import java.util.List;

public class CampanaBLImpl implements CampanaBL {
    private CampanaDAO campanaDAO;

    public CampanaBLImpl() {
        this.campanaDAO = new CampanaDAOImpl();
    }
    @Override
    public Campana create(Campana campana) throws BusinessLogicException {
        try {
            // 1. Validando reglas de negocio
            if (campana.getNombre() == null || campana.getNombre().trim().isEmpty()) {
                throw new BusinessLogicException("El nombre de la campaña es obligatorio.");
            }
            campana.setActivo(true);
            // 3. Creando la campaña en la BD
            campanaDAO.save(campana);
            // 4. Confirmar cambios
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            if (ex instanceof BusinessLogicException) {
                throw (BusinessLogicException) ex;
            } else {
                throw new BusinessLogicException(ex);
            }
        } finally {
            TransactionContext.close();
        }
        return campana;
    }

    @Override
    public Campana update(Campana campana) throws BusinessLogicException {
        try {
            // 1. Validaciones
            if (campana.getIdCampana() == 0) {
                throw new BusinessLogicException("Se requiere un ID válido para actualizar la campaña.");
            }
            if (campana.getNombre() == null || campana.getNombre().trim().isEmpty()) {
                throw new BusinessLogicException("El nombre de la campaña es obligatorio.");
            }
            campanaDAO.update(campana);
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            if (ex instanceof BusinessLogicException) {
                throw (BusinessLogicException) ex;
            } else {
                throw new BusinessLogicException(ex);
            }
        } finally {
            TransactionContext.close();
        }
        return campana;
    }

    @Override
    public Campana remove(Campana campana) throws BusinessLogicException {
        try {
            if (campana.getIdCampana() == 0) {
                throw new BusinessLogicException("Se requiere un ID válido para eliminar la campaña.");
            }

            // Aquí se pueden agregar validaciones extra (ej. verificar si hay cupones amarrados)

            campanaDAO.remove(campana);

            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            if (ex instanceof BusinessLogicException) {
                throw (BusinessLogicException) ex;
            } else {
                throw new BusinessLogicException(ex);
            }
        } finally {
            TransactionContext.close();
        }
        return campana;
    }

    @Override
    public Campana load(Integer id) throws BusinessLogicException {
        Campana campana = null;
        try {
            if (id == null || id <= 0) {
                throw new BusinessLogicException("ID de campaña inválido.");
            }

            campana = campanaDAO.load(id);
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            if (ex instanceof BusinessLogicException) {
                throw (BusinessLogicException) ex;
            } else {
                throw new BusinessLogicException(ex);
            }
        } finally {
            TransactionContext.close();
        }
        return campana;
    }

    @Override
    public List<Campana> loadAll() throws BusinessLogicException {
        List<Campana> lista = null;
        try {
            lista = campanaDAO.listAll();
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            if (ex instanceof BusinessLogicException) {
                throw (BusinessLogicException) ex;
            } else {
                throw new BusinessLogicException(ex);
            }
        } finally {
            TransactionContext.close();
        }
        return lista;
    }
}
