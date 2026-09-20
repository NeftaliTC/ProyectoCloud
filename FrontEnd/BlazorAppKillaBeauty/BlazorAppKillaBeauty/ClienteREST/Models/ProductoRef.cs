using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class ProductoRef
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }
    }
}