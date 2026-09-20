using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class Carrito
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("estado")]
        public string Estado { get; set; } = "";

        [JsonPropertyName("fechaDeCreacion")]
        public string FechaDeCreacion { get; set; } = "";

        [JsonPropertyName("usuario")]
        public Usuario? Usuario { get; set; }

        [JsonPropertyName("detalleCarritoList")]
        public List<CartItem> DetalleCarritoList { get; set; } = new();
    }
}
