package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.killaModelo.*;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.TipoDescuento;
import pe.edu.pucp.killaDAO.PedidoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public List<Pedido> listAll() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = """
                SELECT p.id_pedido, p.fecha_pedido, p.subtotal, p.igv, p.total,
                   p.id_estado_pedido, p.id_cupon,
                   u.id_usuario, u.nombre, u.apellido_paterno, u.apellido_materno, u.correo_electronico, u.telefono,
                   d.id_direccion, d.direccion_detalle, d.referencia, d.distrito, d.provincia, d.departamento,
                   dp.id_detalle_pedido, dp.cantidad, dp.precio_unitario_aplicado,
                   pr.id_producto, pr.nombre AS nombre_producto, pr.precio_base,
                   m.id_marca, m.descripcion AS nombre_marca,
                   img.id_imagen, img.url, img.principal,
                   c.valor_descuento, c.id_tipo_descuento, c.monto_maximo_descuento
            FROM Pedido p
            LEFT JOIN Usuario u ON p.id_usuario = u.id_usuario
            LEFT JOIN Direccion d ON p.id_direccion = d.id_direccion
            LEFT JOIN DetallePedido dp ON p.id_pedido = dp.id_pedido
            LEFT JOIN Producto pr ON dp.id_producto = pr.id_producto
            LEFT JOIN Marca m ON pr.id_marca = m.id_marca
            LEFT JOIN ImagenProducto img ON pr.id_producto = img.id_producto AND img.activo = 1 AND img.principal = 1
            LEFT JOIN Cupon c ON p.id_cupon = c.id_cupon
            ORDER BY p.id_pedido DESC
            """;

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Pedido actual = null;
            while (rs.next()) {
                int idPedido = rs.getInt("id_pedido");

                if (actual == null || actual.getId() != idPedido) {
                    actual = mapRow(rs);
                    pedidos.add(actual);
                }

                int idDetalle = rs.getInt("id_detalle_pedido");
                if (!rs.wasNull()) {
                    DetallePedido det = new DetallePedido();
                    det.setCantidad(rs.getInt("cantidad"));
                    det.setPrecioAplicado(rs.getDouble("precio_unitario_aplicado"));

                    Producto pr = new Producto();
                    pr.setId(rs.getInt("id_producto"));
                    pr.setNombre(rs.getString("nombre_producto"));
                    pr.setPrecioBase(rs.getDouble("precio_base"));

                    Marca m = new Marca();
                    m.setId(rs.getInt("id_marca"));
                    m.setDescripcion(rs.getString("nombre_marca"));
                    pr.setMarca(m);

                    int idImagen = rs.getInt("id_imagen"); // Asegúrate de que el SQL traiga este campo
                    if (!rs.wasNull()) {
                        ImagenProducto img = new ImagenProducto();
                        img.setId(idImagen);
                        img.setUrl(rs.getString("url"));
                        img.setPrincipal(rs.getBoolean("principal"));

                        if (pr.getImagenes() == null) {
                            pr.setImagenes(new ArrayList<>());
                        }

                        // evitar duplicado si ya hay imagen
                        boolean existe = false;
                        for (ImagenProducto i : pr.getImagenes()) {
                            if (i.getId() == img.getId()) existe = true;
                        }
                        if (!existe) {
                            pr.getImagenes().add(img);
                        }
                    }

                    det.setProducto(pr);
                    if (actual.getDetalles() == null) actual.setDetalles(new ArrayList<>());
                    actual.getDetalles().add(det);
                }
            }
        }
        return pedidos;
    }

    @Override
    public List<Pedido> listByCliente(Integer idCliente) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = """
            SELECT p.id_pedido, p.fecha_pedido, p.subtotal, p.igv, p.total,
                   p.id_estado_pedido, p.id_cupon,
                   u.id_usuario, u.nombre, u.apellido_paterno, u.apellido_materno, u.correo_electronico, u.telefono,
                   d.id_direccion, d.direccion_detalle, d.referencia, d.distrito, d.provincia, d.departamento,
                   dp.id_detalle_pedido, dp.cantidad, dp.precio_unitario_aplicado,
                   pr.id_producto, pr.nombre AS nombre_producto, pr.precio_base,
                   m.id_marca, m.descripcion AS nombre_marca,
                   img.id_imagen, img.url, img.principal,
                   c.valor_descuento, c.id_tipo_descuento, c.monto_maximo_descuento
            FROM Pedido p
            LEFT JOIN Usuario u ON p.id_usuario = u.id_usuario
            LEFT JOIN Direccion d ON p.id_direccion = d.id_direccion
            LEFT JOIN DetallePedido dp ON p.id_pedido = dp.id_pedido
            LEFT JOIN Producto pr ON dp.id_producto = pr.id_producto
            LEFT JOIN Marca m ON pr.id_marca = m.id_marca
            LEFT JOIN ImagenProducto img ON pr.id_producto = img.id_producto AND img.activo = 1 AND img.principal = 1
            LEFT JOIN Cupon c ON p.id_cupon = c.id_cupon
            WHERE p.id_usuario = ?
            ORDER BY p.id_pedido DESC
            """;

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                Pedido actual = null;
                while (rs.next()) {
                    int idPedido = rs.getInt("id_pedido");

                    if (actual == null || actual.getId() != idPedido) {
                        actual = mapRow(rs);
                        pedidos.add(actual);
                    }

                    int idDetalle = rs.getInt("id_detalle_pedido");
                    if (!rs.wasNull()) {
                        DetallePedido det = new DetallePedido();
                        det.setCantidad(rs.getInt("cantidad"));
                        det.setPrecioAplicado(rs.getDouble("precio_unitario_aplicado"));

                        Producto pr = new Producto();
                        pr.setId(rs.getInt("id_producto"));
                        pr.setNombre(rs.getString("nombre_producto"));
                        pr.setPrecioBase(rs.getDouble("precio_base"));

                        Marca m = new Marca();
                        m.setId(rs.getInt("id_marca"));
                        m.setDescripcion(rs.getString("nombre_marca"));
                        pr.setMarca(m);

                        int idImagen = rs.getInt("id_imagen"); // Asegúrate de que el SQL traiga este campo
                        if (!rs.wasNull()) {
                            ImagenProducto img = new ImagenProducto();
                            img.setId(idImagen);
                            img.setUrl(rs.getString("url"));
                            img.setPrincipal(rs.getBoolean("principal"));

                            if (pr.getImagenes() == null) {
                                pr.setImagenes(new ArrayList<>());
                            }

                            // evitar duplicado si ya hay imagen
                            boolean existe = false;
                            for (ImagenProducto i : pr.getImagenes()) {
                                if (i.getId() == img.getId()) existe = true;
                            }
                            if (!existe) {
                                pr.getImagenes().add(img);
                            }
                        }

                        det.setProducto(pr);
                        if (actual.getDetalles() == null) actual.setDetalles(new ArrayList<>());
                        actual.getDetalles().add(det);
                    }
                }
            }
        }
        return pedidos;
    }

    @Override
    public Pedido load(Integer id) throws SQLException {
        String sql = """
            SELECT p.id_pedido, p.id_usuario, p.id_direccion, p.id_cupon, p.fecha_pedido,
                   p.subtotal, p.igv, p.total, p.id_estado_pedido,
                   u.nombre, u.apellido_paterno, u.apellido_materno, u.correo_electronico, u.telefono,
                   d.direccion_detalle, d.referencia, d.distrito, d.provincia, d.departamento,
                   c.valor_descuento, c.id_tipo_descuento, c.monto_maximo_descuento
            FROM Pedido p
            LEFT JOIN Usuario u ON p.id_usuario = u.id_usuario
            LEFT JOIN Direccion d ON p.id_direccion = d.id_direccion
            LEFT JOIN Cupon c ON p.id_cupon = c.id_cupon
            WHERE p.id_pedido = ?
            """;

        try (Connection cn = DBManager.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public Pedido save(Pedido pedido) throws SQLException {
        validarPedido(pedido);
        String sql = """
                INSERT INTO Pedido
                (fecha_pedido, subtotal, igv, total, id_usuario, id_direccion, id_cupon, id_estado_pedido)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        Connection cn = TransactionContext.getConnection();

        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setPedidoParams(ps, pedido);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) pedido.setId(keys.getInt(1));
            }
        }

        if (pedido.getDetalles() != null) {
            DetallePedidoDAOImpl detalleDAO = new DetallePedidoDAOImpl();
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalleDAO.save(detalle, pedido.getId());
            }
        }
        return pedido;
    }

    @Override
    public Pedido update(Pedido pedido) throws SQLException {
        validarPedido(pedido);
        String sql = """
                UPDATE Pedido
                SET fecha_pedido = ?, subtotal = ?, igv = ?, total = ?, id_usuario = ?,
                    id_direccion = ?, id_cupon = ?, id_estado_pedido = ?
                WHERE id_pedido = ?
                """;
        Connection cn = TransactionContext.getConnection();

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            setPedidoParams(ps, pedido);
            ps.setInt(9, pedido.getId());
            ps.executeUpdate();
        }

        if (pedido.getDetalles() != null) {
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM DetallePedido WHERE id_pedido = ?")) {
                ps.setInt(1, pedido.getId());
                ps.executeUpdate();
            }
            DetallePedidoDAOImpl detalleDAO = new DetallePedidoDAOImpl();
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalleDAO.save(detalle, pedido.getId());
            }
        }
        return pedido;
    }

    @Override
    public void remove(Pedido pedido) throws SQLException {
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM DetallePedido WHERE id_pedido = ?")) {
            ps.setInt(1, pedido.getId());
            ps.executeUpdate();
        }
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Pedido WHERE id_pedido = ?")) {
            ps.setInt(1, pedido.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateEstado(Integer idPedido, Integer idNuevoEstado) throws SQLException {
        String sql = "UPDATE Pedido SET id_estado_pedido = ? WHERE id_pedido = ?";
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idNuevoEstado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        }
    }

    private void setPedidoParams(PreparedStatement ps, Pedido pedido) throws SQLException {
        setTimestamp(ps, 1, pedido.getFechaPedido());
        ps.setDouble(2, pedido.getSubtotal());
        ps.setDouble(3, pedido.getIgv());
        ps.setDouble(4, pedido.getTotal());
        ps.setInt(5, pedido.getCliente().getId());
        if (pedido.getDireccionEnvio() != null) {
            ps.setInt(6, pedido.getDireccionEnvio().getId());
        } else {
            ps.setNull(6, Types.INTEGER);
        }
        if (pedido.getCupon() != null) ps.setInt(7, pedido.getCupon().getId());
        else ps.setNull(7, Types.INTEGER);
        ps.setInt(8, pedido.getEstadoPedido().getId());
    }

    private Pedido mapRow(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id_pedido"));
        p.setFechaPedido(rs.getTimestamp("fecha_pedido"));
        p.setSubtotal(rs.getDouble("subtotal"));
        p.setIgv(rs.getDouble("igv"));
        p.setTotal(rs.getDouble("total"));
        p.setEstadoPedido(EstadoPedido.fromId(rs.getInt("id_estado_pedido")));

        int idCupon = rs.getInt("id_cupon");
        if (!rs.wasNull()) {
            Cupon cupon = new Cupon();
            cupon.setId(idCupon);
            cupon.setValorDescuento(rs.getDouble("valor_descuento"));
            double montoMax = rs.getDouble("monto_maximo_descuento");
            cupon.setMontoMaximoDescuento(rs.wasNull() ? null : montoMax);
            int idTipo = rs.getInt("id_tipo_descuento");
            if (idTipo == 1) cupon.setTipoDescuento(TipoDescuento.PORCENTAJE);
            else if (idTipo == 2) cupon.setTipoDescuento(TipoDescuento.MONTO_FIJO);
            p.setCupon(cupon);
        }

        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setApellidoPaterno(rs.getString("apellido_paterno"));
        u.setApellidoMaterno(rs.getString("apellido_materno"));
        u.setCorreoElectronico(rs.getString("correo_electronico"));
        u.setTelefono(rs.getString("telefono"));
        p.setCliente(u);

        int idDir = rs.getInt("id_direccion");
        if (!rs.wasNull()) {
            Direccion d = new Direccion();
            d.setId(idDir);
            d.setDireccionDetalle(rs.getString("direccion_detalle"));
            d.setReferencia(rs.getString("referencia"));
            d.setDistrito(rs.getString("distrito"));
            d.setProvincia(rs.getString("provincia"));
            d.setDepartamento(rs.getString("departamento"));
            p.setDireccionEnvio(d);
        }
        return p;
    }

    private void validarPedido(Pedido pedido) throws SQLException {
        if (pedido.getCliente() == null || pedido.getCliente().getId() <= 0) {
            throw new SQLException("Pedido: cliente invalido");
        }
        if (pedido.getDireccionEnvio() != null &&
                pedido.getDireccionEnvio().getId() <= 0) {
            throw new SQLException("Pedido: direccion de envio invalida");
        }
        if (pedido.getEstadoPedido() == null) {
            throw new SQLException("Pedido: estado invalido");
        }
    }

    private void setTimestamp(PreparedStatement ps, int index, java.util.Date value) throws SQLException {
        if (value == null) ps.setTimestamp(index, new Timestamp(System.currentTimeMillis()));
        else ps.setTimestamp(index, new Timestamp(value.getTime()));
    }

    @Override
    public void updateTotal(Integer idPedido, double nuevoTotal) throws SQLException {
        String sql = "UPDATE Pedido SET total = ? WHERE id_pedido = ?";
        Connection cn = TransactionContext.getConnection();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDouble(1, nuevoTotal);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        }
    }
}
