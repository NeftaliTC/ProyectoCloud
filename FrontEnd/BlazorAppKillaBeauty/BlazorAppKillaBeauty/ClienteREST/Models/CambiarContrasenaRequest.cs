using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class CambiarContrasenaRequest
    {
        [JsonPropertyName("contrasenaActual")]
        public string ContrasenaActual { get; set; } = "";

        [JsonPropertyName("nuevaContrasena")]
        public string NuevaContrasena { get; set; } = "";
    }
}
