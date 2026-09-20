package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.TarifaEnvioBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.TarifaEnvio;
import pe.edu.pucp.killaDAO.Impl.TarifaEnvioDAOImpl;
import pe.edu.pucp.killaDAO.TarifaEnvioDAO;

import java.sql.SQLException;
import java.util.List;

public class TarifaEnvioBLImpl implements TarifaEnvioBL {
    private TarifaEnvioDAO tarifaDAO = new TarifaEnvioDAOImpl();

    @Override
    public TarifaEnvio create(TarifaEnvio tarifa) throws BusinessLogicException {
        validarTarifa(tarifa);
        try {
            return tarifaDAO.save(tarifa);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public TarifaEnvio update(TarifaEnvio tarifa) throws BusinessLogicException {
        validarTarifa(tarifa);
        try {
            return tarifaDAO.update(tarifa);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(TarifaEnvio tarifa) throws BusinessLogicException {
        try {
            tarifaDAO.remove(tarifa);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public TarifaEnvio load(Integer id) throws BusinessLogicException {
        try {
            return tarifaDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<TarifaEnvio> listAll() throws BusinessLogicException {
        try {
            return tarifaDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<TarifaEnvio> listByCourierId(Integer idCourier) throws BusinessLogicException {
        try {
            return tarifaDAO.listByCourierId(idCourier);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    private void validarTarifa(TarifaEnvio tarifa) throws BusinessLogicException {
        if (tarifa == null) throw new BusinessLogicException("La tarifa no puede ser nula.");
        if (tarifa.getCourier() == null || tarifa.getCourier().getId() <= 0) throw new BusinessLogicException("La tarifa debe tener un courier valido.");
        if (tarifa.getNombreDistrito() == null || tarifa.getNombreDistrito().trim().isEmpty()) throw new BusinessLogicException("La tarifa debe tener distrito.");
        if (tarifa.getCosto() == null || tarifa.getCosto() < 0) throw new BusinessLogicException("El costo de envio no puede ser negativo.");
    }
}