/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Cliente;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josue
 */
public class ClienteRepository {

    private static List<Cliente> clientes = new ArrayList<>();

    static {
        clientes.add(new Cliente(1, "Carlos", "Ramírez", "72154896"));
        clientes.add(new Cliente(2, "Camila", "Gonzales", "34679825"));
        clientes.add(new Cliente(3, "José", "Gutiérrez", "73589412"));
        clientes.add(new Cliente(4, "Ana", "Torres", "70894521"));
        clientes.add(new Cliente(5, "Luis", "Fernández", "75632148"));
        clientes.add(new Cliente(6, "María", "López", "74851236"));

    }

    // Listar 
    public List<Cliente> findAll() {
        return clientes;
    }

    // Agreagar
    public void save(Cliente cliente) {
        clientes.add(cliente);
    }

    // Editar
    public void update(Cliente cliente) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == cliente.getId()) {
                clientes.set(i, cliente);
                return;
            }
        }
    }

    // eliminar
    public void delete(int id) {
        clientes.removeIf(c -> c.getId() == id);
    }

    // BUSCAR
    public Cliente findById(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

}
