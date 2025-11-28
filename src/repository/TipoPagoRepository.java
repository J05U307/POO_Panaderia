/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.TipoPago;
import java.util.ArrayList;
import java.util.List;
import utils.SerializadorUtil;

/**
 *
 * @author josue
 */
public class TipoPagoRepository {

    private static final String FILE_PATH = "data/tipoPago.dat";
    private static List<TipoPago> tipospagos = new ArrayList<>();

    /*
    static {
        tipospagos.add(new TipoPago(1, "Efectivo"));
        tipospagos.add(new TipoPago(2, "Tarjeta"));
        tipospagos.add(new TipoPago(3, "Yape"));
    }
**/
    public TipoPagoRepository() {
        tipospagos = SerializadorUtil.cargarLista(FILE_PATH);
    }

    //Listar
    public List<TipoPago> findAll() {
        return tipospagos;
    }

    //Agregar 
    public void save(TipoPago pago) {
        tipospagos.add(pago);
        SerializadorUtil.guardarLista(FILE_PATH, tipospagos);
    }

    //Editar
    public void update(TipoPago pago) {
        for (int i = 0; i < tipospagos.size(); i++) {
            if (tipospagos.get(i).getId() == pago.getId()) {
                tipospagos.set(i, pago);
                SerializadorUtil.guardarLista(FILE_PATH, tipospagos);
                return;
            }
        }
    }

    // Elimianr
    public void delete(int id) {
        tipospagos.removeIf(c -> c.getId() == id);
        SerializadorUtil.guardarLista(FILE_PATH, tipospagos);
    }

    // Busacar
    public TipoPago findById(int id) {
        for (TipoPago c : tipospagos) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

}
