/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author josue
 */
public class Comprobante implements Serializable{

    private int id;
    private Pedido pedido;
    private LocalDateTime fecha;
    private TipoPago pago;
    private String serie;
    private int numero;

    public Comprobante() {
    }

    public Comprobante(int id, Pedido pedido, LocalDateTime fecha, TipoPago pago, String serie, int numero) {
        this.id = id;
        this.pedido = pedido;
        this.fecha = fecha;
        this.pago = pago;
        this.serie = serie;
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public TipoPago getPago() {
        return pago;
    }

    public void setPago(TipoPago pago) {
        this.pago = pago;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

}
