package pe.edu.pucp.killaDAO.Base;
import java.sql.SQLException;

public interface BaseDAO<T, ID> {
    //CREATE--> save  READ-->load  update-->update  delete-->remove
    T load(ID id) throws SQLException;
    T save(T t)throws SQLException;
    T update(T t)throws SQLException;
    void remove(T t)throws SQLException;
}
