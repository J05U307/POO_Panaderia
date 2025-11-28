/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import entity.Producto;
import java.util.List;
import javax.swing.JOptionPane;
import service.ProductoService;
import view.GenerarVentaPanel;

/**
 *
 * @author josue
 */
public class DetalleVentaButtonEditor extends ButtonEditorBase {

    private final boolean sumar;
    private GenerarVentaPanel panel;
    private ProductoService serviceProducto = new ProductoService();

    public DetalleVentaButtonEditor(boolean sumar, GenerarVentaPanel panel) {
        this.sumar = sumar;
        this.panel = panel;
    }

    @Override
    public void onClick(int row) {

        int columnaId = 0;

        int columnaPrecio = 2;
        int columnaCantidad = 3;
        int columnaSubtotal = 4;

        int idProducto = Integer.parseInt(table.getValueAt(row, columnaId).toString());
        int cantidad = Integer.parseInt(table.getValueAt(row, columnaCantidad).toString());
        double precio = Double.parseDouble(table.getValueAt(row, columnaPrecio).toString());

        Producto producto = serviceProducto.buscarPorId(idProducto);
        if (producto == null) {
            JOptionPane.showMessageDialog(panel,
                    "No se encontró el producto en el sistema.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stockDisponible = producto.getStock();

        // suma
        if (sumar) {
            if (cantidad + 1 > stockDisponible) {
                JOptionPane.showMessageDialog(panel,
                        "No puedes agregar más unidades.\nStock disponible: " + stockDisponible,
                        "Stock insuficiente",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            cantidad++;
        } else {
            // resto
            if (cantidad > 1) {
                cantidad--;
            }
        }


        table.setValueAt(cantidad, row, columnaCantidad);

        double subtotal = precio * cantidad;
        table.setValueAt(subtotal, row, columnaSubtotal);

        panel.actualizarTotal();
    }

    @Override
    public Object getCellEditorValue() {
        return sumar ? "+" : "-";
    }

}
