USE healthfirst_pims;

INSERT INTO users
(username, password, role, full_name)
VALUES
('admin', 'admin123', 'Admin', 'System Administrator'),
('cashier', 'cash123', 'Cashier', 'HealthFirst Cashier');

INSERT INTO suppliers
(name, contact_person, phone, email, address)
VALUES
('MediSupply', 'John Smith', '0111234567',
 'medisupply@example.com', 'Johannesburg'),
('PharmaCare', 'Sarah Adams', '0117654321',
 'pharmacare@example.com', 'Pretoria');

INSERT INTO medicines
(name, company, medicine_type, price,
 quantity_in_stock, reorder_level, expiry_date, supplier_id)
VALUES
('Panado', 'Adcock Ingram', 'Tablet', 35.00,
 100, 20, '2027-12-31', 1),

('Disprin', 'Reckitt', 'Tablet', 28.50,
 75, 15, '2027-10-31', 1),

('Benylin', 'Johnson & Johnson', 'Syrup', 65.00,
 30, 10, '2027-06-30', 2);
