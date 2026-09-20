using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.Models
{
    public class Usuario
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [Required(ErrorMessage = "El nombre es obligatorio")]
        public string Nombre { get; set; }

        [Required(ErrorMessage = "El apellido paterno es obligatorio")]
        public string ApellidoPaterno { get; set; }

        public string ApellidoMaterno { get; set; }

        [Required(ErrorMessage = "El correo es obligatorio")]
        [EmailAddress(ErrorMessage = "El formato del correo no es válido")]
        public string CorreoElectronico { get; set; }

        [Required(ErrorMessage = "La contraseña es obligatoria")]
        [MinLength(6, ErrorMessage = "La contraseña debe tener al menos 6 caracteres")]
        public string Contrasena { get; set; }

        [Required(ErrorMessage = "La fecha de nacimiento es obligatoria")]
        public DateOnly? FechaNacimiento { get; set; }

        public string Genero { get; set; } = "Selecciona";

        [RegularExpression("^[0-9]+$", ErrorMessage = "El celular solo debe contener números")]
        public string Telefono { get; set; }

        [Required(ErrorMessage = "El DNI es obligatorio")]
        [StringLength(8, MinimumLength = 8, ErrorMessage = "El DNI debe tener 8 dígitos")]
        [RegularExpression("^[0-9]+$", ErrorMessage = "El DNI solo debe contener números")]
        public string Dni { get; set; }

        public bool Activo { get; set; }
        public string TipoUsuario { get; set; }
    }
}