/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package healthfirstpims.gui;
import healthfirstpims.dao.MedicineDAO;
import healthfirstpims.models.Medicine;

import java.sql.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class MedicineManagement extends javax.swing.JFrame {
    private MedicineDAO medicineDAO = new MedicineDAO();
    private void updateMedicine() {
    int selectedRow = tblMedicines.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Please select a medicine to update."
        );
        return;
    }

    try {
        int row = tblMedicines.convertRowIndexToModel(selectedRow);
        int medicineId = Integer.parseInt(
            tblMedicines.getModel().getValueAt(row, 0).toString()
        );

        Medicine medicine = new Medicine();

        medicine.setMedicineId(medicineId);
        medicine.setName(txtName.getText().trim());
        medicine.setCompany(txtCompany.getText().trim());
        medicine.setMedicineType(txtType.getText().trim());
        medicine.setPrice(Double.parseDouble(txtPrice.getText().trim()));
        medicine.setQuantityInStock(Integer.parseInt(txtStock.getText().trim()));
        medicine.setReorderLevel(
            Integer.parseInt(txtReorderLevel.getText().trim())
        );
        medicine.setExpiryDate(
            java.sql.Date.valueOf(txtExpiryDate.getText().trim())
        );
        medicine.setSupplierId(
            Integer.parseInt(txtSupplierId.getText().trim())
        );

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Save changes to this medicine?",
            "Confirm Update",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        MedicineDAO dao = new MedicineDAO();
        dao.updateMedicine(medicine);

        javax.swing.JOptionPane.showMessageDialog(
            this, "Medicine updated successfully."
        );

        loadMedicines();
        clearFields();

    } catch (NumberFormatException ex) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Price, stock, reorder level and supplier ID must be valid numbers."
        );
    } catch (IllegalArgumentException ex) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Enter a valid expiry date in YYYY-MM-DD format."
        );
    } catch (Exception ex) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Could not update medicine: " + ex.getMessage()
        );
    }
}

    private void deleteMedicine() {
    int selectedRow = tblMedicines.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Please select a medicine to delete."
        );
        return;
    }

    int confirm = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete the selected medicine?",
        "Confirm Delete",
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.WARNING_MESSAGE
    );

    if (confirm != javax.swing.JOptionPane.YES_OPTION) {
        return;
    }

    try {
        int row = tblMedicines.convertRowIndexToModel(selectedRow);
        int medicineId = Integer.parseInt(
            tblMedicines.getModel().getValueAt(row, 0).toString()
        );

        MedicineDAO dao = new MedicineDAO();
        dao.deleteMedicine(medicineId);

        javax.swing.JOptionPane.showMessageDialog(
            this, "Medicine deleted successfully."
        );

        loadMedicines();
        clearFields();

    } catch (Exception ex) {
        javax.swing.JOptionPane.showMessageDialog(
            this, "Could not delete medicine: " + ex.getMessage()
        );
    }
}

    
    private void loadMedicines() {

    DefaultTableModel model =
            (DefaultTableModel) tblMedicines.getModel();

    model.setRowCount(0);

    List<Medicine> medicines =
            medicineDAO.getAllMedicines();

    for (Medicine medicine : medicines) {

        model.addRow(new Object[]{
            medicine.getMedicineId(),
            medicine.getName(),
            medicine.getCompany(),
            medicine.getMedicineType(),
            medicine.getPrice(),
            medicine.getQuantityInStock(),
            medicine.getReorderLevel(),
            medicine.getExpiryDate(),
            medicine.getSupplierId()
        });
    }
}

private void clearFields() {

    txtName.setText("");
    txtCompany.setText("");
    txtType.setText("");
    txtPrice.setText("");
    txtStock.setText("");
    txtReorderLevel.setText("");
    txtExpiryDate.setText("");
    txtSupplierId.setText("");
}
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MedicineManagement.class.getName());

    /**
     * Creates new form MedicineManagement
     */
    public MedicineManagement() {
        initComponents();
        loadMedicines();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtName = new javax.swing.JTextField();
        txtCompany = new javax.swing.JTextField();
        txtType = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        txtReorderLevel = new javax.swing.JTextField();
        txtExpiryDate = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMedicines = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSupplierId = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtCompany.addActionListener(this::txtCompanyActionPerformed);

        btnAdd.setText("Add");
        btnAdd.addActionListener(this::btnAddActionPerformed);

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);

        btnDelete.setText("Delete");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        btnClear.setText("Clear");
        btnClear.addActionListener(this::btnClearActionPerformed);

        tblMedicines.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "`ID", "Name", "Company", "Type", "Price", "Stock", "Reorder Level", "Expiry Date", "Supplier ID"
            }
        ));
        jScrollPane1.setViewportView(tblMedicines);

        jLabel1.setText("Medicine name:");

        jLabel2.setText("Company:");

        jLabel3.setText("Type:");

        jLabel4.setText("Price:");

        jLabel5.setText("Stock:");

        jLabel6.setText("Reorder level:");

        jLabel7.setText("Exipiry date:");

        jLabel8.setText("Supplier Id:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(86, 86, 86)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel8)
                                .addGap(31, 31, 31)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSupplierId, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                    .addComponent(txtExpiryDate)
                                    .addComponent(txtReorderLevel)))
                            .addComponent(txtStock)
                            .addComponent(txtPrice)
                            .addComponent(txtType, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCompany)
                            .addComponent(txtName)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(btnAdd)
                        .addGap(30, 30, 30)
                        .addComponent(btnUpdate)
                        .addGap(36, 36, 36)
                        .addComponent(btnDelete)
                        .addGap(62, 62, 62)
                        .addComponent(btnClear))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtReorderLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtExpiryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupplierId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnClear))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCompanyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCompanyActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        try {

        String name = txtName.getText().trim();
        String company = txtCompany.getText().trim();
        String type = txtType.getText().trim();

        double price =
                Double.parseDouble(txtPrice.getText().trim());

        int stock =
                Integer.parseInt(txtStock.getText().trim());

        int reorderLevel =
                Integer.parseInt(
                        txtReorderLevel.getText().trim());

        Date expiryDate =
                Date.valueOf(
                        txtExpiryDate.getText().trim());

        int supplierId =
                Integer.parseInt(
                        txtSupplierId.getText().trim());

        Medicine medicine = new Medicine();

        medicine.setName(name);
        medicine.setCompany(company);
        medicine.setMedicineType(type);
        medicine.setPrice(price);
        medicine.setQuantityInStock(stock);
        medicine.setReorderLevel(reorderLevel);
        medicine.setExpiryDate(expiryDate);
        medicine.setSupplierId(supplierId);

        if (medicineDAO.addMedicine(medicine)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Medicine added successfully."
            );

            loadMedicines();
            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add medicine."
            );
        }

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
                this,
                "Please enter valid numbers for price, stock, reorder level and supplier ID."
        );

    } catch (IllegalArgumentException e) {

        JOptionPane.showMessageDialog(
                this,
                "Expiry date must use YYYY-MM-DD."
        );
    }

    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        updateMedicine();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        deleteMedicine();
    }//GEN-LAST:event_btnDeleteActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new MedicineManagement().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblMedicines;
    private javax.swing.JTextField txtCompany;
    private javax.swing.JTextField txtExpiryDate;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtReorderLevel;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtSupplierId;
    private javax.swing.JTextField txtType;
    // End of variables declaration//GEN-END:variables
}
