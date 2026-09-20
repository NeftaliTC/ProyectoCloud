package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.RolPermisoBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.RolPermiso;
import pe.edu.pucp.killaDAO.RolPermisoDAO;
import pe.edu.pucp.killaDAO.Impl.RolPermisoDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class RolPermisoBLImpl implements RolPermisoBL {
    private RolPermisoDAO rolPermisoDAO = new RolPermisoDAOImpl();

    @Override
    public RolPermiso create(RolPermiso rolPermiso) throws BusinessLogicException {
        try {
            return rolPermisoDAO.save(rolPermiso);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(RolPermiso rolPermiso) throws BusinessLogicException {
        try {
            rolPermisoDAO.remove(rolPermiso);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<RolPermiso> listByTipoUsuario(int tipoUsuario) throws BusinessLogicException {
        try {
            return rolPermisoDAO.listByTipoUsuario(tipoUsuario);
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<RolPermiso> listAll() throws BusinessLogicException {
        try {
            return rolPermisoDAO.listAll();
        } catch(SQLException e) {
            throw new BusinessLogicException(e);
        }
    }
}
