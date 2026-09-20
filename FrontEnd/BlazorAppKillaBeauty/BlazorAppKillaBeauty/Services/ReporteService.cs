using BlazorAppKillaBeauty.ClienteREST.Models;
using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.Services
{
    public class ReporteService
    {
        private readonly HttpClient http;
        private readonly JsonSerializerOptions jsonOptions = new()
        {
            PropertyNameCaseInsensitive = true,
            DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
        };

        public ReporteService(IHttpClientFactory httpClientFactory)
        {
            http = httpClientFactory.CreateClient("KillaApi");
        }

        public async Task<ReporteVentasDto> ObtenerVentasAsync(
            DateTime desde,
            DateTime hasta,
            string categoria)
        {
            var url = "reportes/ventas"
                      + $"?desde={desde:yyyy-MM-dd}"
                      + $"&hasta={hasta:yyyy-MM-dd}"
                      + $"&categoria={Uri.EscapeDataString(categoria)}";

            return await GetAsync<ReporteVentasDto>(url) ?? new ReporteVentasDto();
        }

        public string ObtenerUrlPdfVentas(DateTime desde, DateTime hasta, string categoria)
        {
            var url = "reportes/ventas/pdf"
                      + $"?desde={desde:yyyy-MM-dd}"
                      + $"&hasta={hasta:yyyy-MM-dd}"
                      + $"&categoria={Uri.EscapeDataString(categoria)}";

            return new Uri(http.BaseAddress!, url).ToString();
        }

        public async Task<ReporteInventarioDto> ObtenerInventarioAsync(
            string estado,
            string categoria,
            string subcategoria,
            string orden)
        {
            var url = "reportes/inventario"
                      + $"?estado={Uri.EscapeDataString(estado)}"
                      + $"&categoria={Uri.EscapeDataString(categoria)}"
                      + $"&subcategoria={Uri.EscapeDataString(subcategoria)}"
                      + $"&orden={Uri.EscapeDataString(orden)}";

            return await GetAsync<ReporteInventarioDto>(url) ?? new ReporteInventarioDto();
        }

        public string ObtenerUrlPdfInventario(
            string estado,
            string categoria,
            string subcategoria,
            string orden)
        {
            var url = "reportes/inventario/pdf"
                      + $"?estado={Uri.EscapeDataString(estado)}"
                      + $"&categoria={Uri.EscapeDataString(categoria)}"
                      + $"&subcategoria={Uri.EscapeDataString(subcategoria)}"
                      + $"&orden={Uri.EscapeDataString(orden)}";

            return new Uri(http.BaseAddress!, url).ToString();
        }

        private async Task<T?> GetAsync<T>(string url)
        {
            using var response = await http.GetAsync(url);
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

    }
}
