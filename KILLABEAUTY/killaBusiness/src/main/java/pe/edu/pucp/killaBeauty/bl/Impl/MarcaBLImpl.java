package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.MarcaBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Marca;
import pe.edu.pucp.killaDAO.Impl.MarcaDAOImpl;
import pe.edu.pucp.killaDAO.MarcaDAO;

import java.sql.SQLException;
import java.util.List;

public class MarcaBLImpl implements MarcaBL {
    private MarcaDAO marcaDAO = new MarcaDAOImpl();

    @Override
    public Marca create(Marca marca) throws BusinessLogicException {
        try {
            if(marca.getDescripcion() == null || marca.getDescripcion().isEmpty())
                throw new BusinessLogicException("La descripción de la marca no puede estar vacía");
            return marcaDAO.save(marca);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Marca update(Marca marca) throws BusinessLogicException {
        try {
            return marcaDAO.update(marca);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(Marca marca) throws BusinessLogicException {
        try {
            marcaDAO.remove(marca);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Marca> listAll() throws BusinessLogicException {
        try {
            return marcaDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Marca load(int id) throws BusinessLogicException {
        try {
            return marcaDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }
}
