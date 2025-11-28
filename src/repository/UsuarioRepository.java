/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import utils.SerializadorUtil;

/**
 *
 * @author josue
 */
public class UsuarioRepository {

    private static final String FILE_PATH = "data/usuarios.dat";
    private static List<Usuario> usuarios = new ArrayList<>();

    public UsuarioRepository() {
        usuarios = SerializadorUtil.cargarLista(FILE_PATH);
    }

// listar
    public List<Usuario> findAll() {
        return usuarios;

    }

    // agrega
    public void save(Usuario usuario) {
        usuarios.add(usuario);
        SerializadorUtil.guardarLista(FILE_PATH, usuarios);
    }

    // editar
    public void update(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuario.getId()) {
                usuarios.set(i, usuario);
                SerializadorUtil.guardarLista(FILE_PATH, usuarios);
                return;
            }
        }
    }

    // eliminari()
    public void delete(int id) {
        usuarios.removeIf(c -> c.getId() == id);
        SerializadorUtil.guardarLista(FILE_PATH, usuarios);
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
