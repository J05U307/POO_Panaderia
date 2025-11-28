/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.Cliente;
import entity.Pedido;
import entity.Usuario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import repository.PedidoRepository;

/**
 *
 * @author josue
 */
public class PedidoService {

    private PedidoRepository repo = new PedidoRepository();

    // LISTAR: 
    public List<Pedido> listar() {
        return repo.findAll();
    }

    //Agregar
    public boolean agregar(Cliente cliente, double total, Usuario usuario) {
        int nuevoId = 1;
        if (!repo.findAll().isEmpty()) {
            nuevoId = repo.findAll().get(repo.findAll().size() - 1).getId() + 1;
        }

        LocalDateTime fecha = LocalDateTime.now();

        Pedido nuevo = new Pedido(nuevoId, fecha, cliente, total, usuario, "PROCESO");
        repo.save(nuevo);
        return true;
    }

    public void cambiarEstado(int id) {
        Pedido c = repo.findById(id);

        if (c != null) {
            c.setEstado("PAGADO");
            repo.update(c);
        }
    }

    //BUCAR
    public Pedido buscar(int id) {
        return repo.findById(id);
    }

    public Pedido obtenerUltimo() {
        List<Pedido> lista = repo.findAll();
        if (lista.isEmpty()) {
            return null;
        }
        return lista.get(lista.size() - 1);
    }

    // lista de pedidos en proceso: 
    public List<Pedido> listarPedidosEnProceso() {
        List<Pedido> todos = repo.findAll();
        List<Pedido> enProceso = new ArrayList<>();

        for (Pedido p : todos) {
            if (p.getEstado().equalsIgnoreCase("PROCESO")) {
                enProceso.add(p);
            }
        }

        return enProceso;
    }

    
    // lista de pedidos que ya fueron pagados: 
    public List<Pedido> listarPedidosPagados() {
        List<Pedido> todos = repo.findAll();
        List<Pedido> enProceso = new ArrayList<>();

        for (Pedido p : todos) {
            if (p.getEstado().equalsIgnoreCase("PAGADO")) {
                enProceso.add(p);
            }
        }

        return enProceso;
    }
}
