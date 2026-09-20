using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    [JsonConverter(typeof(JsonStringEnumConverter))]
    public enum Pais
    {
        EEUU = 1,
        COREA = 2,
        JAPON = 3,
        PERU = 4,
        CHILE = 5,
        MEXICO = 6,
        COLOMBIA = 7,
        CHINA = 8
    }

    public class PaisOpcion
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("codigo")]
        public string Codigo { get; set; } = "";

        [JsonPropertyName("descripcion")]
        public string Descripcion { get; set; } = "";

        public Pais PaisEnum => Enum.TryParse<Pais>(Codigo, out var pais) ? pais : default;
    }
}
