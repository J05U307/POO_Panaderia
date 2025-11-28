/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Categoria;
import java.util.ArrayList;
import java.util.List;
import utils.SerializadorUtil;

/**
 *
 * @author josue
 */
public class CategoriaRepository {

    private static final String FILE_PATH = "data/categorias.dat";
    private static List<Categoria> categorias = new ArrayList<>();


    public CategoriaRepository() {

        categorias = SerializadorUtil.cargarLista(FILE_PATH);
    }

    //Listar
    public List<Categoria> findAll() {
        return categorias;
    }

    //Agregar 
    public void save(Categoria categoria) {
        categorias.add(categoria);
        SerializadorUtil.guardarLista(FILE_PATH, categorias);
    }

    //Editar
    public void update(Categoria categoria) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getId() == categoria.getId()) {
                categorias.set(i, categoria);
                SerializadorUtil.guardarLista(FILE_PATH, categorias);
                return;
            }
        }
    }

    // Elimianr
    public void delete(int id) {
        categorias.removeIf(c -> c.getId() == id);
        SerializadorUtil.guardarLista(FILE_PATH, categorias);
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
