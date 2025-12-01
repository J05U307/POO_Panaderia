/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import entity.Categoria;
import entity.Comprobante;
import entity.DetalleVenta;
import entity.Factura;
import entity.Pedido;
import entity.TipoPago;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import service.ComprobanteService;
import service.DetalleVentaService;
import service.PedidoService;
import service.ProductoService;
import service.TipoPagoService;
import utils.GenerarComprobante;
import utils.PedidoEstadoRenderer;

/**
 *
 * @author josue
 */
public class CobrarVentaPanel extends javax.swing.JPanel {

    private TipoPagoService tipoPagoService = new TipoPagoService();
    private PedidoService pedidoService = new PedidoService();
    private ProductoService productoService = new ProductoService();
    private ComprobanteService comprobanteService = new ComprobanteService();
    private DetalleVentaService detalleVentaService = new DetalleVentaService();
    private GenerarComprobante generardorComprobante = new GenerarComprobante();

    public CobrarVentaPanel() {
        initComponents();
        checboxImprimir.setSelected(true);
        configurarTablaPedidos();
        configurarTablaDetallePedido();
        
        if (tablaDetallePedido.getColumnModel().getColumnCount() > 1) {
            tablaDetallePedido.getColumnModel().getColumn(0).setMinWidth(0);
            tablaDetallePedido.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaDetallePedido.getColumnModel().getColumn(0).setWidth(0);
        }
        cargarCompoTipoPago();
        cargarComboComprobante();
        listarPedidosEnProceso();

    }

