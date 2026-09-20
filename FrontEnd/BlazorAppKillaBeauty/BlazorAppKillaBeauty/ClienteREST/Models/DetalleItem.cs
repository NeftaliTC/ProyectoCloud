using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class DetalleItem
    {
        [JsonPropertyName("nombreProducto")]
        public string NombreProducto { get; set; } = "";

        [JsonPropertyName("cantidad")]
        public int Cantidad { get; set; }

        [JsonPropertyName("precioUnitario")]
        public double PrecioUnitario { get; set; }

        [JsonPropertyName("subtotal")]
        public double Subtotal { get; set; }
    }
}