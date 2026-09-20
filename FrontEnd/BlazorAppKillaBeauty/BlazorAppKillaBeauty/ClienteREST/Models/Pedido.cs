using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class Pedido
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("fecha")]
        public string Fecha { get; set; } = "";

        [JsonPropertyName("estado")]
        public string Estado { get; set; } = "";

        [JsonPropertyName("subtotal")]
        public double Subtotal { get; set; }

        [JsonPropertyName("total")]
        public double Total { get; set; }

        [JsonPropertyName("cliente")]
        public Usuario? Cliente { get; set; }

        [JsonPropertyName("productos")]
        public List<DetalleItem> Productos { get; set; } = new();

        [JsonPropertyName("detalles")]
        public List<DetallePedido> Detalles { get; set; } = new();
    }
}