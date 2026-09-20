package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Permiso;
import pe.edu.pucp.killaBeauty.killaModelo.RolPermiso;
import pe.edu.pucp.killaBeauty.killaModelo.TipoUsuario;
import pe.edu.pucp.killaDAO.Impl.PermisoDAOImpl;
import pe.edu.pucp.killaDAO.Impl.RolPermisoDAOImpl;

import java.sql.SQLException;

public class TestRolPermisoDAO {
    public static void main(String[] args) {
        PermisoDAO permisoDAO = new PermisoDAOImpl();
        RolPermisoDAO rolPermisoDAO = new RolPermisoDAOImpl();

        try {
            // Crear permiso temporal
            Permiso permiso = new Permiso();
            permiso.setNombre("PERMISO_TEMP_RP");
            permiso.setDescripcion("Permiso temporal para Rol_Permiso test");
            permiso = permisoDAO.save(permiso);

            RolPermiso rp = new RolPermiso(TipoUsuario.ADMINISTRADOR, permiso);

            // SAVE
            rolPermisoDAO.save(rp);
            System.out.println("ROL_PERMISO SAVE OK");

            // LOAD (por id_permiso, según implementación)
            RolPermiso cargado = rolPermisoDAO.load(permiso.getId());
            System.out.println("ROL_PERMISO LOAD OK -> "
                    + (cargado != null ? cargado.getTipoUsuario().getNombre() : "null"));

            // REMOVE
            rolPermisoDAO.remove(rp);
            System.out.println("ROL_PERMISO REMOVE OK");

            // limpiar permiso
            permisoDAO.remove(permiso);
            System.out.println("PERMISO CLEANUP OK");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
