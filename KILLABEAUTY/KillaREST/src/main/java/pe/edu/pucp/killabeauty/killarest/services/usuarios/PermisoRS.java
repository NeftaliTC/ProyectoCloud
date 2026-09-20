package pe.edu.pucp.killabeauty.killarest.services.usuarios;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killaBeauty.bl.Impl.PermisoBLImpl;
import pe.edu.pucp.killaBeauty.bl.Impl.Promocionales.CuponBLImpl;
import pe.edu.pucp.killaBeauty.bl.PermisoBL;
import pe.edu.pucp.killaBeauty.bl.Promocionales.CuponBL;
import pe.edu.pucp.killaBeauty.killaModelo.Permiso;

import java.util.List;

@Path("/permisos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PermisoRS {
    private final PermisoBL permisoBL = new PermisoBLImpl();
    @GET
    // GET /services/permisos
    // GET /services/permisos?nombre=Lectura
    public Response listarPermisos(@QueryParam("nombre") String nombre) {
        try {
            List<Permiso> permisos;
            if (nombre != null && !nombre.trim().isEmpty()) {
                permisos = permisoBL.listByNombre(nombre);
            } else {
                // Si no mandan nada se lista todo
                permisos = permisoBL.listAll();
            }

            return Response.ok(permisos).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
    // GET /services/permisos
    @POST
    public Response insertarPermiso(Permiso permiso) {
        try {
            Permiso permisoCreado = permisoBL.create(permiso);
            return Response.status(Response.Status.CREATED).entity(permisoCreado).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
    // GET /services/permisos({id}
    @PUT
    @Path("{id}")
    public Response actualizarPermiso(@PathParam("id") int id, Permiso permiso) {
        try {
            permiso.setId(id); // Aseguramos que el ID de la URL sea el que se actualiza
            Permiso permisoActualizado = permisoBL.update(permiso);
            return Response.ok(permisoActualizado).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
    // GET /services/permisos({id}
    @DELETE
    @Path("{id}")
    public Response eliminarPermiso(@PathParam("id") int id) {
        try {
            // Al igual que en Usuario, instanciamos un objeto solo con el ID para el remove
            Permiso permisoAEliminar = new Permiso();
            permisoAEliminar.setId(id);

            permisoBL.remove(permisoAEliminar);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
