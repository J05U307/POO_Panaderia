/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.Rol;
import java.util.List;
import repository.RolRepository;

/**
 *
 * @author josue
 */
public class RolService {

    private RolRepository repo = new RolRepository();

    public List<Rol> listar() {
        return repo.findAll();
    }

    public void agregar(String nombre) {
        int id = 1;
        if (!repo.findAll().isEmpty()) {
            id = repo.findAll().get(repo.findAll().size() - 1).getId() + 1;
        }

        Rol nuevo = new Rol(id, nombre);
        repo.save(nuevo);
    }

    public void eliminar(int id) {
        repo.delete(id);
    }

}
