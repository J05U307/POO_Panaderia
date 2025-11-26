/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.Categoria;
import java.util.List;
import repository.CategoriaRepository;

/**
 *
 * @author josue
 */
public class CategoriaService {

    private CategoriaRepository repo = new CategoriaRepository();

    // Listar
    public List<Categoria> listar() {
        return repo.findAll();
    }

    //Agregar
    public void agregar(String nombre) {
        int nuevoId = 1;
        if (!repo.findAll().isEmpty()) {
            nuevoId = repo.findAll().get(repo.findAll().size() - 1).getId() + 1;
        }
        Categoria nuevo = new Categoria(nuevoId, nombre);
        repo.save(nuevo);
    }

    //Editar
    public void editar(int id, String nombre) {
        Categoria c = repo.findById(id);

        if (c != null) {
            c.setNombre(nombre);
            repo.update(c);
        }
    }

    //BUCAR
    public Categoria buscar(int id) {
        return repo.findById(id);
    }

    // Eliminar
    public void eliminar(int id) {
        repo.delete(id);
    }

}
