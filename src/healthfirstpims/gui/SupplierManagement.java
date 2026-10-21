
package healthfirstpims.gui;

import healthfirstpims.dao.SupplierDAO;
import healthfirstpims.models.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SupplierManagement extends JFrame {

    private JTextField txtName;
    private JTextField txtContactPerson;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextArea txtAddress;
    private JTable tblSuppliers;
    private DefaultTableModel tableModel;

    private final SupplierDAO supplierDAO = new SupplierDAO();

    public SupplierManagement() {
        setTitle("HealthFirstPIMS - Supplier Management");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createForm();
        loadSuppliers();
    }

    private void createForm() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("Supplier Management");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));

        txtName = new JTextField();
        txtContactPerson = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextArea(3, 20);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);

        formPanel.add(new JLabel("Supplier Name:"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Contact Person:"));
        formPanel.add(txtContactPerson);

        formPanel.add(new JLabel("Phone:"));
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Address:"));
        formPanel.add(new JScrollPane(txtAddress));

        tableModel = new DefaultTableModel(
            new String[]{
                "ID", "Supplier Name", "Contact Person",
                "Phone", "Email", "Address"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblSuppliers = new JTable(tableModel);
        tblSuppliers.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        tblSuppliers.setAutoCreateRowSorter(true);

        tblSuppliers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectSupplierRow();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnBack = new JButton("Close");

        btnAdd.addActionListener(e -> addSupplier());
        btnUpdate.addActionListener(e -> updateSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());
        btnClear.addActionListener(e -> clearFields());
        btnRefresh.addActionListener(e -> loadSuppliers());
        btnBack.addActionListener(e -> dispose());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(tblSuppliers), BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void loadSuppliers() {
        try {
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            tableModel.setRowCount(0);

            for (Supplier s : suppliers) {
                tableModel.addRow(new Object[]{
                    s.getSupplierId(),
                    s.getName(),
                    s.getContactPerson(),
                    s.getPhone(),
                    s.getEmail(),
                    s.getAddress()
                });
            }
        } catch (SQLException ex) {
            showError("Could not load suppliers.", ex);
        }
    }

    private void selectSupplierRow() {
        int selectedRow = tblSuppliers.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int row = tblSuppliers.convertRowIndexToModel(selectedRow);

        txtName.setText(valueAt(row, 1));
        txtContactPerson.setText(valueAt(row, 2));
        txtPhone.setText(valueAt(row, 3));
        txtEmail.setText(valueAt(row, 4));
        txtAddress.setText(valueAt(row, 5));
    }

    private String valueAt(int row, int column) {
        Object value = tableModel.getValueAt(row, column);
        return value == null ? "" : value.toString();
    }

    private Supplier readForm() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();

        if (name.isEmpty()) {
            throw new IllegalArgumentException(
                "Supplier name is required."
            );
        }

        if (!email.isEmpty()
                && !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                "Please enter a valid email address."
            );
        }

        Supplier s = new Supplier();
        s.setName(name);
        s.setContactPerson(txtContactPerson.getText().trim());
        s.setPhone(txtPhone.getText().trim());
        s.setEmail(email);
        s.setAddress(txtAddress.getText().trim());

        return s;
    }

    private void addSupplier() {
        try {
            Supplier s = readForm();

            if (supplierDAO.addSupplier(s)) {
                JOptionPane.showMessageDialog(
                    this, "Supplier added successfully."
                );
                loadSuppliers();
                clearFields();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(), "Validation",
                JOptionPane.WARNING_MESSAGE
            );
        } catch (SQLException ex) {
            showError("Could not add supplier.", ex);
        }
    }

    private void updateSupplier() {
        int selectedRow = tblSuppliers.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Select a supplier to update."
            );
            return;
        }

        int row = tblSuppliers.convertRowIndexToModel(selectedRow);
        int supplierId = Integer.parseInt(
            tableModel.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
            this, "Save changes to this supplier?",
            "Confirm Update", JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Supplier s = readForm();
            s.setSupplierId(supplierId);

            if (supplierDAO.updateSupplier(s)) {
                JOptionPane.showMessageDialog(
                    this, "Supplier updated successfully."
                );
                loadSuppliers();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(
                    this, "Supplier was not found."
                );
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(), "Validation",
                JOptionPane.WARNING_MESSAGE
            );
        } catch (SQLException ex) {
            showError("Could not update supplier.", ex);
        }
    }

    private void deleteSupplier() {
        int selectedRow = tblSuppliers.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Select a supplier to delete."
            );
            return;
        }

        int row = tblSuppliers.convertRowIndexToModel(selectedRow);
        int supplierId = Integer.parseInt(
            tableModel.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete this supplier? Suppliers linked to medicines "
                + "may not be deletable.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (supplierDAO.deleteSupplier(supplierId)) {
                JOptionPane.showMessageDialog(
                    this, "Supplier deleted successfully."
                );
                loadSuppliers();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(
                    this, "Supplier was not found."
                );
            }
        } catch (SQLException ex) {
            showError(
                "Could not delete supplier. Check whether this "
                    + "supplier is linked to medicines.",
                ex
            );
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtContactPerson.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        tblSuppliers.clearSelection();
        txtName.requestFocusInWindow();
    }

    private void showError(String message, Exception ex) {
        JOptionPane.showMessageDialog(
            this,
            message + "\n" + ex.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new SupplierManagement().setVisible(true)
        );
    }
}
