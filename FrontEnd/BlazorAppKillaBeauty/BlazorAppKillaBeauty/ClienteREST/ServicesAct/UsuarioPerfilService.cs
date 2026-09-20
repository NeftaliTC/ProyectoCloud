using BlazorAppKillaBeauty.ClienteREST.Models;
using BlazorAppKillaBeauty.ClienteREST.Utils.BlazorAppKillaBeauty.Utils;

namespace BlazorAppKillaBeauty.ClienteREST.ServicesAct
{
    public class UsuarioPerfilService
    {
        private readonly HttpClientUtils _httpClientUtils;

        public UsuarioPerfilService(HttpClientUtils httpClientUtils)
        {
            _httpClientUtils = httpClientUtils;
        }

        public async Task<UsuarioPerfil?> ObtenerPorIdAsync(int id)
        {
            return await _httpClientUtils.GetAsync<UsuarioPerfil>($"usuarios/{id}");
        }

        public async Task<UsuarioPerfil?> ActualizarPerfilAsync(int id, UsuarioPerfil perfil)
        {
            return await _httpClientUtils.PutAsync<UsuarioPerfil>($"usuarios/{id}/perfil", perfil);
        }
    }
}
