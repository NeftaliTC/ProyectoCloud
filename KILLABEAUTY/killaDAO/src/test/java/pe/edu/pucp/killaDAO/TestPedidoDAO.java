package pe.edu.pucp.killaDAO;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.Direccion;
import pe.edu.pucp.killaBeauty.killaModelo.Pedido;
import pe.edu.pucp.killaBeauty.killaModelo.Usuario;
import pe.edu.pucp.killaDAO.Impl.PedidoDAOImpl;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class TestPedidoDAO {

//    public static void main(String[] args) {
//        PedidoDAO pedidoDAO = new PedidoDAOImpl();
//
//        Integer idTipoUsuario = null;
//        Integer idUsuarioCreado = null;
//        Integer idDireccionCreada = null;
//        Integer idPedidoCreado = null;
//
//        try (Connection cn = DBManager.getInstance().getConnection()) {
//
//            // 0) SETUP: asegurar TipoUsuario para FK de Usuario
//            String sqlFindTipo = "SELECT id_tipoUsuario FROM TipoUsuario WHERE nombre = ? LIMIT 1";
//            try (PreparedStatement ps = cn.prepareStatement(sqlFindTipo)) {
//                ps.setString(1, "Cliente");
//                try (ResultSet rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        idTipoUsuario = rs.getInt("id_tipoUsuario");
//                    }
//                }
//            }
//
//            if (idTipoUsuario == null) {
//                String sqlInsertTipo = "INSERT INTO TipoUsuario (nombre) VALUES (?)";
//                try (PreparedStatement ps = cn.prepareStatement(sqlInsertTipo, Statement.RETURN_GENERATED_KEYS)) {
//                    ps.setString(1, "Cliente");
//                    ps.executeUpdate();
//                    try (ResultSet keys = ps.getGeneratedKeys()) {
//                        if (keys.next()) idTipoUsuario = keys.getInt(1);
//                    }
//                }
//            }
//
//            if (idTipoUsuario == null) {
//                throw new RuntimeException("No se pudo crear/obtener TipoUsuario");
//            }
//
//            // 1) CREAR USUARIO
//            String sqlInsertUsuario = """
//                    INSERT INTO Usuario
//                    (nombre, apellido_paterno, apellido_materno, correo_electronico, contrasena, telefono, activo, id_tipoUsuario, fecha_de_inscripcion)
//                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())
//                    """;
//
//            try (PreparedStatement ps = cn.prepareStatement(sqlInsertUsuario, Statement.RETURN_GENERATED_KEYS)) {
//                ps.setString(1, "UsuarioTestPedido");
//                ps.setString(2, "Paterno");
//                ps.setString(3, "Materno");
//                ps.setString(4, "testpedido_" + System.currentTimeMillis() + "@mail.com");
//                ps.setString(5, "123456");
//                ps.setString(6, "999999999");
//                ps.setBoolean(7, true);
//                ps.setInt(8, idTipoUsuario);
//                ps.executeUpdate();
//
//                try (ResultSet keys = ps.getGeneratedKeys()) {
//                    if (keys.next()) idUsuarioCreado = keys.getInt(1);
//                }
//            }
//
//            if (idUsuarioCreado == null) {
//                throw new RuntimeException("No se pudo crear Usuario de prueba");
//            }
//
//            // 2) CREAR DIRECCION
//            String sqlInsertDireccion = """
//                    INSERT INTO Direccion
//                    (Departamento, Provincia, Distrito, Direccion_exacta, Referencia, id_usuario)
//                    VALUES (?, ?, ?, ?, ?, ?)
//                    """;
//
//            try (PreparedStatement ps = cn.prepareStatement(sqlInsertDireccion, Statement.RETURN_GENERATED_KEYS)) {
//                ps.setString(1, "Lima");
//                ps.setString(2, "Lima");
//                ps.setString(3, "San Miguel");
//                ps.setString(4, "Av. Test 123");
//                ps.setString(5, "Referencia de prueba");
//                ps.setInt(6, idUsuarioCreado);
//                ps.executeUpdate();
//
//                try (ResultSet keys = ps.getGeneratedKeys()) {
//                    if (keys.next()) idDireccionCreada = keys.getInt(1);
//                }
//            }
//
//            if (idDireccionCreada == null) {
//                throw new RuntimeException("No se pudo crear Direccion de prueba");
//            }
//
//            System.out.println("SETUP OK -> idTipoUsuario=" + idTipoUsuario
//                    + ", idUsuario=" + idUsuarioCreado
//                    + ", idDireccion=" + idDireccionCreada);
//
//            // 3) SAVE PEDIDO
//            Usuario usuario = new Usuario();
//            usuario.setId(idUsuarioCreado);
//
//            Direccion direccion = new Direccion();
//            direccion.setIdDireccion(idDireccionCreada);
//
//            Pedido nuevo = new Pedido();
//            nuevo.setCliente(usuario);
//            nuevo.setDireccionEnvio(direccion);
//            nuevo.setCupon(null);
//            nuevo.setFechaPedido(LocalDate.now());
//            nuevo.setActivo("PENDIENTE");
//            nuevo.setSubtotal(100.00);
//            nuevo.setIgv(18.00);
//            nuevo.setTotal(118.00);
//
//            Pedido guardado = pedidoDAO.save(nuevo);
//            idPedidoCreado = guardado.getIdPedido();
//            System.out.println("SAVE PEDIDO OK -> idPedido=" + idPedidoCreado);
//
//            // 4) LOAD
//            Pedido cargado = pedidoDAO.load(idPedidoCreado);
//            System.out.println("LOAD OK -> " + (cargado != null ? cargado.getActivo() : "null"));
//
//            // 5) UPDATE
//            if (cargado != null) {
//                cargado.setActivo("CONFIRMADO");
//                cargado.setSubtotal(200.00);
//                cargado.setIgv(36.00);
//                cargado.setTotal(236.00);
//                pedidoDAO.update(cargado);
//
//                Pedido actualizado = pedidoDAO.load(idPedidoCreado);
//                System.out.println("UPDATE OK -> " + (actualizado != null ? actualizado.getActivo() : "null"));
//            }
//
//            // 6) LIST ALL
//            List<Pedido> lista = pedidoDAO.listAll();
//            System.out.println("LIST ALL -> total: " + lista.size());
//
//            // 7) CLEANUP
//            if (idPedidoCreado != null) {
//                Pedido p = new Pedido();
//                p.setIdPedido(idPedidoCreado);
//                pedidoDAO.remove(p);
//                System.out.println("CLEANUP PEDIDO OK");
//            }
//
//            if (idDireccionCreada != null) {
//                try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Direccion WHERE id_direccion = ?")) {
//                    ps.setInt(1, idDireccionCreada);
//                    ps.executeUpdate();
//                }
//                System.out.println("CLEANUP DIRECCION OK");
//            }
//
//            if (idUsuarioCreado != null) {
//                try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Usuario WHERE id_usuario = ?")) {
//                    ps.setInt(1, idUsuarioCreado);
//                    ps.executeUpdate();
//                }
//                System.out.println("CLEANUP USUARIO OK");
//            }
//
//            System.out.println("TEST PEDIDO FINALIZADO");
//
//        } catch (Exception e) {
//            System.err.println("Error en TestPedidoDAO: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
}
