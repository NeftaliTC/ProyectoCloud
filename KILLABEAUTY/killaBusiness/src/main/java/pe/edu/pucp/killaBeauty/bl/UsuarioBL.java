package pe.edu.pucp.killaBeauty.bl;

import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Usuario;

import java.util.List;

public interface UsuarioBL {
    Usuario create(Usuario usuario) throws BusinessLogicException;
    Usuario update(Usuario usuario) throws BusinessLogicException;
    void remove(Usuario usuario) throws BusinessLogicException;
    Usuario load(int id) throws BusinessLogicException;
    Usuario loadByEmail(String email) throws BusinessLogicException;
    List<Usuario> listByTipoUsuario(int idTipoUsuario) throws BusinessLogicException;
    Usuario cambiarContrasena(int id, String contrasenaActual, String nuevaContrasena)throws BusinessLogicException;
    Usuario autenticar(Usuario usuario) throws BusinessLogicException;
}
