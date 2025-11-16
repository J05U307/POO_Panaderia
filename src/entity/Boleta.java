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
public class Boleta extends Comprobante {

    private String dni;

    public Boleta() {
    }

    public Boleta(String dni, int id, Pedido pedido, LocalDateTime fecha) {
        super(id, pedido, fecha);
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

}
