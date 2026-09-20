package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Permiso;
import pe.edu.pucp.killaDAO.Impl.PermisoDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class TestPermisoDAO {
    public static void main(String[] args) {
        PermisoDAO dao = new PermisoDAOImpl();

        try {
            Permiso p = new Permiso();
            p.setNombre("TEST_PERMISO");
            p.setDescripcion("Permiso temporal de prueba");
            p = dao.save(p);
            System.out.println("SAVE OK -> id: " + p.getId());

            Permiso loaded = dao.load(p.getId());
            System.out.println("LOAD OK -> " + (loaded != null ? loaded.getNombre() : "null"));

            p.setDescripcion("Permiso actualizado");
            dao.update(p);
            System.out.println("UPDATE OK");

            List<Permiso> all = dao.listAll();
            System.out.println("LIST ALL -> " + all.size());

            dao.remove(p);
            System.out.println("REMOVE OK -> " + (dao.load(p.getId()) == null));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
