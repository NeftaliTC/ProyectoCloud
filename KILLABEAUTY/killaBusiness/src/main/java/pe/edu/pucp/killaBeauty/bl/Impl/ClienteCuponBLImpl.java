package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.ClienteCuponBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.ClienteCupon;
import pe.edu.pucp.killaDAO.ClienteCuponDAO;
import pe.edu.pucp.killaDAO.Impl.ClienteCuponDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class ClienteCuponBLImpl implements ClienteCuponBL {
    private ClienteCuponDAO clienteCuponDAO = new ClienteCuponDAOImpl();

    @Override
    public ClienteCupon create(ClienteCupon clienteCupon) throws BusinessLogicException {
        validarClienteCupon(clienteCupon);
        try {
            return clienteCuponDAO.save(clienteCupon);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public ClienteCupon update(ClienteCupon clienteCupon) throws BusinessLogicException {
        validarClienteCupon(clienteCupon);
        try {
            return clienteCuponDAO.update(clienteCupon);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(ClienteCupon clienteCupon) throws BusinessLogicException {
        try {
            clienteCuponDAO.remove(clienteCupon);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public ClienteCupon load(Integer id) throws BusinessLogicException {
        try {
            return clienteCuponDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<ClienteCupon> listAll() throws BusinessLogicException {
        try {
            return clienteCuponDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<ClienteCupon> listByUsuarioId(Integer idUsuario) throws BusinessLogicException {
        try {
            return clienteCuponDAO.listByUsuarioId(idUsuario);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    private void validarClienteCupon(ClienteCupon clienteCupon) throws BusinessLogicException {
        if (clienteCupon == null) throw new BusinessLogicException("El cupon del cliente no puede ser nulo.");
        if (clienteCupon.getCupon() == null || clienteCupon.getCupon().getId() <= 0) throw new BusinessLogicException("Debe indicar un cupon valido.");
        if (clienteCupon.getUsuario() == null || clienteCupon.getUsuario().getId() <= 0) throw new BusinessLogicException("Debe indicar un usuario valido.");
    }
}