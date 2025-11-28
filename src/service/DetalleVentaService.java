/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.DetalleVenta;
import entity.Pedido;
import entity.Producto;
import java.util.ArrayList;
import java.util.List;
import repository.DetalleVentaRepository;

/**
 *
 * @author josue
 */
public class DetalleVentaService {

    private DetalleVentaRepository repo = new DetalleVentaRepository();

    //listar: 
    public List<DetalleVenta> listar() {
        return repo.findAll();
    }

    // agregar()
    public boolean agregar(Pedido pedido, Producto producto, int cantidad, double precioVendido, double subtotal) {
        int id = 1;
        if (!repo.findAll().isEmpty()) {
            id = repo.findAll().get(repo.findAll().size() - 1).getId() + 1;
        }
        DetalleVenta nuevo = new DetalleVenta(id, pedido, producto, cantidad, precioVendido, subtotal);
        repo.save(nuevo);
        return true;
    }

    // Bucar:
    public DetalleVenta buscar(int id) {
        return repo.findById(id);
    }

    // Listar DETALLE DE UN PEDIDO: 
    public List<DetalleVenta> listarPorPedido(int idPedido) {
        List<DetalleVenta> todos = repo.findAll();
        List<DetalleVenta> detalles = new ArrayList<>();

        for (DetalleVenta dv : todos) {
            if (dv.getPedido() != null && dv.getPedido().getId() == idPedido) {
                detalles.add(dv);
            }
        }

        return detalles;
    }

}
