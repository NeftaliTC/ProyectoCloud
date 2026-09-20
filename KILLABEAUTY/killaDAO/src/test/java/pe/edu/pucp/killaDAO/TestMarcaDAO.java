package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Pais;
import pe.edu.pucp.killaDAO.Impl.MarcaDAOImpl;
import pe.edu.pucp.killaBeauty.killaModelo.Marca;

import java.sql.SQLException;
import java.util.List;

public class TestMarcaDAO {
    public static void main(String[] args) {
        MarcaDAO marcaDAO = new MarcaDAOImpl();

        try {
            // 1) SAVE
            Marca nueva = new Marca();
            nueva.setDescripcion("Marca Test");
            nueva.setPais(Pais.CHILE);
            Marca guardada = marcaDAO.save(nueva);
            System.out.println("SAVE OK -> id: " + guardada.getId());

            // 2) LOAD
            Marca cargada = marcaDAO.load(guardada.getId());
            System.out.println("LOAD OK -> " + (cargada != null ? cargada.getDescripcion() : "null"));

            // 3) UPDATE
            if (cargada != null) {
                cargada.setDescripcion("Marca Test Updated");
                marcaDAO.update(cargada);
                Marca actualizada = marcaDAO.load(cargada.getId());
                System.out.println("UPDATE OK -> " + actualizada.getDescripcion());
            }

            // 4) LIST ALL
            List<Marca> marcas = marcaDAO.listAll();
            System.out.println("LIST ALL -> total: " + marcas.size());
            for (Marca m : marcas) {
                System.out.println(" - " + m.getId() + " | " + m.getDescripcion() + " | " + m.getPais());
            }

            // 5) REMOVE
            if (cargada != null) {
                marcaDAO.remove(cargada);
                Marca eliminada = marcaDAO.load(cargada.getId());
                System.out.println("REMOVE OK -> " + (eliminada == null ? "eliminada" : "aun existe"));
            }

        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
