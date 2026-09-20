package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.bl.PedidoBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.*;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;
import pe.edu.pucp.killaDAO.*;
import pe.edu.pucp.killaDAO.Impl.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoBLImpl implements PedidoBL {
    private PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAOImpl();
    private ProductoDAO productoDAO = new ProductoDAOImpl();
    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private DireccionDAO direccionDAO = new DireccionDAOImpl();
    private MarcaDAO marcaDAO = new MarcaDAOImpl();
    private EnvioDAO envioDAO = new EnvioDAOImpl();
    private PagoDAO pagoDAO = new PagoDAOImpl();
    private ComprobantePagoDAO comprobanteDAO = new ComprobantePagoDAOImpl();

    @Override
    public Pedido create(Pedido pedido) throws BusinessLogicException {
        validarPedido(pedido);
        if (pedido.getFechaPedido() == null) pedido.setFechaPedido(new Date());
        if (pedido.getEstadoPedido() == null) pedido.setEstadoPedido(EstadoPedido.PENDIENTE);
        recalcularTotales(pedido);

        try {
            TransactionContext.getConnection();
            Pedido guardado = pedidoDAO.save(pedido);
            TransactionContext.commit();
            return guardado;
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pedido createFromCart(Integer idCliente, Integer idDireccion, Integer idCupon, List<DetallePedido> detalles) throws BusinessLogicException {
        if (idCliente == null || idCliente <= 0) throw new BusinessLogicException("Debe indicar un cliente valido.");
        if (detalles == null || detalles.isEmpty()) throw new BusinessLogicException("El carrito no tiene productos para registrar.");

        Pedido pedido = new Pedido();

        Usuario cliente = new Usuario();
        cliente.setId(idCliente);
        pedido.setCliente(cliente);

        if (idDireccion != null && idDireccion > 0) {
            Direccion direccion = new Direccion();
            direccion.setId(idDireccion);
            pedido.setDireccionEnvio(direccion);
        }

        if (idCupon != null && idCupon > 0) {
            Cupon cupon = new Cupon();
            cupon.setId(idCupon);
            pedido.setCupon(cupon);
        }

        pedido.setDetalles(prepararDetallesDesdeCarrito(detalles));
        return create(pedido);
    }

    @Override
    public Pedido cancel(Integer id) throws BusinessLogicException {
        if (id == null || id <= 0) throw new BusinessLogicException("Debe indicar un pedido valido.");

        try {
            Pedido pedido = pedidoDAO.load(id);
            if (pedido == null) throw new BusinessLogicException("No se encontro el pedido indicado.");

            EstadoPedido estado = pedido.getEstadoPedido();
            if (estado == EstadoPedido.ENVIADO || estado == EstadoPedido.ENTREGADO) {
                throw new BusinessLogicException("No se puede cancelar el pedido porque ya esta enviado o entregado.");
            }
            if (estado == EstadoPedido.CANCELADO) {
                return completarPedido(pedido);
            }

            TransactionContext.getConnection();
            pedidoDAO.updateEstado(id, EstadoPedido.CANCELADO.getId());
            TransactionContext.commit();

            pedido.setEstadoPedido(EstadoPedido.CANCELADO);
            return completarPedido(pedido);
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pedido update(Pedido pedido) throws BusinessLogicException {
        validarPedido(pedido);
        recalcularTotales(pedido);

        try {
            TransactionContext.getConnection();
            Pedido actualizado = pedidoDAO.update(pedido);
            TransactionContext.commit();
            return actualizado;
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void remove(Pedido pedido) throws BusinessLogicException {
        if (pedido == null || pedido.getId() <= 0) throw new BusinessLogicException("Debe indicar un pedido valido.");
        try {
            TransactionContext.getConnection();
            pedidoDAO.remove(pedido);
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pedido load(Integer id) throws BusinessLogicException {
        try {
            return completarPedido(pedidoDAO.load(id));
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Pedido> listAll() throws BusinessLogicException {
        try {
            return pedidoDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public List<Pedido> listByCliente(Integer idCliente) throws BusinessLogicException {
        if (idCliente == null || idCliente <= 0)
            throw new BusinessLogicException("Debe indicar un cliente valido.");
        try {
            return pedidoDAO.listByCliente(idCliente);
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    @Override
    public void actualizarTotal(Integer idPedido, double nuevoTotal) throws BusinessLogicException {
        try {
            TransactionContext.getConnection();
            pedidoDAO.updateTotal(idPedido, nuevoTotal);
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void actualizarEstado(Integer idPedido, EstadoPedido estadoPedido) throws BusinessLogicException {
        if (idPedido == null || idPedido <= 0) {
            throw new BusinessLogicException("Debe indicar un pedido valido.");
        }
        if (estadoPedido == null) {
            throw new BusinessLogicException("Debe indicar un estado de pedido valido.");
        }

        try {
            TransactionContext.getConnection();
            pedidoDAO.updateEstado(idPedido, estadoPedido.getId());
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException(e);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarPedido(Pedido pedido) throws BusinessLogicException {
        if (pedido == null)
            throw new BusinessLogicException("El pedido no puede ser nulo.");

        if (pedido.getCliente() == null || pedido.getCliente().getId() <= 0)
            throw new BusinessLogicException("El pedido debe tener un cliente valido.");

        // Temporalmente no validar dirección
        if (pedido.getDireccionEnvio() != null &&
                pedido.getDireccionEnvio().getId() <= 0) {
            throw new BusinessLogicException("El pedido debe tener una direccion de envio valida.");
        }

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty())
            throw new BusinessLogicException("El pedido debe tener al menos un detalle.");

        for (DetallePedido detalle : pedido.getDetalles())
            validarDetalle(detalle);
    }

    private void validarDetalle(DetallePedido detalle) throws BusinessLogicException {
        if (detalle == null) throw new BusinessLogicException("El detalle del pedido no puede ser nulo.");
        if (detalle.getProducto() == null || detalle.getProducto().getId() <= 0) throw new BusinessLogicException("Cada detalle debe tener un producto valido.");
        if (detalle.getCantidad() <= 0) throw new BusinessLogicException("La cantidad del detalle debe ser mayor a cero.");
        if (detalle.getPrecioAplicado() < 0) throw new BusinessLogicException("El precio aplicado no puede ser negativo.");
    }

    private List<DetallePedido> prepararDetallesDesdeCarrito(List<DetallePedido> detalles) throws BusinessLogicException {
        List<DetallePedido> detallesPreparados = new ArrayList<>();

        for (DetallePedido item : detalles) {
            if (item == null || item.getProducto() == null || item.getProducto().getId() <= 0) {
                throw new BusinessLogicException("Cada item del carrito debe tener un producto valido.");
            }
            if (item.getCantidad() <= 0) {
                throw new BusinessLogicException("La cantidad de cada item del carrito debe ser mayor a cero.");
            }

            Producto producto = cargarProducto(item.getProducto().getId());
            validarProductoDisponible(producto, item.getCantidad());

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioAplicado(item.getPrecioAplicado() > 0
                    ? item.getPrecioAplicado()
                    : producto.getPrecioBase());
            detallesPreparados.add(detalle);
        }

        return detallesPreparados;
    }

    private Producto cargarProducto(int idProducto) throws BusinessLogicException {
        try {
            Producto producto = productoDAO.load(idProducto);
            if (producto == null) throw new BusinessLogicException("No se encontro el producto con id " + idProducto + ".");
            return producto;
        } catch (SQLException e) {
            throw new BusinessLogicException(e);
        }
    }

    private void validarProductoDisponible(Producto producto, int cantidad) throws BusinessLogicException {
        if (Boolean.FALSE.equals(producto.getActivo()) || Boolean.FALSE.equals(producto.getDisponible())) {
            throw new BusinessLogicException("El producto " + producto.getNombre() + " no esta disponible.");
        }
        if (producto.getStock() != null && producto.getStock() < cantidad) {
            throw new BusinessLogicException("No hay stock suficiente para " + producto.getNombre() + ".");
        }
    }

    private Pedido completarPedido(Pedido pedido) throws SQLException {
        if (pedido == null) {
            return null;
        }

        completarCliente(pedido);
        completarDireccion(pedido);
        completarDetalles(pedido);
        completarProductos(pedido);
        return pedido;
    }

    private void completarCliente(Pedido pedido) throws SQLException {
        if (pedido.getCliente() == null || pedido.getCliente().getId() <= 0) {
            return;
        }

        Usuario cliente = usuarioDAO.load(pedido.getCliente().getId());
        if (cliente != null) {
            pedido.setCliente(cliente);
        }
    }

    private void completarDireccion(Pedido pedido) throws SQLException {
        if (pedido.getDireccionEnvio() == null || pedido.getDireccionEnvio().getId() <= 0) {
            return;
        }

        Direccion direccion = direccionDAO.load(pedido.getDireccionEnvio().getId());
        if (direccion != null) {
            pedido.setDireccionEnvio(direccion);
        }
    }

    private void completarDetalles(Pedido pedido) throws SQLException {
        if (pedido.getId() <= 0) {
            return;
        }

        pedido.setDetalles(detallePedidoDAO.listByPedidoId(pedido.getId()));
    }

    private void completarProductos(Pedido pedido) throws SQLException {
        if (pedido.getDetalles() == null) {
            return;
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getProducto() == null || detalle.getProducto().getId() <= 0) {
                continue;
            }

            Producto producto = productoDAO.load(detalle.getProducto().getId());
            if (producto == null) {
                continue;
            }

            completarMarca(producto);
            detalle.setProducto(producto);
        }
    }

    private void completarMarca(Producto producto) throws SQLException {
        if (producto.getMarca() == null || producto.getMarca().getId() <= 0) {
            return;
        }

        Marca marca = marcaDAO.load(producto.getMarca().getId());
        if (marca != null) {
            producto.setMarca(marca);
        }
    }

    private void recalcularTotales(Pedido pedido) {
        double totalProductos = 0.0;
        for (DetallePedido detalle : pedido.getDetalles())
            totalProductos += detalle.calcularSubtotal();
        pedido.setTotal(totalProductos);
        pedido.setIgv(totalProductos * 0.18 / 1.18);
        pedido.setSubtotal(totalProductos / 1.18);
    }
}
