/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import entity.Comprobante;
import entity.Pedido;
import java.util.ArrayList;
import java.util.List;
import utils.SerializadorUtil;

/**
 *
 * @author josue
 */
public class ComprobanteRepository {

    private static final String FILE_PATH = "data/comprobantes.dat";
    private static List<Comprobante> comporobantes = new ArrayList<>();

    public ComprobanteRepository() {
        comporobantes = SerializadorUtil.cargarLista(FILE_PATH);
    }

    //Listar
    public List<Comprobante> findAll() {
        return comporobantes;
    }

    public void save(Comprobante comprobante) {
        comporobantes.add(comprobante);
        SerializadorUtil.guardarLista(FILE_PATH, comporobantes);
    }

    public Comprobante findById(int id) {
        for (Comprobante c : comporobantes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public Comprobante findUltimoPorSerie(String serie) {
        Comprobante ultimo = null;

        for (Comprobante c : comporobantes) {
            if (c.getSerie() != null && c.getSerie().equals(serie)) {

                // Si es el primero encontrado
                if (ultimo == null) {
                    ultimo = c;
                } // Si tiene un número mayor que el actual
                else if (c.getNumero() > ultimo.getNumero()) {
                    ultimo = c;
                }
            }
        }

        return ultimo;
    }

    public Comprobante findByPedido(Pedido pedido) {
        for (Comprobante c : comporobantes) {
            if (c.getPedido().getId() == pedido.getId()) {
                return c;
            }
        }
        return null;
    }

}
