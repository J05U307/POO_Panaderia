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
public class Pedido {
    
    private int id; 
    private LocalDateTime fecha; 
    private Cliente cliente; 
    private double total; 
    private TipoPago pago; 
    private Usuario usuario;

    public Pedido() {
    }

    public Pedido(int id, LocalDateTime fecha, Cliente cliente, double total, TipoPago pago, Usuario usuario) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.total = total;
        this.pago = pago;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public TipoPago getPago() {
        return pago;
    }

    public void setPago(TipoPago pago) {
        this.pago = pago;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
}
