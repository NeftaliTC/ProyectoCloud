using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using BlazorAppKillaBeauty.ClienteREST.Models;

namespace BlazorAppKillaBeauty.Services
{
    public class CategoriaService
    {
        private readonly HttpClient http;
        private readonly JsonSerializerOptions jsonOptions = new()
        {
            PropertyNameCaseInsensitive = true,
            DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
        };

        public CategoriaService(IHttpClientFactory httpClientFactory)
        {
            http = httpClientFactory.CreateClient("KillaApi");
        }

        public async Task<List<Categoria>> ListarCategoriasAsync()
        {
            return await GetAsync<List<Categoria>>("categorias") ?? new List<Categoria>();
        }

        public async Task<List<Subcategoria>> ListarSubcategoriasAsync()
        {
            return await GetAsync<List<Subcategoria>>("categorias/subcategorias") ?? new List<Subcategoria>();
        }

        public async Task<List<Subcategoria>> ListarSubcategoriasPorCategoriaAsync(int categoriaId)
        {
            return await GetAsync<List<Subcategoria>>($"categorias/{categoriaId}/subcategorias") ?? new List<Subcategoria>();
        }

        public async Task<Categoria> CrearCategoriaAsync(Categoria categoria)
        {
            using var response = await http.PostAsJsonAsync("categorias", categoria, jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<Categoria>(jsonOptions) ?? categoria;
        }

        public async Task<Categoria> ActualizarCategoriaAsync(Categoria categoria)
        {
            using var response = await http.PutAsJsonAsync($"categorias/{categoria.Id}", categoria, jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<Categoria>(jsonOptions) ?? categoria;
        }

        public async Task EliminarCategoriaAsync(int id)
        {
            using var response = await http.DeleteAsync($"categorias/{id}");
            await EnsureSuccessAsync(response);
        }

        public async Task<Subcategoria> CrearSubcategoriaAsync(Subcategoria subcategoria)
        {
            using var response = await http.PostAsJsonAsync("categorias/subcategorias", subcategoria, jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<Subcategoria>(jsonOptions) ?? subcategoria;
        }

        public async Task<Subcategoria> ActualizarSubcategoriaAsync(Subcategoria subcategoria)
        {
            using var response = await http.PutAsJsonAsync($"categorias/subcategorias/{subcategoria.Id}", subcategoria, jsonOptions);
            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<Subcategoria>(jsonOptions) ?? subcategoria;
        }

        public async Task EliminarSubcategoriaAsync(int id)
        {
            using var response = await http.DeleteAsync($"categorias/subcategorias/{id}");
            await EnsureSuccessAsync(response);
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
    }
}
