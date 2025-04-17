package cos;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
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
        topPanel.add(new JLabel("Filter:"));
        topPanel.add(filterField);
        topPanel.add(filterButton);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {
            "first_name", "last_name", "address", "email_address", "city",
            "region", "zip_postal_code", "home_phone"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadEmployees(connection, null);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = filterField.getText();
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });
    }



    private void loadEmployees(Connection connection, String filter) {
        try {
            tableModel.setRowCount(0);
            String query = "SELECT first_name, last_name, address, email_address, city, country_region, zip_postal_code, home_phone" 
                           +" FROM employees";
            if (filter != null && !filter.isEmpty()) {
                query += " GROUP BY " + filter;
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
