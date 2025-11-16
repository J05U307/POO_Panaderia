/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import data.Data;
import entity.Categoria;
import entity.Producto;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author josue
 */
public class ProductoPanel extends javax.swing.JPanel {

    /**
     * Creates new form GestionarProducto
     */
    public ProductoPanel() {
        initComponents();
        cargarComboCategoria();
        listar();
    }

    private void editar() {

        int fila = tablaProducto.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para editar");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String precioTxt = txtPrecio.getText().trim();
        String stockTxt = txtStock.getText().trim();
        int categoriaIndex = comboCategoria.getSelectedIndex();

        // Validaciones
        if (nombre.isEmpty() || precioTxt.isEmpty() || stockTxt.isEmpty() || categoriaIndex == 0) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        // Convertir precio y stock
        double precio;
        int stock;

        try {
            precio = Double.parseDouble(precioTxt);
            stock = Integer.parseInt(stockTxt);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Precio o Stock inválidos");
            return;
        }

        // Confirmación
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas editar este producto?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {

            // Obtener ID del producto desde la tabla
            int idProducto = (int) tablaProducto.getValueAt(fila, 0);

            // Buscar producto en la lista
            for (Producto p : Data.productos) {
                if (p.getId() == idProducto) {

                    // EDITAR CAMPOS
                    p.setNombre(nombre);
                    p.setPrecio(precio);
                    p.setStock(stock);
                    Categoria categoriaSeleccionada = Data.categorias.get(categoriaIndex - 1);
                    p.setCategoria(categoriaSeleccionada);
                    break;
                }
            }
            listar();
            limpiar();
            JOptionPane.showMessageDialog(this, "Producto editado correctamente");
        }
    }

    private void agregar() {

        String nombre = txtNombre.getText().trim();
        String precioTxt = txtPrecio.getText().trim();
        String stockTxt = txtStock.getText().trim();
        int categoriaIndex = comboCategoria.getSelectedIndex();

        if (nombre.isEmpty() || precioTxt.isEmpty() || stockTxt.isEmpty() || categoriaIndex == 0) {
            JOptionPane.showMessageDialog(this, "Ingrese todos los datos");
            return;
        }

        double precio;
        int stock;

        try {
            precio = Double.parseDouble(precioTxt);
            stock = Integer.parseInt(stockTxt);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio o stock inválido");
            return;
        }

        // Generar nuevo ID
        int nuevoId = 1;
        if (!Data.productos.isEmpty()) {
            nuevoId = Data.productos.get(Data.productos.size() - 1).getId() + 1;
        }

        // Obtener la categoría seleccionada
        Categoria categoriaSeleccionada = Data.categorias.get(categoriaIndex - 1);
        // -1 porque el primer item es "Seleccione categoría"

        // Crear producto
        Producto pro = new Producto(nuevoId, nombre, precio, stock, categoriaSeleccionada);

        // Agregar a la lista
        Data.productos.add(pro);

        JOptionPane.showMessageDialog(this, "Producto agregado correctamente");

        limpiar();
        listar();
    }

    private void limpiar() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        comboCategoria.setSelectedIndex(0);
        btnAgregar.setEnabled(true);
    }

    public void cargarComboCategoria() {
        comboCategoria.removeAllItems();
        comboCategoria.addItem("Seleccione categoría");
        for (Categoria c : Data.categorias) {
            comboCategoria.addItem(c.getNombre());
        }
    }

    private void obtenerDatosTabla() {

        int fila = tablaProducto.getSelectedRow();

        if (fila != -1) {

            // Obtener datos de la tabla
            String nombreOb = (String) tablaProducto.getValueAt(fila, 1);
            double precioOb = (double) tablaProducto.getValueAt(fila, 2);
            int stockOb = (int) tablaProducto.getValueAt(fila, 3);
            String categoriaNombre = (String) tablaProducto.getValueAt(fila, 4);

            // Asignar a los campos
            txtNombre.setText(nombreOb);
            txtPrecio.setText(String.valueOf(precioOb));
            txtStock.setText(String.valueOf(stockOb));

            // Seleccionar categoría en combo
            for (int i = 0; i < comboCategoria.getItemCount(); i++) {
                if (comboCategoria.getItemAt(i).equals(categoriaNombre)) {
                    comboCategoria.setSelectedIndex(i);
                    break;
                }
            }

            btnAgregar.setEnabled(false);
        }

    }

    private void listar() {
        DefaultTableModel modelo = (DefaultTableModel) tablaProducto.getModel();
        modelo.setRowCount(0);

        for (Producto p : Data.productos) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoria().getNombre()
            };
            modelo.addRow(fila);
        }
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
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        comboCategoria = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProducto = new javax.swing.JTable();
        btnNuevo = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();

        jLabel1.setText("Producto");

        jLabel2.setText("Nombre");

        jLabel3.setText("Precio");

        jLabel4.setText("Stock");

        comboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Categoria");

        tablaProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NOMBRE", "PRECIO", "STOCK", "CATEGORIA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProductoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaProducto);
        if (tablaProducto.getColumnModel().getColumnCount() > 0) {
            tablaProducto.getColumnModel().getColumn(0).setResizable(false);
            tablaProducto.getColumnModel().getColumn(1).setResizable(false);
            tablaProducto.getColumnModel().getColumn(2).setResizable(false);
            tablaProducto.getColumnModel().getColumn(3).setResizable(false);
            tablaProducto.getColumnModel().getColumn(4).setResizable(false);
        }

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(comboCategoria, javax.swing.GroupLayout.Alignment.LEADING, 0, 136, Short.MAX_VALUE)
                                .addComponent(txtStock, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.Alignment.LEADING)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
            .addGroup(layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(btnNuevo)
                .addGap(18, 18, 18)
                .addComponent(btnAgregar)
                .addGap(18, 18, 18)
                .addComponent(btnEditar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel1)
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnAgregar)
                    .addComponent(btnEditar))
                .addGap(76, 76, 76))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tablaProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProductoMouseClicked
        obtenerDatosTabla();
    }//GEN-LAST:event_tablaProductoMouseClicked

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        limpiar();
        listar();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregar();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        editar();
    }//GEN-LAST:event_btnEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> comboCategoria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProducto;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
