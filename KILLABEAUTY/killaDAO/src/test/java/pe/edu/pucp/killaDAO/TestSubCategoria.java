package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Categoria;
import pe.edu.pucp.killaBeauty.killaModelo.Subcategoria;
import pe.edu.pucp.killaDAO.Impl.CategoriaDAOImpl;
import pe.edu.pucp.killaDAO.Impl.SubCategoriaDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class TestSubCategoria {

    public static void main(String[] args) {
        CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
        SubCategoriaDAO subCategoriaDAO = new SubCategoriaDAOImpl();

        Categoria categoriaCreada = null;
        Subcategoria subcategoriaCreada = null;

        try {
            // 0) Crear categoría padre para asegurar FK válida
            Categoria categoria = new Categoria();
            categoria.setDescripcion("Categoria Test FK");
            categoriaCreada = categoriaDAO.save(categoria);
            Integer idCategoria = categoriaCreada.getId();
            System.out.println("CATEGORIA SAVE OK -> id: " + idCategoria);

            // 1) SAVE Subcategoria
            Subcategoria nueva = new Subcategoria();
            nueva.setDescripcion("Subcategoria Test");
            subcategoriaCreada = subCategoriaDAO.save(nueva);
            System.out.println("SUBCATEGORIA SAVE OK -> id: " + subcategoriaCreada.getId());

            // 2) LOAD
            Subcategoria cargada = subCategoriaDAO.load(subcategoriaCreada.getId());
            System.out.println("LOAD OK -> " + (cargada != null ? cargada.getDescripcion() : "null"));

            // 3) UPDATE
            if (cargada != null) {
                cargada.setDescripcion("Subcategoria Test Updated");
                subCategoriaDAO.update(cargada);

                Subcategoria actualizada = subCategoriaDAO.load(cargada.getId());
                System.out.println("UPDATE OK -> " + (actualizada != null ? actualizada.getDescripcion() : "null"));
            }

            // 4) LIST BY CATEGORIA
            List<Subcategoria> porCategoria = subCategoriaDAO.listByCategoriaId(idCategoria);
            System.out.println("LIST BY CATEGORIA -> total: " + porCategoria.size());
            for (Subcategoria s : porCategoria) {
                System.out.println(" - " + s.getId() + " | " + s.getDescripcion());
            }

            // 5) REMOVE Subcategoria
            subCategoriaDAO.remove(subcategoriaCreada);
            Subcategoria eliminadaSub = subCategoriaDAO.load(subcategoriaCreada.getId());
            System.out.println("SUBCATEGORIA REMOVE OK -> " + (eliminadaSub == null ? "eliminada" : "aun existe"));

            // 6) REMOVE Categoria (cleanup)
            categoriaDAO.remove(categoriaCreada);
            Categoria eliminadaCat = categoriaDAO.load(categoriaCreada.getId());
            System.out.println("CATEGORIA REMOVE OK -> " + (eliminadaCat == null ? "eliminada" : "aun existe"));

            System.out.println("TEST FINALIZADO OK");

        } catch (SQLException e) {
            System.err.println("Error SQL en test: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e); // para que el test falle realmente
        }
    }
}
