using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using BlazorAppKillaBeauty.ClienteREST.Models;

namespace BlazorAppKillaBeauty.Services
{
    public class PedidoService
    {
        private readonly HttpClient http;

        private readonly JsonSerializerOptions jsonOptions = new()
        {
            PropertyNameCaseInsensitive = true,
            DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
        };

        public PedidoService(IHttpClientFactory httpClientFactory)
        {
            http = httpClientFactory.CreateClient("KillaApi");
        }

        public async Task<List<Pedido>> ListarTodosAsync()
        {
            return await GetAsync<List<Pedido>>("pedido")
                   ?? new List<Pedido>();
        }

        public async Task<Pedido?> ObtenerPorIdAsync(int id)
        {
            return await GetAsync<Pedido>($"pedido/{id}");
        }

        public async Task<PedidoAdminDto> CancelarAsync(int id)
        {
            using var response = await http.PutAsync($"pedido/{id}/cancelar", null);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<PedidoAdminDto>(jsonOptions)
                   ?? throw new InvalidOperationException("No se recibio el pedido cancelado.");
        }

        public async Task<Pedido> CrearAsync(Pedido pedido)
        {
            using var response = await http.PostAsJsonAsync("pedido", pedido, jsonOptions);
            await EnsureSuccessAsync(response);

            return await response.Content.ReadFromJsonAsync<Pedido>(jsonOptions)
                   ?? pedido;
        }

        public async Task<Pedido> ActualizarAsync(Pedido pedido)
        {
            using var response = await http.PutAsJsonAsync($"pedido/{pedido.Id}", pedido, jsonOptions);
            await EnsureSuccessAsync(response);

            return await response.Content.ReadFromJsonAsync<Pedido>(jsonOptions)
                   ?? pedido;
        }

        public async Task<List<PedidoAdminDto>> ObtenerTodosAsync()
        {
            return await GetAsync<List<PedidoAdminDto>>("pedido") ?? new List<PedidoAdminDto>();
        }

        public async Task<PedidoAdminDto?> ObtenerPorIdAsyncAdminDto(int id)
        {
            return await GetAsync<PedidoAdminDto>($"pedido/{id}");
        }

        public async Task<List<PedidoAdminDto>> ObtenerPorClienteAsync(int idCliente)
        {
            return await GetAsync<List<PedidoAdminDto>>($"pedido/cliente/{idCliente}")
                   ?? new List<PedidoAdminDto>();
        }

        public async Task EliminarAsync(int id)
        {
            using var response = await http.DeleteAsync($"pedido/{id}");
            await EnsureSuccessAsync(response);
        }

        private async Task<T?> GetAsync<T>(string url)
        {
            using var response = await http.GetAsync(url);

            if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                return default;

            await EnsureSuccessAsync(response);

            return await response.Content.ReadFromJsonAsync<T>(jsonOptions);
        }

        private static async Task EnsureSuccessAsync(HttpResponseMessage response)
        {
            if (response.IsSuccessStatusCode)
                return;

            var body = await response.Content.ReadAsStringAsync();

            throw new InvalidOperationException(
                string.IsNullOrWhiteSpace(body)
                    ? $"Error REST {(int)response.StatusCode} {response.ReasonPhrase}"
                    : body);
        }

