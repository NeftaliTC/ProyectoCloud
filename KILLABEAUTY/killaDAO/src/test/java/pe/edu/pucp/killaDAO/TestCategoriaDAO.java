package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaDAO.Impl.CategoriaDAOImpl;
import pe.edu.pucp.killaBeauty.killaModelo.Categoria;

import java.sql.SQLException;
import java.util.List;

public class TestCategoriaDAO {
    public static void main(String[] args) {
        CategoriaDAO categoriaDAO = new CategoriaDAOImpl();

        try {
            // 1) SAVE
            Categoria nueva = new Categoria();
            nueva.setDescripcion("Categoria Test");
            Categoria guardada = categoriaDAO.save(nueva);
            System.out.println("SAVE OK -> id: " + guardada.getId());

            // 2) LOAD
            Categoria cargada = categoriaDAO.load(guardada.getId());
            System.out.println("LOAD OK -> " + (cargada != null ? cargada.getDescripcion() : "null"));

            // 3) UPDATE
            if (cargada != null) {
                cargada.setDescripcion("Categoria Test Updated");
                categoriaDAO.update(cargada);

                Categoria actualizada = categoriaDAO.load(cargada.getId());
                System.out.println("UPDATE OK -> " + (actualizada != null ? actualizada.getDescripcion() : "null"));
            }

            // 4) LIST ALL
            List<Categoria> lista = categoriaDAO.listAll();
            System.out.println("LIST ALL -> total: " + lista.size());
            for (Categoria c : lista) {
                System.out.println(" - " + c.getId() + " | " + c.getDescripcion());
            }

            // 5) REMOVE
            if (cargada != null) {
                categoriaDAO.remove(cargada);
                Categoria eliminada = categoriaDAO.load(cargada.getId());
                System.out.println("REMOVE OK -> " + (eliminada == null ? "eliminada" : "aun existe"));
            }

        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
