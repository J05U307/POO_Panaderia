/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import service.PedidoService;

import java.awt.BorderLayout;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.chart.renderer.category.BarRenderer;

import org.jfree.data.category.DefaultCategoryDataset;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;

/**
 *
 * @author josue
 */
public class ReporteMensualPanel extends javax.swing.JPanel {

    private PedidoService pedidoService = new PedidoService();

    public ReporteMensualPanel() {
        initComponents();
        inicializarFechaActual();
        configurarListeners();
        listarDiasDelMes();
    }

    private void inicializarFechaActual() {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        jdataAnio.setYear(hoy.getYear());
        jdataMes.setMonth(hoy.getMonthValue() - 1);
    }

    private void configurarListeners() {
        jdataMes.addPropertyChangeListener("month", evt -> listarDiasDelMes());
        jdataAnio.addPropertyChangeListener("year", evt -> listarDiasDelMes());
    }

    private void listarDiasDelMes() {

        int anio = jdataAnio.getYear();
        int mes = jdataMes.getMonth() + 1;

        YearMonth ym = YearMonth.of(anio, mes);
        int diasEnMes = ym.lengthOfMonth();

        DefaultTableModel model = (DefaultTableModel) tablaRepoteMensual.getModel();
        model.setRowCount(0);

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd MMM yyyy", new java.util.Locale("es"));

        for (int dia = 1; dia <= diasEnMes; dia++) {

            LocalDate fecha = LocalDate.of(anio, mes, dia);

            // datos reales del día:
            int pedidosPagados = pedidoService.contarPagadosPorFecha(fecha);
            double totalRecaudado = pedidoService.totalPagadoPorFecha(fecha);

            String fechaFormateada = fecha.format(formato).toLowerCase();

            model.addRow(new Object[]{
                fechaFormateada,
                pedidosPagados,
                totalRecaudado
            });
        }

        mostrarGrafico();
        SwingUtilities.invokeLater(() -> mostrarGrafico());
    }

    private void mostrarGrafico() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DefaultTableModel model = (DefaultTableModel) tablaRepoteMensual.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            String fecha = model.getValueAt(i, 0).toString();
            double total = 0;

            if (model.getValueAt(i, 2) != null) {
                try {
                    total = Double.parseDouble(model.getValueAt(i, 2).toString());
                } catch (Exception e) {
                    total = 0;
                }
            }

            dataset.addValue(total, "Recaudado", fecha);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Recaudación por Día",
                "Fecha",
                "Total (S/.)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "PE"));
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(0);

        // ⭐ NO mostrar etiqueta si total = 0
        StandardCategoryItemLabelGenerator labelGen
                = new StandardCategoryItemLabelGenerator("S/. {2}", nf) {

            @Override
            public String generateLabel(org.jfree.data.category.CategoryDataset dataset, int row, int column) {
                Number valor = dataset.getValue(row, column);
                if (valor == null || valor.doubleValue() == 0) {
                    return null;
                }
                return super.generateLabel(dataset, row, column);
            }
        };

        renderer.setBaseItemLabelGenerator(labelGen);
        renderer.setBaseItemLabelsVisible(true);

        // ⭐ Rotar más las etiquetas (60 grados)
        CategoryAxis ejeX = chart.getCategoryPlot().getDomainAxis();
        ejeX.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 3));

        // ⭐ Hacer más pequeña la fuente
        ejeX.setTickLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 9));

        ChartPanel chartPanel = new ChartPanel(chart);

        panelGrafico.removeAll();
        panelGrafico.setLayout(new BorderLayout());
        panelGrafico.add(chartPanel, BorderLayout.CENTER);

        // ⭐ Aumentar tamaño del panel
        panelGrafico.setPreferredSize(new Dimension(950, 550));

        panelGrafico.revalidate();
        panelGrafico.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdataMes = new com.toedter.calendar.JMonthChooser();
        jLabel1 = new javax.swing.JLabel();
        panelGrafico = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRepoteMensual = new javax.swing.JTable();
        jdataAnio = new com.toedter.calendar.JYearChooser();

        jLabel1.setText("Reporte Mensual");

        panelGrafico.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout panelGraficoLayout = new javax.swing.GroupLayout(panelGrafico);
        panelGrafico.setLayout(panelGraficoLayout);
        panelGraficoLayout.setHorizontalGroup(
            panelGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 848, Short.MAX_VALUE)
        );
        panelGraficoLayout.setVerticalGroup(
            panelGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        tablaRepoteMensual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Fecha", "Pedidos Pagados", "Total "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaRepoteMensual);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelGrafico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jdataAnio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdataMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jdataMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdataAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelGrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JYearChooser jdataAnio;
    private com.toedter.calendar.JMonthChooser jdataMes;
    private javax.swing.JPanel panelGrafico;
    private javax.swing.JTable tablaRepoteMensual;
    // End of variables declaration//GEN-END:variables
}
