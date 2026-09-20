package pe.edu.pucp.killabeauty.killarest.services.ventas;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.killaBeauty.bl.*;
import pe.edu.pucp.killaBeauty.bl.Impl.*;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.*;
import pe.edu.pucp.killaBeauty.killaModelo.Promocionales.Cupon;
import pe.edu.pucp.killaDAO.ClienteCuponDAO;
import pe.edu.pucp.killaDAO.ProductoDAO;
import pe.edu.pucp.killaDAO.Impl.ClienteCuponDAOImpl;
import pe.edu.pucp.killaDAO.Impl.ProductoDAOImpl;
import pe.edu.pucp.killabeauty.killarest.dto.ErrorDTO;
import pe.edu.pucp.killabeauty.killarest.dto.PedidoCheckoutDTO;
import pe.edu.pucp.killabeauty.killarest.dto.PedidoDespachoDTO;
import pe.edu.pucp.killabeauty.killarest.dto.PedidoDetalleDTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Path("/pedido")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PedidoRS {
    private final PedidoBL pedidoBL = new PedidoBLImpl();
    private final EnvioBL envioBL = new EnvioBLImpl();
    private final CourierBL courierBL = new CourierBLImpl();
    private final PagoBL pagoBL = new PagoBLImpl();
    private final ComprobantePagoBL comprobanteBL = new ComprobantePagoBLImpl();
    private final ClienteCuponDAO clienteCuponDAO = new ClienteCuponDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{8}$");
    private static final Pattern RUC_PATTERN = Pattern.compile("^(10|20)\\d{9}$");
    private static final Random RANDOM = new Random();

    // Construye el DTO completo buscando envio, pago y comprobante del pedido
    private PedidoDetalleDTO construirDTO(Pedido p) throws BusinessLogicException {
        try {
            Envio envio = envioBL.obtenerPorIdPedido(p.getId());

            Pago pago = pagoBL.obtenerPorIdPedido(p.getId());

            ComprobantePago comprobante = null;
            if (pago != null)
                comprobante = comprobanteBL.obtenerPorIdPago(pago.getIdPago());

            return new PedidoDetalleDTO(p, envio, pago, comprobante);
        } catch (Exception ex) {
            throw new BusinessLogicException(ex.getMessage());
        }
    }

    @POST
    @Path("/checkout")
    public Response registrarDesdeCheckout(PedidoCheckoutDTO request) {
        try {
            validarCheckout(request);
            validarCuponDisponibleParaCliente(request.getUsuarioId(), request.getCuponId());

            //Crear pedido
            Pedido pedido = pedidoBL.createFromCart(
                    request.getUsuarioId(),
                    request.getDireccionId(),
                    request.getCuponId(),
                    mapDetallesCarrito(request.getItems())
            );

            boolean pagoExitoso = RANDOM.nextInt(100) < 90;

            //Crear pago
            Pago pago = new Pago();
            pago.setPedido(pedido);
            pago.setMontoPagado(pedido.getTotal() + request.getCostoEnvio() - request.getDescuentoCupon());
            pago.setFechaHoraPago(new java.util.Date());
            pago.setEstado(pagoExitoso);
            pago.setMetodoPago(resolverMetodoPago(request.getMetodoPago()));
            pago = pagoBL.create(pago);

            if (!pagoExitoso) {
                return Response.status(402)
                        .entity(new ErrorDTO("Pago rechazado por la pasarela simulada. Intenta nuevamente o usa otro metodo."))
                        .build();
            }

            // Actualizar total del pedido con el monto real pagado
            pedidoBL.actualizarTotal(pedido.getId(), pago.getMontoPagado());
            pedidoBL.actualizarEstado(pedido.getId(), EstadoPedido.PAGADO);
            pedido.setTotal(pago.getMontoPagado());
            pedido.setEstadoPedido(EstadoPedido.PAGADO);

            descontarStock(pedido);
            registrarUsoCupon(request.getUsuarioId(), request.getCuponId(), pedido);

            //Crear Envio si hay direccion seleccionada
            if (request.getDireccionId() != null && request.getDireccionId() > 0) {
                Courier courier = courierBL.buscarAsignado();
                if (courier != null) {
                    Envio envio = new Envio();
                    envio.setPedido(pedido);
                    envio.setCourier(courier);
                    envio.setCostoEnvio(request.getCostoEnvio());
                    envio.setEstadoEnvio(EstadoEnvio.PENDIENTE);
                    envio.setNumeroSeguimiento(generarNumeroSeguimiento(pedido.getId()));
                    envioBL.create(envio);
                }
            }

            //Crear comprobante
            ComprobantePago comprobante = crearComprobante(request, pago);
            if (comprobante != null)
                comprobanteBL.create(comprobante);

            return Response.status(Response.Status.CREATED)
                    .entity(construirDTO(pedido))
                    .build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(ex.getMessage())).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @POST
    public Response registrar(Pedido pedido) {
        try {
            return Response.status(Response.Status.CREATED)
                    .entity(pedidoBL.create(pedido))
                    .build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @GET
    public Response listar() {
        try {
            List<PedidoDetalleDTO> lista = new ArrayList<>();
            for (Pedido p : pedidoBL.listAll()) lista.add(construirDTO(p));
            return Response.ok(lista).build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Pedido p = pedidoBL.load(id);
            return (p != null) ? Response.ok(construirDTO(p)).build()
                    : Response.status(Response.Status.NOT_FOUND).build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @GET
    @Path("/cliente/{idCliente}")
    public Response listarPorCliente(@PathParam("idCliente") int idCliente) {
        try {
            List<PedidoDetalleDTO> lista = new ArrayList<>();
            for (Pedido p : pedidoBL.listByCliente(idCliente)) lista.add(construirDTO(p));
            return Response.ok(lista).build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Pedido pedido) {
        try {
            pedido.setId(id);
            return Response.ok(pedidoBL.update(pedido)).build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}/cancelar")
    public Response cancelar(@PathParam("id") int id) {
        try {
            return Response.ok(construirDTO(pedidoBL.cancel(id))).build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}/despacho")
    public Response actualizarDespacho(@PathParam("id") int id, PedidoDespachoDTO request) {
        try {
            if (request == null) {
                throw new BusinessLogicException("Debe enviar datos de despacho.");
            }

            Pedido pedido = pedidoBL.load(id);
            if (pedido == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Envio envio = envioBL.obtenerPorIdPedido(id);
            boolean crearEnvio = envio == null;
            if (crearEnvio) {
                envio = new Envio();
                envio.setPedido(pedido);
                envio.setCostoEnvio(0);
            }

            Courier courier = resolverCourierDespacho(envio);
            envio.setCourier(courier);
            envio.setNumeroSeguimiento(resolverNumeroSeguimiento(envio, pedido.getId()));
            envio.setEstadoEnvio(resolverEstadoEnvio(request.getEstadoEnvio()));
            envio.setFechaEnvio(parseFecha(request.getFechaEntrega()));
            envio.setDescripcion(request.getDescripcion());

            if (crearEnvio) {
                envioBL.create(envio);
            } else {
                envioBL.update(envio);
            }

            pedidoBL.actualizarEstado(id, resolverEstadoPedidoDesdeEnvio(envio.getEstadoEnvio()));
            return Response.ok(construirDTO(pedidoBL.load(id))).build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            Pedido p = pedidoBL.load(id);
            if (p == null) return Response.status(Response.Status.NOT_FOUND).build();
            pedidoBL.remove(p);
            return Response.noContent().build();
        } catch (BusinessLogicException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO(ex.getMessage())).build();
        }
    }

    private List<DetallePedido> mapDetallesCarrito(List<PedidoCheckoutDTO.ItemCarritoDTO> items) {
        List<DetallePedido> detalles = new ArrayList<>();

        for (PedidoCheckoutDTO.ItemCarritoDTO item : items) {
            DetallePedido detalle = new DetallePedido();
            Producto producto = new Producto();
            producto.setId(item.getProductoId());
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioAplicado(item.getPrecioAplicado());
            detalles.add(detalle);
        }
        return detalles;
    }

    private MetodoPago resolverMetodoPago(String metodoPago) {
        if (metodoPago == null) return MetodoPago.TARJETA;
        return switch (metodoPago.toUpperCase()) {
            case "YAPE_PLIN" -> MetodoPago.YAPE_PLIN;
            case "TARJETA" -> MetodoPago.TARJETA;
            default -> MetodoPago.TARJETA;
        };
    }

    private String generarNumeroSeguimiento(int idPedido) {
        return String.format("KIL-%d-%06d",
                java.time.LocalDate.now().getYear(), idPedido);
    }

    private ComprobantePago crearComprobante(PedidoCheckoutDTO request, Pago pago) {
        if (request.getTipoComprobante() == null) return null;

        if (request.getTipoComprobante().equalsIgnoreCase("BOLETA")) {
            Boleta b = new Boleta();
            b.setPago(pago);
            b.setFechaEmision(new java.util.Date());
            b.setSerie("B001");
            b.setNumeroCorrelativo(String.format("%08d", pago.getIdPago()));
            b.setTipoComprobante(TipoComprobante.getById(1));
            b.setDni(request.getDni());
            return b;
        } else if (request.getTipoComprobante().equalsIgnoreCase("FACTURA")) {
            Factura f = new Factura();
            f.setPago(pago);
            f.setFechaEmision(new java.util.Date());
            f.setSerie("F001");
            f.setNumeroCorrelativo(String.format("%08d", pago.getIdPago()));
            f.setTipoComprobante(TipoComprobante.getById(2));
            f.setRuc(request.getRuc());
            f.setRazonSocial(request.getRazonSocial());
            f.setDireccionFiscal(request.getDireccionFiscal());
            return f;
        }
        return null;
    }

    private void validarCuponDisponibleParaCliente(Integer usuarioId, Integer cuponId) throws Exception {
        if (cuponId == null || cuponId <= 0) {
            return;
        }

        for (ClienteCupon uso : clienteCuponDAO.listByUsuarioId(usuarioId)) {
            if (uso.getCupon() != null
                    && uso.getCupon().getId() == cuponId
                    && Boolean.TRUE.equals(uso.getUsado())) {
                throw new BusinessLogicException("Este cupon ya fue usado por el cliente.");
            }
        }
    }

    private void registrarUsoCupon(Integer usuarioId, Integer cuponId, Pedido pedido) throws Exception {
        if (cuponId == null || cuponId <= 0) {
            return;
        }

        ClienteCupon uso = new ClienteCupon();
        uso.setFechaUso(new Date());
        uso.setUsado(true);
        uso.setPedido(pedido);

        Cupon cupon = new Cupon();
        cupon.setId(cuponId);
        uso.setCupon(cupon);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        uso.setUsuario(usuario);

        clienteCuponDAO.save(uso);
    }

    private void descontarStock(Pedido pedido) throws Exception {
        if (pedido.getDetalles() == null) {
            return;
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            productoDAO.descontarStock(detalle.getProducto().getId(), detalle.getCantidad());
        }
    }

    private Courier resolverCourierDespacho(Envio envioActual) throws BusinessLogicException {
        try {
            if (envioActual != null && envioActual.getCourier() != null && envioActual.getCourier().getId() > 0) {
                return envioActual.getCourier();
            }
            Courier asignado = courierBL.buscarAsignado();
            if (asignado != null) {
                return asignado;
            }
            throw new BusinessLogicException("No existe un courier asignado para el despacho.");
        } catch (BusinessLogicException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessLogicException("No se pudo resolver el courier: " + ex.getMessage());
        }
    }

    private EstadoEnvio resolverEstadoEnvio(String estado) throws BusinessLogicException {
        if (estado == null || estado.trim().isEmpty()) {
            return EstadoEnvio.PENDIENTE;
        }
        String normalizado = estado.trim().toUpperCase();
        for (EstadoEnvio estadoEnvio : EstadoEnvio.values()) {
            if (estadoEnvio.name().equals(normalizado) || estadoEnvio.getNombre().equals(normalizado)) {
                return estadoEnvio;
            }
        }
        throw new BusinessLogicException("El estado de envio no es valido.");
    }

    private EstadoPedido resolverEstadoPedidoDesdeEnvio(EstadoEnvio estadoEnvio) {
        return switch (estadoEnvio) {
            case EN_CAMINO -> EstadoPedido.ENVIADO;
            case ENTREGADO -> EstadoPedido.ENTREGADO;
            default -> EstadoPedido.EN_PREPARACION;
        };
    }

    private String resolverNumeroSeguimiento(Envio envio, int idPedido) {
        if (envio != null
                && envio.getNumeroSeguimiento() != null
                && !envio.getNumeroSeguimiento().trim().isEmpty()) {
            return envio.getNumeroSeguimiento().trim();
        }
        return generarNumeroSeguimiento(idPedido);
    }

    private Date parseFecha(String fecha) throws BusinessLogicException {
        if (fecha == null || fecha.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDate localDate = LocalDate.parse(fecha.trim());
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception ex) {
            throw new BusinessLogicException("La fecha de entrega debe tener formato yyyy-MM-dd.");
        }
    }

    private void validarCheckout(PedidoCheckoutDTO request) throws BusinessLogicException {
        if (request == null) {
            throw new BusinessLogicException("La solicitud de checkout no puede ser nula.");
        }
        //El checkout siempre necesita saber qué usuario está comprando. no puede ser nulo
        if (request.getUsuarioId() == null || request.getUsuarioId() <= 0) {
            throw new BusinessLogicException("El usuario del checkout debe ser valido.");
        }
        // si no es nulo debe ser válido (puede ser nulo)
        if (request.getDireccionId() != null && request.getDireccionId() < 0) {
            throw new BusinessLogicException("La direccion seleccionada debe ser valida.");
        }
        if (request.getCuponId() != null && request.getCuponId() <= 0) {
            throw new BusinessLogicException("El cupon seleccionado debe ser valido.");
        }
        if (request.getCostoEnvio() < 0) {
            throw new BusinessLogicException("El costo de envio no puede ser negativo.");
        }
        if (request.getDescuentoCupon() < 0) {
            throw new BusinessLogicException("El descuento del cupon no puede ser negativo.");
        }
        validarItemsCheckout(request.getItems());
        validarMetodoPago(request);
        validarComprobanteCheckout(request);
    }

    private void validarItemsCheckout(List<PedidoCheckoutDTO.ItemCarritoDTO> items) throws BusinessLogicException {
        if (items == null || items.isEmpty()) {
            throw new BusinessLogicException("El pedido debe tener al menos un producto.");
        }
        for (PedidoCheckoutDTO.ItemCarritoDTO item : items) {
            if (item == null) {
                throw new BusinessLogicException("El pedido contiene un item invalido.");
            }
            if (item.getProductoId() == null || item.getProductoId() <= 0) {
                throw new BusinessLogicException("Cada item debe tener un producto valido.");
            }
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new BusinessLogicException("La cantidad de cada item debe ser mayor a 0.");
            }
            if (item.getPrecioAplicado() <= 0) {
                throw new BusinessLogicException("El precio aplicado de cada item debe ser mayor a 0.");
            }
        }
    }

    private void validarMetodoPago(PedidoCheckoutDTO request) throws BusinessLogicException {
        if (request.getMetodoPago() == null || request.getMetodoPago().trim().isEmpty()) {
            request.setMetodoPago("TARJETA");
            return;
        }

        String metodoPago = request.getMetodoPago().trim().toUpperCase();
        if (!metodoPago.equals("TARJETA")
                && !metodoPago.equals("YAPE_PLIN")) {
            throw new BusinessLogicException("El metodo de pago seleccionado no es valido.");
        }
        request.setMetodoPago(metodoPago);
    }

    private void validarComprobanteCheckout(PedidoCheckoutDTO request) throws BusinessLogicException {
        if (request.getTipoComprobante() == null || request.getTipoComprobante().trim().isEmpty()) {
            request.setTipoComprobante(null);
            return;
        }

        String tipoComprobante = request.getTipoComprobante().trim().toUpperCase();
        request.setTipoComprobante(tipoComprobante);

        if (tipoComprobante.equals("BOLETA")) {
            if (request.getDni() == null || !DNI_PATTERN.matcher(request.getDni().trim()).matches()) {
                throw new BusinessLogicException("Para boleta debe ingresar un DNI de 8 digitos.");
            }
            request.setDni(request.getDni().trim());
            return;
        }

        if (tipoComprobante.equals("FACTURA")) {
            if (request.getRuc() == null || !RUC_PATTERN.matcher(request.getRuc().trim()).matches()) {
                throw new BusinessLogicException("Para factura debe ingresar un RUC de 11 digitos que empiece con 10 o 20.");
            }
            if (request.getRazonSocial() == null || request.getRazonSocial().trim().isEmpty()) {
                throw new BusinessLogicException("La razon social es obligatoria para factura.");
            }
            if (request.getDireccionFiscal() == null || request.getDireccionFiscal().trim().isEmpty()) {
                throw new BusinessLogicException("La direccion fiscal es obligatoria para factura.");
            }
            request.setRuc(request.getRuc().trim());
            request.setRazonSocial(request.getRazonSocial().trim());
            request.setDireccionFiscal(request.getDireccionFiscal().trim());
            return;
        }

        throw new BusinessLogicException("El tipo de comprobante debe ser BOLETA o FACTURA.");
    }
}
