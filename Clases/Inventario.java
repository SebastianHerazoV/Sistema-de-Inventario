import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Inventario {
    //usamos listas porque tienen tamaño dinamico y metodos dedicados que facilitan el trabajo
    private List<Producto> productos;
    private List<Movimiento> movimientos;

    public Inventario() {
        productos = new ArrayList<>();
        movimientos = new ArrayList<>();
    }

    public boolean agregarProducto(Producto producto) {
        if (!productos.contains(producto)) {
            productos.add(producto);
            return true;
        }
        return false;
    }

    public void registrarEntrada(Producto producto, int cantidad, String responsable) {
        producto.agregarInventario(cantidad);
        movimientos.add(new Movimiento(producto, LocalDate.now(), cantidad, "ENTRADA", responsable));
    }

    public boolean registrarSalida(Producto producto, int cantidad, String responsable) {
        if (producto.removerInventario(cantidad)) {
            movimientos.add(new Movimiento(producto, LocalDate.now(), cantidad, "SALIDA", responsable));
            return true;
        }
        return false;
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productos;
    }

    public List<Movimiento> obtenerMovimientos() {
        return movimientos;
    }

}


