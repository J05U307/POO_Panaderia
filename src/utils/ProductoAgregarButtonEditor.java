/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import entity.Producto;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import view.GenerarVentaPanel;

/**
 *
 * @author josue
 */
public class ProductoAgregarButtonEditor extends ButtonEditorBase {

    private List<Producto> productos;
    private GenerarVentaPanel panel;

    public ProductoAgregarButtonEditor(List<Producto> productos, GenerarVentaPanel panel) {
        this.productos = productos;
        this.panel = panel;
    }

    @Override
    public void onClick(int row) {

        if (productos == null || productos.isEmpty()) {
            return;
        }

        Producto p = productos.get(row);

        // VALIDAD STOCK
        if (p.getStock() <= 0) {
            JOptionPane.showMessageDialog(panel, "No hay stock disponible para este producto", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // VALIDAR QUE NO ESTE AGREGADO
        DefaultTableModel modelDetalle = (DefaultTableModel) panel.getTablaDetalleVenta().getModel();
        for (int i = 0; i < modelDetalle.getRowCount(); i++) {
            int idDetalle = Integer.parseInt(modelDetalle.getValueAt(i, 0).toString());
            if (idDetalle == p.getId()) {
                JOptionPane.showMessageDialog(panel, "Este producto ya está en la venta", "Atención", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        panel.agregarDetalleVenta(p);

    }

    @Override
    public Object getCellEditorValue() {
        return "Agregar";
    }
}
