/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;


import entity.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josue
 */
public class UsuarioRepository {

    private static List<Usuario> usuarios = new ArrayList<>();

    // listar
    public List<Usuario> findAll() {
        return usuarios;
    }

    // agrega
    public void save(Usuario usuario) {
        usuarios.add(usuario);
    }

    // editar
    public void update(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuario.getId()) {
                usuarios.set(i, usuario);
                return;
            }
        }
    }

    // eliminari()
    public void delete(int id) {
        usuarios.removeIf(c -> c.getId() == id);
    }

    // Busacar
    public Usuario findById(int id) {
        for (Usuario us : usuarios) {
            if (us.getId() == id) {
                return us;
            }
        }
        return null;
    }

}
