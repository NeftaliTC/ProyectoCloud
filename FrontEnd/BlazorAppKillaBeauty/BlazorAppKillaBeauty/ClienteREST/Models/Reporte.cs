using System.Globalization;
using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class ReporteVentasDto
    {
        [JsonPropertyName("kpis")]
        public VentasKpisDto Kpis { get; set; } = new();

        [JsonPropertyName("ventasPorDia")]
        public List<VentaPorDiaDto> VentasPorDia { get; set; } = new();

        [JsonPropertyName("ventasPorCategoria")]
        public List<VentaPorCategoriaDto> VentasPorCategoria { get; set; } = new();

        [JsonPropertyName("productosMasVendidos")]
        public List<TopProductoDto> ProductosMasVendidos { get; set; } = new();

        [JsonPropertyName("clientesTop")]
        public List<TopClienteDto> ClientesTop { get; set; } = new();

        [JsonPropertyName("pedidos")]
        public List<VentaDto> Pedidos { get; set; } = new();

        [JsonPropertyName("categorias")]
        public List<string> Categorias { get; set; } = new();
    }

    public class VentasKpisDto
    {
        [JsonPropertyName("ingresos")]
        public decimal Ingresos { get; set; }

        [JsonPropertyName("pedidosTotales")]
        public int PedidosTotales { get; set; }

        [JsonPropertyName("pedidoPromedio")]
        public decimal PedidoPromedio { get; set; }

        [JsonPropertyName("unidadesVendidas")]
        public int UnidadesVendidas { get; set; }
    }

    public class VentaPorDiaDto
    {
        [JsonPropertyName("fecha")]
        public string Fecha { get; set; } = "";

        [JsonPropertyName("total")]
        public decimal Total { get; set; }
    }

    public class VentaPorCategoriaDto
    {
        [JsonPropertyName("categoria")]
        public string Categoria { get; set; } = "";

        [JsonPropertyName("total")]
        public decimal Total { get; set; }
    }

    public class TopProductoDto
    {
        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = "";

        [JsonPropertyName("marca")]
        public string Marca { get; set; } = "";

        [JsonPropertyName("categoria")]
        public string Categoria { get; set; } = "";

        [JsonPropertyName("cantidadVendida")]
        public int CantidadVendida { get; set; }

        [JsonPropertyName("precioUnitario")]
        public decimal PrecioUnitario { get; set; }

        [JsonPropertyName("imagenUrl")]
        public string ImagenUrl { get; set; } = "";
    }

    public class TopClienteDto
    {
        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = "";

        [JsonPropertyName("pedidos")]
        public int Pedidos { get; set; }

        [JsonPropertyName("unidades")]
        public int Unidades { get; set; }

        [JsonPropertyName("totalComprado")]
        public decimal TotalComprado { get; set; }
    }

    public class VentaDto
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("fecha")]
        public string Fecha { get; set; } = "";

        [JsonPropertyName("cliente")]
        public string Cliente { get; set; } = "";

        [JsonPropertyName("estado")]
        public string Estado { get; set; } = "";

        [JsonPropertyName("total")]
        public decimal Total { get; set; }

        [JsonPropertyName("productos")]
        public List<ProductoVentaDto> Productos { get; set; } = new();

        [JsonIgnore]
        public DateTime FechaPedido => DateTime.TryParse(Fecha, CultureInfo.InvariantCulture, out var fecha)
            ? fecha
            : DateTime.MinValue;
    }

    public class ProductoVentaDto
    {
        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = "";

        [JsonPropertyName("marca")]
        public string Marca { get; set; } = "";

        [JsonPropertyName("categoria")]
        public string Categoria { get; set; } = "";

        [JsonPropertyName("cantidad")]
        public int Cantidad { get; set; }

        [JsonPropertyName("precioUnitario")]
        public decimal PrecioUnitario { get; set; }

        [JsonPropertyName("imagenUrl")]
        public string ImagenUrl { get; set; } = "";
    }

    public class ReporteInventarioDto
    {
        [JsonPropertyName("kpis")]
        public InventarioKpisDto Kpis { get; set; } = new();

        [JsonPropertyName("stockPorCategoria")]
        public List<CategoriaStockDto> StockPorCategoria { get; set; } = new();

        [JsonPropertyName("resumenStock")]
        public ResumenStockDto ResumenStock { get; set; } = new();

        [JsonPropertyName("productos")]
        public List<ProductoInventarioDto> Productos { get; set; } = new();

        [JsonPropertyName("categorias")]
        public List<string> Categorias { get; set; } = new();

        [JsonPropertyName("subcategorias")]
        public List<string> Subcategorias { get; set; } = new();
    }

    public class InventarioKpisDto
    {
        [JsonPropertyName("valorTotal")]
        public decimal ValorTotal { get; set; }

        [JsonPropertyName("stockCritico")]
        public int StockCritico { get; set; }

        [JsonPropertyName("agotados")]
        public int Agotados { get; set; }

        [JsonPropertyName("unidadesTotales")]
        public int UnidadesTotales { get; set; }
    }

    public class CategoriaStockDto
    {
        [JsonPropertyName("categoria")]
        public string Categoria { get; set; } = "";

        [JsonPropertyName("unidades")]
        public int Unidades { get; set; }

        [JsonPropertyName("porcentaje")]
        public int Porcentaje { get; set; }
    }

    public class ResumenStockDto
    {
        [JsonPropertyName("agotado")]
        public int Agotado { get; set; }

        [JsonPropertyName("critico")]
        public int Critico { get; set; }

        [JsonPropertyName("saludable")]
        public int Saludable { get; set; }
    }

    public class ProductoInventarioDto
    {
        [JsonPropertyName("sku")]
        public string Sku { get; set; } = "";

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = "";

        [JsonPropertyName("marca")]
        public string Marca { get; set; } = "";

        [JsonPropertyName("categoria")]
        public string Categoria { get; set; } = "";

        [JsonPropertyName("subcategoria")]
        public string Subcategoria { get; set; } = "";

        [JsonPropertyName("stockActual")]
        public int StockActual { get; set; }

        [JsonPropertyName("precioUnitario")]
        public decimal PrecioUnitario { get; set; }

        [JsonPropertyName("valorInventario")]
        public decimal ValorInventario { get; set; }

        [JsonPropertyName("stockEstado")]
        public string StockEstado { get; set; } = "Saludable";

        [JsonPropertyName("imagenUrl")]
        public string ImagenUrl { get; set; } = "";
    }
}
