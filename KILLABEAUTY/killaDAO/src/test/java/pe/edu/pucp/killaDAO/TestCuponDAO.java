package pe.edu.pucp.killaDAO;

import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.TipoDescuento;
import pe.edu.pucp.killaDAO.Impl.Promocionales.CuponDAOImpl;
import pe.edu.pucp.killaDAO.Promocionales.CuponDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TestCuponDAO {
    public static void main(String[] args) {
        try {
            CuponDAO cuponDAO = new CuponDAOImpl();

            // 1) SAVE - Prueba de guardado
            Cupon nuevo = new Cupon();
            nuevo.setCodigo("KILLATEST2026");
            nuevo.setDescripcion("Descuento de prueba (Primavera)");

            // Nuevos métodos y atributos
            nuevo.setTipoDescuento(TipoDescuento.PORCENTAJE);
            nuevo.setValorDescuento(20.0);
            nuevo.setMontoMaximoDescuento(50.0);
            nuevo.setMontoMinimoCompra(100.0);
            nuevo.setMaxUsosGenerales(100);
            nuevo.setCampana(null);

            nuevo.setFechaInicio(LocalDate.now());
            nuevo.setFechaFin(LocalDate.now().plusDays(7));
            nuevo.setActivo(true);

            Cupon guardado = cuponDAO.save(nuevo);
            System.out.println("SAVE OK -> id: " + guardado.getId());

            // 2) LOAD
            Cupon cargado = cuponDAO.load(guardado.getId());
            System.out.println("LOAD OK -> Código: " + (cargado != null ? cargado.getCodigo() : "null") +
                    " | Tipo: " + cargado.getTipoDescuento().name());

            // 3) UPDATE - Actualización
            if (cargado != null) {
                cargado.setValorDescuento(25.0);
                cargado.setDescripcion("Descuento ACTUALIZADO");
                cuponDAO.update(cargado);

                Cupon actualizado = cuponDAO.load(cargado.getId());
                System.out.println("UPDATE OK -> Nuevo valor descuento: " + actualizado.getValorDescuento());
            }

            // 4) LIST ALL
            List<Cupon> cupones = cuponDAO.listAll();
            System.out.println("LIST ALL -> total: " + cupones.size());
            for (Cupon c : cupones) {
                System.out.println(" - ID: " + c.getId() + " | Código: " + c.getCodigo() +
                        " | Activo: " + c.isActivo() + " | Usos Max: " + c.getMaxUsosGenerales());
            }

            // 5) REMOVE (Borrado lógico)
            if (cargado != null) {
                cuponDAO.remove(cargado);
                Cupon eliminado = cuponDAO.load(cargado.getId());

                System.out.println("REMOVE OK -> " + (eliminado != null && !eliminado.isActivo() ? "inactivo" : "aun activo"));
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
