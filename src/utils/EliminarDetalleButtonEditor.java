/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.table.DefaultTableModel;
import view.GenerarVentaPanel;

/**
 *
 * @author josue
 */
public class EliminarDetalleButtonEditor extends ButtonEditorBase {

    private GenerarVentaPanel panel;

    public EliminarDetalleButtonEditor(GenerarVentaPanel panel) {
        this.panel = panel;
    }

    @Override
    public void onClick(int row) {
        DefaultTableModel model = (DefaultTableModel) panel.getTablaDetalleVenta().getModel();
        panel.getTablaDetalleVenta().getCellEditor().stopCellEditing();
        
        model.removeRow(row);
        panel.actualizarTotal();
    }

    @Override
    public Object getCellEditorValue() {
        return "Eliminar";
    }
}
