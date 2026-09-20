namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class ImagenProducto
    {
        public int Id { get; set; }
        public string Url { get; set; } = "";
        public string? Titulo { get; set; }
        public int Orden { get; set; }
        public bool Principal { get; set; }
        public bool Activo { get; set; }
    }
}