        public async Task<PedidoAdminDto> CheckoutAsync(PedidoCheckoutDto request)
        {
            using var response = await http.PostAsJsonAsync("pedido/checkout", request, jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<PedidoAdminDto>(jsonOptions)
                   ?? throw new InvalidOperationException("No se recibio respuesta del checkout.");
        }

        public async Task<PedidoAdminDto> ActualizarDespachoAsync(int idPedido, PedidoDespachoDto request)
        {
            using var response = await http.PutAsJsonAsync($"pedido/{idPedido}/despacho", request, jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<PedidoAdminDto>(jsonOptions)
                   ?? throw new InvalidOperationException("No se recibio respuesta del despacho.");
        }

        public class PedidoCheckoutDto
        {
            [JsonPropertyName("usuarioId")]
            public int UsuarioId { get; set; }

            [JsonPropertyName("direccionId")]
            public int DireccionId { get; set; }

            [JsonPropertyName("cuponId")]
            public int? CuponId { get; set; }

            [JsonPropertyName("costoEnvio")]
            public decimal CostoEnvio { get; set; }

            [JsonPropertyName("descuentoCupon")]
            public decimal DescuentoCupon { get; set; }

            [JsonPropertyName("metodoPago")]
            public string MetodoPago { get; set; } = "";

            [JsonPropertyName("tipoComprobante")]
            public string TipoComprobante { get; set; } = "";

            [JsonPropertyName("dni")]
            public string Dni { get; set; } = "";

            [JsonPropertyName("ruc")]
            public string Ruc { get; set; } = "";

            [JsonPropertyName("razonSocial")]
            public string RazonSocial { get; set; } = "";

            [JsonPropertyName("direccionFiscal")]
            public string DireccionFiscal { get; set; } = "";

            [JsonPropertyName("items")]
            public List<ItemCarritoDto> Items { get; set; } = new();

            public class ItemCarritoDto
            {
                [JsonPropertyName("productoId")]
                public int ProductoId { get; set; }

                [JsonPropertyName("cantidad")]
                public int Cantidad { get; set; }

                [JsonPropertyName("precioAplicado")]
                public decimal PrecioAplicado { get; set; }
            }
        }

        public class PedidoDespachoDto
        {
            [JsonPropertyName("estadoEnvio")]
            public string EstadoEnvio { get; set; } = "PENDIENTE";

            [JsonPropertyName("fechaEntrega")]
            public string FechaEntrega { get; set; } = "";

            [JsonPropertyName("descripcion")]
            public string Descripcion { get; set; } = "";
        }

        public class PedidoAdminDto
        {
            [JsonPropertyName("id")]
            public int Id { get; set; }

            [JsonPropertyName("fecha")]
            public string Fecha { get; set; } = "";

            [JsonPropertyName("estado")]
            public string Estado { get; set; } = "PENDIENTE";

            [JsonPropertyName("subtotal")]
            public decimal Subtotal { get; set; }

            [JsonPropertyName("igv")]
            public decimal Igv { get; set; }

            [JsonPropertyName("total")]
            public decimal Total { get; set; }

            [JsonPropertyName("clienteId")]
            public int ClienteId { get; set; }

            [JsonPropertyName("cliente")]
            public string Cliente { get; set; } = "";

            [JsonPropertyName("correo")]
            public string Correo { get; set; } = "";

            [JsonPropertyName("contacto")]
            public string Contacto { get; set; } = "";

            [JsonPropertyName("direccionId")]
            public int DireccionId { get; set; }

            [JsonPropertyName("direccion")]
            public string Direccion { get; set; } = "";

            [JsonPropertyName("referencia")]
            public string Referencia { get; set; } = "";

            [JsonPropertyName("distrito")]
            public string Distrito { get; set; } = "";

            [JsonPropertyName("provincia")]
            public string Provincia { get; set; } = "";

            [JsonPropertyName("departamento")]
            public string Departamento { get; set; } = "";

            [JsonPropertyName("cuponId")]
            public int? CuponId { get; set; }

            [JsonPropertyName("numeroSeguimiento")]
            public string NumeroSeguimiento { get; set; } = "";

            [JsonPropertyName("costoEnvio")]
            public decimal CostoEnvio { get; set; }

            [JsonPropertyName("courierId")]
            public int CourierId { get; set; }

            [JsonPropertyName("courier")]
            public string Courier { get; set; } = "";

            [JsonPropertyName("estadoEnvio")]
            public string EstadoEnvio { get; set; } = "";

            [JsonPropertyName("fechaEntrega")]
            public string FechaEntrega { get; set; } = "";

            [JsonPropertyName("pagoAprobado")]
            public bool PagoAprobado { get; set; }

            [JsonPropertyName("metodoPago")]
            public string MetodoPago { get; set; } = "";

            [JsonPropertyName("tipoComprobante")]
            public string TipoComprobante { get; set; } = "";

            [JsonPropertyName("serieComprobante")]
            public string SerieComprobante { get; set; } = "";

            [JsonPropertyName("numeroCorrelativo")]
            public string NumeroCorrelativo { get; set; } = "";

            [JsonPropertyName("descuentoCupon")]
            public decimal DescuentoCupon { get; set; }

            [JsonPropertyName("documentoIdentidad")]
            public string DocumentoIdentidad { get; set; } = "";

            [JsonPropertyName("razonSocial")]
            public string RazonSocial { get; set; } = "";

            [JsonPropertyName("productos")]
            public List<DetallePedidoAdminDto> Productos { get; set; } = new();

            [JsonIgnore]
            public DateTime FechaPedido =>
                DateTime.TryParse(Fecha, out var fecha) ? fecha : DateTime.MinValue;

            [JsonIgnore]
            public string Codigo => $"KIL-{Id:000000}";

            [JsonIgnore]
            public bool Cancelado => Estado.Equals("CANCELADO", StringComparison.OrdinalIgnoreCase);

            [JsonIgnore]
            public string EstadoTexto => Estado.ToUpperInvariant() switch
            {
                "PENDIENTE" => "Pendiente",
                "PAGADO" => "Pagado",
                "EN_PREPARACION" => "En preparacion",
                "ENVIADO" => "Enviado",
                "ENTREGADO" => "Entregado",
                "CANCELADO" => "Cancelado",
                _ => Estado
            };

            [JsonIgnore]
            public string ClaseEstado => Estado.ToUpperInvariant() switch
            {
                "PENDIENTE" => "warning",
                "PAGADO" => "info",
                "EN_PREPARACION" => "warning",
                "ENVIADO" => "info",
                "ENTREGADO" => "active",
                "CANCELADO" => "danger",
                _ => "info"
            };

            [JsonIgnore]
            public string Ubicacion => string.Join(", ", new[] { Distrito, Provincia, Departamento }
                .Where(valor => !string.IsNullOrWhiteSpace(valor)));
        }

        public class DetallePedidoAdminDto
        {
            [JsonPropertyName("productoId")]
            public int ProductoId { get; set; }

            [JsonPropertyName("nombreProducto")]
            public string NombreProducto { get; set; } = "";

            [JsonPropertyName("imagenes")]
            public List<string> Imagenes { get; set; } = new();

            [JsonPropertyName("marca")]
            public string Marca { get; set; } = "";

            [JsonPropertyName("cantidad")]
            public int Cantidad { get; set; }

            [JsonPropertyName("precioUnitarioOrig")]
            public decimal PrecioUnitario { get; set; }

            [JsonPropertyName("precioUnitarioDesc")]
            public decimal PrecioUnitarioDescuento { get; set; }

            [JsonPropertyName("ahorroVolumen")]
            public decimal AhorroVolumen { get; set; }

            [JsonPropertyName("subtotal")]
            public decimal Subtotal { get; set; }
        }

        public class ImagenProductoApi
        {
            [JsonPropertyName("url")]
            public string Url { get; set; } = "";

            [JsonPropertyName("principal")]
            public bool Principal { get; set; }
        }
    }
}
