namespace BlazorAppKillaBeauty.ClienteREST.Utils
{
    using System.Net.Http.Json;
    using System.Text.Json;
    using System.Text.Json.Serialization;

    namespace BlazorAppKillaBeauty.Utils
    {
        // Ya no hace falta ponerle el <T> a toda la clase, 
        // es más moderno ponérselo solo a los métodos.

        public class HttpClientUtils
            // clase plantilla generica para realizar llamados 
        {
            private readonly HttpClient _httpClient;
            private readonly JsonSerializerOptions _jsonOptions;

            // Inyectamos el HttpClient (como ya hacías en tus servicios)
            public HttpClientUtils(HttpClient httpClient)
            {
                //convierte datos c# <-> json
                _httpClient = httpClient;

                _jsonOptions = new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true, /* al leer JSON no importe si las mayúsculas coinciden*/
                    PropertyNamingPolicy = JsonNamingPolicy.CamelCase, 
                    DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull  /*Si una propiedad vale null, no la envía*/
                };
            }

            // Método GET Genérico
            public async Task<T?> GetAsync<T>(string url)
            {
                // Aquí usamos el reemplazo moderno: HttpResponseMessage
                HttpResponseMessage response = await _httpClient.GetAsync(url);

                if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                {
                    return default; // Devuelve nulo si no encuentra nada (Error 404)
                }

                response.EnsureSuccessStatusCode(); // Lanza error si el servidor falla

                // Lee y traduce mágicamente el JSON a tu objeto en una línea
                return await response.Content.ReadFromJsonAsync<T>(_jsonOptions);
            }

            // Método POST Genérico (Plantilla)
            public async Task<T?> PostAsync<T>(string url, object data)
            {
                // PostAsJsonAsync hace el empaquetado del JSON y la petición al mismo tiempo
                HttpResponseMessage response = await _httpClient.PostAsJsonAsync(url, data, _jsonOptions);

                if (!response.IsSuccessStatusCode)
                {
                    // Leemos el texto que Java nos envió de regreso (ej. "El correo ya existe")
                    string errorDeJava = await response.Content.ReadAsStringAsync();

                    // Lanzamos el error pero CON EL TEXTO REAL
                    throw new Exception(errorDeJava);
                }

                return await response.Content.ReadFromJsonAsync<T>(_jsonOptions);
            }

            public async Task<T?> PutAsync<T>(string url, object data)
            {
                // Muy parecido al POST, pero usando PutAsJsonAsync
                HttpResponseMessage response = await _httpClient.PutAsJsonAsync(url, data, _jsonOptions);

                if (!response.IsSuccessStatusCode)
                {
                    // Leemos el texto que Java nos envió de regreso (ej. "El correo ya existe")
                    string errorDeJava = await response.Content.ReadAsStringAsync();

                    // Lanzamos el error pero CON EL TEXTO REAL
                    throw new Exception(errorDeJava);
                }

                return await response.Content.ReadFromJsonAsync<T>(_jsonOptions);
            }

            // Método DELETE Genérico (Plantilla para Eliminar)
            // Ojo: Delete solo un true/false si funcionó
            public async Task<bool> DeleteAsync(string url)
            {
                HttpResponseMessage response = await _httpClient.DeleteAsync(url);

                // Devuelve true si se eliminó correctamente (Status 200 o 204)
                return response.IsSuccessStatusCode;
            }
        }
    }
}
