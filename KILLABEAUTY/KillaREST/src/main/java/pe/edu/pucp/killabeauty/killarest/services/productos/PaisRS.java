package pe.edu.pucp.killabeauty.killarest.services.productos;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killaBeauty.killaModelo.Pais;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("/paises")
@Produces(MediaType.APPLICATION_JSON)
public class PaisRS {

    @GET
    public Response listarPaises() {
        List<PaisDTO> paises = Arrays.stream(Pais.values())
                .map(PaisDTO::new)
                .collect(Collectors.toList());
        return Response.ok(paises).build();
    }

    @GET
    @Path("{id}")
    public Response obtenerPaisPorId(@PathParam("id") int id) {
        for (Pais pais : Pais.values()) {
            if (pais.getId() == id) {
                return Response.ok(new PaisDTO(pais)).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public static class PaisDTO {
        private int id;
        private String codigo;
        private String descripcion;

        public PaisDTO() {
        }

        public PaisDTO(Pais pais) {
            this.id = pais.getId();
            this.codigo = pais.name();
            this.descripcion = pais.getDescripcion();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
}
