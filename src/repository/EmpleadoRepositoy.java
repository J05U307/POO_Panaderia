/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Empleado;
import java.util.ArrayList;
import java.util.List;
import utils.SerializadorUtil;

/**
 *
 * @author josue
 */
public class EmpleadoRepositoy {

    private static final String FILE_PATH = "data/empleados.dat";
    private static List<Empleado> empleados = new ArrayList<>();

    public EmpleadoRepositoy() {

        empleados = SerializadorUtil.cargarLista(FILE_PATH);
    }

    //listar
    public List<Empleado> findAll() {
        return empleados;
    }

    //agregar
    public void save(Empleado empleado) {
        empleados.add(empleado);
        SerializadorUtil.guardarLista(FILE_PATH, empleados);
    }

    // editar
    public void update(Empleado empleado) {
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getId() == empleado.getId()) {
                empleados.set(i, empleado);
                SerializadorUtil.guardarLista(FILE_PATH, empleados);
                return;
            }
        }
    }

    // eliminar
    public void delete(int id) {
        empleados.removeIf(c -> c.getId() == id);
        SerializadorUtil.guardarLista(FILE_PATH, empleados);
    }

    //Buscar
    public Empleado findById(int id) {
        for (Empleado empleado : empleados) {
            if (empleado.getId() == id) {
                return empleado;
            }
        }
        return null;
    }

    // ULTIMO  ID: 
    public Empleado findLast() {
        if (empleados.isEmpty()) {
            return null;
        }
        return empleados.get(empleados.size() - 1);
    }
}
