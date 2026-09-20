package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Categoria;

import java.util.List;

public interface CategoriaBL {
    Categoria create(Categoria categoria) throws BusinessLogicException;
    Categoria update(Categoria categoria) throws BusinessLogicException;
    void remove(Categoria categoria) throws BusinessLogicException;
    List<Categoria> listAll() throws BusinessLogicException;
    Categoria load(int id) throws BusinessLogicException;
}
