import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Inventario inventario = new Inventario();

        System.out.println("=== APLICACIÓN DE CONTROL DE INVENTARIOS ===");
        int opcion;

        do {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Registrar producto");
            System.out.println("2. Registrar entrada de stock");
            System.out.println("3. Registrar salida de stock");
            System.out.println("4. Ver productos");
            System.out.println("5. Ver movimientos");
            System.out.println("0. Salir");

            System.out.print("Opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.print("ID del producto: ");
                    String id = scanner.nextLine();

                    System.out.print("Nombre del producto: ");
                    String nombre = scanner.nextLine();

                    System.out.print("Nombre de la categoría: ");
                    String nombreCategoria = scanner.nextLine();
                    System.out.print("Descripción de la categoría: ");
                    String descripcionCategoria = scanner.nextLine();
                    Categoria categoria = new Categoria(nombreCategoria, descripcionCategoria);

                    System.out.print("Nombre del proveedor: ");
                    String nombreProveedor = scanner.nextLine();
                    System.out.print("Contacto del proveedor: ");
                    String contactoProveedor = scanner.nextLine();
                    Proveedor proveedor = new Proveedor(nombreProveedor, contactoProveedor);

                    System.out.print("Stock inicial: ");
                    int stock = scanner.nextInt();

                    System.out.print("Precio: ");
                    double precio = scanner.nextDouble();
                    scanner.nextLine(); // limpiar buffer

                    Producto nuevo = new Producto(id, nombre, categoria, proveedor, stock, precio);
                    if (inventario.agregarProducto(nuevo)) {
                        System.out.println("Producto registrado.");
                    } else {
                        System.out.println("El producto ya existe.");
                    }
                    break;

                case 2:
                    System.out.print("ID del producto: ");
                    String idEntrada = scanner.nextLine();
                    Producto pEntrada = buscarProductoPorId(inventario, idEntrada);
                    if (pEntrada == null) {
                        System.out.println("Producto no encontrado.");
                        break;
                    }
                    System.out.print("Cantidad a añadir: ");
                    int cantidadEntrada = scanner.nextInt();
                    scanner.nextLine(); // limpiar buffer
                    System.out.print("Responsable: ");
                    String responsableEntrada = scanner.nextLine();
                    inventario.registrarEntrada(pEntrada, cantidadEntrada, responsableEntrada);
                    break;

                case 3:
                    System.out.print("ID del producto: ");
                    String idSalida = scanner.nextLine();
                    Producto pSalida = buscarProductoPorId(inventario, idSalida);
                    if (pSalida == null) {
                        System.out.println("Producto no encontrado.");
                        break;
                    }
                    System.out.print("Cantidad a retirar: ");
                    int cantidadSalida = scanner.nextInt();
                    scanner.nextLine(); // limpiar buffer
                    System.out.print("Responsable: ");
                    String responsableSalida = scanner.nextLine();
                    if (!inventario.registrarSalida(pSalida, cantidadSalida, responsableSalida)) {
                        System.out.println("Stock insuficiente.");
                    }
                    break;

                case 4:
                    System.out.println("\n=== PRODUCTOS ===");
                    for (Producto prod : inventario.getTodosLosProductos()) {
                        System.out.println(prod.getId() + " - " + prod.getNombre() + " | Stock: " + prod.getStock());
                    }
                    break;

                case 5:
                    System.out.println("\n=== MOVIMIENTOS ===");
                    for (Movimiento mov : inventario.getMovimientos()) {
                        System.out.println(mov);
                    }
                    break;

                case 0:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    private static Producto buscarProductoPorId(Inventario inventario, String id) {
        for (Producto p : inventario.getTodosLosProductos()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
}
