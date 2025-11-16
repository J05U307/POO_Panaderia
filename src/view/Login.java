/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import entity.Categoria;
import entity.Empleado;
import entity.Rol;
import entity.Usuario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import data.Data;
import entity.Producto;

public class Login extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Login.class.getName());

    /**
     * Creates new form Login
     */
    private List<Usuario> usuarios = new ArrayList<>();

    public Login() {

        initComponents();
        this.setLocationRelativeTo(null);
        cargarDatos();

    }

    // Creacion de objetos 
    private void cargarDatos() {
        // Creacion de rol
        Rol vend = new Rol(1, "Vendedor");
        Rol caj = new Rol(2, "Cajero");
        Rol admi = new Rol(3, "Administrador");

        // Creacion de empleados: 
        Empleado emple1 = new Empleado(1, "Carlos", "Ramirez", "75481236", "987654321", 25, LocalDate.of(2023, 5, 10), 1800);
        Empleado emple2 = new Empleado(2, "Maria", "Lopez", "70894512", "912345678", 30, LocalDate.of(2022, 11, 3), 2200);
        Empleado emple3 = new Empleado(3, "Jose", "Fernandez", "72145896", "900112233", 28, LocalDate.of(2024, 1, 20), 1950);

        // Creacion de Usuarios: 
        Usuario user1 = new Usuario(1, "carlos", "123", emple1, vend);
        Usuario user2 = new Usuario(2, "maria", "222", emple2, caj);
        Usuario user3 = new Usuario(3, "jose", "333", emple3, admi);
        usuarios.add(user1);
        usuarios.add(user2);
        usuarios.add(user3);

        // Datos Categoria
        Categoria ca1 = new Categoria(1, "Pan");
        Categoria ca2 = new Categoria(2, "Torta");
        Categoria ca3 = new Categoria(3, "queques");
        Categoria ca4 = new Categoria(4, "nose");
        Categoria ca5 = new Categoria(5, "nosejas");
        Data.categorias.add(ca1);
        Data.categorias.add(ca2);
        Data.categorias.add(ca3);
        Data.categorias.add(ca4);
        Data.categorias.add(ca5);

        
        // Datos Productos
        Producto p1 = new Producto(1, "Pan francés", 0.50, 50, ca1); // Pan
        Producto p2 = new Producto(2, "Torta chocolate", 20.0, 10, Data.categorias.get(1));
        Producto p3 = new Producto(3, "Queque vainilla", 8.0, 15, Data.categorias.get(2)); 
        Producto p4 = new Producto(4, "Pan integral", 1.20, 30, Data.categorias.get(3)); 
        Producto p5 = new Producto(5, "Producto especial", 2.0, 0, Data.categorias.get(4)); 
        Data.productos.add(p1);
        Data.productos.add(p2);
        Data.productos.add(p3);
        Data.productos.add(p4);
        Data.productos.add(p5);

    
    }

    private void login(String user, String pass) {

        for (Usuario u : usuarios) {
            if (u.getUser().equals(user) && u.getPassword().equals(pass)) {

                Inicio ini = new Inicio();
                ini.setVisible(true);
                this.dispose();

                return;
            }
        }

        // System.out.println("Usuario o contraseña incorrectos.");
        JOptionPane.showMessageDialog(this, "Usuario o clave incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("USER: ");

        jLabel2.setText("PASSWORD: ");

        jButton1.setText("INICAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(203, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(94, 94, 94)
                .addComponent(jButton1)
                .addContainerGap(134, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String user = txtUser.getText();
        String pass = txtPassword.getText();

        login(user, pass);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
