/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entity.Categoria;
import entity.Cliente;
import entity.Producto;
import entity.TipoPago;
import entity.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josue
 */
public class Data {

    // Usuarios
    public static List<Usuario> usuarios = new ArrayList<>();

    //Tipo Pago
    public static List<TipoPago> tipoPagos = new ArrayList<>();
    
    // Categoria
    public static List<Categoria> categorias = new ArrayList<>();

    //Productos
    public static List<Producto> productos = new ArrayList<>();

    //Clientes
    public static List<Cliente> clientes = new ArrayList<>();

}
