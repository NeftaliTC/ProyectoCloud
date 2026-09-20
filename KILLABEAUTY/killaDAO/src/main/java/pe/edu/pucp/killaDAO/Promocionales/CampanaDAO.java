package pe.edu.pucp.killaDAO.Promocionales;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Campana;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;

public interface CampanaDAO extends BaseDAO<Campana, Integer>{
    List<Campana> listAll() throws SQLException;
}
