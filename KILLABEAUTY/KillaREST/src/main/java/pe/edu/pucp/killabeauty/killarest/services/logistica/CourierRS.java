package pe.edu.pucp.killabeauty.killarest.services.logistica;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killabeauty.killarest.dto.ErrorDTO;
import pe.edu.pucp.killaBeauty.bl.CourierBL;
import pe.edu.pucp.killaBeauty.bl.Impl.CourierBLImpl;
import pe.edu.pucp.killaBeauty.killaModelo.Courier;

@Path("/courier")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourierRS {
    private final CourierBL courierBL = new CourierBLImpl();

    @GET
    @Path("test")
    public Response test() {
        return Response.ok("CourierRS funciona").build();
    }

    @GET
    public Response listar() {
        try {
            return Response.ok(courierBL.listAll()).build();
        } catch (Exception ex) {
            return serverError(ex);
        }
    }

    @GET
    @Path("{id}")
    public Response obtenerPorId(@PathParam("id") Integer id) {
        try {
            Courier courier = courierBL.load(id);
            if (courier == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorDTO("No existe un courier con id " + id))
                        .build();
            }
            return Response.ok(courier).build();
        } catch (Exception ex) {
            return serverError(ex);
        }
    }

    @POST
    public Response registrar(Courier courier) {
        try {
            Courier creado = courierBL.create(courier);
            return Response.status(Response.Status.CREATED).entity(creado).build();
        } catch (Exception ex) {
            return badRequest(ex);
        }
    }

    @PUT
    public Response actualizar(Courier courier) {
        try {
            return Response.ok(courierBL.update(courier)).build();
        } catch (Exception ex) {
            return badRequest(ex);
        }
    }

    @PUT
    @Path("{id}")
    public Response actualizarPorId(@PathParam("id") int id, Courier courier) {
        try {
            courier.setId(id);
            return Response.ok(courierBL.update(courier)).build();
        } catch (Exception ex) {
            return badRequest(ex);
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            // Primero cargamos el courier para poder pasárselo al método remove
            Courier courier = courierBL.load(id);
            if (courier == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorDTO("No existe un courier con id " + id))
                        .build();
            }

            // Llamamos a tu método de lógica de negocio
            courierBL.remove(courier);

            return Response.ok().build();
        } catch (Exception ex) {
            return badRequest(ex);
        }
    }

    private Response badRequest(Exception ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorDTO(ex.getMessage()))
                .build();
    }

    private Response serverError(Exception ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorDTO(ex.getMessage()))
                .build();
    }
}
