package pe.edu.pucp.killaBeauty.killaModelo;

public class DetalleCarrito {
    private int id;
    private int cantidad;
    private Producto producto;
    private CarritoDeCompras carritoDeCompras;

    public DetalleCarrito() {
    }

    public DetalleCarrito(int id, int cantidad, Producto producto, CarritoDeCompras carritoDeCompras) {
        this.id = id;
        this.cantidad = cantidad;
        this.producto = producto;
        this.carritoDeCompras = carritoDeCompras;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public CarritoDeCompras getCarritoDeCompras() {
        return carritoDeCompras;
    }

    public void setCarritoDeCompras(CarritoDeCompras carritoDeCompras) {
        this.carritoDeCompras = carritoDeCompras;
    }
}

