/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author josue
 */
public class PedidoEstadoRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
        );

        // Obtener estado (columna 6)
        String estado = table.getValueAt(row, 6).toString();

        if (estado.equalsIgnoreCase("PROCESO")) {
            // Amarillo suave
            c.setBackground(new Color(255, 255, 153));
        } else if (estado.equalsIgnoreCase("PAGADO")) {
            // Verde suave
            c.setBackground(new Color(204, 255, 204));
        } else if (estado.equalsIgnoreCase("ANULADO")) {
            // Rojo suave
            c.setBackground(new Color(255, 153, 153));
        } else {
            c.setBackground(Color.WHITE);
        }

        // Mantener selección visible
        if (isSelected) {
            c.setBackground(new Color(184, 207, 229));
        }

        return c;
    }
}
