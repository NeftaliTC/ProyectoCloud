using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.Services
{
    public class CampanaService
    {
        private readonly HttpClient http;
        private readonly JsonSerializerOptions jsonOptions = new()
        {
            PropertyNameCaseInsensitive = true,
            DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
        };

        public CampanaService(IHttpClientFactory httpClientFactory)
        {
            http = httpClientFactory.CreateClient("KillaApi");
        }

        public async Task<List<Campana>> ObtenerTodasAsync()
        {
            return await GetAsync<List<Campana>>("campanas") ?? new List<Campana>();
        }

        public async Task<List<Campana>> ObtenerActivasAsync()
        {
            var campanas = await ObtenerTodasAsync();
            return campanas.Where(c => c.Activa).ToList();
        }

        public async Task<Campana?> ObtenerPorIdAsync(int id)
        {
            return await GetAsync<Campana>($"campanas/{id}");
        }

        public async Task<Campana> GuardarAsync(Campana campana)
        {
            using var response = campana.Id == 0
                ? await http.PostAsJsonAsync("campanas", campana, jsonOptions)
                : await http.PutAsJsonAsync($"campanas/{campana.Id}", campana, jsonOptions);

            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<Campana>(jsonOptions) ?? campana;
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

    public class Campana
    {
        [JsonPropertyName("idCampana")]
        public int Id { get; set; }

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = "";

        [JsonPropertyName("descripcion")]
        public string Descripcion { get; set; } = "";

        [JsonPropertyName("activo")]
        public bool Activa { get; set; } = true;
    }
}