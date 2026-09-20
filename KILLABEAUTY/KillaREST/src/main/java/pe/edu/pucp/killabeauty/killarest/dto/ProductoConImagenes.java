package pe.edu.pucp.killabeauty.killarest.dto;

import pe.edu.pucp.killaBeauty.killaModelo.ImagenProducto;
import pe.edu.pucp.killaBeauty.killaModelo.Producto;

import java.util.List;

public class ProductoConImagenes {

    private Producto producto;
    private List<ImagenProducto> imagenes;

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<ImagenProducto> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenProducto> imagenes) {
        this.imagenes = imagenes;
    }
}
