package pe.edu.pucp.killabeauty.killarest.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import pe.edu.pucp.killabeauty.killarest.services.logistica.CourierRS;
import pe.edu.pucp.killabeauty.killarest.services.productos.*;
import pe.edu.pucp.killabeauty.killarest.services.promocionales.CampanaRS;
import pe.edu.pucp.killabeauty.killarest.services.promocionales.CuponRS;
import pe.edu.pucp.killabeauty.killarest.services.reportes.ReporteRS;
import pe.edu.pucp.killabeauty.killarest.services.usuarios.DireccionRS;
import pe.edu.pucp.killabeauty.killarest.services.usuarios.PermisoRS;
import pe.edu.pucp.killabeauty.killarest.services.usuarios.UsuarioRS;
import pe.edu.pucp.killabeauty.killarest.services.ventas.CarritoRS;
import pe.edu.pucp.killabeauty.killarest.services.ventas.DetalleCarritoRS;
import pe.edu.pucp.killabeauty.killarest.services.ventas.PedidoRS;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class KillaRestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(ProductoRS.class);
        resources.add(CategoriaRS.class);
        resources.add(MarcaRS.class);
        resources.add(PaisRS.class);
        resources.add(CourierRS.class);
        resources.add(CampanaRS.class);
        resources.add(CuponRS.class);
        resources.add(PermisoRS.class);
        resources.add(UsuarioRS.class);
        resources.add(PedidoRS.class);
        resources.add(DireccionRS.class);
        resources.add(CarritoRS.class);
        resources.add(DetalleCarritoRS.class);
        resources.add(EscalaPrecioRS.class);
        resources.add(ReporteRS.class);
        return resources;
    }
}

