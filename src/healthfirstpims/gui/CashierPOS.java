
package healthfirstpims.gui;

import healthfirstpims.database.DatabaseConnection;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CashierPOS extends JFrame {

    private JTextField txtSearch;
    private JTextField txtQuantity;
    private JLabel lblTotal;

    private JTable tblMedicines;
    private JTable tblCart;

    private DefaultTableModel medicineModel;
    private DefaultTableModel cartModel;

    private TableRowSorter<DefaultTableModel> medicineSorter;

    private final Map<Integer, CartItem> cart = new LinkedHashMap<>();

    public CashierPOS() {
        setTitle("HealthFirstPIMS - Point of Sale");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createForm();
        loadMedicines();
    }

    private void createForm() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel heading = new JLabel("Cashier Point of Sale");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(heading, BorderLayout.NORTH);

        // Medicine search and table
        JPanel medicinePanel = new JPanel(new BorderLayout(5, 5));
        medicinePanel.setBorder(
            BorderFactory.createTitledBorder("Available Medicines")
        );

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        txtSearch = new JTextField();
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        medicineModel = new DefaultTableModel(
            new String[]{
                "ID", "Medicine", "Company",
                "Price", "Stock", "Expiry Date"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblMedicines = new JTable(medicineModel);
        tblMedicines.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        medicineSorter = new TableRowSorter<>(medicineModel);
        tblMedicines.setRowSorter(medicineSorter);

        medicinePanel.add(searchPanel, BorderLayout.NORTH);
        medicinePanel.add(
            new JScrollPane(tblMedicines), BorderLayout.CENTER
        );

        // Cart table
        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.setBorder(
            BorderFactory.createTitledBorder("Shopping Cart")
        );

        cartModel = new DefaultTableModel(
            new String[]{
                "Medicine ID", "Medicine", "Unit Price",
                "Quantity", "Line Total"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblCart = new JTable(cartModel);
        tblCart.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        cartPanel.add(
            new JScrollPane(tblCart), BorderLayout.CENTER
        );

        // Quantity and cart buttons
        JPanel cartControls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtQuantity = new JTextField("1", 5);

        JButton btnAdd = new JButton("Add to Cart");
        JButton btnRemove = new JButton("Remove Item");
        JButton btnClear = new JButton("Clear Cart");
        JButton btnRefresh = new JButton("Refresh Stock");
        JButton btnClose = new JButton("Close");

        btnAdd.addActionListener(e -> addToCart());
        btnRemove.addActionListener(e -> removeFromCart());
        btnClear.addActionListener(e -> clearCart());
        btnRefresh.addActionListener(e -> loadMedicines());
        btnClose.addActionListener(e -> dispose());

        cartControls.add(new JLabel("Quantity:"));
        cartControls.add(txtQuantity);
        cartControls.add(btnAdd);
        cartControls.add(btnRemove);
        cartControls.add(btnClear);
        cartControls.add(btnRefresh);
        cartControls.add(btnClose);

        JPanel cartBottom = new JPanel(new BorderLayout(5, 5));

        lblTotal = new JLabel("Cart Total: R 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        cartBottom.add(cartControls, BorderLayout.CENTER);
        cartBottom.add(lblTotal, BorderLayout.SOUTH);

        cartPanel.add(cartBottom, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT, medicinePanel, cartPanel
        );
        splitPane.setResizeWeight(0.5);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Filter medicine names and companies as the cashier types.
        txtSearch.getDocument().addDocumentListener(
            new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filterMedicines();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filterMedicines();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filterMedicines();
                }
            }
        );
    }

    private void filterMedicines() {
        String search = txtSearch.getText().trim();

        if (search.isEmpty()) {
            medicineSorter.setRowFilter(null);
        } else {
            medicineSorter.setRowFilter(
                RowFilter.regexFilter(
                    "(?i)" + java.util.regex.Pattern.quote(search),
                    1, 2
                )
            );
        }
    }

    private void loadMedicines() {
        String sql =
            "SELECT medicine_id, name, company, price, "
            + "quantity_in_stock, expiry_date "
            + "FROM medicines ORDER BY name";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            medicineModel.setRowCount(0);

            while (rs.next()) {
                medicineModel.addRow(new Object[]{
                    rs.getInt("medicine_id"),
                    rs.getString("name"),
                    rs.getString("company"),
                    rs.getBigDecimal("price"),
                    rs.getInt("quantity_in_stock"),
                    rs.getDate("expiry_date")
                });
            }

        } catch (SQLException ex) {
            showError("Could not load medicines.", ex);
        }
    }

    private void addToCart() {
        int selectedRow = tblMedicines.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Select a medicine first."
            );
            return;
        }

        final int quantity;

        try {
            quantity = Integer.parseInt(
                txtQuantity.getText().trim()
            );

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(
                    this, "Quantity must be greater than zero."
                );
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Enter a valid whole-number quantity."
            );
            return;
        }

        int row = tblMedicines.convertRowIndexToModel(selectedRow);

        int id = Integer.parseInt(
            medicineModel.getValueAt(row, 0).toString()
        );

        String name = medicineModel.getValueAt(row, 1).toString();

        BigDecimal price = new BigDecimal(
            medicineModel.getValueAt(row, 3).toString()
        );

        int stock = Integer.parseInt(
            medicineModel.getValueAt(row, 4).toString()
        );

        CartItem existing = cart.get(id);
        int existingQuantity = existing == null ? 0 : existing.quantity;

        if (stock <= 0) {
            JOptionPane.showMessageDialog(
                this, "This medicine is out of stock."
            );
            return;
        }

        if (existingQuantity + quantity > stock) {
            JOptionPane.showMessageDialog(
                this,
                "Insufficient stock. Available: " + stock
                    + ", already in cart: " + existingQuantity + "."
            );
            return;
        }

        if (existing == null) {
            cart.put(
                id, new CartItem(id, name, price, stock, quantity)
            );
        } else {
            existing.quantity += quantity;
        }

        refreshCartTable();
        txtQuantity.setText("1");
    }

    private void refreshCartTable() {
        cartModel.setRowCount(0);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.values()) {
            BigDecimal lineTotal = item.price.multiply(
                BigDecimal.valueOf(item.quantity)
            );

            total = total.add(lineTotal);

            cartModel.addRow(new Object[]{
                item.id,
                item.name,
                item.price,
                item.quantity,
                lineTotal
            });
        }

        lblTotal.setText(
            "Cart Total: R " + total.toPlainString()
        );
    }

    private void removeFromCart() {
        int selectedRow = tblCart.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Select a cart item to remove."
            );
            return;
        }

        int row = tblCart.convertRowIndexToModel(selectedRow);

        int id = Integer.parseInt(
            cartModel.getValueAt(row, 0).toString()
        );

        cart.remove(id);
        refreshCartTable();
    }

    private void clearCart() {
        if (cart.isEmpty()) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Remove all items from the cart?",
            "Clear Cart",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            cart.clear();
            refreshCartTable();
        }
    }

    private void showError(String message, Exception ex) {
        JOptionPane.showMessageDialog(
            this,
            message + "\n" + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private static class CartItem {
        private final int id;
        private final String name;
        private final BigDecimal price;
        private final int stock;
        private int quantity;

        private CartItem(
                int id, String name, BigDecimal price,
                int stock, int quantity) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.quantity = quantity;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new CashierPOS().setVisible(true)
        );
    }
}
