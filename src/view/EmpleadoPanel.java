/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import entity.Empleado;
import entity.Rol;
import entity.Usuario;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import service.EmpleadoService;
import service.RolService;
import service.UsuarioService;

/**
 *
 * @author josue
 */
public class EmpleadoPanel extends javax.swing.JPanel {

    private EmpleadoService serviceEmpleado = new EmpleadoService();
    private UsuarioService serviceUsuario = new UsuarioService();
    private RolService serviceRol = new RolService();

    /**
     * Creates new form EmpleadoPanel
     */
    public EmpleadoPanel() {
        initComponents();
        listar();
        cargarComboRol();
    }

    private static class DatosFormulario {

        String nombre;
        String apellido;
        String dni;
        String celular;
        int edad;
        LocalDate fechaIngreso;
        double salario;
        String user;
        String password;
        Rol rol;
    }

    private DatosFormulario obtenerDatosFormulario() {

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDNI.getText().trim();
        String celular = txtCelular.getText().trim();
        String edadTxt = txtEdad.getText().trim();
        String salarioTxt = txtSalario.getText().trim();
        String user = txtUser.getText().trim();
        String password = txtPassword.getText().trim();
        int rolIndex = comboRol.getSelectedIndex();
        Date fechaDate = dateIngreso.getDate();

        // ===== VALIDACIONES =====
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || celular.isEmpty()
                || edadTxt.isEmpty() || salarioTxt.isEmpty()
                || user.isEmpty() || password.isEmpty()
                || fechaDate == null || rolIndex == 0) {

            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return null;
        }

        if (!dni.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(this, "El DNI debe tener exactamente 8 dígitos.");
            return null;
        }

        if (!celular.matches("\\d{9}")) {
            JOptionPane.showMessageDialog(this, "El celular debe tener exactamente 9 dígitos.");
            return null;
        }

        int edad;
        double salario;

        try {
            edad = Integer.parseInt(edadTxt);
            salario = Double.parseDouble(salarioTxt);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Edad o salario inválidos.");
            return null;
        }

        // Obtener LocalDate del JDateChooser
        LocalDate fechaIngreso = fechaDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Obtener rol
        Rol rol = serviceRol.listar().get(rolIndex - 1);

        // ===== LLENAR OBJETO =====
        DatosFormulario datos = new DatosFormulario();
        datos.nombre = nombre;
        datos.apellido = apellido;
        datos.dni = dni;
        datos.celular = celular;
        datos.edad = edad;
        datos.fechaIngreso = fechaIngreso;
        datos.salario = salario;
        datos.user = user;
        datos.password = password;
        datos.rol = rol;

