package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Subcategoria;

import java.util.List;

public interface SubcategoriaBL {
    Subcategoria create(Subcategoria subcategoria) throws BusinessLogicException;
    Subcategoria update(Subcategoria subcategoria) throws BusinessLogicException;
    void remove(Subcategoria subcategoria) throws BusinessLogicException;
    List<Subcategoria> listAll() throws BusinessLogicException;
    List<Subcategoria> listByCategoriaId(int idCategoria) throws BusinessLogicException;
    Subcategoria load(int id) throws BusinessLogicException;
}
