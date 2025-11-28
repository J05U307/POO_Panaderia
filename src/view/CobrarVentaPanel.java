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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;
import service.ComprobanteService;
import service.DetalleVentaService;
import service.PedidoService;
import service.TipoPagoService;

/**
 *
 * @author josue
 */
public class CobrarVentaPanel extends javax.swing.JPanel {

    private TipoPagoService tipoPagoService = new TipoPagoService();
    private PedidoService pedidoService = new PedidoService();
    private ComprobanteService comprobanteService = new ComprobanteService();
    private DetalleVentaService detalleVentaService = new DetalleVentaService();

    public CobrarVentaPanel() {
        initComponents();
        cargarCompoTipoPago();
        cargarComboComprobante();
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
        pedidoService.cambiarEstado(idPedido);

        limpiar();
        listarPedidosEnProceso();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        labelNombreCliente = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        comboComprobante = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();

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

        jLabel5.setText("Total");

        jLabel6.setText("Cliente");

        jLabel7.setText("Tipo Pago");

        comboTipoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("PAGAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setText("Comprobante");

        labelNombreCliente.setText(".....");

        labelTotal.setText(".....");

        comboComprobante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton2.setText("Actualizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labelTotal)
                                    .addComponent(labelNombreCliente)
                                    .addComponent(comboTipoPago, 0, 169, Short.MAX_VALUE)
                                    .addComponent(comboComprobante, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(jButton1)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(labelNombreCliente))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(labelTotal))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(comboTipoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(comboComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addComponent(jButton1)))
                .addContainerGap(40, Short.MAX_VALUE))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       crearComprobante();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboComprobante;
    private javax.swing.JComboBox<String> comboTipoPago;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel labelNombreCliente;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JTable tablaDetallePedido;
    private javax.swing.JTable tablaPedidos;
    // End of variables declaration//GEN-END:variables
}
