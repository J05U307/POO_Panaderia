/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josue
 */
public class ProductoRepository {

    private static List<Producto> productos = new ArrayList<>();

    
    static{
        
    }
    //Listar
    public List<Producto> findAll() {
        return productos;
    }

    // Agregar
    public void save(Producto producto) {
        productos.add(producto);
    }

    // Editar
    public void update(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == producto.getId()) {
                productos.set(i, producto);
                return;
            }
        }
    }
    
    // ELIMAR
    
    public void delete(int id){
        productos.removeIf(c -> c.getId() == id);
    }
    
    // buscar
    public Producto findById(int id){
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

}
