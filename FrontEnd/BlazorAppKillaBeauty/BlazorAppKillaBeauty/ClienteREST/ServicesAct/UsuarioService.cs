using BlazorAppKillaBeauty.ClienteREST.Models;
using BlazorAppKillaBeauty.ClienteREST.Utils.BlazorAppKillaBeauty.Utils;
using BlazorAppKillaBeauty.Services;
using static System.Net.WebRequestMethods;

namespace BlazorAppKillaBeauty.ClienteREST.ServicesAct
{
    public class UsuarioService
    {
        private readonly HttpClientUtils _httpClientUtils;

        public UsuarioService(HttpClientUtils httpClientUtils)
        {
            _httpClientUtils = httpClientUtils;
        }

        public async Task<Usuario> RegistrarUsuarioAsync(Usuario usuario)
        {
            // El backend se encargará del hash de la contra, la fecha de inscripción y el tipo de usuario comprador por defecto
            return await _httpClientUtils.PostAsync<Usuario>("usuarios", usuario);
        }
        // POST: /usuarios/login
        public async Task<Usuario> LoginAsync(Usuario credenciales)
        {
            return await _httpClientUtils.PostAsync<Usuario>("usuarios/login", credenciales);
        }

        // GET: /usuarios?tipo={idTipoUsuario}
        public async Task<List<Usuario>> ListarPorTipoUsuarioAsync(int idTipoUsuario)
        {
            return await _httpClientUtils.GetAsync<List<Usuario>>($"usuarios?tipo={idTipoUsuario}") ?? new List<Usuario>();
        }

        // GET: /usuarios/email/{correo}
        public async Task<Usuario> ObtenerUsuarioPorEmailAsync(string email)
        {
            return await _httpClientUtils.GetAsync<Usuario>($"usuarios/email/{email}");
        }

        // PUT: /usuarios/{id}
        public async Task<Usuario> ActualizarUsuarioAsync(int id, Usuario usuario)
        {
            return await _httpClientUtils.PutAsync<Usuario>($"usuarios/{id}/perfil", usuario);
        }

        // DELETE: /usuarios/{id}
        public async Task<bool> EliminarUsuarioAsync(int id)
        {
            //devuelve un booleano confirmando el éxito
            return await _httpClientUtils.DeleteAsync($"usuarios/{id}");
        }   

        //// GET: /usuarios/{id}/resenas

        //public async Task<List<ResenaProducto>> BuscarResenasPorUsuarioAsync(int idUsuario)
        //{
        //    return await _httpClientUtils.GetAsync<List<ResenaProducto>>($"usuarios/{idUsuario}/resenas");
        //}
    }
}
