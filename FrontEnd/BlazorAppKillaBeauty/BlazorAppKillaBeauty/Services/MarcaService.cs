using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using BlazorAppKillaBeauty.ClienteREST.Models;

namespace BlazorAppKillaBeauty.Services
{
    public class MarcaService
    {
        private readonly HttpClient http;
        private readonly JsonSerializerOptions jsonOptions = new()
        {
            PropertyNameCaseInsensitive = true,
            DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
        };

        public MarcaService(IHttpClientFactory httpClientFactory)
        {
            http = httpClientFactory.CreateClient("KillaApi");
        }

        public async Task<List<Marca>> ListarTodasAsync()
        {
            return await GetAsync<List<Marca>>("marcas") ?? new List<Marca>();
        }

        public async Task<Marca?> ObtenerPorIdAsync(int id)
        {
            return await GetAsync<Marca>($"marcas/{id}");
        }

        public async Task<Marca> CrearAsync(Marca nuevaMarca)
        {
            using var response = await http.PostAsJsonAsync("marcas", nuevaMarca, jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<Marca>(jsonOptions) ?? nuevaMarca;
        }

        public async Task<Marca> ActualizarMarcaAsync(Marca marca)
        {
            using var response = await http.PutAsJsonAsync($"marcas/{marca.Id}", marca, jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<Marca>(jsonOptions) ?? marca;
        }

        public async Task EliminarMarcaAsync(int id)
        {
            using var response = await http.DeleteAsync($"marcas/{id}");
            await EnsureSuccessAsync(response);
        }

        private async Task<T?> GetAsync<T>(string url)
        {
            using var response = await http.GetAsync(url);
            if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
            {
                return default;
            }
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<T>(jsonOptions);
        }

        private static async Task EnsureSuccessAsync(HttpResponseMessage response)
        {
            if (response.IsSuccessStatusCode)
            {
                return;
            }

            var body = await response.Content.ReadAsStringAsync();
            throw new InvalidOperationException(
                string.IsNullOrWhiteSpace(body)
                    ? $"Error REST {(int)response.StatusCode} {response.ReasonPhrase}"
                    : body);
        }
    }
}
