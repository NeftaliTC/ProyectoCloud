package pe.edu.pucp.killaDAO;
import pe.edu.pucp.killaBeauty.reporte.DTO.InventarioReporteData;
import pe.edu.pucp.killaBeauty.reporte.DTO.VentaReporteData;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ReporteDAO {
    List<VentaReporteData> listarVentas(LocalDate desde, LocalDate hasta) throws SQLException;
    List<InventarioReporteData> listarInventario() throws SQLException;
}
