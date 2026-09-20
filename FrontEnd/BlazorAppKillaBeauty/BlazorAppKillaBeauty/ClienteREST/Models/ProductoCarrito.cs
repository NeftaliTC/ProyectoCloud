using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class ProductoCarrito
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = "";

        [JsonPropertyName("precioBase")]
        public decimal PrecioBase { get; set; }

        [JsonPropertyName("imagenes")]
        public List<ImagenProducto> Imagenes { get; set; } = new();

        [JsonIgnore]
        public string Imagen =>
            Imagenes.FirstOrDefault(i => i.Principal)?.Url
            ?? Imagenes.FirstOrDefault()?.Url
            ?? "/Images/Logo.png";

        [JsonPropertyName("stock")]
        public int Stock { get; set; }

        public Marca? Marca { get; set; }
        public Categoria? Categoria { get; set; }
        public Subcategoria? Subcategoria { get; set; }
    }
}
