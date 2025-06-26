import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        
        System.out.println("Aplicacion de control de inventario");

        
        Categoria electronica = new Categoria("Electronica", "Dispoitivos Electronicos");
        Proveedor proveedorElectronica = new Proveedor("Empresa Cualquiera", "contacto@proveedor.com");

        
        Producto telefono = new Producto("P001", "Telefono", electronica, proveedorElectronica, 10, 699.99);

        
        System.out.println("Stock inicial del producto: " + telefono.getStock());

        Inventario inventario = new Inventario();
        inventario.registrarEntrada(telefono, 5, "Felipe");
        
        System.out.println("Stock despues de añadir 5 unidades: " + telefono.getStock());

        if (inventario.registrarSalida(telefono, 7, "Manager de bodega")) {
            System.out.println("Stock despues de remover 7 unidades: " + telefono.getStock());
        } else {
            System.out.println("No hay suficiente stock para remover.");
        }

        // Print all movements
        System.out.println("\nMovimientos del inventario:");
        for (Movimiento movimiento : inventario.getMovimientos()) {
            System.out.println(movimiento);
        }
    }
}