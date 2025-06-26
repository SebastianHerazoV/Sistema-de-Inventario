import java.time.LocalDate;

public class Movimiento {
    private Producto producto;
    private LocalDate fecha;
    private int cantidad;
    private String tipoMovimiento; // "ENTRADA" o "SALIDA"
    private String responsable;

    public Movimiento(Producto producto, LocalDate fecha, int cantidad, String tipoMovimiento, String responsable) {
        this.producto = producto;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.tipoMovimiento = tipoMovimiento;
        this.responsable = responsable;
    }

    // Getters
    public Producto getProducto() { return producto; }
    public LocalDate getFecha() { return fecha; }
    public int getCantidad() { return cantidad; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public String getResponsable() { return responsable; }

    @Override
    public String toString() {
        return fecha + " - " + tipoMovimiento + " - " + producto.getNombre() + " x" + cantidad + " por " + responsable;
    }
}

