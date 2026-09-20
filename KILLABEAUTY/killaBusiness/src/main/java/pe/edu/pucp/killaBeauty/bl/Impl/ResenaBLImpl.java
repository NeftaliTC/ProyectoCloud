package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.ResenaBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Resena;
import pe.edu.pucp.killaDAO.Impl.RolPermisoDAOImpl;
import pe.edu.pucp.killaDAO.ResenaDAO;
import pe.edu.pucp.killaDAO.Impl.ResenaDAOImpl;
import pe.edu.pucp.killaDAO.RolPermisoDAO;

import java.sql.SQLException;
import java.util.List;

public class ResenaBLImpl implements ResenaBL {
    private ResenaDAO resenaDAO = new ResenaDAOImpl();
    @Override
    public Resena create(Resena r) throws BusinessLogicException {
        try {

            if(r.getProducto() == null) {
                throw new BusinessLogicException("La reseña debe pertenecer a un producto");
            }
            if(r.getComentario() == null || r.getComentario().trim().isEmpty()) {
                throw new BusinessLogicException("El comentario de la reseña no puede estar vacío");
            }

            return resenaDAO.save(r);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Resena update(Resena r) throws BusinessLogicException {
        try {
            return resenaDAO.update(r);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(Resena r) throws BusinessLogicException {
        try {
            resenaDAO.remove(r);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Resena load(Integer id) throws BusinessLogicException {
        try {
            return resenaDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Resena> listAll() throws BusinessLogicException {
        return List.of();
    }

    @Override
    public List<Resena> listByProductoId(int idProducto) throws BusinessLogicException {
        try {

            return resenaDAO.listByProductoId(idProducto);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Resena> listByUsuarioId(int idUsuario) throws BusinessLogicException {
        try {
            return resenaDAO.listByUsuarioId(idUsuario);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }
}
