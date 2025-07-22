import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class Inventario {
    private Map<String, Producto> productos;
    private List<Movimiento> movimientos;

    public Inventario() {
        productos = new HashMap<>();
        movimientos = new ArrayList<>();
    }

    public boolean agregarProducto(Producto producto) {
        if (!productos.containsKey(producto.getId())) {
            productos.put(producto.getId(), producto);
            return true;
        }
        return false;
    }

    public void registrarEntrada(Producto producto, int cantidad, String responsable) {
        Producto productoExistente = productos.get(producto.getId());
        if (productoExistente == null) {
            System.out.println("El producto no está registrado en el inventario.");
            return;
        }

        productoExistente.AñadirStock(cantidad);
        movimientos.add(new Movimiento(productoExistente, LocalDate.now(), cantidad, "ENTRADA", responsable));
    }

    public boolean registrarSalida(Producto producto, int cantidad, String responsable) {
        Producto productoExistente = productos.get(producto.getId());
        if (productoExistente == null) {
            System.out.println("El producto no está registrado en el inventario.");
            return false;
        }

        if (productoExistente.removerStock(cantidad)) {
            movimientos.add(new Movimiento(productoExistente, LocalDate.now(), cantidad, "SALIDA", responsable));
            return true;
        }

        return false;
    }

    public List<Producto> getTodosLosProductos() {
        return new ArrayList<>(productos.values());
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void guardarProductosCSV(String rutaArchivo){
        try(PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))){
            writer.println("id,nombre,categoria,descripcionCategoria,proveedor,infoContacto,stock,precio");

            for (Producto p : productos.values()) {
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
            reader.readLine(); // Skip header
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

    public void guardarMovimientosCSV(String rutaArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println("fecha,tipoMovimiento,idProducto,nombreProducto,cantidad,responsable");
            for (Movimiento m : movimientos) {
                writer.printf("%s,%s,%s,%s,%d,%s%n",
                        m.getFecha().toString(),
                        m.getTipoMovimiento(),
                        m.getProducto().getId(),
                        m.getProducto().getNombre(),
                        m.getCantidad(),
                        m.getResponsable());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar los movimientos");
        }
    }

    // New method to load movements from CSV
    public void cargarMovimientosCSV(String rutaArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            reader.readLine(); // Skip header
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",", -1);
                // Expected parts: fecha,tipoMovimiento,idProducto,nombreProducto,cantidad,responsable
                if (partes.length < 6) continue;

                LocalDate fecha = LocalDate.parse(partes[0]);
                String tipoMovimiento = partes[1];
                String idProducto = partes[2];
                // String nombreProducto = partes[3]; // Not used to reconstruct, product is found by ID
                int cantidad = Integer.parseInt(partes[4]);
                String responsable = partes[5];

                // Try to find the product by ID
                Producto producto = getProductoPorId(idProducto);
                if (producto != null) {
                    // Only add if the product exists in the inventory
                    movimientos.add(new Movimiento(producto, fecha, cantidad, tipoMovimiento, responsable));
                } else {
                    System.out.println("Advertencia: Producto con ID " + idProducto + " no encontrado para movimiento. Saltando movimiento.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar movimientos");
        } catch (NumberFormatException e) {
            System.out.println("Error en el formato de números al cargar movimientos");
        } catch (Exception e) { // Catch any other parsing errors for LocalDate
            System.out.println("Error al parsear fecha o otros datos en movimientos: " + e.getMessage());
        }
    }


    public Producto getProductoPorId(String id) {
        return productos.get(id);
    }
}