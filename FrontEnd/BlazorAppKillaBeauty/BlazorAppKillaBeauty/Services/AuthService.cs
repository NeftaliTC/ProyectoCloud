using System.Security.Claims;

public class AuthService
{
    public bool EstaLogueado { get; private set; }
    public int? UsuarioId { get; private set; }
    public string NombreUsuario { get; private set; } = "";
    public string CorreoElectronico { get; private set; } = "";
    public string Rol { get; private set; } = "";

    public string Dni { get; private set; } = "";

    public void Login(string nombre, int usuarioId, string rol, string correoElectronico, string dni)
    {
        EstaLogueado = true;
        UsuarioId = usuarioId;
        NombreUsuario = nombre;
        CorreoElectronico = correoElectronico;
        Dni = dni;
        Rol = rol ?? "Cliente";  // si llega null se le asigna cliente 
    }

    public void LoadFromPrincipal(ClaimsPrincipal user)
    {
        if (user.Identity?.IsAuthenticated != true)
        {
            Logout();
            return;
        }

        var idTexto = user.FindFirst("UserId")?.Value;
        int.TryParse(idTexto, out var usuarioId);

        EstaLogueado = true;
        UsuarioId = usuarioId > 0 ? usuarioId : null;
        NombreUsuario = user.FindFirst("DisplayName")?.Value
            ?? user.Identity?.Name
            ?? "";
        CorreoElectronico = user.FindFirst(ClaimTypes.Email)?.Value ?? "";
        Dni = user.FindFirst("Dni")?.Value ?? "";
        Rol = user.FindFirst(ClaimTypes.Role)?.Value ?? "";
    }

    public void Logout()
    {
        EstaLogueado = false;
        UsuarioId = null;
        NombreUsuario = "";
        CorreoElectronico = "";
        Dni = "";
        Rol = "";
    }
}
