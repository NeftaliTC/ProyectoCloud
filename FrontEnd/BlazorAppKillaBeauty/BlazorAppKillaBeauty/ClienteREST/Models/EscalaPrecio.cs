using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class EscalaPrecio
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("cantidadMinima")]
        public int CantidadMinima { get; set; }

        [JsonPropertyName("precioUnitario")]
        public decimal PrecioUnitario { get; set; }

        [JsonPropertyName("activo")]
        public bool Activo { get; set; } = true;

        [JsonPropertyName("producto")]
        public ProductoRef Producto { get; set; } = new();
    }
}