    private void anularPedido() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un pedido.");
            return;
        }

        int idPedido = (int) tablaPedidos.getValueAt(fila, 0);
        Pedido pedido = pedidoService.buscar(idPedido);

        if (pedido == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se pudo cargar el pedido.");
            return;
        }

        // CONFIRMACIÓN
        int confirmar = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea ANULAR el pedido?",
                "Confirmar",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirmar != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        //cambiar estado del pedido
        pedidoService.cambiarEstadoAnulado(idPedido);

        // obtener todos los detalles del pedido
        List<DetalleVenta> detalles = detalleVentaService.listarPorPedido(idPedido);

        // 3. Devolver stock por cada producto
        for (DetalleVenta det : detalles) {
            int idProducto = det.getProducto().getId();
            int cantidad = det.getCantidad();

            productoService.devolverStockProducto(idProducto, cantidad);
        }

        javax.swing.JOptionPane.showMessageDialog(this, "Pedido anulado correctamente.");

        // 4. Limpiar e listar nuevamente
        limpiar();
        listarPedidosEnProceso();
    }

    private void crearComprobante() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un pedido.");
            return;
        }

        int idPedido = (int) tablaPedidos.getValueAt(fila, 0);
        Pedido pedido = pedidoService.buscar(idPedido);

        if (pedido == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se pudo cargar el pedido.");
            return;
        }

        // Tipo de pago
        int indexTipoPago = comboTipoPago.getSelectedIndex();
        if (indexTipoPago <= 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un tipo de pago.");
            return;
        }
        TipoPago tipoPago = tipoPagoService.listar().get(indexTipoPago - 1);
        // Comprobante
        String tipoComprobante = comboComprobante.getSelectedItem().toString();

        if (tipoComprobante.equals("Boleta")) {

            // DNI viene del cliente del pedido
            String dni = "";
            if (pedido.getCliente() != null) {
                dni = pedido.getCliente().getDni();
            }

            comprobanteService.crearBoleta(pedido, tipoPago, dni);

            javax.swing.JOptionPane.showMessageDialog(this, "Boleta generada correctamente");

        } else if (tipoComprobante.equals("Factura")) {

            // Abrir ventana pequeña para RUC y Razón Social
            FormFacturaDialog dialog = new FormFacturaDialog();
            dialog.setVisible(true);

            if (!dialog.isAceptado()) {
                return; // usuario canceló
            }

            String ruc = dialog.getRuc();
            String razon = dialog.getRazonSocial();

            comprobanteService.crearFactura(pedido, tipoPago, ruc, razon);

            javax.swing.JOptionPane.showMessageDialog(this, "Factura generada correctamente");

        }

        // Actualizar estado del pedido
        pedidoService.cambiarEstadoPagado(idPedido);
        limpiar();
        listarPedidosEnProceso();

        //Imprimir
        if (checboxImprimir.isSelected()) {
            imprimirComprovante(idPedido);
        } else {
            return;
        }

    }

    private void imprimirComprovante(int idPedido) {
        String ruta = generardorComprobante.generarComprobantePDF(idPedido);
        if (ruta.startsWith("ERROR")) {
            JOptionPane.showMessageDialog(this, ruta, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            java.awt.Desktop.getDesktop().open(new File(ruta));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "PDF generado: " + ruta, "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiar() {
        labelNombreCliente.setText(".....");
        labelTotal.setText(".....");

        ((DefaultTableModel) tablaPedidos.getModel()).setRowCount(0);
        ((DefaultTableModel) tablaDetallePedido.getModel()).setRowCount(0);
        tablaPedidos.clearSelection();
        tablaDetallePedido.clearSelection();

        comboTipoPago.setSelectedIndex(0);
        comboComprobante.setSelectedIndex(0);
    }

    private void datosPedidoLabel() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila == -1) {
            return;
        }
        // Cliente
        String nombreCliente = (String) tablaPedidos.getValueAt(fila, 2);
        labelNombreCliente.setText(nombreCliente);

        // Total
        Object totalObj = tablaPedidos.getValueAt(fila, 5);
        double total = (totalObj instanceof Double)
                ? (double) totalObj
                : Double.parseDouble(totalObj.toString());
        labelTotal.setText(String.format("S/ %.2f", total));
    }

    private void listarDetallePedido() {

        int fila = tablaPedidos.getSelectedRow();
        if (fila == -1) {
            return; // no seleccionado
        }

        int idPedido = (int) tablaPedidos.getValueAt(fila, 0);

        DefaultTableModel modelo = (DefaultTableModel) tablaDetallePedido.getModel();
        modelo.setRowCount(0);

        for (DetalleVenta de : detalleVentaService.listarPorPedido(idPedido)) {
            modelo.addRow(new Object[]{
                de.getId(),
                de.getProducto().getNombre(),
                de.getCantidad(),
                de.getPrecioVendido(),
                de.getSubtotal()
            });
        }

        if (tablaDetallePedido.getColumnModel().getColumnCount() > 1) {
            tablaDetallePedido.getColumnModel().getColumn(0).setMinWidth(0);
            tablaDetallePedido.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaDetallePedido.getColumnModel().getColumn(0).setWidth(0);
        }
    }

    public void listarPedidosEnProceso() {
        DefaultTableModel modelo = (DefaultTableModel) tablaPedidos.getModel();
        modelo.setRowCount(0);

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("h:mm a");

        for (Pedido pedido : pedidoService.listarPedidosEnProceso()) {

            String nombreCliente = (pedido.getCliente() != null)
                    ? pedido.getCliente().getNombre()
                    : "Sin cliente";

            String nombreEmpleado = "Desconocido";
            if (pedido.getUsuario() != null && pedido.getUsuario().getEmpleado() != null) {
                nombreEmpleado = pedido.getUsuario().getEmpleado().getNombre();
            }

            // 👉 Obtener fecha y hora por separado
            String fecha = pedido.getFecha().format(formatoFecha);
            String hora = pedido.getFecha().format(formatoHora);

            modelo.addRow(new Object[]{
                pedido.getId(),
                nombreEmpleado,
                nombreCliente,
                fecha,
                hora,
                pedido.getTotal(),
                pedido.getEstado()
            });
        }

        if (tablaPedidos.getColumnModel().getColumnCount() > 1) {
            tablaPedidos.getColumnModel().getColumn(0).setMinWidth(0);
            tablaPedidos.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaPedidos.getColumnModel().getColumn(0).setWidth(0);
        }
    }

    public void cargarCompoTipoPago() {
        comboTipoPago.removeAllItems();
        comboTipoPago.addItem("Seleccione un tipo pago");
        for (TipoPago c : tipoPagoService.listar()) {
            comboTipoPago.addItem(c.getNombre());
        }
    }

    private void cargarComboComprobante() {
        comboComprobante.removeAllItems();
        comboComprobante.addItem("Boleta");
        comboComprobante.addItem("Factura");
    }

    private void configurarTablaPedidos() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Usuario", "Cliente", "Fecha", "Hora", "Total", "Estado"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas no editables
            }
        };

        tablaPedidos.setModel(modelo);

        // Ajustes opcionales de tamaño
        tablaPedidos.getColumnModel().getColumn(0).setPreferredWidth(40); // ID
        tablaPedidos.getColumnModel().getColumn(1).setPreferredWidth(120); // Usuario
        tablaPedidos.getColumnModel().getColumn(2).setPreferredWidth(120); // Cliente
        tablaPedidos.getColumnModel().getColumn(3).setPreferredWidth(85); // Fecha
        tablaPedidos.getColumnModel().getColumn(4).setPreferredWidth(70); // Hora
        tablaPedidos.getColumnModel().getColumn(5).setPreferredWidth(60); // Total
        tablaPedidos.getColumnModel().getColumn(6).setPreferredWidth(70); // Estado

        PedidoEstadoRenderer renderer = new PedidoEstadoRenderer();
        for (int i = 0; i < tablaPedidos.getColumnCount(); i++) {
            tablaPedidos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void configurarTablaDetallePedido() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "IdDetVenta", "Producto", "Cantidad", "Precio Uni.", "Subtotal"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editables
            }
        };

        tablaDetallePedido.setModel(modelo);

        // Ajustes de tamaño
        tablaDetallePedido.getColumnModel().getColumn(0).setPreferredWidth(70);
        tablaDetallePedido.getColumnModel().getColumn(1).setPreferredWidth(180);
        tablaDetallePedido.getColumnModel().getColumn(2).setPreferredWidth(60);
        tablaDetallePedido.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaDetallePedido.getColumnModel().getColumn(4).setPreferredWidth(80);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPedidos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDetallePedido = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        comboTipoPago = new javax.swing.JComboBox<>();
        btnPagar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        labelNombreCliente = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        comboComprobante = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();
        tbnAnular = new javax.swing.JButton();
        checboxImprimir = new javax.swing.JCheckBox();

        jToggleButton1.setText("jToggleButton1");

        jLabel1.setText("Cola de pedidos");

        tablaPedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Usuario", "Cliente", "Fecha", "Hora", "Total", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaPedidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPedidosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaPedidos);
        if (tablaPedidos.getColumnModel().getColumnCount() > 0) {
            tablaPedidos.getColumnModel().getColumn(0).setResizable(false);
            tablaPedidos.getColumnModel().getColumn(6).setResizable(false);
        }

        tablaDetallePedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdDetVenta", "Producto", "Cantidad", "Precio Uni.", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablaDetallePedido);

        jLabel2.setText("Detalle Del depedio");

        jLabel4.setText("Informacion del Pedido");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Total");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Cliente");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Tipo Pago");

        comboTipoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnPagar.setBackground(new java.awt.Color(204, 255, 204));
        btnPagar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnPagar.setText("PAGAR PEDIDO");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Comprobante");

        labelNombreCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelNombreCliente.setText(".....");

        labelTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTotal.setText(".....");

        comboComprobante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton2.setText("Actualizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnHistorial.setText("Historial Pedidos");
        btnHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialActionPerformed(evt);
            }
        });

        tbnAnular.setBackground(new java.awt.Color(255, 153, 153));
        tbnAnular.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tbnAnular.setText("ANULAR PEDIDO");
        tbnAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbnAnularActionPerformed(evt);
            }
        });

        checboxImprimir.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        checboxImprimir.setText("Imprimir Comprobante");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel2))
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8))
                                .addGap(67, 67, 67)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labelTotal)
                                    .addComponent(labelNombreCliente)
                                    .addComponent(comboTipoPago, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(comboComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(checboxImprimir)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(tbnAnular, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(140, 140, 140))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jButton2)
                            .addComponent(btnHistorial))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(238, 238, 238)
                                .addComponent(jLabel3))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(labelNombreCliente))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(labelTotal))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(comboTipoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(comboComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(checboxImprimir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tbnAnular, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(63, 63, 63))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tablaPedidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPedidosMouseClicked
        listarDetallePedido();
        datosPedidoLabel();
    }//GEN-LAST:event_tablaPedidosMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        limpiar();
        listarPedidosEnProceso();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        crearComprobante();

    }//GEN-LAST:event_btnPagarActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed

        javax.swing.JDialog dialog = new javax.swing.JDialog();
        // Configurar el JDialog
        dialog.setUndecorated(false); // Sin bordes
        dialog.setModal(true); // No bloquea la ventana principal
        dialog.setSize(this.getParent().getWidth(), this.getParent().getHeight());
        dialog.setLocationRelativeTo(null); // Centrar en la pantalla

        // Crear el panel de cliente y agregarlo al diálogo
        HistorialPedidosPanel pedPanel = new HistorialPedidosPanel();

        pedPanel.setSize(dialog.getSize());
        dialog.add(pedPanel);

        // Hacer visible el diálogo
        dialog.setVisible(true);
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void tbnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbnAnularActionPerformed
        anularPedido();
    }//GEN-LAST:event_tbnAnularActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnPagar;
    private javax.swing.JCheckBox checboxImprimir;
    private javax.swing.JComboBox<String> comboComprobante;
    private javax.swing.JComboBox<String> comboTipoPago;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel labelNombreCliente;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JTable tablaDetallePedido;
    private javax.swing.JTable tablaPedidos;
    private javax.swing.JButton tbnAnular;
    // End of variables declaration//GEN-END:variables
}
