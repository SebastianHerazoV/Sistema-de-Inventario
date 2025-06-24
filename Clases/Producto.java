import java.util.Objects;

public class Producto {
    private String id;
    private String nombre;
    private Categoria categoria;
    private Proveedor proveedor;
    private int stock;
    private double precio;

    public Producto(String id, String nombre, Categoria categoria, Proveedor proveedor, int stock, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.stock = stock;
        this.precio = precio;
    }

    // Métodos para modificar el inventario
    public void agregarInventario(int cantidad) {
        if (cantidad > 0) {
            stock += cantidad;
        }
    }

    public boolean removerInventario(int cantidad) {
        if (cantidad > 0 && stock >= cantidad) {
            stock -= cantidad;
            return true;
        }
        return false;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public int getInventario() { return stock; }
    public void setInventario(int inventario) { this.stock = inventario; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

