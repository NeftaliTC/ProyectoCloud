package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Resena;
import pe.edu.pucp.killaDAO.Base.BaseDAO;

import java.sql.SQLException;
import java.util.List;
public interface ResenaDAO extends BaseDAO<Resena,Integer> {
    List<Resena> listByProductoId(int idProducto) throws SQLException;
    List<Resena> listByUsuarioId(int idUsuario) throws SQLException;
}