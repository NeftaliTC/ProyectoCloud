package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.killaBeauty.bl.EscalaPrecioBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.EscalaPrecio;
import pe.edu.pucp.killaDAO.EscalaPrecioDAO;
import pe.edu.pucp.killaDAO.Impl.EscalaPrecioDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class EscalaPrecioBLImpl implements EscalaPrecioBL {
    private EscalaPrecioDAO escalaDAO = new EscalaPrecioDAOImpl();

    @Override
    public EscalaPrecio create(EscalaPrecio e) throws BusinessLogicException {
        try {
            if(e.getProducto() == null)
                throw new BusinessLogicException("La escala debe asociarse a un producto");
            if(e.getCantidadMinima() <= 0)
                throw new BusinessLogicException("La cantidad mínima debe ser mayor a 0");
            if(e.getPrecioUnitario() <= 0)
                throw new BusinessLogicException("El precio unitario debe ser mayor a 0");
            return escalaDAO.save(e);
        } catch (SQLException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    @Override
    public EscalaPrecio update(EscalaPrecio e) throws BusinessLogicException {
        try {
            return escalaDAO.update(e);
        } catch (SQLException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    @Override
    public void remove(EscalaPrecio e) throws BusinessLogicException {
        try {
            escalaDAO.remove(e);
        } catch (SQLException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    @Override
    public List<EscalaPrecio> listAll() throws BusinessLogicException {
        try {
            return escalaDAO.listAll();
        } catch (SQLException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    @Override
    public List<EscalaPrecio> listByProductoId(int idProducto) throws BusinessLogicException {
        try {
            return escalaDAO.listByProductoId(idProducto);
        } catch (SQLException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    @Override
    public EscalaPrecio load(int id) throws BusinessLogicException {
        try {
            return escalaDAO.load(id);
        } catch (SQLException ex) {
            throw new BusinessLogicException(ex);
        }
    }
}
