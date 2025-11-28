/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Color;
import javax.swing.JOptionPane;
import view.GenerarVentaPanel;

/**
 *
 * @author josue
 */
public class ClienteButtonEditor extends ButtonEditorBase {

    private int selectedRow = -1;
    private final GenerarVentaPanel panel;

    public ClienteButtonEditor(GenerarVentaPanel panel) {
        this.panel = panel;
    }

    @Override
    public void onClick(int row) {

        if (selectedRow != -1 && selectedRow != row) {
            JOptionPane.showMessageDialog(panel,
                    "Solo puedes seleccionar un cliente.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Si es la MISMA fila → es deselección
        if (selectedRow == row) {

            // Limpiar labels
            panel.setLabelIDCliente(".....");
            panel.setLabelNombreCliente(".....");

            // Restaurar color
            table.clearSelection();
            table.setSelectionBackground(Color.WHITE);

            selectedRow = -1;

            // Restaurar nombre del botón
            table.setValueAt("Seleccionar", row, 3);

            return;
        }

        // SELECCIONAR
        selectedRow = row;

        // Pintar fila
        table.setRowSelectionInterval(row, row);
        table.setSelectionBackground(new Color(0, 180, 0));

        // Obtener datos del cliente
        int idCliente = Integer.parseInt(table.getValueAt(row, 0).toString());
        String nombreCliente = table.getValueAt(row, 1).toString();

        // Colocar en labels
        panel.setLabelIDCliente(String.valueOf(idCliente));
        panel.setLabelNombreCliente(nombreCliente);

        // Cambiar texto del botón
        table.setValueAt("Deseleccionar", row, 3);

    }

    @Override
    public Object getCellEditorValue() {
        return "Seleccionar";
    }
}
