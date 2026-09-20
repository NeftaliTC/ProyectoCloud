package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Marca;

import java.util.List;

public interface MarcaBL {
    Marca create(Marca m) throws BusinessLogicException;
    Marca update(Marca m) throws BusinessLogicException;
    void remove(Marca m) throws BusinessLogicException;
    Marca load(int id) throws BusinessLogicException;
    List<Marca> listAll() throws BusinessLogicException;
}
