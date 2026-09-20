package pe.edu.pucp.killabeauty.killarest.services.ventas;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killaBeauty.bl.DetalleCarritoBL;
import pe.edu.pucp.killaBeauty.bl.Impl.DetalleCarritoBLImpl;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.DetalleCarrito;
import pe.edu.pucp.killabeauty.killarest.dto.ErrorDTO;

@Path("/detalle-carrito")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DetalleCarritoRS {

    private final DetalleCarritoBL detalleBL = new DetalleCarritoBLImpl();

    @POST
    @Path("/agregar")
    public Response agregar(DetalleCarrito detalle) {
        try {
            return Response.status(Response.Status.CREATED)
                    .entity(detalleBL.create(detalle))
                    .build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO(ex.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}/cantidad/{cantidad}")
    public Response actualizarCantidad(@PathParam("id") int id,
                                       @PathParam("cantidad") int cantidad) {
        try {
            DetalleCarrito detalle = detalleBL.load(id);

            if (detalle == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorDTO("Detalle de carrito no encontrado."))
                        .build();
            }

            detalle.setCantidad(cantidad);
            return Response.ok(detalleBL.update(detalle)).build();

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
            DetalleCarrito detalle = detalleBL.load(id);

            if (detalle == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorDTO("Detalle de carrito no encontrado."))
                        .build();
            }

            detalleBL.remove(detalle);
            return Response.noContent().build();

        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO(ex.getMessage()))
                    .build();
        }
    }
}
