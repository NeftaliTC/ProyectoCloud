package pe.edu.pucp.killabeauty.killarest.services.reportes;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killaBeauty.bl.Impl.ReporteBLImpl;
import pe.edu.pucp.killaBeauty.bl.ReporteBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.reporte.GeneradorReporteInventario;
import pe.edu.pucp.killaBeauty.reporte.GeneradorReporteVentas;
import pe.edu.pucp.killaBeauty.reporte.DTO.ReporteInventarioDTO;
import pe.edu.pucp.killaBeauty.reporte.DTO.ReporteVentasDTO;
import pe.edu.pucp.killabeauty.killarest.dto.ErrorDTO;

@Path("/reportes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReporteRS {
    private final ReporteBL reporteBL = new ReporteBLImpl();
    private final GeneradorReporteVentas generadorReporteVentas = new GeneradorReporteVentas();
    private final GeneradorReporteInventario generadorReporteInventario = new GeneradorReporteInventario();

    @GET
    @Path("/ventas")
    public Response obtenerVentas(@QueryParam("desde") String desde,
                                  @QueryParam("hasta") String hasta,
                                  @QueryParam("categoria") String categoria) {
        try {
            return Response.ok(reporteBL.obtenerReporteVentas(desde, hasta, categoria)).build();
        } catch (BusinessLogicException ex) {
            return badRequest(ex);
        } catch (Exception ex) {
            return serverError(ex);
        }
    }

    @GET
    @Path("/ventas/pdf")
    @Produces("application/pdf")
    public Response descargarVentasPdf(@QueryParam("desde") String desde,
                                       @QueryParam("hasta") String hasta,
                                       @QueryParam("categoria") String categoria) {
        try {
            ReporteVentasDTO reporte = reporteBL.obtenerReporteVentas(desde, hasta, categoria);
            byte[] pdf = generadorReporteVentas.generarReporteVentas(reporte, desde, hasta, categoria);

            return Response.ok(pdf, "application/pdf")
                    .header("Content-Disposition", "attachment; filename=\"reporte_ventas.pdf\"")
                    .build();
        } catch (BusinessLogicException ex) {
            return badRequest(ex);
        } catch (Exception ex) {
            return serverError(ex);
        }
    }

    @GET
    @Path("/inventario")
    public Response obtenerInventario(@QueryParam("estado") String estado,
                                      @QueryParam("categoria") String categoria,
                                      @QueryParam("subcategoria") String subcategoria,
                                      @QueryParam("orden") String orden) {
        try {
            return Response.ok(reporteBL.obtenerReporteInventario(estado, categoria, subcategoria, orden)).build();
        } catch (BusinessLogicException ex) {
            return badRequest(ex);
        } catch (Exception ex) {
            return serverError(ex);
        }
    }

    @GET
    @Path("/inventario/pdf")
    @Produces("application/pdf")
    public Response descargarInventarioPdf(@QueryParam("estado") String estado,
                                           @QueryParam("categoria") String categoria,
                                           @QueryParam("subcategoria") String subcategoria,
                                           @QueryParam("orden") String orden) {
        try {
            ReporteInventarioDTO reporte = reporteBL.obtenerReporteInventario(estado, categoria, subcategoria, orden);
            byte[] pdf = generadorReporteInventario.generarReporteInventario(reporte, estado, categoria, subcategoria, orden);

            return Response.ok(pdf, "application/pdf")
                    .header("Content-Disposition", "attachment; filename=\"reporte_inventario.pdf\"")
                    .build();
        } catch (BusinessLogicException ex) {
            return badRequest(ex);
        } catch (Exception ex) {
            return serverError(ex);
        }
    }

    private Response badRequest(Exception ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorDTO(ex.getMessage()))
                .build();
    }

    private Response serverError(Exception ex) {
        ex.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorDTO(ex.getMessage()))
                .build();
    }
}
