using BlazorAppKillaBeauty.ClienteREST.Models;
using BlazorAppKillaBeauty.ClienteREST.Utils.BlazorAppKillaBeauty.Utils;
using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.ServicesAct
{
    public class PaisService
    {
        private readonly HttpClientUtils _httpClientUtils;

        public PaisService(HttpClientUtils httpClientUtils)
        {
            _httpClientUtils = httpClientUtils;
        }

        public async Task<List<PaisOpcion>> ListarTodosAsync()
        {
            return await _httpClientUtils.GetAsync<List<PaisOpcion>>("paises") ?? new List<PaisOpcion>();
        }
    }
}
