using System.ComponentModel.DataAnnotations;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class UsuarioPerfil
    {
        public int Id { get; set; }

        [Required(ErrorMessage = "El nombre es obligatorio")]
        public string Nombre { get; set; } = "";

        [Required(ErrorMessage = "El apellido paterno es obligatorio")]
        public string ApellidoPaterno { get; set; } = "";

        public string ApellidoMaterno { get; set; } = "";

        public string CorreoElectronico { get; set; } = "";

        public DateOnly? FechaNacimiento { get; set; }

        public string Genero { get; set; } = "Selecciona";

        public string Telefono { get; set; } = "";

        public string Dni { get; set; } = "";
    }
}
