/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Rol;
import java.util.ArrayList;
import java.util.List;
import utils.SerializadorUtil;

/**
 *
 * @author josue
 */
public class RolRepository {

    private static final String FILE_PATH = "data/roles.dat";
    private static List<Rol> roles = new ArrayList<>();

    public RolRepository() {
        roles = SerializadorUtil.cargarLista(FILE_PATH);
    }

    public List<Rol> findAll() {
        return roles;
    }

    public void save(Rol rol) {
        roles.add(rol);
        SerializadorUtil.guardarLista(FILE_PATH, roles);
    }

    public void update(Rol rol) {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == rol.getId()) {
                roles.set(i, rol);
                SerializadorUtil.guardarLista(FILE_PATH, roles);
                return;
            }
        }
    }

    public void delete(int id) {
        roles.removeIf(c -> c.getId() == id);
        SerializadorUtil.guardarLista(FILE_PATH, roles);
    }

    public Rol findById(int id) {
        for (Rol c : roles) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

}
