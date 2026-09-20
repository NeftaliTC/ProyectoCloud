package pe.edu.pucp.killabeauty.killarest.dto;

import pe.edu.pucp.killaBeauty.killaModelo.ImagenProducto;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;

import java.util.List;

public class ProductoCatalogoDTO {
    private int id;
    private String nombre;
    private double precioBase;
    private String categoria;
    private String subcategoria;
    private String marca;
    //    private Boolean esPopular;
    private Boolean activo;
    private List<ImagenProducto> imagenes;

    public ProductoCatalogoDTO() {
    }

    public ProductoCatalogoDTO(Producto producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.precioBase = producto.getPrecioBase();


        this.categoria = (producto.getSubcategoria() != null
                && producto.getSubcategoria().getCategoria() != null
                && producto.getSubcategoria().getCategoria().getDescripcion() != null)
                ? producto.getSubcategoria().getCategoria().getDescripcion()
                : "Sin Categoría";

        this.subcategoria = (producto.getSubcategoria() != null
                && producto.getSubcategoria().getDescripcion() != null)
                ? producto.getSubcategoria().getDescripcion()
                : "Sin Subcategoría";

        this.marca = (producto.getMarca() != null
                && producto.getMarca().getDescripcion() != null)
                ? producto.getMarca().getDescripcion()
                : "Sin Marca";

//        this.esPopular = producto.getPromocion() != null ? producto.getPromocion() : false;
        this.activo = producto.getDisponible() != null ? producto.getDisponible() : false;
        this.imagenes = producto.getImagenes();
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

//    public Boolean getEsPopular() {
//        return esPopular;
//    }
//
//    public void setEsPopular(Boolean esPopular) {
//        this.esPopular = esPopular;
//    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    private int getIdMarca(Producto producto) {
        return producto.getMarca() != null ? producto.getMarca().getId() : 0;
    }

    private int getIdSubcategoria(Producto producto) {
        return producto.getSubcategoria() != null ? producto.getSubcategoria().getId() : 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public List<ImagenProducto> getImagenes() { return imagenes; }

    public void setImagenes(List<ImagenProducto> imagenes) { this.imagenes = imagenes; }
}