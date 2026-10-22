
# HealthFirstPIMS

## Project Overview
HealthFirstPIMS is a Java Swing pharmacy inventory and point-of-sale management system connected to a MySQL database. It supports administrative tasks, medicine and supplier management, cashier sales, billing, and inventory reporting.

## Technologies Used
- Java
- Java Swing
- NetBeans IDE
- MySQL Server
- MySQL Workbench
- JDBC

## Main Features
- User login and authentication
- Role-based Admin and Cashier dashboards
- Medicine inventory management
- Supplier management
- User management
- Cashier point of sale
- Shopping cart and checkout
- Receipt generation
- Sales and sale-item records
- Automatic stock updates after checkout
- Low-stock reports
- Expiry reports
- Inventory and stock-value reports

## Database
Database name: `healthfirst_pims`

Main tables:
- `users`
- `suppliers`
- `medicines`
- `sales`
- `sale_items`

## Setup Instructions
1. Install Java and NetBeans.
2. Install and start MySQL Server.
3. Create the `healthfirst_pims` database and its required tables.
4. Configure the database connection in `DatabaseConnection.java`.
5. Ensure the MySQL JDBC driver is available to the project.
6. Open the project in NetBeans.
7. Build and run the application.
8. Log in using a valid account already configured in the database.

## Testing
Test login and role access, medicine and supplier management, cashier checkout, receipt generation, database updates, and inventory reports.

## Security Notes
Do not publish database passwords, private credentials, or real user passwords in this repository. Use appropriately hashed passwords and validate user input.

## Developer
Student project — Programming 632
