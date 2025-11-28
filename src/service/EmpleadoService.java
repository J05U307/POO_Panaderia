/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.Empleado;
import java.time.LocalDate;
import java.util.List;
import repository.EmpleadoRepositoy;

/**
 *
 * @author josue
 */
public class EmpleadoService {

    private EmpleadoRepositoy repo = new EmpleadoRepositoy();

    //LISTAR
    public List<Empleado> listar() {
        return repo.findAll();
    }

    // agregar
    public boolean agregar(String nombre, String apellido, String dni, String celular, int edad, LocalDate fecha, double salario) {
        int id = 1;
        if (!repo.findAll().isEmpty()) {
            id = repo.findAll().get(repo.findAll().size() - 1).getId() + 1;
        }
        Empleado nuevo = new Empleado(id, nombre, apellido, dni, celular, edad, fecha, salario);

        repo.save(nuevo);
        return true;

    }

    // Bucar:
    public Empleado buscar(int id) {
        return repo.findById(id);
    }

    // editar
    public void editar(int id, String nombre, String apellido, String dni, String celular, int edad, LocalDate fecha, double salario) {
        Empleado e = repo.findById(id);
        if (e != null) {
            e.setNombre(nombre);
            e.setApellido(apellido);
            e.setDni(dni);
            e.setCelular(celular);
            e.setEdad(edad);
            e.setFechaIngreso(fecha);
            e.setSalario(salario);
            repo.update(e);
        }
    }

    // eliminar
    public void eliminar(int id) {
        repo.delete(id);
    }

    // ULTIMO ID: 
    public Empleado obtenerUltimo() {
        return repo.findLast();
    }

}
