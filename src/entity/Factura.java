/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDateTime;

/**
 *
 * @author josue
 */
public class Factura extends Comprobante {

    private String ruc;
    private String razonSocial;

    public Factura() {
    }

    public Factura(String ruc, String razonSocial, int id, Pedido pedido, LocalDateTime fecha, TipoPago pago, String serie, int numero) {
        super(id, pedido, fecha, pago, serie, numero);
        this.ruc = ruc;
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

}
