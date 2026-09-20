using BlazorAppKillaBeauty.ClienteREST.Models;
using BlazorAppKillaBeauty.ClienteREST.Utils.BlazorAppKillaBeauty.Utils;

namespace BlazorAppKillaBeauty.ClienteREST.ServicesAct
{
    public class UsuarioPasswordService
    {
        private readonly HttpClientUtils _httpClientUtils;

        public UsuarioPasswordService(HttpClientUtils httpClientUtils)
        {
            _httpClientUtils = httpClientUtils;
        }

        public async Task CambiarContrasenaAsync(int usuarioId, CambiarContrasenaRequest request)
        {
            await _httpClientUtils.PutAsync<object>($"usuarios/{usuarioId}/password", request);
        }
    }
}
