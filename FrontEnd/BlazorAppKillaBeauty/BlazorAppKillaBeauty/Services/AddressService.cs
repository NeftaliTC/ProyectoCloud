using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.Services
{
    public class AddressService
    {
        private readonly HttpClient http;
        private readonly JsonSerializerOptions jsonOptions = new()
        {
            PropertyNameCaseInsensitive = true,
            PropertyNamingPolicy = JsonNamingPolicy.CamelCase, // Aquí está la magia
            DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
        };

        public AddressService(IHttpClientFactory httpClientFactory)
        {
            http = httpClientFactory.CreateClient("KillaApi");
        }

        public List<Address> UserAddresses { get; private set; } = new();
        public Address? DireccionSeleccionada { get; set; }

        public async Task<List<Address>> CargarPorUsuarioAsync(int usuarioId)
        {
            UserAddresses = await GetAsync<List<Address>>($"direcciones?usuarioId={usuarioId}") ?? new List<Address>();
            DireccionSeleccionada = ObtenerPredeterminada();
            return UserAddresses;
        }

        public Address? ObtenerPredeterminada()
        {
            return UserAddresses.FirstOrDefault(a => a.IsDefault);
        }

        public async Task<Address> CrearAsync(Address direccion, int usuarioId)
        {
            direccion.Usuario = new UsuarioDireccion { Id = usuarioId };
            direccion.Activo = true;

            using var response = await http.PostAsJsonAsync("direcciones", direccion, jsonOptions);
            await EnsureSuccessAsync(response);

            var creada = await response.Content.ReadFromJsonAsync<Address>(jsonOptions) ?? direccion;
            await CargarPorUsuarioAsync(usuarioId);
            return creada;
        }

        public async Task<Address> ActualizarAsync(Address direccion, int usuarioId)
        {
            direccion.Usuario = new UsuarioDireccion { Id = usuarioId };
            direccion.Activo = true;

            using var response = await http.PutAsJsonAsync($"direcciones/{direccion.Id}", direccion, jsonOptions);
            await EnsureSuccessAsync(response);

            var actualizada = await response.Content.ReadFromJsonAsync<Address>(jsonOptions) ?? direccion;
            await CargarPorUsuarioAsync(usuarioId);
            return actualizada;
        }

        public async Task EliminarAsync(int id, int usuarioId)
        {
            using var response = await http.DeleteAsync($"direcciones/{id}");
            await EnsureSuccessAsync(response);
            await CargarPorUsuarioAsync(usuarioId);
        }

        public async Task MarcarPredeterminadaAsync(Address direccion, int usuarioId)
        {
            direccion.IsDefault = true;
            await ActualizarAsync(direccion, usuarioId);
        }

        public decimal CalcularEnvio(Address direccion)
        {
            return direccion.Distrito switch
            {
                "Miraflores" => 10,
                "San Isidro" => 12,
                "Santiago de Surco" => 15,
                "San Borja" => 12,
                "La Molina" => 15,
                "Ate" => 18,
                "Comas" => 18,
                "Los Olivos" => 16,
                "San Juan de Lurigancho" => 20,
                "Villa El Salvador" => 20,
                _ => 15
            };
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

            if (!string.IsNullOrWhiteSpace(body))
            {
                try
                {
                    // Intentamos deserializar el JSON para sacar solo el texto de "message"
                    using var doc = JsonDocument.Parse(body);
                    if (doc.RootElement.TryGetProperty("message", out var messageProp))
                    {
                        throw new InvalidOperationException(messageProp.GetString());
                    }
                }
                catch (JsonException)
                {
                    // Si por alguna razón no es un JSON válido, dejamos el comportamiento por defecto
                }
            }

            throw new InvalidOperationException(
                string.IsNullOrWhiteSpace(body)
                    ? $"Error REST {(int)response.StatusCode} {response.ReasonPhrase}"
                    : body);
        }

    }

    public class Address
    {
        public int Id { get; set; }
        public string Alias { get; set; } = "";
        public string DireccionDetalle { get; set; } = "";
        public string Distrito { get; set; } = "";
        public string Provincia { get; set; } = "";
        public string Departamento { get; set; } = "";
        public string Telefono { get; set; } = "";
        public string Referencia { get; set; } = "";
        public string CodigoPostal { get; set; } = "";
        public bool Activo { get; set; } = true;

        [JsonPropertyName("esPredeterminada")]
        public bool IsDefault { get; set; }

        [JsonPropertyName("usuario")]
        public UsuarioDireccion? Usuario { get; set; }
    }

    public class UsuarioDireccion
    {
        public int Id { get; set; }
    }
}
