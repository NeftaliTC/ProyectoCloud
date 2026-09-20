package pe.edu.pucp.killaBeauty.bl.Impl;

import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.bl.ComprobantePagoBL;
import pe.edu.pucp.killaBeauty.bl.exception.BusinessLogicException;
import pe.edu.pucp.killaBeauty.killaModelo.Boleta;
import pe.edu.pucp.killaBeauty.killaModelo.ComprobantePago;
import pe.edu.pucp.killaBeauty.killaModelo.Factura;
import pe.edu.pucp.killaBeauty.killaModelo.TipoComprobante;
import pe.edu.pucp.killaDAO.ComprobantePagoDAO;
import pe.edu.pucp.killaDAO.Impl.ComprobantePagoDAOImpl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class ComprobantePagoBLImpl implements ComprobantePagoBL {

    private ComprobantePagoDAO comprobanteDAO = new ComprobantePagoDAOImpl();

    private static final Pattern SERIE_PATTERN = Pattern.compile("^[BF]\\d{3}$");
    private static final Pattern NUMERO_CORRELATIVO_PATTERN = Pattern.compile("^\\d{8}$");
    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{8}$");
    private static final Pattern RUC_PATTERN = Pattern.compile("^(10|20)\\d{9}$");

    @Override
    public ComprobantePago create(ComprobantePago c) throws BusinessLogicException {
        validarComprobante(c);
        try {
            // Iniciamos transacción para asegurar consistencia entre tablas
            TransactionContext.getConnection();
            ComprobantePago comprobanteGuardado = comprobanteDAO.save(c);
            TransactionContext.commit();
            return comprobanteGuardado;
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new BusinessLogicException("Error critico al emitir el comprobante: " + e.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ComprobantePago load(Integer id) throws BusinessLogicException {
        if (id == null || id <= 0) {
            throw new BusinessLogicException("El id del comprobante debe ser valido.");
        }
        try {
            return comprobanteDAO.load(id);
        } catch (SQLException e) {
            throw new BusinessLogicException("Error al cargar el comprobante: " + e.getMessage());
        }
    }

    @Override
    public List<ComprobantePago> listAll() throws BusinessLogicException {
        try {
            return comprobanteDAO.listAll();
        } catch (SQLException e) {
            throw new BusinessLogicException("Error al listar los comprobantes: " + e.getMessage());
        }
    }

    @Override
    public ComprobantePago obtenerPorIdPago(Integer idPago) throws BusinessLogicException {
        if (idPago == null || idPago <= 0) {
            throw new BusinessLogicException("El id del pago debe ser valido.");
        }
        try {
            return comprobanteDAO.buscarPorIdPago(idPago);
        } catch (Exception ex) {
            throw new BusinessLogicException("Error al obtener comprobante: " + ex.getMessage());
        }
    }

    private void validarComprobante(ComprobantePago c) throws BusinessLogicException {
        if (c == null) {
            throw new BusinessLogicException("El comprobante no puede ser nulo.");
        }
        if (!(c instanceof Boleta) && !(c instanceof Factura)) {
            throw new BusinessLogicException("El comprobante debe ser una boleta o una factura.");
        }
        if (c.getPago() == null || c.getPago().getIdPago() <= 0) {
            throw new BusinessLogicException("El comprobante debe estar asociado a un pago valido.");
        }
        if (c.getFechaEmision() == null) {
            c.setFechaEmision(new Date());
        }
        if (esVacio(c.getSerie()) || !SERIE_PATTERN.matcher(c.getSerie().trim().toUpperCase()).matches()) {
            throw new BusinessLogicException("La serie del comprobante debe tener formato B001 o F001.");
        }
        c.setSerie(c.getSerie().trim().toUpperCase());

        if (esVacio(c.getNumeroCorrelativo()) || !NUMERO_CORRELATIVO_PATTERN.matcher(c.getNumeroCorrelativo().trim()).matches()) {
            throw new BusinessLogicException("El numero correlativo debe contener 8 digitos.");
        }
        c.setNumeroCorrelativo(c.getNumeroCorrelativo().trim());

        if (c instanceof Boleta) {
            validarBoleta((Boleta) c);
        } else {
            validarFactura((Factura) c);
        }
    }

    private void validarBoleta(Boleta boleta) throws BusinessLogicException {
        if (boleta.getTipoComprobante() == null) {
            boleta.setTipoComprobante(TipoComprobante.BOLETA);
        }
        if (boleta.getTipoComprobante() != TipoComprobante.BOLETA) {
            throw new BusinessLogicException("El tipo de comprobante no corresponde a una boleta.");
        }
        if (esVacio(boleta.getDni()) || !DNI_PATTERN.matcher(boleta.getDni().trim()).matches()) {
            throw new BusinessLogicException("El DNI de la boleta debe contener 8 digitos numericos.");
        }
        boleta.setDni(boleta.getDni().trim());
    }

    private void validarFactura(Factura factura) throws BusinessLogicException {
        if (factura.getTipoComprobante() == null) {
            factura.setTipoComprobante(TipoComprobante.FACTURA);
        }
        if (factura.getTipoComprobante() != TipoComprobante.FACTURA) {
            throw new BusinessLogicException("El tipo de comprobante no corresponde a una factura.");
        }
        if (esVacio(factura.getRuc()) || !RUC_PATTERN.matcher(factura.getRuc().trim()).matches()) {
            throw new BusinessLogicException("El RUC debe contener 11 digitos y empezar con 10 o 20.");
        }
        if (esVacio(factura.getRazonSocial())) {
            throw new BusinessLogicException("La razon social es obligatoria para factura.");
        }
        if (esVacio(factura.getDireccionFiscal())) {
            throw new BusinessLogicException("La direccion fiscal es obligatoria para factura.");
        }
        factura.setRuc(factura.getRuc().trim());
        factura.setRazonSocial(factura.getRazonSocial().trim());
        factura.setDireccionFiscal(factura.getDireccionFiscal().trim());
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
