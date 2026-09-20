package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.DetalleCarrito;
import pe.edu.pucp.killaDAO.Impl.DetalleCarritoDAOImpl;
import java.sql.SQLException;
import java.util.List;

public class TestDetalleCarritoDAO {
    public static void main(String[] args) {
        /*DetalleCarritoDAO dao = new DetalleCarritoDAOImpl();

        try {
            // 1) SAVE
            DetalleCarrito nuevo = new DetalleCarrito();
            nuevo.setCantidad(3);
            nuevo.getProducto().setId(1); //Producto existe
            nuevo.setId(1); //Carrito existe

            DetalleCarrito guardado = dao.save(nuevo);
            System.out.println("SAVE OK -> id detalle: " + guardado.getId());

            // 2) LOAD
            DetalleCarrito cargado = dao.load(guardado.getId());
            System.out.println("LOAD OK -> Cantidad cargada: " + (cargado != null ? cargado.getCantidad() : "null"));

            // 3) UPDATE
            if (cargado != null) {
                cargado.setCantidad(10); // Actualizar a 10 unidades
                dao.update(cargado);
                DetalleCarrito actualizado = dao.load(cargado.getIdDetalleCarrito());
                System.out.println("UPDATE OK -> Nueva cantidad: " + actualizado.getCantidad());
            }

            // 4) LIST ALL
            List<DetalleCarrito> lista = dao.listAll();
            System.out.println("LIST ALL -> Total en tabla: " + lista.size());

            // 5) REMOVE
            if (cargado != null) {
                dao.remove(cargado);
                DetalleCarrito eliminado = dao.load(cargado.getIdDetalleCarrito());
                System.out.println("REMOVE OK -> " + (eliminado == null ? "Eliminado con éxito" : "Aún existe"));
            }

        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            e.printStackTrace();
        }*/
    }
}
