/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.Boleta;
import entity.Comprobante;
import entity.Factura;
import entity.Pedido;
import entity.TipoPago;
import java.time.LocalDateTime;
import repository.ComprobanteRepository;

/**
 *
 * @author josue
 */
public class ComprobanteService {

    private ComprobanteRepository repo = new ComprobanteRepository();

    public Boleta crearBoleta(Pedido pedido, TipoPago pago, String dni) {

        int nuevoId = 1;
        if (!repo.findAll().isEmpty()) {
            nuevoId = repo.findAll().get(repo.findAll().size() - 1).getId() + 1;
        }

        Boleta boleta = new Boleta(dni, nuevoId, pedido, LocalDateTime.now(), pago, "B001", generarNumero("B001"));
        repo.save(boleta);
        return boleta;
    }

    public Factura crearFactura(Pedido pedido, TipoPago pago, String ruc, String razonSocial) {

        int nuevoId = 1;
        if (!repo.findAll().isEmpty()) {
            nuevoId = repo.findAll().get(repo.findAll().size() - 1).getId() + 1;
        }

        Factura factura = new Factura(ruc, razonSocial, nuevoId, pedido, LocalDateTime.now(), pago, "F001", generarNumero("F001"));
        repo.save(factura);
        return factura;
    }

    private int generarNumero(String serie) {
        Comprobante ultimo = repo.findUltimoPorSerie(serie);
        if (ultimo == null) {
            return 1;
        }

        return ultimo.getNumero() + 1;
    }
}
