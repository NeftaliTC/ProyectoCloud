package pe.edu.pucp.killabeauty.killarest.services.productos;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.killaBeauty.bl.EscalaPrecioBL;
import pe.edu.pucp.killaBeauty.bl.Impl.EscalaPrecioBLImpl;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.EscalaPrecio;

import java.util.List;

@Path("escala-precio")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EscalaPrecioRS {

    private final EscalaPrecioBL escalaBL = new EscalaPrecioBLImpl();

    @GET
    @Path("producto/{idProducto}")
    public List<EscalaPrecio> listarPorProducto(@PathParam("idProducto") int idProducto)
            throws BusinessLogicException {
        return escalaBL.listByProductoId(idProducto);
    }

    @POST
    public EscalaPrecio crear(EscalaPrecio escala) throws BusinessLogicException {
        return escalaBL.create(escala);
    }

    @PUT
    @Path("{id}")
    public EscalaPrecio actualizar(@PathParam("id") int id, EscalaPrecio escala)
            throws BusinessLogicException {
        escala.setId(id);
        return escalaBL.update(escala);
    }

    @DELETE
    @Path("{id}")
    public void eliminar(@PathParam("id") int id) throws BusinessLogicException {
        EscalaPrecio escala = escalaBL.load(id);
        escalaBL.remove(escala);
    }
}
