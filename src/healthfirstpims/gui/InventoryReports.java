
package healthfirstpims.gui;

import healthfirstpims.database.DatabaseConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class InventoryReports extends JFrame {

    private final DefaultTableModel tableModel;
    private final JTable reportTable;
    private final JLabel statusLabel;

    public InventoryReports() {
        setTitle("HealthFirstPIMS - Inventory Reports");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);
        reportTable.setAutoCreateRowSorter(true);
        statusLabel = new JLabel("Select a report to begin.");

        JButton btnLowStock = new JButton("Low-Stock Report");
        JButton btnExpiry = new JButton("Expiry Report");
        JButton btnInventory = new JButton("Inventory Report");
        JButton btnClose = new JButton("Close");

        btnLowStock.addActionListener(e -> loadLowStockReport());
        btnExpiry.addActionListener(e -> loadExpiryReport());
        btnInventory.addActionListener(e -> loadInventoryReport());
        btnClose.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnLowStock);
        buttonPanel.add(btnExpiry);
        buttonPanel.add(btnInventory);
        buttonPanel.add(btnClose);

        setLayout(new BorderLayout(10, 10));
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(reportTable), BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void loadLowStockReport() {
        String sql =
            "SELECT medicine_id, name, company, "
            + "quantity_in_stock, reorder_level "
            + "FROM medicines "
            + "WHERE quantity_in_stock <= reorder_level "
            + "ORDER BY quantity_in_stock ASC";

        loadReport(
            "Low-Stock Report",
            sql,
            "No medicines are currently at or below their reorder level."
        );
    }

    private void loadExpiryReport() {
        String sql =
            "SELECT medicine_id, name, company, expiry_date, "
            + "quantity_in_stock, "
            + "CASE "
            + "WHEN expiry_date < CURRENT_DATE THEN 'EXPIRED' "
            + "WHEN expiry_date <= DATE_ADD(CURRENT_DATE, "
            + "INTERVAL 30 DAY) THEN 'EXPIRING SOON' "
            + "END AS expiry_status "
            + "FROM medicines "
            + "WHERE expiry_date <= DATE_ADD(CURRENT_DATE, "
            + "INTERVAL 30 DAY) "
            + "ORDER BY expiry_date ASC";

        loadReport(
            "Expiry Report",
            sql,
            "No expired or soon-to-expire medicines were found."
        );
    }

    private void loadInventoryReport() {
        String sql =
            "SELECT medicine_id, name, company, price, "
            + "quantity_in_stock, reorder_level, "
            + "(price * quantity_in_stock) AS stock_value "
            + "FROM medicines "
            + "ORDER BY name ASC";

        loadReport(
            "Inventory Report",
            sql,
            "No medicines are currently recorded."
        );
    }

    private void loadReport(
            String reportName,
            String sql,
            String emptyMessage) {

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        try (Connection connection =
                    DatabaseConnection.getConnection();
             PreparedStatement statement =
                    connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            int columnCount =
                    result.getMetaData().getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(
                    result.getMetaData().getColumnLabel(i)
                );
            }

            int rowCount = 0;

            while (result.next()) {
                Object[] row = new Object[columnCount];

                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = result.getObject(i);
                }

                tableModel.addRow(row);
                rowCount++;
            }

            statusLabel.setText(
                reportName + " - " + rowCount + " record(s)"
            );

            if (rowCount == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    emptyMessage,
                    reportName,
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load report: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() ->
            new InventoryReports().setVisible(true)
        );
    }
}
