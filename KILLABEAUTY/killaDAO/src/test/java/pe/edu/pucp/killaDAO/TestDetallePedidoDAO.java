package pe.edu.pucp.killaDAO;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.killaBeauty.killaModelo.DetallePedido;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;
import pe.edu.pucp.killaDAO.Impl.DetallePedidoDAOImpl;

import java.sql.*;
import java.util.List;

public class TestDetallePedidoDAO {

    public static void main(String[] args) {
        DetallePedidoDAO dao = new DetallePedidoDAOImpl();

        Integer idTipoUsuario = null;
        Integer idUsuario = null;
        Integer idDireccion = null;
        Integer idCategoria = null;
        Integer idMarca = null;
        Integer idProducto = null;
        Integer idPedido = null;
        Integer idDetalle = null;

        try (Connection cn = DBManager.getInstance().getConnection()) {

            // TipoUsuario
            try (PreparedStatement ps = cn.prepareStatement(
                    "SELECT id_tipoUsuario FROM TipoUsuario WHERE descripcion = ? LIMIT 1")) {
                ps.setString(1, "Cliente");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) idTipoUsuario = rs.getInt(1);
                }
            }
            if (idTipoUsuario == null) {
                try (PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO TipoUsuario(descripcion) VALUES(?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, "Cliente");
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) idTipoUsuario = keys.getInt(1);
                    }
                }
            }

            // Usuario
            try (PreparedStatement ps = cn.prepareStatement("""
                    INSERT INTO Usuario
                    (descripcion, apellido_paterno, apellido_materno, correo_electronico, contrasena, telefono, activo, id_tipoUsuario, fecha_de_inscripcion)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())
                    """, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "UserDetTest");
                ps.setString(2, "Paterno");
                ps.setString(3, "Materno");
                ps.setString(4, "dettest_" + System.currentTimeMillis() + "@mail.com");
                ps.setString(5, "123456");
                ps.setString(6, "999999999");
                ps.setBoolean(7, true);
                ps.setInt(8, idTipoUsuario);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) idUsuario = keys.getInt(1);
                }
            }

            // Direccion
            try (PreparedStatement ps = cn.prepareStatement("""
                    INSERT INTO Direccion
                    (departamento, provincia, distrito, direccion_detalle, referencia, id_usuario)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "Lima");
                ps.setString(2, "Lima");
                ps.setString(3, "San Miguel");
                ps.setString(4, "Av Test 123");
                ps.setString(5, "Ref");
                ps.setInt(6, idUsuario);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) idDireccion = keys.getInt(1);
                }
            }

            // Categoria
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO Categoria(descripcion) VALUES(?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "CatDetTest");
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) idCategoria = keys.getInt(1);
                }
            }

            // Marca
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO Marca(descripcion, pais_origen) VALUES(?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "MarcaDetTest");
                ps.setString(2, "PE");
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) idMarca = keys.getInt(1);
                }
            }

            // Producto
            try (PreparedStatement ps = cn.prepareStatement("""
                    INSERT INTO Producto(descripcion, precio_base, stock, disponible, promocion, id_marca, id_categoria)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "ProductoDetTest");
                ps.setDouble(2, 50.0);
                ps.setInt(3, 100);
                ps.setBoolean(4, true);
                ps.setBoolean(5, false);
                ps.setInt(6, idMarca);
                ps.setInt(7, idCategoria);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) idProducto = keys.getInt(1);
                }
            }

            // Pedido
            try (PreparedStatement ps = cn.prepareStatement("""
                    INSERT INTO Pedido(fecha_pedido, subtotal, id_cupon, igv, total, id_usuario, id_direccion, estado_pedido)
                    VALUES (NOW(), ?, NULL, ?, ?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, 0.0);
                ps.setDouble(2, 0.0);
                ps.setDouble(3, 0.0);
                ps.setInt(4, idUsuario);
                ps.setInt(5, idDireccion);
                ps.setString(6, "PENDIENTE");
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) idPedido = keys.getInt(1);
                }
            }

            // Save detalle
            Producto productoRef = new Producto();
            productoRef.setId(idProducto);

            DetallePedido nuevo = new DetallePedido();
            nuevo.setCantidad(2);
            nuevo.setPrecioAplicado(50.0);
            nuevo.setProducto(productoRef);

            DetallePedido guardado = dao.save(nuevo, idPedido);
            idDetalle = guardado.getIdDetallePedido();
            System.out.println("SAVE OK -> idDetalle=" + idDetalle);

            // Load
            DetallePedido cargado = dao.load(idDetalle);
            System.out.println("LOAD OK -> " + (cargado != null ? cargado.getCantidad() : "null"));

            // Update
            if (cargado != null) {
                cargado.setCantidad(3);
                cargado.setPrecioAplicado(45.0);
                cargado.setProducto(productoRef);
                dao.update(cargado, idPedido);

                DetallePedido actualizado = dao.load(idDetalle);
                System.out.println("UPDATE OK -> " + (actualizado != null ? actualizado.getCantidad() : "null"));
            }

            // List all
            List<DetallePedido> lista = dao.listAll();
            System.out.println("LIST ALL -> total: " + lista.size());

            // Cleanup
            if (idDetalle != null) {
                DetallePedido d = new DetallePedido();
                d.setIdDetallePedido(idDetalle);
                dao.remove(d);
            }

            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Pedido WHERE id_pedido = ?")) {
                ps.setInt(1, idPedido);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Producto WHERE id_producto = ?")) {
                ps.setInt(1, idProducto);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Marca WHERE id_marca = ?")) {
                ps.setInt(1, idMarca);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Categoria WHERE id_categoria = ?")) {
                ps.setInt(1, idCategoria);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Direccion WHERE id_direccion = ?")) {
                ps.setInt(1, idDireccion);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Usuario WHERE id_usuario = ?")) {
                ps.setInt(1, idUsuario);
                ps.executeUpdate();
            }

            System.out.println("TEST DETALLE PEDIDO OK");

        } catch (Exception e) {
            System.err.println("Error en TestDetallePedidoDAO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
