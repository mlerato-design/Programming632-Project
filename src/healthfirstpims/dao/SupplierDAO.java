package healthfirstpims.dao;

import healthfirstpims.database.DatabaseConnection;
import healthfirstpims.models.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();

        String sql = "SELECT * FROM suppliers ORDER BY supplier_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                suppliers.add(new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("contact_person"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address")
                ));
            }
        }

        return suppliers;
    }

    public boolean addSupplier(Supplier s) throws SQLException {
        String sql = "INSERT INTO suppliers "
            + "(name, contact_person, phone, email, address) "
            + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getContactPerson());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getAddress());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateSupplier(Supplier s) throws SQLException {
        String sql = "UPDATE suppliers SET name=?, "
            + "contact_person=?, phone=?, email=?, address=? "
            + "WHERE supplier_id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getContactPerson());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getAddress());
            ps.setInt(6, s.getSupplierId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteSupplier(int supplierId) throws SQLException {
        String sql = "DELETE FROM suppliers WHERE supplier_id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, supplierId);
            return ps.executeUpdate() > 0;
        }
    }
}
