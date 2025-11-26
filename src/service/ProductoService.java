/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.Categoria;
import entity.Producto;
import java.util.List;
import repository.ProductoRepository;

/**
 *
 * @author josue
 */
public class ProductoService {

    private ProductoRepository repo = new ProductoRepository();

    // List
    public List<Producto> listar() {
        return repo.findAll();
    }

    // Agregar
    public void agregar(String nombre, double precio, int stock, Categoria categoria) {
        int id = 1;
        if (!repo.findAll().isEmpty()) {
            id = repo.findAll().get(repo.findAll().size() - 1).getId() + 1;
        }

        Producto nuevo = new Producto(id, nombre, precio, stock, categoria);
        repo.save(nuevo);
    }

    // EIDTAR
    public void editar(int id, String nombre, double precio, int stock, Categoria categoria) {
        Producto pro = repo.findById(id);

        if (pro != null) {
            pro.setNombre(nombre);
            pro.setPrecio(precio);
            pro.setStock(stock);
            pro.setCategoria(categoria);
            repo.update(pro);
        }
    }

    //Elimar
    public void eliminar(int id) {
        repo.delete(id);
    }
}
