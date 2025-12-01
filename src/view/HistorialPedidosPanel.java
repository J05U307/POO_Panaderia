/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import entity.DetalleVenta;
import entity.Pedido;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import service.DetalleVentaService;
import service.PedidoService;
import utils.GenerarComprobante;
import utils.PedidoEstadoRenderer;

/**
 *
 * @author josue
 */
public class HistorialPedidosPanel extends javax.swing.JPanel {

    private PedidoService pedidoService = new PedidoService();
    private GenerarComprobante generardorComprobante = new GenerarComprobante();
    private DetalleVentaService detalleVentaService = new DetalleVentaService();

    public HistorialPedidosPanel() {
        initComponents();
        configurarTablaDetallePedido();
        configurarTablaPedidos();
        dateFecha.setDate(java.sql.Date.valueOf(LocalDate.now()));

        if (tablaDetallePedido.getColumnModel().getColumnCount() > 1) {
            tablaDetallePedido.getColumnModel().getColumn(0).setMinWidth(0);
            tablaDetallePedido.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaDetallePedido.getColumnModel().getColumn(0).setWidth(0);
        }
        
        filtrarPedidosPorFecha();
        mostarInformacion();
        listenerjdata();
    }

    private void imprimirComprobante() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido ", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener ID del pedido (columna 0)
        int idPedido = Integer.parseInt(tablaPedidos.getValueAt(fila, 0).toString());

        String ruta = generardorComprobante.generarComprobantePDF(idPedido);

        // Validar errores
        if (ruta.startsWith("ERROR")) {
            JOptionPane.showMessageDialog(this, ruta, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Abrir el PDF generado automáticamente
        try {
            java.awt.Desktop.getDesktop().open(new File(ruta));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "PDF generado: " + ruta, "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void mostarInformacion() {
        int contadorProceso = 0;
        int contadorPagado = 0;
        int contatadorAnulado = 0;
        double sumaTotal = 0;

        DefaultTableModel modelo = (DefaultTableModel) tablaPedidos.getModel();
        int filas = modelo.getRowCount();

        for (int i = 0; i < filas; i++) {

            String estado = modelo.getValueAt(i, 6).toString();  // columna ESTADO
            double total = Double.parseDouble(modelo.getValueAt(i, 5).toString()); // columna TOTAL

            if (estado.equalsIgnoreCase("PROCESO")) {
                contadorProceso++;
            } else if (estado.equalsIgnoreCase("PAGADO")) {
                contadorPagado++;
                sumaTotal += total;
            } else if (estado.equalsIgnoreCase("ANULADO")) {
                contatadorAnulado++;
            }

        }

        // Actualizar labels
        labeltotalPedidos.setText(String.valueOf(contadorProceso + contadorPagado + contatadorAnulado));
        labelProceso.setText(String.valueOf(contadorProceso));
        labelPagados.setText(String.valueOf(contadorPagado));
        labelTotal.setText(String.valueOf(contadorPagado + contadorProceso));
        labelAnulados.setText(String.valueOf(contatadorAnulado));
        labelTotal.setText(String.format("S/ %.2f", sumaTotal));

    }

    //Listener jdatachjoser
    private void listenerjdata() {
        dateFecha.addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                filtrarPedidosPorFecha();
                mostarInformacion();
            }
        });
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

    // LISTAR PEDIDOS
    private void filtrarPedidosPorFecha() {

        DefaultTableModel modelo = (DefaultTableModel) tablaPedidos.getModel();
        modelo.setRowCount(0);

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("h:mm a");

        // Convertir JDateChooser → LocalDate
        LocalDate fechaFiltro = dateFecha.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        for (Pedido pedido : pedidoService.listar()) {

            LocalDate fechaPedido = pedido.getFecha().toLocalDate();

            // 👉 FILTRO
            if (!fechaPedido.equals(fechaFiltro)) {
                continue;
            }

            String nombreCliente = (pedido.getCliente() != null)
                    ? pedido.getCliente().getNombre()
                    : "Sin cliente";

            String nombreEmpleado = "Desconocido";
            if (pedido.getUsuario() != null && pedido.getUsuario().getEmpleado() != null) {
                nombreEmpleado = pedido.getUsuario().getEmpleado().getNombre();
            }

            modelo.addRow(new Object[]{
                pedido.getId(),
                nombreEmpleado,
                nombreCliente,
                pedido.getFecha().format(formatoFecha),
                pedido.getFecha().format(formatoHora),
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPedidos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dateFecha = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDetallePedido = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        labelProceso = new javax.swing.JLabel();
        labelAnulados = new javax.swing.JLabel();
        labelPagados = new javax.swing.JLabel();
        labeltotalPedidos = new javax.swing.JLabel();

        tablaPedidos.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaPedidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPedidosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaPedidos);

        jLabel1.setText("HISTORIAL DE TODOS LOS PEDIDOS");

        jLabel2.setText("Fecha");

        tablaDetallePedido.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tablaDetallePedido);

        jButton1.setText("Imprimir Comprobante");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Totol Pedidos");

        jLabel4.setText("Pedidos Pagados");

        jLabel5.setText("Pedidos Anulados");

        jLabel6.setText("Pedidos En proceso");

        jLabel7.setText("GANANCIA");

        jLabel8.setText("Total");

        labelTotal.setText("total");

        labelProceso.setText("proceso");

        labelAnulados.setText("anulados");

        labelPagados.setText("pagados");

        labeltotalPedidos.setText("totalPedidos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel8)
                        .addGap(90, 90, 90)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labeltotalPedidos)
                            .addComponent(labelTotal)
                            .addComponent(labelPagados)
                            .addComponent(labelAnulados)
                            .addComponent(labelProceso))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(labeltotalPedidos))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(labelPagados))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(labelAnulados))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(labelProceso))
                .addGap(37, 37, 37)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(labelTotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(42, 42, 42))
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(53, 53, 53)
                        .addComponent(dateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tablaPedidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPedidosMouseClicked
        listarDetallePedido();
    }//GEN-LAST:event_tablaPedidosMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        imprimirComprobante();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateFecha;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelAnulados;
    private javax.swing.JLabel labelPagados;
    private javax.swing.JLabel labelProceso;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JLabel labeltotalPedidos;
    private javax.swing.JTable tablaDetallePedido;
    private javax.swing.JTable tablaPedidos;
    // End of variables declaration//GEN-END:variables
}
