import java.io.*;
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
        if (!productos.contains(producto)) {
            System.out.println("El producto no está registrado en el inventario.");
            return;
        }

        producto.AñadirStock(cantidad);
        movimientos.add(new Movimiento(producto, LocalDate.now(), cantidad, "ENTRADA", responsable));
    }

    public boolean registrarSalida(Producto producto, int cantidad, String responsable) {
        if (!productos.contains(producto)) {
            System.out.println("El producto no está registrado en el inventario.");
            return false;
        }

        if (producto.removerStock(cantidad)) {
            movimientos.add(new Movimiento(producto, LocalDate.now(), cantidad, "SALIDA", responsable));
            return true;
        }

        return false;
    }


    public List<Producto> getTodosLosProductos() {
        return productos;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void guardarProductosCSV(String rutaArchivo){
        try(PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))){
            writer.println("id,categoria,descripcionCategoria,proveedor,infoContacto,stock,precio");

            for (Producto p : productos) {
                writer.printf("%s,%s,%s,%s,%s,%s,%d,%.2f%n",
                        p.getId(),
                        p.getNombre(),
                        p.getCategoria().getNombre(),
                        p.getCategoria().getDescripcion(),
                        p.getProveedor().getNombre(),
                        p.getProveedor().getinfoContacto(),
                        p.getStock(),
                        p.getPrecio());
            }
        }
        catch(IOException e){
            System.out.println("Error al guardar los productos");
            
        }
    }

    public void cargarProductosCSV(String rutaArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            reader.readLine();
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",", -1);
                if (partes.length < 8) continue;

                String id = partes[0];
                String nombre = partes[1];
                String nombreCategoria = partes[2];
                String descripcionCategoria = partes[3];
                String nombreProveedor = partes[4];
                String contactoProveedor = partes[5];
                int stock = Integer.parseInt(partes[6]);
                double precio = Double.parseDouble(partes[7]);

                Categoria categoria = new Categoria(nombreCategoria, descripcionCategoria);
                Proveedor proveedor = new Proveedor(nombreProveedor, contactoProveedor);
                Producto producto = new Producto(id, nombre, categoria, proveedor, stock, precio);
            
                agregarProducto(producto);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar productos");
        } catch (NumberFormatException e) {
            System.out.println("Error en el formato de números");
        }
    }
}


