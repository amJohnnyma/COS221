package cos;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ProductsTab extends JPanel {
    private JTable table;
    private JTextField filterField;
    private DefaultTableModel tableModel;
    private Connection connection;

    public ProductsTab(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        filterField = new JTextField();
        JButton addButton = new JButton("Add Product");
        topPanel.add(new JLabel("Filter: "), BorderLayout.WEST);
        topPanel.add(filterField, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadProductData();

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

        addButton.addActionListener(e -> showAddProductDialog());
    }

    private void loadProductData() {
        try {
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, product_name, list_price, supplier_ids, category FROM products");

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(meta.getColumnName(i));
            }

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                tableModel.addRow(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage());
        }
    }

    private void showAddProductDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Product", true);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox<String> supplierBox = new JComboBox<>();
        JComboBox<String> categoryBox = new JComboBox<>();

        // Load suppliers
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, company FROM suppliers");
            while (rs.next()) {
                supplierBox.addItem(rs.getInt("id") + " - " + rs.getString("company"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + ex.getMessage());
        }

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT category FROM products GROUP BY category");
            while (rs.next()) {
                categoryBox.addItem(rs.getString("category"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + ex.getMessage());
        }

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Price:"));
        dialog.add(priceField);
        dialog.add(new JLabel("Supplier:"));
        dialog.add(supplierBox);
        dialog.add(new JLabel("Category:"));
        dialog.add(categoryBox);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        dialog.add(saveButton);
        dialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int supplierId = Integer.parseInt(supplierBox.getSelectedItem().toString().split(" - ")[0]);
                int categoryId = Integer.parseInt(categoryBox.getSelectedItem().toString().split(" - ")[0]);

                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO products (name, price, supplier_id, category_id) VALUES (?, ?, ?, ?)");
                ps.setString(1, name);
                ps.setDouble(2, price);
                ps.setInt(3, supplierId);
                ps.setInt(4, categoryId);
                ps.executeUpdate();
                ps.close();

                dialog.dispose();
                loadProductData(); // Refresh table
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
