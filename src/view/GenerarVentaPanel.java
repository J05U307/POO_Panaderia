package view;

import entity.Cliente;
import entity.DetalleVenta;
import entity.Pedido;
import entity.Producto;
import entity.Usuario;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import service.ClienteService;
import service.DetalleVentaService;
import service.PedidoService;
import service.ProductoService;
import utils.ButtonRenderer;
import utils.ClienteButtonEditor;
import utils.DetalleVentaButtonEditor;
import utils.EliminarDetalleButtonEditor;
import utils.ProductoAgregarButtonEditor;

/**
 *
 * @author josue
 */
public class GenerarVentaPanel extends javax.swing.JPanel {

    private Usuario usuarioLogueado;
    private ProductoService productoService = new ProductoService();
    private ClienteService clienteService = new ClienteService();
    private PedidoService pedidoService = new PedidoService();
    private DetalleVentaService detalleVentaService = new DetalleVentaService();
    private List<Producto> productosActuales;
    private CobrarVentaPanel cobrarVentaPanel;

    public GenerarVentaPanel(Usuario usuarioLogueado, CobrarVentaPanel cobrarVentaPanel) {

        this.usuarioLogueado = usuarioLogueado;
        this.cobrarVentaPanel = cobrarVentaPanel;
        initComponents();
        //Ide de cliente
        labelIDCliente.setVisible(false);

        configurarModeloClientes();
        configurarModeloProductos();
        configurarModeloDetalleVenta();
        configurarTablas();

        // Buscar producto 
        agregarBuscadorProducto();
        // Busacar cliente.
        agregarBuscadorCliente();

    }

