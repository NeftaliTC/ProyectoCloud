package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.reporte.DTO.ReporteInventarioDTO;
import pe.edu.pucp.killaBeauty.reporte.DTO.ReporteVentasDTO;

public interface ReporteBL {
    ReporteVentasDTO obtenerReporteVentas(String desde, String hasta, String categoria)
            throws BusinessLogicException;
    ReporteInventarioDTO obtenerReporteInventario(String estado, String categoria, String subcategoria, String orden)
            throws BusinessLogicException;
}
