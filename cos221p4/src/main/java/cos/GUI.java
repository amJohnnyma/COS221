package cos;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame frame;
    private JTabbedPane tabbedPane;

    DatabaseConnector db = new DatabaseConnector();
    
    public GUI() {
        db.connect();
        frame = new JFrame("Business Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        tabbedPane = new JTabbedPane();

        // Add tabs
        tabbedPane.addTab("Employees", buildEmployeesTab());
        tabbedPane.addTab("Products", buildProductsTab());
        tabbedPane.addTab("Report", buildReportTab());
        tabbedPane.addTab("Notifications", buildNotificationsTab());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel buildEmployeesTab() {
        JPanel panel = new JPanel(new BorderLayout());

        //  will replace this with a real JTable and filter later
        JLabel label = new JLabel("Employee Table Here");
        panel.add(label, BorderLayout.CENTER);

        return panel;
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
