/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.Empleado;
import entity.Rol;
import entity.Usuario;
import java.util.List;
import repository.UsuarioRepository;

/**
 *
 * @author josue
 */
public class UsuarioService {

    private UsuarioRepository repo = new UsuarioRepository();

    // LISTAR
    public List<Usuario> listar() {
        return repo.findAll();
    }

    // agregar
    public void agregar(String user, String password, Empleado empleado, Rol rol) {
        int nuevoId = 1;
        if (!repo.findAll().isEmpty()) {
            nuevoId = repo.findAll().get(repo.findAll().size() - 1).getId() + 1;
        }
        Usuario nuevo = new Usuario(nuevoId, user, password, empleado, rol);
        repo.save(nuevo);
    }

    //Editar
    public void editar(int id, String user, String password, Empleado empleado, Rol rol) {
        Usuario u = repo.findById(id);

        if (u != null) {
            u.setUser(user);
            u.setPassword(password);
            u.setEmpleado(empleado);
            u.setRol(rol);
            repo.update(u);
        }
    }

    // Eliminar
    public void eliminar(int id) {
        repo.delete(id);
    }
    
    // bucar:
    public Usuario buscar(int id){
        return repo.findById(id);
    }

}
