package cos;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class EmployeesTab extends JPanel {
    private JTable table;
    private JTextField filterField;
    private DefaultTableModel tableModel;

    public EmployeesTab(Connection connection) {
        setLayout(new BorderLayout());

        // Top panel for filter
        JPanel topPanel = new JPanel(new FlowLayout());
        filterField = new JTextField(20);
        JButton filterButton = new JButton("Filter");
        topPanel.add(new JLabel("Filter by name or city:"));
        topPanel.add(filterField);
        topPanel.add(filterButton);
        add(topPanel, BorderLayout.NORTH);

        /*
         * For the Employees tab, add a table component to show at a minimum the first name, last name, address,
address line 2, city, region, postal code, phone, office where the employee works, and whether they are
active (e.g., still employed).
         */
        // Table model and table
        String[] columnNames = {
            "first_name", "last_name", "address",/* "Address 2",*/ "city",
            "region", "zip_postal_code", "home_phone"/*, "Office", "Active"*/
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load initial data
        loadEmployees(connection, null);

        // Add filter action
        filterButton.addActionListener(e -> {
            String keyword = filterField.getText().trim();
            loadEmployees(connection, keyword);
        });
    }



    private void loadEmployees(Connection connection, String filter) {
        try {
            tableModel.setRowCount(0); // clear table
            String query = "SELECT first_name, last_name, address, city, country_region, zip_postal_code, home_phone" 
                           +" FROM employees";
            if (filter != null && !filter.isEmpty()) {
                query += " WHERE first_name LIKE ? OR last_name LIKE ? OR city LIKE ?";
            }

            PreparedStatement stmt = connection.prepareStatement(query);
            if (filter != null && !filter.isEmpty()) {
                String f = "%" + filter + "%";
                stmt.setString(1, f);
                stmt.setString(2, f);
                stmt.setString(3, f);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getObject(i));
                }
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
