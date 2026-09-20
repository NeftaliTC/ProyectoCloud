package pe.edu.pucp.killaBeauty.bl.Impl;

import org.mindrot.jbcrypt.BCrypt;
import pe.edu.pucp.killaBeauty.bl.UsuarioBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.TipoUsuario;
import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
import pe.edu.pucp.killaDAO.Impl.UsuarioDAOImpl;
import pe.edu.pucp.killaDAO.UsuarioDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class UsuarioBLImpl implements UsuarioBL {
    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{8}$");
    private static final Pattern CELULAR_PATTERN = Pattern.compile("^9\\d{8}$");
    private static final int EDAD_MINIMA = 18;
    private static final int LONGITUD_MINIMA_CONTRASENA = 6;

    @Override
    public Usuario create(Usuario usuario) throws BusinessLogicException {
        validarRegistro(usuario);
        try {
            Usuario usuarioExistente = usuarioDAO.loadByEmail(usuario.getCorreoElectronico());
            if (usuarioExistente != null) {
                throw new BusinessLogicException("El correo electronico ya se encuentra registrado.");
            }

            String passwordHasheado = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
            usuario.setContrasena(passwordHasheado);
            usuario.setActivo(true);
            usuario.setTipoUsuario(TipoUsuario.CLIENTE);
            usuario.setFechaDeInscripcion(new java.util.Date());
            return usuarioDAO.save(usuario);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error interno: " + e.getMessage());
        }
    }

    @Override
    public Usuario update(Usuario usuario) throws BusinessLogicException {
        validarPerfil(usuario);
        try {
            Usuario usuarioActual = usuarioDAO.load(usuario.getId());
            if (usuarioActual == null) {
                throw new BusinessLogicException("El usuario con ID " + usuario.getId() + " no existe en el sistema.");
            }

            usuarioActual.setNombre(usuario.getNombre());
            usuarioActual.setApellidoPaterno(usuario.getApellidoPaterno());
            usuarioActual.setApellidoMaterno(usuario.getApellidoMaterno());
            usuarioActual.setFechaNacimiento(usuario.getFechaNacimiento());
            usuarioActual.setGenero(usuario.getGenero());
            usuarioActual.setTelefono(usuario.getTelefono());


            if (usuario.getTipoUsuario() != null) {
                usuarioActual.setTipoUsuario(usuario.getTipoUsuario());
            }
            usuarioActual.setActivo(usuario.getActivo());

            usuarioDAO.update(usuarioActual);
            usuarioActual.setContrasena(null);
            return usuarioActual;
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void remove(Usuario usuario) throws BusinessLogicException {
        if (usuario == null || usuario.getId() <= 0) {
            throw new BusinessLogicException("Se requiere un usuario valido para eliminar.");
        }
        try {
            usuarioDAO.remove(usuario);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Usuario load(int id) throws BusinessLogicException {
        if (id <= 0) {
            throw new BusinessLogicException("El id del usuario debe ser valido.");
        }
        try {
            return usuarioDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Usuario loadByEmail(String email) throws BusinessLogicException {
        String correo = normalizarCorreo(email);
        try {
            return usuarioDAO.loadByEmail(correo);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Usuario> listByTipoUsuario(int idTipoUsuario) throws BusinessLogicException {
        if (idTipoUsuario <= 0) {
            throw new BusinessLogicException("El tipo de usuario debe ser valido.");
        }
        try {
            return usuarioDAO.listByTipoUsuario(idTipoUsuario);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public Usuario cambiarContrasena(int id, String contrasenaActual, String nuevaContrasena) throws BusinessLogicException {
        if (id <= 0) {
            throw new BusinessLogicException("El usuario debe ser valido.");
        }
        if (contrasenaActual == null || contrasenaActual.trim().isEmpty()) {
            throw new BusinessLogicException("Debe ingresar su contrasena actual.");
        }
        if (nuevaContrasena == null || nuevaContrasena.length() < LONGITUD_MINIMA_CONTRASENA) {
            throw new BusinessLogicException("La nueva contrasena debe tener al menos 6 caracteres.");
        }
        if (contrasenaActual.equals(nuevaContrasena)) {
            throw new BusinessLogicException("La nueva contrasena no puede ser igual a la anterior.");
        }
        try {
            Usuario usuarioActual = usuarioDAO.load(id);
            if (usuarioActual == null) {
                throw new BusinessLogicException("El usuario no existe en el sistema.");
            }

            boolean contrasenaValida = BCrypt.checkpw(contrasenaActual, usuarioActual.getContrasena());
            if (!contrasenaValida) {
                throw new BusinessLogicException("La contrasena actual ingresada es incorrecta.");
            }

            String passwordHasheado = BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt());
            usuarioActual.setContrasena(passwordHasheado);
            return usuarioDAO.update(usuarioActual);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error interno: " + e.getMessage());
        }
    }

    @Override
    public Usuario autenticar(Usuario usuario) throws BusinessLogicException {
        if (usuario == null || usuario.getCorreoElectronico() == null || usuario.getContrasena() == null) {
            throw new BusinessLogicException("El correo y la contrasena son campos obligatorios.");
        }

        String correo = normalizarCorreo(usuario.getCorreoElectronico());
        try {
            Usuario usuarioBD = usuarioDAO.loadByEmail(correo);
            if (usuarioBD == null) {
                throw new BusinessLogicException("Correo o contrasena incorrectos.");
            }
            boolean contrasenaValida = BCrypt.checkpw(usuario.getContrasena(), usuarioBD.getContrasena());
            if (!contrasenaValida) {
                throw new BusinessLogicException("Correo o contrasena incorrectos.");
            }
            usuarioBD.setUltimoAcceso(new java.util.Date());
            usuarioDAO.actualizarUltimoAcceso(usuarioBD.getId());
            usuarioBD.setContrasena(null);
            return usuarioBD;
        } catch (SQLException e) {
            throw new BusinessLogicException("Error interno en el servidor de datos al intentar procesar el ingreso: " + e.getMessage());
        }
    }

    private void validarRegistro(Usuario usuario) throws BusinessLogicException {
        validarBaseUsuario(usuario);
        if (usuario.getContrasena() == null || usuario.getContrasena().length() < LONGITUD_MINIMA_CONTRASENA) {
            throw new BusinessLogicException("La contrasena debe tener al menos 6 caracteres.");
        }
        if (usuario.getDni() == null || !DNI_PATTERN.matcher(usuario.getDni().trim()).matches()) {
            throw new BusinessLogicException("El DNI debe contener 8 digitos numericos.");
        }
        usuario.setDni(usuario.getDni().trim());
    }

    private void validarPerfil(Usuario usuario) throws BusinessLogicException {
        validarBaseUsuario(usuario);
        if (usuario.getId() <= 0) {
            throw new BusinessLogicException("Se requiere un ID valido para actualizar el usuario.");
        }
    }

    private void validarBaseUsuario(Usuario usuario) throws BusinessLogicException {
        if (usuario == null) {
            throw new BusinessLogicException("El usuario no puede ser nulo.");
        }
        if (esVacio(usuario.getNombre())) {
            throw new BusinessLogicException("El nombre es obligatorio.");
        }
        if (esVacio(usuario.getApellidoPaterno())) {
            throw new BusinessLogicException("El apellido paterno es obligatorio.");
        }
        if (esVacio(usuario.getCorreoElectronico())) {
            throw new BusinessLogicException("El correo electronico es obligatorio.");
        }

        String correo = normalizarCorreo(usuario.getCorreoElectronico());
        if (!EMAIL_PATTERN.matcher(correo).matches()) {
            throw new BusinessLogicException("El correo electronico no tiene un formato valido.");
        }
        usuario.setCorreoElectronico(correo);

        validarFechaNacimiento(usuario.getFechaNacimiento());
        usuario.setNombre(usuario.getNombre().trim());
        usuario.setApellidoPaterno(usuario.getApellidoPaterno().trim());
        usuario.setApellidoMaterno(esVacio(usuario.getApellidoMaterno()) ? null : usuario.getApellidoMaterno().trim());
        usuario.setTelefono(normalizarCelular(usuario.getTelefono()));
        usuario.setGenero(normalizarGenero(usuario.getGenero()));
    }

    private void validarFechaNacimiento(LocalDate fechaNacimiento) throws BusinessLogicException {
        if (fechaNacimiento == null) {
            throw new BusinessLogicException("La fecha de nacimiento es obligatoria.");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new BusinessLogicException("La fecha de nacimiento no puede ser futura.");
        }
        if (Period.between(fechaNacimiento, LocalDate.now()).getYears() < EDAD_MINIMA) {
            throw new BusinessLogicException("El usuario debe ser mayor de edad.");
        }
    }

    private String normalizarCorreo(String correo) throws BusinessLogicException {
        if (esVacio(correo)) {
            throw new BusinessLogicException("El correo electronico es obligatorio.");
        }
        return correo.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizarCelular(String celular) throws BusinessLogicException {
        if (esVacio(celular)) {
            return null;
        }
        String valor = celular.trim();
        if (!CELULAR_PATTERN.matcher(valor).matches()) {
            throw new BusinessLogicException("El celular debe contener 9 digitos y empezar con 9.");
        }
        return valor;
    }

    private String normalizarGenero(String genero) {
        if (esVacio(genero) || genero.trim().equalsIgnoreCase("Selecciona")) {
            return null;
        }
        return genero.trim();
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
