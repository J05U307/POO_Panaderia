/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Producto;
import java.util.ArrayList;
import java.util.List;
import utils.SerializadorUtil;

/**
 *
 * @author josue
 */
public class ProductoRepository {

    private static final String FILE_PATH = "data/productos.dat";
    private static List<Producto> productos = new ArrayList<>();

    public ProductoRepository() {
        productos = SerializadorUtil.cargarLista(FILE_PATH);
    }

    //Listar
    public List<Producto> findAll() {
        return productos;
    }

    // Agregar
    public void save(Producto producto) {
        productos.add(producto);
        SerializadorUtil.guardarLista(FILE_PATH, productos);
    }

    // Editar
    public void update(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == producto.getId()) {
                productos.set(i, producto);
                SerializadorUtil.guardarLista(FILE_PATH, productos);
                return;
            }
        }
    }

    // ELIMAR
    public void delete(int id) {
        productos.removeIf(c -> c.getId() == id);
        SerializadorUtil.guardarLista(FILE_PATH, productos);
    }

    // buscar
    public Producto findById(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    // Buscar por nombre en tiempo real: 
    public List<Producto> findByNombreContains(String filtro) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

}
