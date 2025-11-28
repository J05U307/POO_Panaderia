/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import entity.DetalleVenta;
import entity.Pedido;
import entity.Usuario;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;
import service.DetalleVentaService;
import service.PedidoService;
import utils.PedidoEstadoRenderer;

/**
 *
 * @author josue
 */
public class PedidosEnviadosPanel extends javax.swing.JPanel {

    private Usuario usuarioLogueado;
    private PedidoService pedidoService = new PedidoService();
    private DetalleVentaService detalleVentaService = new DetalleVentaService();

    public PedidosEnviadosPanel(Usuario usuarioLogueado) {
        initComponents();
        this.usuarioLogueado = usuarioLogueado;
        configurarTablaPedidos();
        configurarTablaDetallePedido();
        /// DIA ACTUAL
        dateFecha.setDate(java.sql.Date.valueOf(LocalDate.now()));
        filtrarPedidosPorFecha();
        informacion();

        listenerjdata();

    }

    private void informacion() {

        int contadorProceso = 0;
        int contadorPagado = 0;
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
            }

            // sumaTotal += total;
        }

        // Actualizar labels
        labelProceso.setText(String.valueOf(contadorProceso));
        labelPagado.setText(String.valueOf(contadorPagado));
        labelTotal.setText(String.valueOf(contadorPagado + contadorProceso));
    }

    //Listener jdatachjoser
    private void listenerjdata() {
        dateFecha.addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                filtrarPedidosPorFecha();
                informacion();
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
    }

    private void filtrarPedidosPorFecha() {

        DefaultTableModel modelo = (DefaultTableModel) tablaPedidos.getModel();
        modelo.setRowCount(0);

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("h:mm a");

        // Convertir JDateChooser → LocalDate
        LocalDate fechaFiltro = dateFecha.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        for (Pedido pedido : pedidoService.listarPorUsuario(usuarioLogueado)) {

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPedidos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDetallePedido = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labelProceso = new javax.swing.JLabel();
        labelPagado = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dateFecha = new com.toedter.calendar.JDateChooser();

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

        jLabel1.setText("Pedidos Enviados");

        labelTotal.setText("total");

        jLabel3.setText("Pedidos En Proseso");

        jLabel4.setText("Pedidos Pagagos");

        labelProceso.setText("proceso");

        labelPagado.setText("pagado");

        jLabel2.setText("Fecha");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(30, 30, 30)
                                .addComponent(labelTotal))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelPagado)
                                    .addComponent(labelProceso))))))
                .addGap(18, 18, 18))
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(labelTotal))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(labelProceso))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(labelPagado)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tablaPedidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPedidosMouseClicked
        listarDetallePedido();
    }//GEN-LAST:event_tablaPedidosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelPagado;
    private javax.swing.JLabel labelProceso;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JTable tablaDetallePedido;
    private javax.swing.JTable tablaPedidos;
    // End of variables declaration//GEN-END:variables
}
