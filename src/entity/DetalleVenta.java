/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;

/**
 *
 * @author josue
 */
public class DetalleVenta implements Serializable{

    private int id;
    private Pedido pedido;
    private Producto producto;
    private int cantidad;
    private double precioVendido;
    private double subtotal;

    public DetalleVenta() {
    }

    public DetalleVenta(int id, Pedido pedido, Producto producto, int cantidad, double precioVendido,double subtotal) {
        this.id = id;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVendido = precioVendido;
        this.subtotal = subtotal;
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

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getPrecioVendido() {
        return precioVendido;
    }

    public void setPrecioVendido(double precioVendido) {
        this.precioVendido = precioVendido;
    }
    
    

}
