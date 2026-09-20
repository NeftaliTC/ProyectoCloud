using System.Net.Http.Json;
using System.Security.Claims;
using System.Text.Json;
using BlazorAppKillaBeauty.ClienteREST.Models;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;

namespace BlazorAppKillaBeauty.Security
{
    public static class AuthEndpoints
    {
        public static IEndpointRouteBuilder MapAuthEndpoints(this IEndpointRouteBuilder endpoints)
        {
            endpoints.MapPost("/auth/login", async (HttpContext context, IHttpClientFactory httpClientFactory) =>
            {
                var form = await context.Request.ReadFormAsync();
                var correo = form["correoElectronico"].ToString();
                var contrasena = form["contrasena"].ToString();
                var returnUrl = form["returnUrl"].ToString();

                if (string.IsNullOrWhiteSpace(correo) || string.IsNullOrWhiteSpace(contrasena))
                {
                    return Results.LocalRedirect("/login?error=1");
                }

                var http = httpClientFactory.CreateClient("KillaApi");
                var response = await http.PostAsJsonAsync("usuarios/login", new
                {
                    correoElectronico = correo,
                    contrasena
                });

                if (!response.IsSuccessStatusCode)
                {
                    return Results.LocalRedirect("/login?error=1");
                }

                var jsonOptions = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
                var usuario = await response.Content.ReadFromJsonAsync<Usuario>(jsonOptions);
                if (usuario is null || usuario.Id <= 0)
                {
                    return Results.LocalRedirect("/login?error=1");
                }

                var rol = NormalizarRol(usuario.TipoUsuario);
                var nombre = $"{usuario.Nombre} {usuario.ApellidoPaterno}".Trim();

                var claims = new List<Claim>
                {
                    new(ClaimTypes.Name, usuario.CorreoElectronico ?? correo),
                    new(ClaimTypes.Email, usuario.CorreoElectronico ?? correo),
                    new(ClaimTypes.Role, rol),
                    new("DisplayName", string.IsNullOrWhiteSpace(nombre) ? correo : nombre),
                    new("UserId", usuario.Id.ToString()),
                    new("Dni", usuario.Dni ?? "")
                };

                var principal = new ClaimsPrincipal(
                    new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme));

                await context.SignInAsync(
                    CookieAuthenticationDefaults.AuthenticationScheme,
                    principal,
                    new AuthenticationProperties
                    {
                        IsPersistent = true,
                        ExpiresUtc = DateTimeOffset.UtcNow.AddHours(8)
                    });

                if (!string.IsNullOrWhiteSpace(returnUrl) && Uri.IsWellFormedUriString(returnUrl, UriKind.Relative))
                {
                    return Results.LocalRedirect(returnUrl);
                }

                return rol is "ADMINISTRADOR" or "TRABAJADOR"
                    ? Results.LocalRedirect("/admin")
                    : Results.LocalRedirect("/");
            });

            endpoints.MapGet("/auth/logout", async (HttpContext context) =>
            {
                await context.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
                return Results.LocalRedirect("/");
            });

            return endpoints;
        }

        private static string NormalizarRol(string? rol)
        {
            return (rol ?? "CLIENTE").Trim().ToUpperInvariant() switch
            {
                "ADMIN" => "ADMINISTRADOR",
                "ADMINISTRADOR" => "ADMINISTRADOR",
                "VENDEDOR" => "TRABAJADOR",
                "TRABAJADOR" => "TRABAJADOR",
                _ => "CLIENTE"
            };
        }
    }
}