        return datos;
    }

    private void editar() {
        int fila = tablaEmpleado.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para editar");
            return;
        }

        DatosFormulario datos = obtenerDatosFormulario();
        if (datos == null) {
            return;
        }

        int idUsuario = (int) tablaEmpleado.getValueAt(fila, 0);
        int idEmpleado = (int) tablaEmpleado.getValueAt(fila, 1);

        serviceEmpleado.editar(
                idEmpleado, datos.nombre, datos.apellido, datos.dni,
                datos.celular, datos.edad, datos.fechaIngreso, datos.salario
        );

        Empleado empleadoActualizado = serviceEmpleado.buscar(idEmpleado);

        serviceUsuario.editar(
                idUsuario, datos.user, datos.password,
                empleadoActualizado, datos.rol
        );

        listar();
        limpiar();
        JOptionPane.showMessageDialog(this, "Empleado editado correctamente");
    }

    private void eliminar() {

        int fila = tablaEmpleado.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para elimar");
            return;
        }

        int idUsuario = (int) tablaEmpleado.getValueAt(fila, 0); // USUARIO
        int idEmpleado = (int) tablaEmpleado.getValueAt(fila, 1); // Empleado

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar este Usuario?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            serviceUsuario.eliminar(idUsuario);
            serviceEmpleado.eliminar(idEmpleado);
            listar();
            limpiar();
            JOptionPane.showMessageDialog(this, "Empleado eliminado correctamente");
        }
    }

    private void obtenerDatosTabla() {
        int fila = tablaEmpleado.getSelectedRow();

        if (fila != -1) {

            String nombre = tablaEmpleado.getValueAt(fila, 2).toString();
            String apellido = tablaEmpleado.getValueAt(fila, 3).toString();
            String dni = tablaEmpleado.getValueAt(fila, 4).toString();
            String celular = tablaEmpleado.getValueAt(fila, 5).toString();
            String edad = tablaEmpleado.getValueAt(fila, 6).toString();
            LocalDate fechaIngreso = (LocalDate) tablaEmpleado.getValueAt(fila, 7);
            String salario = tablaEmpleado.getValueAt(fila, 8).toString();
            String user = tablaEmpleado.getValueAt(fila, 9).toString();
            String password = tablaEmpleado.getValueAt(fila, 10).toString();
            String rolNombre = tablaEmpleado.getValueAt(fila, 11).toString();

            // Asignar 
            // Asignar a campos del formulario
            txtNombre.setText(nombre);
            txtApellido.setText(apellido);
            txtDNI.setText(dni);
            txtCelular.setText(celular);
            txtEdad.setText(edad);
            txtSalario.setText(salario);
            txtUser.setText(user);
            txtPassword.setText(password);

            // Convertir LocalDate a Date para el JDateChooser
            Date fecha = Date.from(
                    fechaIngreso.atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
            );
            dateIngreso.setDate(fecha);

            // Seleccionar el rol correcto en el combo
            for (int i = 1; i < comboRol.getItemCount(); i++) {
                if (comboRol.getItemAt(i).equalsIgnoreCase(rolNombre)) {
                    comboRol.setSelectedIndex(i);
                    break;
                }
            }

            btnAgregar.setEnabled(false);
        }
    }

    private void agregar() {

        DatosFormulario datos = obtenerDatosFormulario();
        if (datos == null) {
            return; // validación falló
        }
        boolean creado = serviceEmpleado.agregar(
                datos.nombre, datos.apellido, datos.dni,
                datos.celular, datos.edad, datos.fechaIngreso, datos.salario
        );

        if (creado) {
            Empleado nuevo = serviceEmpleado.obtenerUltimo();
            serviceUsuario.agregar(datos.user, datos.password, nuevo, datos.rol);

            JOptionPane.showMessageDialog(this, "Empleado agregado correctamente");
        }

        listar();
        limpiar();
    }

    private void limpiar() {

        txtNombre.setText("");
        txtApellido.setText("");
        txtDNI.setText("");
        txtCelular.setText("");
        txtEdad.setText("");
        txtSalario.setText("");
        txtUser.setText("");
        txtPassword.setText("");
        dateIngreso.setDate(null);
        comboRol.setSelectedIndex(0);
        btnAgregar.setEnabled(true);
    }

    private void cargarComboRol() {
        comboRol.removeAllItems();
        comboRol.addItem("Seleccione Rol");
        for (Rol rol : serviceRol.listar()) {
            comboRol.addItem(rol.getNombre());
        }
    }

    private void listar() {
        DefaultTableModel modelo = (DefaultTableModel) tablaEmpleado.getModel();
        modelo.setRowCount(0);

        for (Usuario u : serviceUsuario.listar()) {
            Object[] fila = {
                u.getId(),
                u.getEmpleado().getId(),
                u.getEmpleado().getNombre(),
                u.getEmpleado().getApellido(),
                u.getEmpleado().getDni(),
                u.getEmpleado().getCelular(),
                u.getEmpleado().getEdad(),
                u.getEmpleado().getFechaIngreso(),
                u.getEmpleado().getSalario(),
                u.getUser(),
                u.getPassword(),
                u.getRol().getNombre()
            };
            modelo.addRow(fila);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        dateIngreso = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtDNI = new javax.swing.JTextField();
        txtCelular = new javax.swing.JTextField();
        txtEdad = new javax.swing.JTextField();
        txtSalario = new javax.swing.JTextField();
        txtUser = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        comboRol = new javax.swing.JComboBox<>();
        btnNuevo = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEmpleado = new javax.swing.JTable();

        jLabel1.setText("Empleado");

        jLabel2.setText("Nombre");

        jLabel3.setText("Apellido");

        jLabel4.setText("DNI");

        jLabel5.setText("Celular");

        jLabel6.setText("Edad");

        jLabel7.setText("Fecha Ingreso");

        jLabel8.setText("Salario");

        jLabel9.setText("User");

        jLabel10.setText("Password");

        jLabel11.setText("Rol");

        comboRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        tablaEmpleado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "ID Empleado", "Nombre", "Apellido", "DNI", "Celular", "Edad", "F. Ingreso", "Salario", "User", "Password", "Rol"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEmpleadoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaEmpleado);
        if (tablaEmpleado.getColumnModel().getColumnCount() > 0) {
            tablaEmpleado.getColumnModel().getColumn(0).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(1).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(2).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(3).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(4).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(5).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(6).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(7).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(8).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(9).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(10).setResizable(false);
            tablaEmpleado.getColumnModel().getColumn(11).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAgregar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar)))
                .addContainerGap(651, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                    .addComponent(txtCelular)
                    .addComponent(txtNombre)
                    .addComponent(txtApellido)
                    .addComponent(txtDNI)
                    .addComponent(txtEdad)
                    .addComponent(txtSalario)
                    .addComponent(txtUser)
                    .addComponent(txtPassword)
                    .addComponent(comboRol, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtEdad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(dateIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(comboRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNuevo)
                            .addComponent(btnAgregar)
                            .addComponent(btnEditar)
                            .addComponent(btnEliminar)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregar();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void tablaEmpleadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEmpleadoMouseClicked
        obtenerDatosTabla();
    }//GEN-LAST:event_tablaEmpleadoMouseClicked

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        limpiar();
        listar();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> comboRol;
    private com.toedter.calendar.JDateChooser dateIngreso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaEmpleado;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCelular;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtEdad;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtSalario;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