    public void crearPedido() {

        DefaultTableModel model = (DefaultTableModel) tablaDetalleVenta.getModel();
        int filas = model.getRowCount();

        // Validar existencia de productos
        if (filas == 0) {
            JOptionPane.showMessageDialog(this,
                    "No se puede crear el pedido: no hay productos agregados.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Total
        String textoTotal = labelTotal.getText().replace("S/", "").trim();
        double totalObtenido = Double.parseDouble(textoTotal.replace(",", "."));

        // Cliente
        Cliente clienteObtenido = null;
        String textoIDCliente = labelIDCliente.getText().trim();

        if (!textoIDCliente.equals(".....")) {
            try {
                int idCliente = Integer.parseInt(textoIDCliente);
                clienteObtenido = clienteService.buscar(idCliente);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "El ID del cliente no es válido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Crear pedido
        boolean creado = pedidoService.agregar(clienteObtenido, totalObtenido, usuarioLogueado);

        if (!creado) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear el pedido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pedido pedido = pedidoService.obtenerUltimo();

        // Crear detalle y actualizar stock
        for (int i = 0; i < filas; i++) {

            int idProducto = Integer.parseInt(model.getValueAt(i, 0).toString());
            double precio = Double.parseDouble(model.getValueAt(i, 2).toString().replace(",", "."));
            int cantidad = Integer.parseInt(model.getValueAt(i, 3).toString());
            double subtotal = Double.parseDouble(model.getValueAt(i, 4).toString().replace(",", "."));

            Producto producto = productoService.buscarPorId(idProducto);

            detalleVentaService.agregar(pedido, producto, cantidad, precio, subtotal);

            // ACTUALIZAR STOCK
            productoService.actualizarStock(idProducto, cantidad);
        }

        JOptionPane.showMessageDialog(this, "Pedido creado correctamente.");
        // llamar a listar pedidos en cobrar ventapanel: 
        cobrarVentaPanel.listarPedidosEnProceso();
        cancelar();
    }

    public void cancelar() {
        txtproducto.setText("");
        txtCliente.setText("");

        ((DefaultTableModel) tablaBuscarProducto.getModel()).setRowCount(0);
        ((DefaultTableModel) tablaBuscarCliente.getModel()).setRowCount(0);
        ((DefaultTableModel) tablaDetalleVenta.getModel()).setRowCount(0);

        setLabelIDCliente(".....");
        setLabelNombreCliente(".....");
        labelTotal.setText(".....");

        tablaBuscarProducto.clearSelection();
        tablaBuscarCliente.clearSelection();
        tablaDetalleVenta.clearSelection();

        productosActuales = new ArrayList<>();
    }

    public void actualizarTotal() {
        double total = 0;

        DefaultTableModel model = (DefaultTableModel) tablaDetalleVenta.getModel();
        int filas = model.getRowCount();

        int columnaSubtotal = 4;

        for (int i = 0; i < filas; i++) {
            total += Double.parseDouble(model.getValueAt(i, columnaSubtotal).toString());
        }

        labelTotal.setText(String.format("S/ %.2f", total));
    }

    public void agregarDetalleVenta(Producto producto) {
        DefaultTableModel model = (DefaultTableModel) tablaDetalleVenta.getModel();

        double precio = producto.getPrecio();

        int cantidad = 1;

        double subtotal = precio * cantidad;

        model.addRow(new Object[]{
            producto.getId(),
            producto.getNombre(),
            precio,
            cantidad,
            subtotal,
            "+",
            "-",
            "Eliminar"
        });
        actualizarTotal();

    }

    // Cliente
    private void agregarBuscadorCliente() {
        txtCliente.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            private void filtrar() {
                String texto = txtCliente.getText().trim();
                List<Cliente> resultados = clienteService.buscarPorNombreODni(texto);

                cargarTablaClientes(resultados);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
        });

    }

    private void cargarTablaClientes(List<Cliente> lista) {
        DefaultTableModel model = (DefaultTableModel) tablaBuscarCliente.getModel();
        model.setRowCount(0);

        for (Cliente cliente : lista) {
            model.addRow(new Object[]{
                cliente.getId(),
                cliente.getNombre() + " " + cliente.getApellido(),
                cliente.getDni(),
                "Seleccionar"
            });
        }
    }

    // Producto
    private void agregarBuscadorProducto() {
        txtproducto.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            private void filtrar() {
                String texto = txtproducto.getText().trim();
                List<Producto> resultados = productoService.buscarPorNombre(texto);
                cargarTablaProductos(resultados);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
        });
    }

    private void cargarTablaProductos(List<Producto> lista) {
        this.productosActuales = lista;
        DefaultTableModel model = (DefaultTableModel) tablaBuscarProducto.getModel();
        model.setRowCount(0);

        for (Producto p : lista) {
            model.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                "Agregar"
            });
        }

        //actualizar editor con la nueva lista
        tablaBuscarProducto.getColumn("Agregar").setCellEditor(
                new ProductoAgregarButtonEditor(productosActuales, this)
        );
    }

