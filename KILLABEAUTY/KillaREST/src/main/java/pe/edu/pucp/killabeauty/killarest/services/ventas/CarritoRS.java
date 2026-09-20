package pe.edu.pucp.killabeauty.killarest.services.ventas;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killaBeauty.bl.CarritoDeComprasBL;
import pe.edu.pucp.killaBeauty.bl.Impl.CarritoDeComprasBLImpl;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.CarritoDeCompras;
import pe.edu.pucp.killabeauty.killarest.dto.ErrorDTO;

import java.util.List;

@Path("/carrito")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarritoRS {
    private final CarritoDeComprasBL carritoBL = new CarritoDeComprasBLImpl();

    @POST
    public Response registrar(CarritoDeCompras carrito) {
        try {
            if (carrito == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("Datos del carrito incompletos.")).build();
            }
            return Response.status(Response.Status.CREATED)
                    .entity(carritoBL.create(carrito))
                    .build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            CarritoDeCompras carrito = carritoBL.load(id);
            return (carrito != null) ? Response.ok(carrito).build()
                    : Response.status(Response.Status.NOT_FOUND).build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(ex.getMessage())).build();
        }catch (Exception ex) {
            return Response.serverError().entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @GET
    @Path("/usuario/{idUsuario}")
    public Response listarPorUsuario(@PathParam("idUsuario") int idUsuario) {
        try {
            List<CarritoDeCompras> carritos = carritoBL.listByUsuarioId(idUsuario);
            return Response.ok(carritos).build();

        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity(new ErrorDTO(ex.getMessage())).build();
        }catch (Exception ex) {
            return Response.serverError().entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, CarritoDeCompras carrito) {
        try {
            if (carrito == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("Datos incompletos para actualizar.")).build();
            }
            carrito.setId(id);
            CarritoDeCompras actualizado = carritoBL.update(carrito);
            return Response.ok(actualizado).build();

        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO(ex.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            CarritoDeCompras carrito = new CarritoDeCompras();
            carrito.setId(id);
            carritoBL.remove(carrito);
            return Response.noContent().build();
        } catch (BusinessLogicException ex) {
            if (ex.getMessage().toLowerCase().contains("no existe") || ex.getMessage().toLowerCase().contains("no encontrado")) {
                return Response.status(Response.Status.NOT_FOUND).entity(new ErrorDTO(ex.getMessage())).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(ex.getMessage())).build();
        } catch (Exception ex) {
            return Response.serverError().entity(new ErrorDTO(ex.getMessage())).build();
        }
    }
}
