package pe.edu.pucp.killabeauty.killarest.services.productos;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killaBeauty.bl.CategoriaBL;
import pe.edu.pucp.killaBeauty.bl.Impl.CategoriaBLImpl;
import pe.edu.pucp.killaBeauty.bl.Impl.MarcaBLImpl;
import pe.edu.pucp.killaBeauty.bl.Impl.SubcategoriaBLImpl;
import pe.edu.pucp.killaBeauty.bl.MarcaBL;
import pe.edu.pucp.killaBeauty.bl.ProductoBL;
import pe.edu.pucp.killaBeauty.bl.SubcategoriaBL;
import pe.edu.pucp.killaBeauty.killaModelo.Categoria;
import pe.edu.pucp.killaBeauty.killaModelo.Subcategoria;

import java.util.List;

@Path("/categorias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoriaRS {
    private final CategoriaBL categoriaBL = new CategoriaBLImpl();
    private final SubcategoriaBL subcategoriaBL = new SubcategoriaBLImpl();
//
//    @GET
//    public Response listarCategorias() {
//        return Response.ok().build();
//    }
    // CATEGORÍAS PRINCIPALES


    @GET
    public Response listarCategorias() {
        try {
            List<Categoria> categorias = categoriaBL.listAll();
            return Response.ok(categorias).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    public Response obtenerCategoriaPorId(@PathParam("id") int id) {
        try {
            Categoria categoria = categoriaBL.load(id);
            if (categoria != null) {
                return Response.ok(categoria).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    public Response insertarCategoria(Categoria categoria) {
        try {
            Categoria categoriaCreada = categoriaBL.create(categoria);
            return Response.status(Response.Status.CREATED).entity(categoriaCreada).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response actualizarCategoria(@PathParam("id") int id, Categoria categoria) {
        try {
            categoria.setId(id);
            Categoria categoriaActualizada = categoriaBL.update(categoria);
            return Response.ok(categoriaActualizada).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminarCategoria(@PathParam("id") int id) {
        try {
            Categoria categoriaAEliminar = new Categoria();
            categoriaAEliminar.setId(id);
            categoriaBL.remove(categoriaAEliminar);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

/// /

    // Obtener subcategorías de una categoría específica

    // GET /services/categorias/{id}/subcategorias
    @GET
    @Path("{id}/subcategorias")
    public Response listarSubcategoriasPorCategoria(@PathParam("id") int idCategoria) {
        try {
            List<Subcategoria> subcategorias = subcategoriaBL.listByCategoriaId(idCategoria);
            return Response.ok(subcategorias).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    //  /services/categorias/subcategorias
    @GET
    @Path("subcategorias")
    public Response listarTodasSubcategorias() {
        try {
            List<Subcategoria> subcategorias = subcategoriaBL.listAll();
            return Response.ok(subcategorias).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    //  /services/categorias/subcategorias/{id}
    @GET
    @Path("subcategorias/{id}")
    public Response obtenerSubcategoriaPorId(@PathParam("id") int id) {
        try {
            Subcategoria subcategoria = subcategoriaBL.load(id);
            if (subcategoria != null) {
                return Response.ok(subcategoria).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    //  /services/categorias/subcategorias
    @POST
    @Path("subcategorias")
    public Response insertarSubcategoria(Subcategoria subcategoria) {
        try {
            Subcategoria subcategoriaCreada = subcategoriaBL.create(subcategoria);
            return Response.status(Response.Status.CREATED).entity(subcategoriaCreada).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    //  /services/categorias/subcategorias/{id}
    @PUT
    @Path("subcategorias/{id}")
    public Response actualizarSubcategoria(@PathParam("id") int id, Subcategoria subcategoria) {
        try {
            subcategoria.setId(id);
            Subcategoria subActualizada = subcategoriaBL.update(subcategoria);
            return Response.ok(subActualizada).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // /services/categorias/subcategorias/{id}
    @DELETE
    @Path("subcategorias/{id}")
    public Response eliminarSubcategoria(@PathParam("id") int id) {
        try {
            Subcategoria subAEliminar = new Subcategoria();
            subAEliminar.setId(id);
            subcategoriaBL.remove(subAEliminar);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
