package pe.edu.pucp.killaBeauty.bl.Promocionales;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Campana;

import java.util.List;

public interface CampanaBL {
    Campana create(Campana campana) throws BusinessLogicException;
    Campana update(Campana campana) throws BusinessLogicException;

    Campana remove(Campana campana) throws BusinessLogicException;

    Campana load(Integer idCampana) throws BusinessLogicException;
    List<Campana> loadAll() throws BusinessLogicException;
}
