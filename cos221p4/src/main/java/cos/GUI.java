package cos;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class GUI {
    private JFrame frame;
    private JTabbedPane tabbedPane;

    private DatabaseConnector db;
    private Connection conn;

    public GUI() {
        db = new DatabaseConnector();
        db.connect();
        conn = db.getConnection();

        frame = new JFrame("Business Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        tabbedPane = new JTabbedPane();

        // Add tabs
        tabbedPane.addTab("Employees", new EmployeesTab(conn));
        tabbedPane.addTab("Products", buildProductsTab());
        tabbedPane.addTab("Report", buildReportTab());
        tabbedPane.addTab("Notifications", buildNotificationsTab());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }



    private JPanel buildProductsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton addButton = new JButton("Add Product");
        // Add action listener to open popup here

        panel.add(addButton, BorderLayout.NORTH);
        panel.add(new JLabel("Products Table Here"), BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildReportTab() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel("Live report generated on tab open"), BorderLayout.CENTER);

        //dynamic reportsd

        return panel;
    }

    private JPanel buildNotificationsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel("Client Notifications Management"), BorderLayout.CENTER);

        // This tab will manage add, update, delete, list clients + search inactive ones

        return panel;
    }

    public void display() {
        frame.setVisible(true);
    }
}


