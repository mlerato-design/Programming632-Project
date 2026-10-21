package healthfirstpims.dao;

import healthfirstpims.database.DatabaseConnection;
import healthfirstpims.models.Medicine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {

    
    public boolean addMedicine(Medicine medicine) {

        String sql = "INSERT INTO medicines "
                + "(name, company, medicine_type, price, "
                + "quantity_in_stock, reorder_level, expiry_date, supplier_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, medicine.getName());
            statement.setString(2, medicine.getCompany());
            statement.setString(3, medicine.getMedicineType());
            statement.setDouble(4, medicine.getPrice());
            statement.setInt(5, medicine.getQuantityInStock());
            statement.setInt(6, medicine.getReorderLevel());
            statement.setDate(7, medicine.getExpiryDate());
            statement.setInt(8, medicine.getSupplierId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public List<Medicine> getAllMedicines() {

        List<Medicine> medicines = new ArrayList<>();

        String sql = "SELECT * FROM medicines";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Medicine medicine = new Medicine();

                medicine.setMedicineId(
                        result.getInt("medicine_id"));

                medicine.setName(
                        result.getString("name"));

                medicine.setCompany(
                        result.getString("company"));

                medicine.setMedicineType(
                        result.getString("medicine_type"));

                medicine.setPrice(
                        result.getDouble("price"));

                medicine.setQuantityInStock(
                        result.getInt("quantity_in_stock"));

                medicine.setReorderLevel(
                        result.getInt("reorder_level"));

                medicine.setExpiryDate(
                        result.getDate("expiry_date"));

                medicine.setSupplierId(
                        result.getInt("supplier_id"));

                medicines.add(medicine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicines;
    }

    
    public boolean updateMedicine(Medicine medicine) {

        String sql = "UPDATE medicines SET "
                + "name=?, company=?, medicine_type=?, price=?, "
                + "quantity_in_stock=?, reorder_level=?, "
                + "expiry_date=?, supplier_id=? "
                + "WHERE medicine_id=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, medicine.getName());
            statement.setString(2, medicine.getCompany());
            statement.setString(3, medicine.getMedicineType());
            statement.setDouble(4, medicine.getPrice());
            statement.setInt(5, medicine.getQuantityInStock());
            statement.setInt(6, medicine.getReorderLevel());
            statement.setDate(7, medicine.getExpiryDate());
            statement.setInt(8, medicine.getSupplierId());
            statement.setInt(9, medicine.getMedicineId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean deleteMedicine(int medicineId) {

        String sql =
                "DELETE FROM medicines WHERE medicine_id=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, medicineId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
