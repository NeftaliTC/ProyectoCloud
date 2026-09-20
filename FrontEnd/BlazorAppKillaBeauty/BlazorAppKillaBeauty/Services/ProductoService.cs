using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using BlazorAppKillaBeauty.ClienteREST.Models;

using System.Text;
using Microsoft.AspNetCore.Components.Forms;
using System.Net.Http.Headers;

namespace BlazorAppKillaBeauty.Services
{
    public class ProductoService
    {
        private readonly HttpClient http;
        private readonly JsonSerializerOptions jsonOptions = new()
        {
            PropertyNameCaseInsensitive = true,
            DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
        };

        public ProductoService(IHttpClientFactory httpClientFactory)
        {
            http = httpClientFactory.CreateClient("KillaApi");
        }

        private List<Producto>? catalogoCache;

        public async Task<List<Producto>> ObtenerCatalogoAsync()
        {
            if (catalogoCache != null)
                return catalogoCache;

            catalogoCache = await http.GetFromJsonAsync<List<Producto>>("productos/catalogo")
                           ?? new List<Producto>();

            return catalogoCache;
        }

        public async Task<List<Producto>> ObtenerPopularesAsync()
        {
            var productos = await ObtenerCatalogoAsync();
            return productos.Where(p => p.EsPopular).ToList();
        }

        public async Task<List<ProductoApi>> ObtenerTodosApiAsync()
        {
            return await GetAsync<List<ProductoApi>>("productos") ?? new List<ProductoApi>();
        }

        public async Task<ProductoApi?> ObtenerPorIdAsync(int id)
        {
            return await GetAsync<ProductoApi>($"productos/{id}");
        }

        public async Task<Producto?> ObtenerCatalogoPorIdAsync(int id)
        {
            var productos = await ObtenerCatalogoAsync();
            return productos.FirstOrDefault(p => p.Id == id);
        }

        public async Task<List<ResenaProducto>> ObtenerResenasAsync(int productoId)
        {
            return await GetAsync<List<ResenaProducto>>($"productos/{productoId}/resenas") ?? new List<ResenaProducto>();
        }

        public async Task<List<Categoria>> ObtenerCategoriasAsync()
        {
            return await GetAsync<List<Categoria>>("categorias") ?? new List<Categoria>();
        }

        public async Task<List<Subcategoria>> ObtenerSubcategoriasAsync()
        {
            return await GetAsync<List<Subcategoria>>("categorias/subcategorias") ?? new List<Subcategoria>();
        }

        public async Task<List<Marca>> ObtenerMarcasAsync()
        {
            return await GetAsync<List<Marca>>("marcas") ?? new List<Marca>();
        }

