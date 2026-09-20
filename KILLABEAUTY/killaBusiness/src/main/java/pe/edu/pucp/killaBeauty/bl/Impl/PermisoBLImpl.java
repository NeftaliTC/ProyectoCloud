package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.PermisoBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Permiso;
import pe.edu.pucp.killaDAO.Impl.PermisoDAOImpl;
import pe.edu.pucp.killaDAO.PermisoDAO;

import java.sql.SQLException;
import java.util.List;

public class PermisoBLImpl implements PermisoBL {
    private PermisoDAO permisoDAO = new PermisoDAOImpl();

    @Override
    public Permiso create(Permiso permiso) throws BusinessLogicException {
        try {
            return permisoDAO.save(permiso);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Permiso update(Permiso permiso) throws BusinessLogicException {
        try {
            return permisoDAO.update(permiso);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(Permiso permiso) throws BusinessLogicException {
        try {
            permisoDAO.remove(permiso);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Permiso> listAll() throws BusinessLogicException {
        try {
            return permisoDAO.listAll();
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Permiso> listByNombre(String nombre) throws BusinessLogicException {
        try {
            return permisoDAO.listByNombre(nombre);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }
}
