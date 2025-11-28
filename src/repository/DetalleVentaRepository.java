/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.DetalleVenta;
import java.util.ArrayList;
import java.util.List;
import utils.SerializadorUtil;

/**
 *
 * @author josue
 */
public class DetalleVentaRepository {
    
    
    private static final String FILE_PATH = "data/detalleventa.dat";
    private static List<DetalleVenta> detalleventas = new ArrayList<>();

    public DetalleVentaRepository() {
        detalleventas = SerializadorUtil.cargarLista(FILE_PATH);
    }
    
    
    //Listar
    public List<DetalleVenta> findAll() {
        return detalleventas;
    }

    //Agregar 
    public void save(DetalleVenta venta) {
        detalleventas.add(venta);
        SerializadorUtil.guardarLista(FILE_PATH, detalleventas);
    }

    //Editar
    public void update(DetalleVenta venta) {
        for (int i = 0; i < detalleventas.size(); i++) {
            if (detalleventas.get(i).getId() == venta.getId()) {
                detalleventas.set(i, venta);
                SerializadorUtil.guardarLista(FILE_PATH, detalleventas);
                return;
            }
        }
    }

    // Elimianr
    public void delete(int id) {
        detalleventas.removeIf(c -> c.getId() == id);
        SerializadorUtil.guardarLista(FILE_PATH, detalleventas);
    }

    // Busacar
    public DetalleVenta findById(int id) {
        for (DetalleVenta c : detalleventas) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }
    
    
    
}
