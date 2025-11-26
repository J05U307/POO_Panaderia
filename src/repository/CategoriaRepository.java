/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Categoria;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josue
 */
public class CategoriaRepository {

    private static List<Categoria> categorias = new ArrayList<>();

    static {
        categorias.add(new Categoria(1, "Bebidas"));
        categorias.add(new Categoria(2, "Snacks"));
        categorias.add(new Categoria(3, "Lácteos"));
    }
    

    //Listar
    public List<Categoria> findAll() {
        return categorias;
    }

    //Agregar 
    public void save(Categoria categoria) {
        categorias.add(categoria);
    }

    //Editar
    public void update(Categoria categoria) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getId() == categoria.getId()) {
                categorias.set(i, categoria);
                return;
            }
        }
    }

    // Elimianr
    public void delete(int id) {
        categorias.removeIf(c -> c.getId() == id);
    }

    // Busacar
    public Categoria findById(int id) {
        for (Categoria c : categorias) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }
}
