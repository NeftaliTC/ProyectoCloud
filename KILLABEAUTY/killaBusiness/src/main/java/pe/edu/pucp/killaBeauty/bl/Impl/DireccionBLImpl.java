package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.bl.DireccionBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.bl.utils.FormatHelper;
import pe.edu.pucp.killaBeauty.killaModelo.Direccion;
import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
import pe.edu.pucp.killaDAO.DireccionDAO;
import pe.edu.pucp.killaDAO.Impl.DireccionDAOImpl;
import pe.edu.pucp.killaDAO.Impl.UsuarioDAOImpl;
import pe.edu.pucp.killaDAO.UsuarioDAO;

import java.util.List;
import java.util.regex.Pattern;

public class DireccionBLImpl implements DireccionBL {

    private DireccionDAO direccionDAO = new DireccionDAOImpl();
    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    private static final Pattern CELULAR_PATTERN = Pattern.compile("^9\\d{8}$");
    private static final Pattern CODIGO_POSTAL_PATTERN = Pattern.compile("^\\d{5}$");

    @Override
    public Direccion create(Direccion d) throws BusinessLogicException {
        validarDireccion(d, false);
        try {
            TransactionContext.getConnection();

            normalizarDireccion(d);
            validarAliasDuplicado(d);

            List<Direccion> lista = direccionDAO.listarPorUsuario(d.getUsuario().getId());
            d.setEsPredeterminada(lista.isEmpty());
            d.setActivo(true);

            Direccion guardada = direccionDAO.save(d);
            TransactionContext.commit();
            return guardada;
        } catch (BusinessLogicException ex) {
            TransactionContext.rollback();
            throw ex;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new BusinessLogicException("Error al crear la direccion: " + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Direccion update(Direccion d) throws BusinessLogicException {
        validarDireccion(d, true);
        try {
            TransactionContext.getConnection();

            normalizarDireccion(d);
            validarAliasDuplicado(d);

            if (Boolean.TRUE.equals(d.getEsPredeterminada())) {
                direccionDAO.resetearPredeterminadas(d.getUsuario().getId());
            } else if (d.getEsPredeterminada() == null) {
                d.setEsPredeterminada(false);
            }

            direccionDAO.update(d);
            TransactionContext.commit();
            return d;
        } catch (BusinessLogicException ex) {
            TransactionContext.rollback();
            throw ex;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new BusinessLogicException("Error al actualizar la direccion: " + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void remove(Direccion d) throws BusinessLogicException {
        if (d == null || d.getId() <= 0) {
            throw new BusinessLogicException("Se requiere una direccion valida para eliminar.");
        }
        try {
            TransactionContext.getConnection();
            direccionDAO.remove(d);
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new BusinessLogicException("Error al eliminar la direccion: " + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Direccion load(Integer id) throws BusinessLogicException {
        if (id == null || id <= 0) {
            throw new BusinessLogicException("El id de la direccion debe ser valido.");
        }
        try {
            return direccionDAO.load(id);
        } catch (Exception e) {
            throw new BusinessLogicException("Error al cargar la direccion: " + e.getMessage());
        }
    }

    @Override
    public List<Direccion> listAll() throws BusinessLogicException {
        try {
            return direccionDAO.listAll();
        } catch (Exception e) {
            throw new BusinessLogicException("Error al listar todas las direcciones: " + e.getMessage());
        }
    }

    @Override
    public List<Direccion> listarPorUsuario(Integer idUsuario) throws BusinessLogicException {
        if (idUsuario == null || idUsuario <= 0) {
            throw new BusinessLogicException("El id del usuario debe ser valido.");
        }
        try {
            List<Direccion> lista = direccionDAO.listarPorUsuario(idUsuario);
            lista.sort((d1, d2) -> Boolean.compare(Boolean.TRUE.equals(d2.getEsPredeterminada()), Boolean.TRUE.equals(d1.getEsPredeterminada())));
            return lista;
        } catch (Exception e) {
            throw new BusinessLogicException("Error al listar las direcciones del usuario: " + e.getMessage());
        }
    }

    private void validarDireccion(Direccion d, boolean requiereId) throws BusinessLogicException {
        if (d == null) {
            throw new BusinessLogicException("La direccion no puede ser nula.");
        }
        if (requiereId && d.getId() <= 0) {
            throw new BusinessLogicException("Se requiere un ID valido para actualizar la direccion.");
        }

        completarUsuarioDesdeDireccionActual(d, requiereId);

        if (d.getUsuario() == null || d.getUsuario().getId() <= 0) {
            throw new BusinessLogicException("La direccion debe pertenecer a un usuario valido.");
        }
        validarUsuarioExistente(d.getUsuario().getId());

        if (esVacio(d.getAlias())) {
            throw new BusinessLogicException("El alias de la direccion es obligatorio.");
        }
        if (esVacio(d.getDireccionDetalle())) {
            throw new BusinessLogicException("La direccion es obligatoria.");
        }
        if (esVacio(d.getDepartamento())) {
            throw new BusinessLogicException("El departamento es obligatorio.");
        }
        if (esVacio(d.getProvincia())) {
            throw new BusinessLogicException("La provincia es obligatoria.");
        }
        if (esVacio(d.getDistrito())) {
            throw new BusinessLogicException("El distrito es obligatorio.");
        }
        if (esVacio(d.getTelefono()) || !CELULAR_PATTERN.matcher(d.getTelefono().trim()).matches()) {
            throw new BusinessLogicException("El telefono de contacto debe contener 9 digitos y empezar con 9.");
        }
        if (!esVacio(d.getCodigoPostal()) && !CODIGO_POSTAL_PATTERN.matcher(d.getCodigoPostal().trim()).matches()) {
            throw new BusinessLogicException("El codigo postal debe contener 5 digitos numericos.");
        }
    }

    private void completarUsuarioDesdeDireccionActual(Direccion d, boolean requiereId) throws BusinessLogicException {
        if (!requiereId || d.getUsuario() != null) {
            return;
        }
        try {
            Direccion actual = direccionDAO.load(d.getId());
            if (actual == null) {
                throw new BusinessLogicException("La direccion indicada no existe.");
            }
            d.setUsuario(actual.getUsuario());
        } catch (BusinessLogicException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessLogicException("Error al validar la direccion: " + ex.getMessage());
        }
    }

    private void validarUsuarioExistente(int idUsuario) throws BusinessLogicException {
        try {
            Usuario usuario = usuarioDAO.load(idUsuario);
            if (usuario == null) {
                throw new BusinessLogicException("El usuario asociado a la direccion no existe.");
            }
        } catch (BusinessLogicException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessLogicException("Error al validar el usuario de la direccion: " + ex.getMessage());
        }
    }

    private void validarAliasDuplicado(Direccion d) throws BusinessLogicException {
        try {
            List<Direccion> lista = direccionDAO.listarPorUsuario(d.getUsuario().getId());
            for (Direccion existente : lista) {
                if (existente.getAlias().equalsIgnoreCase(d.getAlias()) && existente.getId() != d.getId()) {
                    throw new BusinessLogicException("Ya tienes una direccion registrada con el alias '" + d.getAlias() + "'.");
                }
            }
        } catch (BusinessLogicException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessLogicException("Error al validar el alias de la direccion: " + ex.getMessage());
        }
    }

    private void normalizarDireccion(Direccion d) {
        d.setAlias(FormatHelper.capitalizarTexto(d.getAlias().trim()));
        d.setDireccionDetalle(FormatHelper.capitalizarTexto(d.getDireccionDetalle().trim()));
        d.setDepartamento(FormatHelper.capitalizarTexto(d.getDepartamento().trim()));
        d.setProvincia(FormatHelper.capitalizarTexto(d.getProvincia().trim()));
        d.setDistrito(FormatHelper.capitalizarTexto(d.getDistrito().trim()));
        d.setTelefono(d.getTelefono().trim());
        d.setCodigoPostal(esVacio(d.getCodigoPostal()) ? null : d.getCodigoPostal().trim());
        d.setReferencia(esVacio(d.getReferencia()) ? null : FormatHelper.capitalizarTexto(d.getReferencia().trim()));
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
