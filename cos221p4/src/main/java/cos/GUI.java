package cos;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class GUI {
    private JFrame frame;
    private JTabbedPane tabbedPane;

    private DatabaseConnector db;
    private Connection conn;

    public GUI() {
        db = new DatabaseConnector();
        db.connect();
        conn = db.getConnection();

        frame = new JFrame("Northwind Trading Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        tabbedPane = new JTabbedPane();

        // Add tabs
        tabbedPane.addTab("Employees", new EmployeesTab(conn));
        tabbedPane.addTab("Products",new ProductsTab(conn));
        tabbedPane.addTab("Report", new ReportTab(conn));
        tabbedPane.addTab("Notifications", new NotificationsTab(conn));

        frame.add(tabbedPane);
        frame.setVisible(true);

        tabbedPane.addChangeListener(e -> {
            Component selected = tabbedPane.getSelectedComponent();
            if (selected instanceof ReportTab reportTab) {
                reportTab.generateReport(); // Reload on tab switch
            }
        });


    }



    public void display() {
        frame.setVisible(true);
    }
}


