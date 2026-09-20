package pe.edu.pucp.killaBeauty.bl.Impl.Promocionales;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.bl.Promocionales.CuponBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Campana;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.TipoDescuento;
import pe.edu.pucp.killaDAO.Impl.Promocionales.CampanaDAOImpl;
import pe.edu.pucp.killaDAO.Impl.Promocionales.CuponDAOImpl;
import pe.edu.pucp.killaDAO.Promocionales.CampanaDAO;
import pe.edu.pucp.killaDAO.Promocionales.CuponDAO;

import java.util.List;

public class CuponBLImpl implements CuponBL {

    private CuponDAO cuponDAO = new CuponDAOImpl();
    private CampanaDAO campanaDAO = new CampanaDAOImpl();

    @Override
    public Cupon create(Cupon cupon) throws BusinessLogicException {
        try {
            validarCupon(cupon, false);
            validarCampana(cupon, true);
            Cupon guardado = cuponDAO.save(cupon);
            TransactionContext.commit();
            return guardado;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw wrap(ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Cupon update(Cupon cupon) throws BusinessLogicException {
        try {
            validarCupon(cupon, true);
            validarCampana(cupon, false);
            Cupon actualizado = cuponDAO.update(cupon);
            TransactionContext.commit();
            return actualizado;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw wrap(ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void remove(Cupon cupon) throws BusinessLogicException {
        try {
            if (cupon == null || cupon.getId() <= 0) {
                throw new BusinessLogicException("Se requiere un ID valido para eliminar el cupon.");
            }
            cuponDAO.remove(cupon);
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw wrap(ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Cupon load(Integer id) throws BusinessLogicException {
        try {
            if (id == null || id <= 0) throw new BusinessLogicException("ID de cupon invalido.");
            Cupon cupon = cuponDAO.load(id);
            TransactionContext.commit();
            return cupon;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw wrap(ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<Cupon> listAll() throws BusinessLogicException {
        try {
            List<Cupon> lista = cuponDAO.listAll();
            TransactionContext.commit();
            return lista;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw wrap(ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarCupon(Cupon cupon, boolean requiereId) throws BusinessLogicException {
        if (cupon == null) throw new BusinessLogicException("El cupon no puede ser nulo.");
        if (requiereId && cupon.getId() <= 0) throw new BusinessLogicException("Se requiere un ID valido para actualizar el cupon.");
        if (cupon.getCodigo() == null || cupon.getCodigo().trim().isEmpty()) throw new BusinessLogicException("El codigo del cupon es obligatorio.");
        if (cupon.getValorDescuento() <= 0) throw new BusinessLogicException("El valor de descuento debe ser mayor a cero.");
        if (cupon.getTipoDescuento() == null) throw new BusinessLogicException("El cupon debe tener tipo de descuento.");
        if (cupon.getTipoDescuento() == TipoDescuento.PORCENTAJE && cupon.getValorDescuento() > 100) {
            throw new BusinessLogicException("El porcentaje de descuento no puede ser mayor al 100%.");
        }
        if (cupon.getFechaInicio() == null || cupon.getFechaFin() == null) throw new BusinessLogicException("El cupon debe tener fechas de vigencia.");
        if (cupon.getFechaInicio().isAfter(cupon.getFechaFin())) throw new BusinessLogicException("La fecha de inicio no puede ser posterior a la fecha de fin.");
    }

    private void validarCampana(Cupon cupon, boolean exigirActiva) throws Exception {
        if (cupon.getCampana() != null && cupon.getCampana().getIdCampana() > 0) {
            Campana campanaBD = campanaDAO.load(cupon.getCampana().getIdCampana());
            if (campanaBD == null) throw new BusinessLogicException("La campana asociada no existe.");
            if (exigirActiva && !campanaBD.isActivo()) throw new BusinessLogicException("No se puede asociar un cupon a una campana inactiva.");
        }
    }

    private BusinessLogicException wrap(Exception ex) {
        if (ex instanceof BusinessLogicException) return (BusinessLogicException) ex;
        return new BusinessLogicException(ex);
    }
}