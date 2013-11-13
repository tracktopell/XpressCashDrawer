/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpressosystems.xpresscashdrawer.view;

import javax.swing.JFrame;

/**
 *
 * @author Softtek
 */
public class DialogConfiguracionSistema extends javax.swing.JDialog {

	/**
	 * Creates new form DialogConfiguracionBTImpresora
	 */
	public DialogConfiguracionSistema(JFrame parent) {
		super(parent,true);
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        aceptar = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nombreNegocio = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        direccion = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        telefonos = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        clientePorDefault = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Datos de Negocio");
        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        aceptar.setText("Guardar");
        jPanel1.add(aceptar);

        cancelar.setText("Cancelar");
        jPanel1.add(cancelar);

        jPanel3.add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanel5.setLayout(new java.awt.GridLayout(5, 0));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nombre del Establecimiento :");
        jLabel1.setPreferredSize(new java.awt.Dimension(200, 19));
        jPanel2.add(jLabel1);

        nombreNegocio.setColumns(25);
        jPanel2.add(nombreNegocio);

        jPanel5.add(jPanel2);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Dirección :");
        jLabel2.setPreferredSize(new java.awt.Dimension(200, 19));
        jPanel4.add(jLabel2);

        direccion.setColumns(35);
        jPanel4.add(direccion);

        jPanel5.add(jPanel4);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Telefonos :");
        jLabel3.setPreferredSize(new java.awt.Dimension(200, 19));
        jPanel6.add(jLabel3);

        telefonos.setColumns(20);
        jPanel6.add(telefonos);

        jPanel5.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("EMail :");
        jLabel4.setPreferredSize(new java.awt.Dimension(200, 19));
        jPanel7.add(jLabel4);

        email.setColumns(15);
        jPanel7.add(email);

        jPanel5.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Cliente por default para venta:");
        jLabel5.setPreferredSize(new java.awt.Dimension(200, 19));
        jPanel8.add(jLabel5);

        clientePorDefault.setColumns(15);
        jPanel8.add(clientePorDefault);

        jPanel5.add(jPanel8);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(623, 285));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptar;
    private javax.swing.JButton cancelar;
    private javax.swing.JTextField clientePorDefault;
    private javax.swing.JTextField direccion;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTextField nombreNegocio;
    private javax.swing.JTextField telefonos;
    // End of variables declaration//GEN-END:variables

	/**
	 * @return the aceptar
	 */
	public javax.swing.JButton getAceptar() {
		return aceptar;
	}

	/**
	 * @return the cancelar
	 */
	public javax.swing.JButton getCancelar() {
		return cancelar;
	}

	/**
	 * @return the clientePorDefault
	 */
	public javax.swing.JTextField getClientePorDefault() {
		return clientePorDefault;
	}

	/**
	 * @return the direccion
	 */
	public javax.swing.JTextField getDireccion() {
		return direccion;
	}

	/**
	 * @return the email
	 */
	public javax.swing.JTextField getEmail() {
		return email;
	}

	/**
	 * @return the nombreNegocio
	 */
	public javax.swing.JTextField getNombreNegocio() {
		return nombreNegocio;
	}

	/**
	 * @return the telefonos
	 */
	public javax.swing.JTextField getTelefonos() {
		return telefonos;
	}
	
}
