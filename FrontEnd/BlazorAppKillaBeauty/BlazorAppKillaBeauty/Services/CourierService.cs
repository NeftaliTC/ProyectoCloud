using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.Services
{
    public class CourierService
    {
        private readonly HttpClient http;
        private readonly JsonSerializerOptions jsonOptions = new()
        {
            PropertyNameCaseInsensitive = true,
            DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
        };

        public CourierService(IHttpClientFactory httpClientFactory)
        {
            http = httpClientFactory.CreateClient("KillaApi");
        }

        public async Task<List<Courier>> ObtenerTodosAsync()
        {
            return await GetAsync<List<Courier>>("courier") ?? new List<Courier>();
        }

        public async Task<Courier?> ObtenerPorIdAsync(int id)
        {
            return await GetAsync<Courier>($"courier/{id}");
        }

        public async Task<Courier> CrearAsync(Courier courier)
        {
            using var response = await http.PostAsJsonAsync("courier", courier, jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<Courier>(jsonOptions) ?? courier;
        }

        public async Task<Courier> ActualizarAsync(Courier courier)
        {
            using var response = await http.PutAsJsonAsync($"courier/{courier.Id}", courier, jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<Courier>(jsonOptions) ?? courier;
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
            string mensajeLimpio = body;
            if (body.Contains("\"message\":\""))
            {
                mensajeLimpio = body.Split("\"message\":\"")[1].Replace("\"}", "");
            }

            throw new Exception(mensajeLimpio);
        }

        public async Task EliminarAsync(int id)
        {
            var response = await http.DeleteAsync($"courier/{id}");

            await EnsureSuccessAsync(response);
        }
    }

    public class Courier
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = "";

        [JsonPropertyName("ruc")]
        public string Ruc { get; set; } = "";

        [JsonPropertyName("telefono")]
        public string Telefono { get; set; } = "";

        [JsonPropertyName("activo")]
        public bool Activo { get; set; }

        [JsonPropertyName("correo")]
        public string Correo { get; set; } = "";

        [JsonPropertyName("esAsignado")]
        public bool EsAsignado { get; set; }
    }
}
