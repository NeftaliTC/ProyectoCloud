using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class Marca
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("descripcion")]
        public string Descripcion { get; set; } = "";

        [JsonPropertyName("pais")]
        public Pais Pais { get; set; }

        [JsonPropertyName("activo")]
        public bool Activo { get; set; }
    }
}