    // Confi de tablas: 
    private void configurarModeloClientes() {
        tablaBuscarCliente.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "DNI", "Seleccionar"}
        ));

        estiloTabla(tablaBuscarCliente);
        ocultarColumnaID(tablaBuscarCliente);

        // Ajustar ancho de columnas visibles
        tablaBuscarCliente.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaBuscarCliente.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaBuscarCliente.getColumnModel().getColumn(3).setPreferredWidth(90);
    }

    private void configurarModeloProductos() {
        tablaBuscarProducto.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Producto", "Precio", "Stock", "Agregar"}
        ));

        estiloTabla(tablaBuscarProducto);
        ocultarColumnaID(tablaBuscarProducto);

        tablaBuscarProducto.getColumnModel().getColumn(1).setPreferredWidth(160);
        tablaBuscarProducto.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaBuscarProducto.getColumnModel().getColumn(3).setPreferredWidth(70);
        tablaBuscarProducto.getColumnModel().getColumn(4).setPreferredWidth(90);
    }

    private void configurarModeloDetalleVenta() {
        tablaDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Producto", "Precio", "Cantidad", "Subtotal", "+", "-", "Eliminar"}
        ));

        estiloTabla(tablaDetalleVenta);
        ocultarColumnaID(tablaDetalleVenta);

        tablaDetalleVenta.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaDetalleVenta.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaDetalleVenta.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaDetalleVenta.getColumnModel().getColumn(4).setPreferredWidth(90);
        tablaDetalleVenta.getColumnModel().getColumn(5).setPreferredWidth(50);
        tablaDetalleVenta.getColumnModel().getColumn(6).setPreferredWidth(50);
        tablaDetalleVenta.getColumnModel().getColumn(7).setPreferredWidth(100);
    }

    // Tabla general
    private void estiloTabla(JTable tabla) {
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void ocultarColumnaID(JTable tabla) {
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);
    }

    private void configurarTablas() {

        //Clientes
        tablaBuscarCliente.getColumn("Seleccionar").setCellRenderer(new ButtonRenderer());
        tablaBuscarCliente.getColumn("Seleccionar").setCellEditor(new ClienteButtonEditor(this));
        //Productos
        tablaBuscarProducto.getColumn("Agregar").setCellRenderer(new ButtonRenderer());
        tablaBuscarProducto.getColumn("Agregar").setCellEditor(new ProductoAgregarButtonEditor(productosActuales, this));

        // Detalle venta
        tablaDetalleVenta.getColumn("+").setCellRenderer(new ButtonRenderer());
        tablaDetalleVenta.getColumn("+").setCellEditor(new DetalleVentaButtonEditor(true, this));

        tablaDetalleVenta.getColumn("-").setCellRenderer(new ButtonRenderer());
        tablaDetalleVenta.getColumn("-").setCellEditor(new DetalleVentaButtonEditor(false, this));

        tablaDetalleVenta.getColumn("Eliminar").setCellRenderer(new ButtonRenderer());
        tablaDetalleVenta.getColumn("Eliminar").setCellEditor(new EliminarDetalleButtonEditor(this));

    }

    // GET 
    public JTable getTablaDetalleVenta() {
        return tablaDetalleVenta;
    }

    //Set
    public void setLabelIDCliente(String id) {
        labelIDCliente.setText(id);
    }

    public void setLabelNombreCliente(String nombre) {
        labelNombreCliente.setText(nombre);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtproducto = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDetalleVenta = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnEnviarCajero = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaBuscarProducto = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaBuscarCliente = new javax.swing.JTable();
        txtCliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        labelNombreCliente = new javax.swing.JLabel();
        labelIDCliente = new javax.swing.JLabel();

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jLabel1.setText("Nueva venta");

        jLabel2.setText("Producto");

        tablaDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tablaDetalleVenta);

        jLabel3.setText("Pedido Actual");

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnEnviarCajero.setText("Enviar  al Cajero");
        btnEnviarCajero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarCajeroActionPerformed(evt);
            }
        });

        jLabel4.setText("Cliente");

        tablaBuscarProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tablaBuscarProducto);

        tablaBuscarCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tablaBuscarCliente);

        jLabel5.setText("Total Venta");

        labelTotal.setText(".....");

        jLabel6.setText("Cliente");

        labelNombreCliente.setText(".....");

        labelIDCliente.setText(".....");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEnviarCajero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(labelIDCliente)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel6))
                                        .addComponent(jLabel5))
                                    .addGap(188, 188, 188)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelTotal, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(labelNombreCliente, javax.swing.GroupLayout.Alignment.TRAILING)))))))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtproducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(labelTotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(labelNombreCliente)
                            .addComponent(labelIDCliente))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(btnEnviarCajero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(27, 27, 27))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEnviarCajeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarCajeroActionPerformed
        crearPedido();
    }//GEN-LAST:event_btnEnviarCajeroActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEnviarCajero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel labelIDCliente;
    private javax.swing.JLabel labelNombreCliente;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JTable tablaBuscarCliente;
    private javax.swing.JTable tablaBuscarProducto;
    private javax.swing.JTable tablaDetalleVenta;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtproducto;
    // End of variables declaration//GEN-END:variables
}
