package pe.edu.pucp.killabeauty.killarest.services.promocionales;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killaBeauty.bl.Promocionales.CuponBL;
import pe.edu.pucp.killaBeauty.bl.Impl.Promocionales.CuponBLImpl;
import pe.edu.pucp.killaBeauty.killaModelo.ClienteCupon;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;
import pe.edu.pucp.killaBeauty.bl.ClienteCuponBL;
import pe.edu.pucp.killaBeauty.bl.Impl.ClienteCuponBLImpl;
import pe.edu.pucp.killabeauty.killarest.dto.DisponibilidadCuponDTO;

import java.util.List;

@Path("/cupones")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CuponRS {
    private final CuponBL cuponBL = new CuponBLImpl();
    private final ClienteCuponBL clienteCuponBL = new ClienteCuponBLImpl();

    @GET
    public Response listarCupones() {
        try {
            List<Cupon> cupones = cuponBL.listAll();
            return Response.ok(cupones).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    public Response obtenerCuponPorId(@PathParam("id") int id) {
        try {
            Cupon cupon = cuponBL.load(id);
            if (cupon != null) return Response.ok(cupon).build();
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/codigo/{codigo}/usuario/{idUsuario}")
    public Response verificarDisponibilidad(@PathParam("codigo") String codigo,
                                            @PathParam("idUsuario") int idUsuario) {
        try {
            Cupon cupon = cuponBL.listAll().stream()
                    .filter(c -> c.getCodigo() != null && c.getCodigo().equalsIgnoreCase(codigo))
                    .findFirst()
                    .orElse(null);

            if (cupon == null || !cupon.isActivo()) {
                return Response.ok(new DisponibilidadCuponDTO(false, "Cupon no encontrado o inactivo.")).build();
            }

            for (ClienteCupon uso : clienteCuponBL.listByUsuarioId(idUsuario)) {
                if (uso.getCupon() != null
                        && uso.getCupon().getId() == cupon.getId()
                        && Boolean.TRUE.equals(uso.getUsado())) {
                    return Response.ok(new DisponibilidadCuponDTO(false, "Ya usaste este cupon anteriormente.")).build();
                }
            }

            return Response.ok(new DisponibilidadCuponDTO(true, null)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/disponibles/usuario/{idUsuario}")
    public Response listarDisponiblesParaUsuario(@PathParam("idUsuario") int idUsuario) {
        try {
            List<Cupon> todos = cuponBL.listAll();
            List<ClienteCupon> usos = clienteCuponBL.listByUsuarioId(idUsuario);

            List<Cupon> disponibles = todos.stream()
                    .filter(Cupon::isActivo)
                    .filter(c -> usos.stream().noneMatch(u ->
                            u.getCupon() != null
                                    && u.getCupon().getId() == c.getId()
                                    && Boolean.TRUE.equals(u.getUsado())))
                    .toList();

            return Response.ok(disponibles).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    public Response insertarCupon(Cupon cupon) {
        try {
            Cupon creado = cuponBL.create(cupon);
            return Response.status(Response.Status.CREATED).entity(creado).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response actualizarCupon(@PathParam("id") int id, Cupon cupon) {
        try {
            cupon.setId(id);
            Cupon actualizado = cuponBL.update(cupon);
            return Response.ok(actualizado).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminarCupon(@PathParam("id") int id) {
        try {
            Cupon cupon = new Cupon();
            cupon.setId(id);
            cuponBL.remove(cupon);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}