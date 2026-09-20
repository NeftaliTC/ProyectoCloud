using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class CartItem
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("cantidad")]
        public int Cantidad { get; set; }

        [JsonPropertyName("producto")]
        public ProductoCarrito? Producto { get; set; }
    }
}