        public async Task<ProductoApi> CrearAsync(ProductoApi producto)
        {
            using var response = await http.PostAsJsonAsync("productos", ProductoRequest.From(producto), jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<ProductoApi>(jsonOptions) ?? producto;
        }

        public async Task<ProductoApi> ActualizarAsync(ProductoApi producto)
        {
            using var response = await http.PutAsJsonAsync($"productos/{producto.Id}", ProductoRequest.From(producto), jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<ProductoApi>(jsonOptions) ?? producto;
        }

        public async Task<ProductoApi> ActualizarConImagenesAsync(
    ProductoApi producto,
    List<string> urls)
        {
            var dto = new ProductoConImagenesRequest
            {
                Producto = ProductoRequest.From(producto),
                Imagenes = urls.Select((url, index) => new ImagenProductoRequest
                {
                    Url = url,
                    Titulo = $"Imagen producto {index + 1}",
                    Orden = index + 1,
                    Principal = false,
                    Activo = true
                }).ToList()
            };

            using var response = await http.PutAsJsonAsync(
                $"productos/{producto.Id}/con-imagenes", dto, jsonOptions);
            await EnsureSuccessAsync(response);
            catalogoCache = null;
            return await response.Content.ReadFromJsonAsync<ProductoApi>(jsonOptions) ?? producto;
        }

        private async Task<string> SubirBytesACloudinaryAsync(
            string nombre, string contentType, byte[] bytes)
        {
            Console.WriteLine($"=== SUBIENDO: {nombre} ({bytes.Length} bytes) ===");

            using var client = new HttpClient();
            using var content = new MultipartFormDataContent();

            content.Add(new StringContent("killa_unsigned"), "upload_preset");

            var fileContent = new ByteArrayContent(bytes);
            fileContent.Headers.ContentType = MediaTypeHeaderValue.Parse(contentType);
            content.Add(fileContent, "file", nombre);

            var response = await client.PostAsync(
                "https://api.cloudinary.com/v1_1/dlkbckbdm/image/upload",
                content
            );

            var body = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"Cloudinary status: {response.StatusCode}");
            Console.WriteLine($"Cloudinary body: {body}");

            if (!response.IsSuccessStatusCode)
                throw new InvalidOperationException(body);

            var data = JsonSerializer.Deserialize<CloudinaryResponse>(body, new JsonSerializerOptions
            {
                PropertyNameCaseInsensitive = true
            });
            return data?.SecureUrl ?? "";
        }

        public async Task DarDeBajaAsync(ProductoApi producto)
        {
            producto.Activo = false;
            producto.Disponible = false;
            await ActualizarAsync(producto);
        }

        private async Task<List<string>> SubirImagenesACloudinaryAsync(IReadOnlyList<IBrowserFile> imagenes)
        {
            var urls = new List<string>();

            foreach (var imagen in imagenes)
            {
                var url = await SubirImagenACloudinaryAsync(imagen);

                if (!string.IsNullOrWhiteSpace(url))
                {
                    urls.Add(url);
                }
            }

            return urls;
        }

        private async Task<string> SubirImagenACloudinaryAsync(IBrowserFile imagen)
        {

            try
            {
                using var ms = new MemoryStream();
                await imagen.OpenReadStream(maxAllowedSize: 5 * 1024 * 1024).CopyToAsync(ms);

                using var client = new HttpClient();
                using var content = new MultipartFormDataContent();

                var fileBytes = new ByteArrayContent(ms.ToArray());
                fileBytes.Headers.ContentType = MediaTypeHeaderValue.Parse(imagen.ContentType);
                content.Add(fileBytes, "file", imagen.Name);

                content.Add(new StringContent("killa_unsigned"), "upload_preset");

                Console.WriteLine("Enviando request a Cloudinary...");
                var response = await client.PostAsync(
                    "https://api.cloudinary.com/v1_1/dlkbckbdm/image/upload",
                    content
                );

                var body = await response.Content.ReadAsStringAsync();

                if (!response.IsSuccessStatusCode)
                    throw new InvalidOperationException(body);

                var data = JsonSerializer.Deserialize<CloudinaryResponse>(body, jsonOptions);
                return data?.SecureUrl ?? "";
            }
            catch (Exception ex)
            {
                Console.WriteLine($"=== EXCEPCION: {ex.GetType().Name} ===");
                Console.WriteLine($"Mensaje: {ex.Message}");
                Console.WriteLine($"StackTrace: {ex.StackTrace}");
                throw;
            }
        }

        private async Task<T?> GetAsync<T>(string url)
        {
            using var response = await http.GetAsync(url);

            if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
            {
                return default;
            }

            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<T>(jsonOptions);
        }

        private static async Task EnsureSuccessAsync(HttpResponseMessage response)
        {
            if (response.IsSuccessStatusCode)
            {
                return;
            }

            var body = await response.Content.ReadAsStringAsync();

            throw new InvalidOperationException(
                string.IsNullOrWhiteSpace(body)
                    ? $"Error REST {(int)response.StatusCode} {response.ReasonPhrase}"
                    : body);
        }

        public async Task<bool> RegistrarResenaAsync(int productoId, ResenaProducto resena)
        {
            using var response = await http.PostAsJsonAsync($"productos/{productoId}/resenas", resena, jsonOptions);
            return response.IsSuccessStatusCode;
        }
        private class ProductoRequest
        {
            [JsonPropertyName("id")]
            public int Id { get; set; }

            [JsonPropertyName("nombre")]
            public string Nombre { get; set; } = "";

            [JsonPropertyName("precioBase")]
            public decimal PrecioBase { get; set; }

            [JsonPropertyName("stock")]
            public int Stock { get; set; }

            [JsonPropertyName("disponible")]
            public bool Disponible { get; set; }

            [JsonPropertyName("promocion")]
            public bool Promocion { get; set; }

            [JsonPropertyName("activo")]
            public bool Activo { get; set; }

            [JsonPropertyName("marca")]
            public IdRef Marca { get; set; } = new();

            [JsonPropertyName("subcategoria")]
            public IdRef Subcategoria { get; set; } = new();

            public static ProductoRequest From(ProductoApi producto)
            {
                return new ProductoRequest
                {
                    Id = producto.Id,
                    Nombre = producto.Nombre?.Trim() ?? "",
                    PrecioBase = producto.PrecioBase,
                    Stock = producto.Stock,
                    Disponible = producto.Disponible,
                    Promocion = producto.Promocion,
                    Activo = producto.Activo,
                    Marca = new IdRef { Id = producto.Marca?.Id ?? 0 },
                    Subcategoria = new IdRef { Id = producto.Subcategoria?.Id ?? 0 }
                };
            }
        }

        public async Task<ProductoApi> CrearConImagenesAsync(
    ProductoApi producto,
    List<string> urls)
        {
            var dto = new ProductoConImagenesRequest
            {
                Producto = ProductoRequest.From(producto),
                Imagenes = urls.Select((url, index) => new ImagenProductoRequest
                {
                    Url = url,
                    Titulo = $"Imagen producto {index + 1}",
                    Orden = index + 1,
                    Principal = index == 0,
                    Activo = true
                }).ToList()
            };

            using var response = await http.PostAsJsonAsync("productos/con-imagenes", dto, jsonOptions);
            await EnsureSuccessAsync(response);
            catalogoCache = null;
            return await response.Content.ReadFromJsonAsync<ProductoApi>(jsonOptions) ?? producto;
        }

        private class IdRef
        {
            [JsonPropertyName("id")]
            public int Id { get; set; }
        }

        private class ProductoConImagenesRequest
        {
            [JsonPropertyName("producto")]
            public ProductoRequest Producto { get; set; } = new();

            [JsonPropertyName("imagenes")]
            public List<ImagenProductoRequest> Imagenes { get; set; } = new();
        }

        private class ImagenProductoRequest
        {
            [JsonPropertyName("url")]
            public string Url { get; set; } = "";

            [JsonPropertyName("titulo")]
            public string Titulo { get; set; } = "";

            [JsonPropertyName("orden")]
            public int Orden { get; set; }

            [JsonPropertyName("principal")]
            public bool Principal { get; set; }

            [JsonPropertyName("activo")]
            public bool Activo { get; set; }
        }

        private class CloudinaryResponse
        {
            [JsonPropertyName("secure_url")]
            public string SecureUrl { get; set; } = "";
        }
    }
}
