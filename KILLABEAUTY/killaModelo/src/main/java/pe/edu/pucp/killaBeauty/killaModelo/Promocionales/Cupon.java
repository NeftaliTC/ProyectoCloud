package pe.edu.pucp.killaBeauty.killaModelo.Promocionales;
import java.time.LocalDate;

public class Cupon {
    private int id;
    private String codigo;
    private String descripcion;
    private double valorDescuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activo;
    private Double montoMaximoDescuento;
    private Double montoMinimoCompra;
    private TipoDescuento tipoDescuento;
    private Integer maxUsosGenerales;
    private Campana campana;
    // Constructores
    public Cupon() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(double valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Double getMontoMaximoDescuento() {
        return montoMaximoDescuento;
    }

    public void setMontoMaximoDescuento(Double montoMaximoDescuento) {
        this.montoMaximoDescuento = montoMaximoDescuento;
    }

    public Double getMontoMinimoCompra() {
        return montoMinimoCompra;
    }

    public void setMontoMinimoCompra(Double montoMinimoCompra) {
        this.montoMinimoCompra = montoMinimoCompra;
    }

    public TipoDescuento getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(TipoDescuento tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public Integer getMaxUsosGenerales() {
        return maxUsosGenerales;
    }

    public void setMaxUsosGenerales(Integer maxUsosGenerales) {
        this.maxUsosGenerales = maxUsosGenerales;
    }

    public Campana getCampana() {
        return campana;
    }

    public void setCampana(Campana campana) {
        this.campana = campana;
    }
}
