/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.Cliente;
import java.util.List;
import repository.ClienteRepository;

/**
 *
 * @author josue
 */
public class ClienteService {

    private ClienteRepository repo = new ClienteRepository();

    // Listar
    public List<Cliente> listar() {
        return repo.findAll();
    }

    //Agregar
    public void agregar(String nombre, String apellido, String dni) {
        int id = 1;
        if (!repo.findAll().isEmpty()) {
            id = repo.findAll().get(repo.findAll().size() - 1).getId() + 1;
        }
        Cliente nuevo = new Cliente(id, nombre, apellido, dni);
        repo.save(nuevo);
    }

    // EDITAR
    public void editar(int id, String nombre, String apellido, String dni) {
        Cliente c = repo.findById(id);
        if (c != null) {
            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setDni(dni);
            repo.update(c);
        }
    }

    // ELIMINAR
    public void eliminar(int id) {
        repo.delete(id);
    }

}
