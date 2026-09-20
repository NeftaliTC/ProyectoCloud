package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.CategoriaBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Categoria;
import pe.edu.pucp.killaDAO.CategoriaDAO;
import pe.edu.pucp.killaDAO.Impl.CategoriaDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class CategoriaBLImpl implements CategoriaBL {
    private CategoriaDAO categoriaDAO = new CategoriaDAOImpl();

    @Override
    public Categoria create(Categoria c) throws BusinessLogicException {
        try {
            if(c.getDescripcion() == null || c.getDescripcion().isEmpty())
                throw new BusinessLogicException("El nombre de la categoría no puede estar vacío");
            return categoriaDAO.save(c);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Categoria update(Categoria c) throws BusinessLogicException {
        try {
            return categoriaDAO.update(c);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(Categoria c) throws BusinessLogicException {
        try {
            categoriaDAO.remove(c);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Categoria> listAll() throws BusinessLogicException {
        try {
            return categoriaDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Categoria load(int id) throws BusinessLogicException {
        try {
            return categoriaDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }
}
