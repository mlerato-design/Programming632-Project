
package healthfirstpims.gui;

import healthfirstpims.database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserManagement extends JFrame {

    private JTextField txtUsername;
    private JTextField txtFullName;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JTable tblUsers;
    private DefaultTableModel tableModel;

    public UserManagement() {
        setTitle("HealthFirstPIMS - User Management");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createForm();
        loadUsers();
    }

    private void createForm() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("User Management");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(heading, BorderLayout.NORTH);

        txtUsername = new JTextField();
        txtFullName = new JTextField();
        txtPassword = new JPasswordField();

        cmbRole = new JComboBox<>(new String[]{"Admin", "Cashier"});

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));

        formPanel.add(new JLabel("Username:"));
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(txtFullName);

        formPanel.add(new JLabel("Password (new/reset):"));
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("Role:"));
        formPanel.add(cmbRole);

        tableModel = new DefaultTableModel(
            new String[]{"ID", "Username", "Full Name", "Role"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblUsers = new JTable(tableModel);
        tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsers.setAutoCreateRowSorter(true);

        tblUsers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectUserRow();
            }
        });

        JButton btnAdd = new JButton("Add User");
        JButton btnUpdate = new JButton("Update User");
        JButton btnDelete = new JButton("Delete User");
        JButton btnClear = new JButton("Clear");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnClose = new JButton("Close");

        btnAdd.addActionListener(e -> addUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnClear.addActionListener(e -> clearFields());
        btnRefresh.addActionListener(e -> loadUsers());
        btnClose.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClose);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(tblUsers), BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void loadUsers() {
        String sql =
            "SELECT user_id, username, full_name, role "
            + "FROM users ORDER BY user_id";

        tableModel.setRowCount(0);

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("full_name"),
                    rs.getString("role")
                });
            }

        } catch (SQLException ex) {
            showError("Could not load users.", ex);
        }
    }

    private void selectUserRow() {
        int selectedRow = tblUsers.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int row = tblUsers.convertRowIndexToModel(selectedRow);

        txtUsername.setText(tableModel.getValueAt(row, 1).toString());
        txtFullName.setText(tableModel.getValueAt(row, 2).toString());
        cmbRole.setSelectedItem(tableModel.getValueAt(row, 3).toString());

        // Never retrieve or display the stored password.
        txtPassword.setText("");
    }

    private boolean validateForm(boolean requirePassword) {
        String username = txtUsername.getText().trim();
        String fullName = txtFullName.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Username and full name are required."
            );
            return false;
        }

        if (requirePassword && password.isBlank()) {
            JOptionPane.showMessageDialog(
                this, "Enter a password for the new account."
            );
            return false;
        }

        if (!password.isEmpty() && password.length() < 8) {
            JOptionPane.showMessageDialog(
                this, "Passwords must be at least 8 characters long."
            );
            return false;
        }

        return true;
    }

    private void addUser() {
        if (!validateForm(true)) {
            return;
        }

        String sql =
            "INSERT INTO users (username, password, role, full_name) "
            + "VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtUsername.getText().trim());
            ps.setString(2, new String(txtPassword.getPassword()));
            ps.setString(3, cmbRole.getSelectedItem().toString());
            ps.setString(4, txtFullName.getText().trim());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                this, "User account added successfully."
            );

            loadUsers();
            clearFields();

        } catch (SQLException ex) {
            showError(
                "Could not add user. Check whether the username "
                    + "already exists.",
                ex
            );
        }
    }

    private void updateUser() {
        int selectedRow = tblUsers.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Select a user to update."
            );
            return;
        }

        if (!validateForm(false)) {
            return;
        }

        int row = tblUsers.convertRowIndexToModel(selectedRow);
        int userId = Integer.parseInt(
            tableModel.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
            this, "Save changes to this user?",
            "Confirm Update", JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String password = new String(txtPassword.getPassword());

        String sql;

        if (password.isEmpty()) {
            sql = "UPDATE users SET username=?, full_name=?, role=? "
                + "WHERE user_id=?";
        } else {
            sql = "UPDATE users SET username=?, full_name=?, role=?, "
                + "password=? WHERE user_id=?";
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtUsername.getText().trim());
            ps.setString(2, txtFullName.getText().trim());
            ps.setString(3, cmbRole.getSelectedItem().toString());

            if (password.isEmpty()) {
                ps.setInt(4, userId);
            } else {
                ps.setString(4, password);
                ps.setInt(5, userId);
            }

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(
                    this, "User updated successfully."
                );
                loadUsers();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(
                    this, "User was not found."
                );
            }

        } catch (SQLException ex) {
            showError(
                "Could not update user. Check whether the username "
                    + "already exists.",
                ex
            );
        }
    }

    private void deleteUser() {
        int selectedRow = tblUsers.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Select a user to delete."
            );
            return;
        }

        int row = tblUsers.convertRowIndexToModel(selectedRow);
        int userId = Integer.parseInt(
            tableModel.getValueAt(row, 0).toString()
        );

        String selectedUsername =
            tableModel.getValueAt(row, 1).toString();

        if ("admin".equalsIgnoreCase(selectedUsername)) {
            JOptionPane.showMessageDialog(
                this, "The default admin account is protected."
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete user '" + selectedUsername + "'?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM users WHERE user_id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                this, "User deleted successfully."
            );

            loadUsers();
            clearFields();

        } catch (SQLException ex) {
            showError(
                "Could not delete user. A user with recorded sales "
                    + "may need to be retained for historical records.",
                ex
            );
        }
    }

    private void clearFields() {
        txtUsername.setText("");
        txtFullName.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        tblUsers.clearSelection();
        txtUsername.requestFocusInWindow();
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
            new UserManagement().setVisible(true)
        );
    }
}
