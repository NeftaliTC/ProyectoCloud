package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.SubcategoriaBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Subcategoria;
import pe.edu.pucp.killaDAO.Impl.SubCategoriaDAOImpl;
import pe.edu.pucp.killaDAO.SubCategoriaDAO;

import java.sql.SQLException;
import java.util.List;

public class SubcategoriaBLImpl implements SubcategoriaBL {
    private SubCategoriaDAO subcategoriaDAO = new SubCategoriaDAOImpl();

    @Override
    public Subcategoria create(Subcategoria s) throws BusinessLogicException {
        try {
            if(s.getDescripcion() == null || s.getDescripcion().isEmpty())
                throw new BusinessLogicException("El nombre de la subcategoría no puede estar vacío");
            if(s.getCategoria() == null)
                throw new BusinessLogicException("La subcategoría debe pertenecer a una categoría");
            return subcategoriaDAO.save(s);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Subcategoria update(Subcategoria s) throws BusinessLogicException {
        try {
            return subcategoriaDAO.update(s);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(Subcategoria s) throws BusinessLogicException {
        try {
            subcategoriaDAO.remove(s);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Subcategoria> listAll() throws BusinessLogicException {
        try {
            return subcategoriaDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Subcategoria> listByCategoriaId(int idCategoria) throws BusinessLogicException {
        try {
            return subcategoriaDAO.listByCategoriaId(idCategoria);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Subcategoria load(int id) throws BusinessLogicException {
        try {
            return subcategoriaDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }
}
