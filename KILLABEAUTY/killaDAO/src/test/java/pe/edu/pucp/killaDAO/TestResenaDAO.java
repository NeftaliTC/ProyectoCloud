package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Resena;
import pe.edu.pucp.killaDAO.Impl.ResenaDAOImpl;
import java.sql.SQLException;
import java.util.List;

public class TestResenaDAO {
    public static void main(String[] args) {
        /*ResenaDAO dao = new ResenaDAOImpl();

        try {
            // 1) SAVE
            Resena nueva = new Resena();
            nueva.setTitulo("Me encantó");
            nueva.setComentario("Es de buena calidad.");
            nueva.setCalificacion(5);
            nueva.setVerificado(true);

            // Usamos Composición (sacamos el ID de los objetos)
            nueva.getCliente().setId(1);  // Debe existir en tabla Usuario
            nueva.getProducto().setId(1); // Debe existir en tabla Producto

            Resena guardada = dao.save(nueva);
            System.out.println("SAVE OK -> id: " + guardada.getIdResena());

            // 2) LOAD
            Resena cargada = dao.load(guardada.getIdResena());
            if (cargada != null) {
                System.out.println("LOAD OK -> Titulo: " + cargada.getTitulo());
                System.out.println("        -> Usuario ID: " + cargada.getCliente().getId());
            }

            // 3) UPDATE
            if (cargada != null) {
                cargada.setTitulo("Editado: Muy bueno");
                dao.update(cargada);
                Resena actualizada = dao.load(cargada.getIdResena());
                System.out.println("UPDATE OK -> Nuevo titulo: " + actualizada.getTitulo());
            }

            // 4) LIST ALL
            List<Resena> lista = dao.listAll();
            System.out.println("LIST ALL -> total reseñas: " + lista.size());

            // 5) REMOVE
            if (cargada != null) {
                dao.remove(cargada);
                Resena eliminada = dao.load(cargada.getIdResena());
                System.out.println("REMOVE OK -> " + (eliminada == null ? "eliminado" : "error al borrar"));
            }

        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            e.printStackTrace();
        }*/
    }
}
