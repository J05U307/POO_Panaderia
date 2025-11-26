/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Empleado;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josue
 */
public class EmpleadoRepositoy {

    private static List<Empleado> empleados = new ArrayList<>();

    //listar
    public List<Empleado> findAll() {
        return empleados;
    }

    //agregar
    public void save(Empleado empleado) {
        empleados.add(empleado);
    }

    // editar
    public void update(Empleado empleado) {
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getId() == empleado.getId()) {
                empleados.set(i, empleado);
            }
        }
    }

    // eliminar
    public void delete(int id) {
        empleados.removeIf(c -> c.getId() == id);
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
}
