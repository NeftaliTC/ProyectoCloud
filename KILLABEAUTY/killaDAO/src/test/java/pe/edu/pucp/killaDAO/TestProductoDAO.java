package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Producto;
import pe.edu.pucp.killaDAO.Impl.ProductoDAOImpl;
import java.sql.SQLException;
import java.util.List;

public class TestProductoDAO {
    public static void main(String[] args) {
        ProductoDAO dao = new ProductoDAOImpl();

        try {
            // 1) SAVE
            Producto p = new Producto();
            p.setNombre("Labial Matte Killa");
            p.setPrecioBase(35.90);
            p.setStock(40);
            p.setDisponible(true);
            p.setPromocion(false);
            p.getMarca().setId(1);     // Debe existir Marca en BD
            p.getSubcategoria().setId(1); // Debe existir Categoria en BD

            Producto guardado = dao.save(p);
            System.out.println("SAVE OK -> id: " + guardado.getId());

            // 2) LOAD
            Producto cargado = dao.load(guardado.getId());
            System.out.println("LOAD OK -> " + (cargado != null ? cargado.getNombre() : "null"));

            // 3) UPDATE
            if (cargado != null) {
                cargado.setNombre("Labial Matte PRO");
                dao.update(cargado);
                System.out.println("UPDATE OK -> " + cargado.getNombre());
            }

            // 4) LIST ALL
            List<Producto> lista = dao.listAll();
            System.out.println("LIST ALL -> total: " + lista.size());

            // 5) REMOVE
            dao.remove(cargado);
            System.out.println("REMOVE OK");

        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
