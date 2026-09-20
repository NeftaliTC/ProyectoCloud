package pe.edu.pucp.killabeauty.killarest.services.productos;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killaBeauty.bl.Impl.MarcaBLImpl;
import pe.edu.pucp.killaBeauty.bl.Impl.ProductoBLImpl;
import pe.edu.pucp.killaBeauty.bl.MarcaBL;
import pe.edu.pucp.killaBeauty.bl.ProductoBL;
import pe.edu.pucp.killaBeauty.killaModelo.Marca;

import java.util.List;

@Path("/marcas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MarcaRS {
    private final MarcaBL marcaBL = new MarcaBLImpl();
//    @GET
//    public Response listarMarcas() {
//        return Response.ok().build();
//    }


    //  GET /services/marcas
    @GET
    public Response listarMarcas() {
        try {
            List<Marca> marcas = marcaBL.listAll();
            return Response.ok(marcas).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    // Ruta: GET /services/marcas/{id}
    @GET
    @Path("{id}")
    public Response obtenerMarcaPorId(@PathParam("id") int id) {
        try {
            Marca marca = marcaBL.load(id);
            if (marca != null) {
                return Response.ok(marca).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // POST /services/marcas
    @POST
    public Response insertarMarca(Marca marca) {
        try {
            Marca marcaCreada = marcaBL.create(marca);
            return Response.status(Response.Status.CREATED).entity(marcaCreada).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // PUT /services/marcas/{id}
    @PUT
    @Path("{id}")
    public Response actualizarMarca(@PathParam("id") int id, Marca marca) {
        try {
            marca.setId(id); // Aseguramos que el ID de la URL se inyecte en el objeto a actualizar
            Marca marcaActualizada = marcaBL.update(marca);
            return Response.ok(marcaActualizada).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    //  DELETE /services/marcas/{id}

    @DELETE
    @Path("{id}")
    public Response eliminarMarca(@PathParam("id") int id) {
        try {
            // Creamos un objeto Marca vacío solo con el ID necesario para el método remove
            Marca marcaAEliminar = new Marca();
            marcaAEliminar.setId(id);

            marcaBL.remove(marcaAEliminar);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
