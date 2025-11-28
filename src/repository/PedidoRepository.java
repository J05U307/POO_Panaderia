/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Pedido;
import entity.Usuario;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utils.SerializadorUtil;

/**
 *
 * @author josue
 */
public class PedidoRepository {

    private static final String FILE_PATH = "data/pedidos.dat";
    private static List<Pedido> pedidos = new ArrayList<>();

    public PedidoRepository() {
        pedidos = SerializadorUtil.cargarLista(FILE_PATH);
    }

    //Listar
    public List<Pedido> findAll() {
        return pedidos;
    }

    //Agregar 
    public void save(Pedido pedido) {
        pedidos.add(pedido);
        SerializadorUtil.guardarLista(FILE_PATH, pedidos);
    }

    //Editar
    public void update(Pedido pedido) {
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getId() == pedido.getId()) {
                pedidos.set(i, pedido);
                SerializadorUtil.guardarLista(FILE_PATH, pedidos);
                return;
            }
        }
    }

    // Elimianr
    public void delete(int id) {
        pedidos.removeIf(c -> c.getId() == id);
        SerializadorUtil.guardarLista(FILE_PATH, pedidos);
    }

    // Busacar
    public Pedido findById(int id) {
        for (Pedido c : pedidos) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public List<Pedido> findByFecha(LocalDate fechaBuscada) {
        List<Pedido> resultado = new ArrayList<>();

        for (Pedido p : findAll()) {
            if (p.getFecha().toLocalDate().equals(fechaBuscada)) {
                resultado.add(p);
            }
        }

        return resultado;
    }

    public List<Pedido> findByUsuario(Usuario usuario) {
        List<Pedido> resultado = new ArrayList<>();

        for (Pedido p : pedidos) {
            if (p.getUsuario().getId() == usuario.getId()) {
                resultado.add(p);
            }
        }

        return resultado;
    }

}
