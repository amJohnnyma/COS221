package cos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ReportTab extends JPanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public ReportTab(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);
        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        generateReport(); // Load report on tab open
    }

    public void generateReport() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        String query = """
            SELECT 
                s.company AS warehouse_name,
                p.category AS category_name,
                COUNT(p.id) AS product_count
            FROM 
                products p
            JOIN 
                suppliers s ON p.supplier_ids = s.id
            GROUP BY 
                s.company, p.category
            ORDER BY 
                s.company, p.category
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Setup columns
            tableModel.addColumn("Warehouse");
            tableModel.addColumn("Category");
            tableModel.addColumn("Product Count");

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("warehouse_name"));
                row.add(rs.getString("category_name"));
                row.add(rs.getInt("product_count"));
                tableModel.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage());
        }
    }
}
