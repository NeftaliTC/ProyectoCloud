using BlazorAppKillaBeauty.ClienteREST.Models;
using BlazorAppKillaBeauty.ClienteREST.Utils.BlazorAppKillaBeauty.Utils;

namespace BlazorAppKillaBeauty.ClienteREST.ServicesAct
{
    public class EscalaPrecioService
    {
        private readonly HttpClientUtils _httpClientUtils;

        public EscalaPrecioService(HttpClientUtils httpClientUtils)
        {
            _httpClientUtils = httpClientUtils;
        }

        public async Task<List<EscalaPrecio>> ListarPorProductoAsync(int idProducto)
        {
            return await _httpClientUtils
                .GetAsync<List<EscalaPrecio>>($"escala-precio/producto/{idProducto}")
                ?? new List<EscalaPrecio>();
        }

        public async Task<EscalaPrecio?> CrearAsync(EscalaPrecio escala)
        {
            return await _httpClientUtils.PostAsync<EscalaPrecio>("escala-precio", escala);
        }

        public async Task<EscalaPrecio?> ActualizarAsync(int id, EscalaPrecio escala)
        {
            return await _httpClientUtils.PutAsync<EscalaPrecio>($"escala-precio/{id}", escala);
        }

        public async Task<bool> EliminarAsync(int id)
        {
            return await _httpClientUtils.DeleteAsync($"escala-precio/{id}");
        }
    }
}
