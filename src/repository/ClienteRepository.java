/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Cliente;
import java.util.ArrayList;
import java.util.List;
import utils.SerializadorUtil;

/**
 *
 * @author josue
 */
public class ClienteRepository {

    private static final String FILE_PATH = "data/clientes.dat";
    private static List<Cliente> clientes = new ArrayList<>();

    // Listar 
    public ClienteRepository() {
        clientes = SerializadorUtil.cargarLista(FILE_PATH);
    }

    public List<Cliente> findAll() {
        return clientes;
    }

    // Agreagar
    public void save(Cliente cliente) {
        clientes.add(cliente);
        SerializadorUtil.guardarLista(FILE_PATH, clientes);
    }

    // Editar
    public void update(Cliente cliente) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == cliente.getId()) {
                clientes.set(i, cliente);
                SerializadorUtil.guardarLista(FILE_PATH, clientes);
                return;
            }
        }
    }

    // eliminar
    public void delete(int id) {
        clientes.removeIf(c -> c.getId() == id);
        SerializadorUtil.guardarLista(FILE_PATH, clientes);
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

    public List<Cliente> findByNombreOrDniContains(String filtro) {
        List<Cliente> resultado = new ArrayList<>();
        String texto = filtro.toLowerCase();

        for (Cliente c : clientes) {
            if (c.getNombre().toLowerCase().contains(texto)
                    || c.getApellido().toLowerCase().contains(texto)
                    || c.getDni().toLowerCase().contains(texto)) {

                resultado.add(c);
            }
        }

        return resultado;
    }

}
