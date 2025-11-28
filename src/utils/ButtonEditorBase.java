/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author josue
 */
public abstract class ButtonEditorBase extends AbstractCellEditor implements TableCellEditor, ActionListener {

    protected final JButton button = new JButton();
    protected JTable table;
    protected int row;

    public ButtonEditorBase() {
        button.addActionListener(this);
    }

    // Cada tabla implementa su acción
    public abstract void onClick(int row);

    @Override
    public void actionPerformed(ActionEvent e) {
        onClick(row);
        fireEditingStopped();
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected,
            int row, int column) {

        this.table = table;
        this.row = row;

        if (value != null) {
            button.setText(value.toString());
        } else {
            button.setText("");
        }

        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
