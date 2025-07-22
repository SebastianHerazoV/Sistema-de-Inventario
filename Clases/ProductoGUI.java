import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductoGUI extends JFrame {
    private Inventario inventario;

    // para el input xdd
    private JTextField idField;
    private JTextField nombreField;
    private JTextField categoriaNombreField;
    private JTextField categoriaDescripcionField;
    private JTextField proveedorNombreField;
    private JTextField proveedorContactoField;
    private JSpinner stockSpinner;
    private JTextField precioField;

    // para la lista de productos
    private JTable productTable;
    private DefaultTableModel productTableModel;

    //para la lista de movimientos
    private JTable movementTable;
    private DefaultTableModel movementTableModel;

    public ProductoGUI(Inventario inventario) {
        this.inventario = inventario;
        setTitle("Gestión de Productos y Movimientos");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setResizeWeight(0.3);

        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = new JTextField(20);
        nombreField = new JTextField(20);
        categoriaNombreField = new JTextField(20);
        categoriaDescripcionField = new JTextField(20);
        proveedorNombreField = new JTextField(20);
        proveedorContactoField = new JTextField(20);
        stockSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
        precioField = new JTextField(20);

        formPanel.add(new JLabel("ID Producto:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Categoría (Nombre):"));
        formPanel.add(categoriaNombreField);
        formPanel.add(new JLabel("Categoría (Descripción):"));
        formPanel.add(categoriaDescripcionField);
        formPanel.add(new JLabel("Proveedor (Nombre):"));
        formPanel.add(proveedorNombreField);
        formPanel.add(new JLabel("Proveedor (Contacto):"));
        formPanel.add(proveedorContactoField);
        formPanel.add(new JLabel("Stock Inicial:"));
        formPanel.add(stockSpinner);
        formPanel.add(new JLabel("Precio:"));
        formPanel.add(precioField);

        JButton registerButton = new JButton("Registrar Producto");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarProducto();
            }
        });

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(registerButton, BorderLayout.SOUTH);

        
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        rightSplitPane.setResizeWeight(0.5);

        
        String[] productColumnNames = {"ID", "Nombre", "Categoría", "Proveedor", "Stock", "Precio"};
        productTableModel = new DefaultTableModel(productColumnNames, 0);
        productTable = new JTable(productTableModel);
        productTable.setFillsViewportHeight(true);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productScrollPane.setBorder(BorderFactory.createTitledBorder("Productos"));

        
        String[] movementColumnNames = {"Fecha", "Tipo", "Producto", "Cantidad", "Responsable"};
        movementTableModel = new DefaultTableModel(movementColumnNames, 0);
        movementTable = new JTable(movementTableModel);
        movementTable.setFillsViewportHeight(true);
        JScrollPane movementScrollPane = new JScrollPane(movementTable);
        movementScrollPane.setBorder(BorderFactory.createTitledBorder("Movimientos"));

        rightSplitPane.setTopComponent(productScrollPane);
        rightSplitPane.setBottomComponent(movementScrollPane);

        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightSplitPane);

        add(mainSplitPane, BorderLayout.CENTER);

        
        cargarProductosEnTabla();
        cargarMovimientosEnTabla();
    }

    private void registrarProducto() {
        String id = idField.getText();
        String nombre = nombreField.getText();
        String categoriaNombre = categoriaNombreField.getText();
        String categoriaDescripcion = categoriaDescripcionField.getText();
        String proveedorNombre = proveedorNombreField.getText();
        String proveedorContacto = proveedorContactoField.getText();
        int stock = (int) stockSpinner.getValue();

        double precio;
        try {
            precio = Double.parseDouble(precioField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un precio válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (id.isEmpty() || nombre.isEmpty() || categoriaNombre.isEmpty() || proveedorNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Categoria categoria = new Categoria(categoriaNombre, categoriaDescripcion);
        Proveedor proveedor = new Proveedor(proveedorNombre, proveedorContacto);
        Producto nuevoProducto = new Producto(id, nombre, categoria, proveedor, stock, precio);

        if (inventario.agregarProducto(nuevoProducto)) {
            JOptionPane.showMessageDialog(this, "Producto registrado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            inventario.guardarProductosCSV("productos.csv");
            clearFields();
            cargarProductosEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "El producto con este ID ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void registrarEntradaGUI() {
        String idEntrada = JOptionPane.showInputDialog(this, "ID del producto para entrada:");
        if (idEntrada == null || idEntrada.trim().isEmpty()) return;

        Producto pEntrada = inventario.getProductoPorId(idEntrada);
        if (pEntrada == null) {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cantidadStr = JOptionPane.showInputDialog(this, "Cantidad a añadir:");
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) return;
        int cantidadEntrada;
        try {
            cantidadEntrada = Integer.parseInt(cantidadStr);
            if (cantidadEntrada <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String responsableEntrada = JOptionPane.showInputDialog(this, "Responsable de la entrada:");
        if (responsableEntrada == null || responsableEntrada.trim().isEmpty()) return;

        inventario.registrarEntrada(pEntrada, cantidadEntrada, responsableEntrada);
        JOptionPane.showMessageDialog(this, "Entrada registrada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        inventario.guardarProductosCSV("productos.csv");
        inventario.guardarMovimientosCSV("movimientos.csv");
        cargarProductosEnTabla();
        cargarMovimientosEnTabla();
    }

    public void registrarSalidaGUI() {
        String idSalida = JOptionPane.showInputDialog(this, "ID del producto para salida:");
        if (idSalida == null || idSalida.trim().isEmpty()) return;

        Producto pSalida = inventario.getProductoPorId(idSalida);
        if (pSalida == null) {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cantidadStr = JOptionPane.showInputDialog(this, "Cantidad a retirar:");
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) return;
        int cantidadSalida;
        try {
            cantidadSalida = Integer.parseInt(cantidadStr);
            if (cantidadSalida <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String responsableSalida = JOptionPane.showInputDialog(this, "Responsable de la salida:");
        if (responsableSalida == null || responsableSalida.trim().isEmpty()) return;

        if (inventario.registrarSalida(pSalida, cantidadSalida, responsableSalida)) {
            JOptionPane.showMessageDialog(this, "Salida registrada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            inventario.guardarProductosCSV("productos.csv");
            inventario.guardarMovimientosCSV("movimientos.csv");
            cargarProductosEnTabla();
            cargarMovimientosEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Stock insuficiente para la salida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void cargarProductosEnTabla() {
        productTableModel.setRowCount(0);
        List<Producto> allProducts = inventario.getTodosLosProductos();
        for (Producto p : allProducts) {
            Object[] rowData = {
                p.getId(),
                p.getNombre(),
                p.getCategoria().getNombre(),
                p.getProveedor().getNombre(),
                p.getStock(),
                String.format("%.2f", p.getPrecio())
            };
            productTableModel.addRow(rowData);
        }
    }

    private void cargarMovimientosEnTabla() {
        movementTableModel.setRowCount(0);
        List<Movimiento> allMovements = inventario.getMovimientos();
        for (Movimiento m : allMovements) {
            Object[] rowData = {
                m.getFecha(),
                m.getTipoMovimiento(),
                m.getProducto().getNombre(),
                m.getCantidad(),
                m.getResponsable()
            };
            movementTableModel.addRow(rowData);
        }
    }

    private void clearFields() {
        idField.setText("");
        nombreField.setText("");
        categoriaNombreField.setText("");
        categoriaDescripcionField.setText("");
        proveedorNombreField.setText("");
        proveedorContactoField.setText("");
        stockSpinner.setValue(0);
        precioField.setText("");
    }

    public static void main(String[] args) {
        Inventario inventario = new Inventario();
        inventario.cargarProductosCSV("productos.csv");
        inventario.cargarMovimientosCSV("movimientos.csv");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final ProductoGUI gui = new ProductoGUI(inventario);
                gui.setVisible(true);

                JButton entradaButton = new JButton("Registrar Entrada");
                entradaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gui.registrarEntradaGUI();
                    }
                });

                JButton salidaButton = new JButton("Registrar Salida");
                salidaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gui.registrarSalidaGUI();
                    }
                });

                JPanel actionButtonsPanel = new JPanel();
                actionButtonsPanel.add(entradaButton);
                actionButtonsPanel.add(salidaButton);

                gui.add(actionButtonsPanel, BorderLayout.NORTH);
                gui.revalidate();
                gui.repaint();
            }
        });
    }
}