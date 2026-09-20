package pe.edu.pucp.killaDAO.Impl;

import pe.edu.pucp.dbManager.DBManager;
import pe.edu.pucp.dbManager.TransactionContext;
import pe.edu.pucp.killaBeauty.killaModelo.Boleta;
import pe.edu.pucp.killaBeauty.killaModelo.ComprobantePago;
import pe.edu.pucp.killaBeauty.killaModelo.Factura;
import pe.edu.pucp.killaBeauty.killaModelo.Pago;
import pe.edu.pucp.killaBeauty.killaModelo.TipoComprobante;
import pe.edu.pucp.killaDAO.ComprobantePagoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComprobantePagoDAOImpl implements ComprobantePagoDAO {
    @Override
    public List<ComprobantePago> listAll() throws SQLException {
        List<ComprobantePago> lista = new ArrayList<>();
        String sql = """
                SELECT id_comprobante, fecha_emision, serie, numero_correlativo, id_pago, id_tipo_comprobante 
                FROM ComprobantePago
                """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    @Override
    public ComprobantePago buscarPorIdPago(Integer idPago) throws SQLException {
        String sql = """
            SELECT cp.id_comprobante, cp.fecha_emision, cp.serie, cp.numero_correlativo,
                   cp.id_pago, cp.id_tipo_comprobante,
                   b.dni,
                   f.ruc, f.razon_social, f.direccion_fiscal
            FROM ComprobantePago cp
            LEFT JOIN Boleta b ON cp.id_comprobante = b.id_comprobante
            LEFT JOIN Factura f ON cp.id_comprobante = f.id_comprobante
            WHERE cp.id_pago = ?
            """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPago);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dni = rs.getString("dni");
                    String ruc = rs.getString("ruc");

                    if (dni != null) {
                        Boleta b = new Boleta();
                        b.setIdComprobante(rs.getInt("id_comprobante"));
                        b.setFechaEmision(rs.getTimestamp("fecha_emision"));
                        b.setSerie(rs.getString("serie"));
                        b.setNumeroCorrelativo(rs.getString("numero_correlativo"));
                        b.setTipoComprobante(TipoComprobante.getById(rs.getInt("id_tipo_comprobante")));
                        Pago pago = new Pago();
                        pago.setIdPago(rs.getInt("id_pago"));
                        b.setPago(pago);
                        b.setDni(dni);
                        return b;
                    } else if (ruc != null) {
                        Factura f = new Factura();
                        f.setIdComprobante(rs.getInt("id_comprobante"));
                        f.setFechaEmision(rs.getTimestamp("fecha_emision"));
                        f.setSerie(rs.getString("serie"));
                        f.setNumeroCorrelativo(rs.getString("numero_correlativo"));
                        f.setTipoComprobante(TipoComprobante.getById(rs.getInt("id_tipo_comprobante")));
                        Pago pago = new Pago();
                        pago.setIdPago(rs.getInt("id_pago"));
                        f.setPago(pago);
                        f.setRuc(ruc);
                        f.setRazonSocial(rs.getString("razon_social"));
                        f.setDireccionFiscal(rs.getString("direccion_fiscal"));
                        return f;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ComprobantePago load(Integer id) throws SQLException {
        String sql = """
                SELECT id_comprobante, fecha_emision, serie, numero_correlativo, id_pago, id_tipo_comprobante 
                FROM ComprobantePago WHERE id_comprobante = ?
                """;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public ComprobantePago save(ComprobantePago c) throws SQLException {
        String sql = """
                INSERT INTO ComprobantePago (fecha_emision, serie, numero_correlativo, id_pago, id_tipo_comprobante)
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection con = TransactionContext.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new Timestamp(c.getFechaEmision().getTime()));
            ps.setString(2, c.getSerie());
            ps.setString(3, c.getNumeroCorrelativo());
            ps.setInt(4, c.getPago().getIdPago());
            ps.setInt(5, c.getTipoComprobante().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setIdComprobante(rs.getInt(1));
            }

            // Inserción en tablas hijas usando la misma conexión de transacción
            if (c instanceof Boleta) {
                Boleta b = (Boleta) c;
                try (PreparedStatement psB = con.prepareStatement("INSERT INTO Boleta(dni, id_comprobante) VALUES(?,?)")) {
                    psB.setString(1, b.getDni());
                    psB.setInt(2, c.getIdComprobante());
                    psB.executeUpdate();
                }
            } else if (c instanceof Factura) {
                Factura f = (Factura) c;
                try (PreparedStatement psF = con.prepareStatement("INSERT INTO Factura(ruc, razon_social, direccion_fiscal, id_comprobante) VALUES(?,?,?,?)")) {
                    psF.setString(1, f.getRuc());
                    psF.setString(2, f.getRazonSocial());
                    psF.setString(3, f.getDireccionFiscal());
                    psF.setInt(4, c.getIdComprobante());
                    psF.executeUpdate();
                }
            }
        }
        return c;
    }

    @Override
    public ComprobantePago update(ComprobantePago comprobantePago) throws SQLException {
        throw new UnsupportedOperationException("No se permite modificar comprobantes emitidos.");
    }

    @Override
    public void remove(ComprobantePago comprobantePago) throws SQLException {
        throw new UnsupportedOperationException("No se permite eliminar comprobantes emitidos.");
    }

    private ComprobantePago mapRow(ResultSet rs) throws SQLException {
        ComprobantePago c = new ComprobantePago();
        c.setIdComprobante(rs.getInt("id_comprobante"));
        c.setFechaEmision(rs.getTimestamp("fecha_emision"));
        c.setSerie(rs.getString("serie"));
        c.setNumeroCorrelativo(rs.getString("numero_correlativo"));
        Pago p = new Pago();
        p.setIdPago(rs.getInt("id_pago"));
        c.setPago(p);
        c.setTipoComprobante(TipoComprobante.getById(rs.getInt("id_tipo_comprobante")));
        return c;
    }
}
