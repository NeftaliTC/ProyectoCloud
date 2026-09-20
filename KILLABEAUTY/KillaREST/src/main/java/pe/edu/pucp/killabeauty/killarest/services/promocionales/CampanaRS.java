package pe.edu.pucp.killabeauty.killarest.services.promocionales;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killaBeauty.bl.Impl.Promocionales.CampanaBLImpl;
import pe.edu.pucp.killaBeauty.bl.Promocionales.CampanaBL;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Campana;

import java.util.List;

@Path("/campanas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CampanaRS {
    private final CampanaBL campanaBL = new CampanaBLImpl();

    @GET
    public Response listarCampanas() {
        try {
            List<Campana> campanas = campanaBL.loadAll();
            return Response.ok(campanas).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    public Response obtenerCampanaPorId(@PathParam("id") int id) {
        try {
            Campana campana = campanaBL.load(id);
            if (campana != null) {
                return Response.ok(campana).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    public Response insertarCampana(Campana campana) {
        try {
            Campana creada = campanaBL.create(campana);
            return Response.status(Response.Status.CREATED).entity(creada).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response actualizarCampana(@PathParam("id") int id, Campana campana) {
        try {
            campana.setIdCampana(id);
            Campana actualizada = campanaBL.update(campana);
            return Response.ok(actualizada).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminarCampana(@PathParam("id") int id) {
        try {
            Campana campana = new Campana();
            campana.setIdCampana(id);
            campanaBL.remove(campana);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}