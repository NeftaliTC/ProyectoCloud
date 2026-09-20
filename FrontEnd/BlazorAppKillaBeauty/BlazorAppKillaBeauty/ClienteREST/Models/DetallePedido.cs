using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class DetallePedido
    {
        [JsonPropertyName("cantidad")]
        public int Cantidad { get; set; }

        [JsonPropertyName("precioAplicado")]
        public double PrecioAplicado { get; set; }

        [JsonPropertyName("producto")]
        public ProductoRef? Producto { get; set; }
    }
}
