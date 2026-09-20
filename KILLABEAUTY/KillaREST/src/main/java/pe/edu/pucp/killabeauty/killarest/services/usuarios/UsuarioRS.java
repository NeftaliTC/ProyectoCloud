    package pe.edu.pucp.killabeauty.killarest.services.usuarios;

    import jakarta.ws.rs.*;
    import jakarta.ws.rs.core.MediaType;
    import jakarta.ws.rs.core.Response;
    import pe.edu.pucp.killaBeauty.bl.Impl.Promocionales.CuponBLImpl;
    import pe.edu.pucp.killaBeauty.bl.Impl.ResenaBLImpl;
    import pe.edu.pucp.killaBeauty.bl.Impl.UsuarioBLImpl;
    import pe.edu.pucp.killaBeauty.bl.Promocionales.CuponBL;
    import pe.edu.pucp.killaBeauty.bl.ResenaBL;
    import pe.edu.pucp.killaBeauty.bl.UsuarioBL;
    import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
    import pe.edu.pucp.killaBeauty.killaModelo.Resena;
    import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
    import pe.edu.pucp.killabeauty.killarest.dto.CambiarContrasenaDTO;

    import java.util.List;

    @Path("/usuarios")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public class UsuarioRS {
        private final UsuarioBL usuarioBL = new UsuarioBLImpl();
        private final ResenaBL resenaBL = new ResenaBLImpl();
        @GET
        public Response listarPorTipoUsuario(@QueryParam("tipo") int idTipoUsuario) {
            try {
                List<Usuario> usuarios = usuarioBL.listByTipoUsuario(idTipoUsuario);
                return Response.ok(usuarios).build();
            } catch (Exception e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        }

        @GET
        @Path("{id}")
        public Response obtenerUsuarioPorId(@PathParam("id") int id) {
            try {
                Usuario usuario = usuarioBL.load(id);
                if (usuario != null) {
                    return Response.ok(usuario).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            } catch (Exception e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        }

        @GET
        @Path("email/{correo}")
        public Response obtenerUsuarioPorEmail(@PathParam("correo") String email) {
            try {
                Usuario usuario = usuarioBL.loadByEmail(email);
                if (usuario != null) {
                    return Response.ok(usuario).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            } catch (Exception e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        }
        @POST
        public Response insertarUsuario(Usuario usuario) {
            try {
                Usuario usuarioCreado = usuarioBL.create(usuario);
                return Response.status(Response.Status.CREATED).entity(usuarioCreado).build();
            } catch (BusinessLogicException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
            } catch (Exception e) {
                // Errores de servidor (500)
                return Response.serverError().entity(e.getMessage()).build();
            }
        }

        @PUT
        @Path("{id}/perfil")
        public Response actualizarPerfil(@PathParam("id") int id, Usuario datosPerfil) {
            try {
                if (datosPerfil == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Datos del perfil vacíos o inválidos.")
                            .build();
                }
                datosPerfil.setId(id);
                Usuario usuarioActualizado = usuarioBL.update(datosPerfil);
                return Response.ok(usuarioActualizado).build();
            }catch (BusinessLogicException e) {
                if (e.getMessage().contains("no existe")) {
                    return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

            }
            catch (Exception e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        }

        @PUT
        @Path("{id}/password")
        public Response cambiarContrasena(@PathParam("id") int id, CambiarContrasenaDTO request) {
            try {
                if (request == null){
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                Usuario usuarioActualizado = usuarioBL.cambiarContrasena(id, request.getContrasenaActual(), request.getNuevaContrasena());
                return Response.ok(usuarioActualizado).build();
            } catch (BusinessLogicException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(e.getMessage())
                        .build();
            }catch (Exception e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        }

        @PUT
        @Path("{id}")
        public Response actualizarUsuario(@PathParam("id") int id, Usuario usuario) {
            try {
                // para datos donde no se necesita volver a pedir la contraseña
                usuario.setId(id);
                Usuario usuarioActualizado = usuarioBL.update(usuario);
                return Response.ok(usuarioActualizado).build();
            } catch (Exception e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        }
        @DELETE
        @Path("{id}")
        public Response eliminarUsuario(@PathParam("id") int id) {
            try {
                Usuario usuarioAEliminar = new Usuario();
                usuarioAEliminar.setId(id);

                usuarioBL.remove(usuarioAEliminar);
                return Response.noContent().build();
            } catch (Exception e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        }
        @POST
        @Path("/login")
        public Response login(Usuario credenciales) {
            try {
                // Validación de nulidad defensiva en la entrada HTTP
                if (credenciales == null || credenciales.getCorreoElectronico() == null || credenciales.getContrasena() == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Debe ingresar el correo y la contraseña.")
                            .build();
                }
                Usuario usuarioLogueado = usuarioBL.autenticar(credenciales);

                return Response.ok(usuarioLogueado).build();

            } catch (BusinessLogicException e) {
                //  Si la BL rechaza las credenciales respondemos con un HTTP 401
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(e.getMessage())
                        .build();


            } catch (Exception ex) {
                return Response.serverError().entity(ex.getMessage()).build();
            }
        }
        // reseñas emitidas por el usuario, si se muestra en la pagina de inicio
        @GET
        @Path("{id}/resenas")
        public Response buscarResenasPorUsuario(@PathParam("id")int idUsuario){
            try{
                List<Resena> resenas = resenaBL.listByUsuarioId(idUsuario);
                return Response.ok(resenas).build();
            }catch(Exception e){
                return Response.serverError().entity(e.getMessage()).build();
            }
        }
    }
