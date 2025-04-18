package cos;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

class Customer {
    int id;
    String firstName, lastName, email, phone, mobilePhone, jobTitle, company;
    String homePhone, faxNumber, address, city, stateProvince, zipPostalCode;
    String countryRegion, webPage, notes;

    // Constructor
    public Customer(int id, String firstName, String lastName, String email, String phone,
            String mobilePhone, String jobTitle, String company, String homePhone,
            String faxNumber, String address, String city, String stateProvince,
            String zipPostalCode, String countryRegion, String webPage, String notes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.mobilePhone = mobilePhone;
        this.jobTitle = jobTitle;
        this.company = company;
        this.homePhone = homePhone;
        this.faxNumber = faxNumber;
        this.address = address;
        this.city = city;
        this.stateProvince = stateProvince;
        this.zipPostalCode = zipPostalCode;
        this.countryRegion = countryRegion;
        this.webPage = webPage;
        this.notes = notes;
    }
}

public class NotificationsTab extends JPanel {


    private enum ClientFilterMode {
    ACTIVE, INACTIVE, ALL
}

    private java.util.List<Customer> customers = new ArrayList<>();

    private JTable clientsTable;
    private DefaultTableModel tableModel;

    private JTextField companyField = new JTextField();
    private JTextField lastNameField = new JTextField();
    private JTextField firstNameField = new JTextField();
    private JTextField emailAddressField = new JTextField();
    private JTextField jobTitleField = new JTextField();
    private JTextField businessPhoneField = new JTextField();
    private JTextField homePhoneField = new JTextField();
    private JTextField mobilePhoneField = new JTextField();
    private JTextField faxNumberField = new JTextField();
    private JTextArea addressField = new JTextArea();
    private JTextField cityField = new JTextField();
    private JTextField stateProvinceField = new JTextField();
    private JTextField zipPostalCodeField = new JTextField();
    private JTextField countryRegionField = new JTextField();
    private JTextField webPageField = new JTextField();
    private JTextArea notesField = new JTextArea();

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String mobilePhone;
    private String jobTitle;
    private String company;
    private String homePhone;
    private String faxNumber;
    private String address;
    private String city;
    private String stateProvince;
    private String zipPostalCode;
    private String countryRegion;
    private String webPage;
    private String notes;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton toggleFilterButton;
    private JButton showAllButton;

    private JTextField filterField;


    private int selectedClientId = -1;
    private ClientFilterMode filterMode = ClientFilterMode.ACTIVE;
    public NotificationsTab(Connection connection) {
        setLayout(new BorderLayout());

        // Top: Table
        tableModel = new DefaultTableModel(new String[] { "ID", "Name", "Email", "Phone", "Active" }, 0);
        //To get active -- rest inactive
        //In table customers use id
        //Match id (customers) to customer_id (orders)
        //Check if shipped_date is past


        clientsTable = new JTable(tableModel);
        clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: Form + Buttons
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // Set layout and add the fields to your panel/form
        // Example:
        formPanel.add(new JLabel("Company:"));
        formPanel.add(companyField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Email Address:"));
        formPanel.add(emailAddressField);
        formPanel.add(new JLabel("Job Title:"));
        formPanel.add(jobTitleField);
        formPanel.add(new JLabel("Business Phone:"));
        formPanel.add(businessPhoneField);
        formPanel.add(new JLabel("Home Phone:"));
        formPanel.add(homePhoneField);
        formPanel.add(new JLabel("Mobile Phone:"));
        formPanel.add(mobilePhoneField);
        formPanel.add(new JLabel("Fax Number:"));
        formPanel.add(faxNumberField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(new JScrollPane(addressField)); // For larger text
        formPanel.add(new JLabel("City:"));
        formPanel.add(cityField);
        formPanel.add(new JLabel("State/Province:"));
        formPanel.add(stateProvinceField);
        formPanel.add(new JLabel("Zip/Postal Code:"));
        formPanel.add(zipPostalCodeField);
        formPanel.add(new JLabel("Country/Region:"));
        formPanel.add(countryRegionField);
        formPanel.add(new JLabel("Web Page:"));
        formPanel.add(webPageField);
        formPanel.add(new JLabel("Notes:"));
        formPanel.add(new JScrollPane(notesField)); // For larger text

        addButton = new JButton("Add Client");
        updateButton = new JButton("Update Client");
        deleteButton = new JButton("Delete Client");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        toggleFilterButton = new JButton("Show Inactive Clients");
        toggleFilterButton.addActionListener(e -> {
            if (filterMode == ClientFilterMode.ACTIVE) {
                filterMode = ClientFilterMode.INACTIVE;
                toggleFilterButton.setText("Show Active Clients");
            } else {
                filterMode = ClientFilterMode.ACTIVE;
                toggleFilterButton.setText("Show Inactive Clients");
            }
            showAllButton.setText("Show All Clients");
            loadClients(connection);
        });
        
        showAllButton = new JButton("Show All Clients");
        showAllButton.addActionListener(e -> {
            filterMode = ClientFilterMode.ALL;
            toggleFilterButton.setText("Show Inactive Clients"); // Reset label
            showAllButton.setText("Showing All Clients");
            loadClients(connection);
        });
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(toggleFilterButton);
        buttonPanel.add(showAllButton);

        // Event Listeners
        addButton.addActionListener(e -> addClient(connection));
        updateButton.addActionListener(e -> updateClient(connection));
        deleteButton.addActionListener(e -> deleteClient(connection));

        clientsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && clientsTable.getSelectedRow() != -1) {
                int row = clientsTable.getSelectedRow();
                selectedClientId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

                Customer selected = customers.stream()
                        .filter(c -> c.id == selectedClientId)
                        .findFirst()
                        .orElse(null);

                if (selected != null) {
                    firstNameField.setText(selected.firstName);
                    lastNameField.setText(selected.lastName);
                    emailAddressField.setText(selected.email);
                    businessPhoneField.setText(selected.phone);
                    mobilePhoneField.setText(selected.mobilePhone);
                    jobTitleField.setText(selected.jobTitle);
                    companyField.setText(selected.company);
                    homePhoneField.setText(selected.homePhone);
                    faxNumberField.setText(selected.faxNumber);
                    addressField.setText(selected.address);
                    cityField.setText(selected.city);
                    stateProvinceField.setText(selected.stateProvince);
                    zipPostalCodeField.setText(selected.zipPostalCode);
                    countryRegionField.setText(selected.countryRegion);
                    webPageField.setText(selected.webPage);
                    notesField.setText(selected.notes);
                }
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout());
        filterField = new JTextField(20);
        topPanel.add(filterField, BorderLayout.NORTH);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        clientsTable.setRowSorter(sorter);
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = filterField.getText();
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        topPanel.add(new JLabel("Filter: "));
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        



        // Load initial data
        loadClients(connection);
    }

    private boolean isClientActive(Connection conn, int customerId) {
    String query = """
        SELECT COUNT(*) AS active_count
        FROM orders
        WHERE customer_id = ? AND shipped_date IS NOT NULL
    """;

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, customerId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("active_count") > 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    private void loadClients(Connection conn) {
        tableModel.setRowCount(0);
        customers.clear();

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {

            while (rs.next()) {
            int customerId = rs.getInt("id");

            boolean isActive = isClientActive(conn, customerId);

            if (filterMode == ClientFilterMode.ACTIVE && !isActive) continue;
            if (filterMode == ClientFilterMode.INACTIVE && isActive) continue;


                Customer customer = new Customer(
                        customerId,
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email_address"),
                        rs.getString("business_phone"),
                        rs.getString("mobile_phone"),
                        rs.getString("job_title"),
                        rs.getString("company"),
                        rs.getString("home_phone"),
                        rs.getString("fax_number"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("state_province"),
                        rs.getString("zip_postal_code"),
                        rs.getString("country_region"),
                        rs.getString("web_page"),
                        rs.getString("notes"));

                customers.add(customer);

                // Only show selected columns in the table
                tableModel.addRow(new Object[] {
                        customer.id,
                        customer.firstName + " " + customer.lastName,
                        customer.email,
                        customer.homePhone,
                        isActive ? "Active" : "Inactive"
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading clients: " + e.getMessage());
        }
    }


    private void addClient(Connection conn) {
        // Get all the input values
        firstName = firstNameField.getText();
        lastName = lastNameField.getText();
        email = emailAddressField.getText();
        phone = businessPhoneField.getText();
        mobilePhone = mobilePhoneField.getText();
        jobTitle = jobTitleField.getText();
        company = companyField.getText();
        homePhone = homePhoneField.getText();
        faxNumber = faxNumberField.getText();
        address = addressField.getText();
        city = cityField.getText();
        stateProvince = stateProvinceField.getText();
        zipPostalCode = zipPostalCodeField.getText();
        countryRegion = countryRegionField.getText();
        webPage = webPageField.getText();
        notes = notesField.getText();

        // Validate required fields: first_name, last_name, and email_address
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields (First Name, Last Name, Email Address).");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to add this client?", "Confirm Add",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO customers (first_name, last_name, email_address, mobile_phone, job_title, company, home_phone, "
                        +
                        "business_phone, fax_number, address, city, state_province, zip_postal_code, country_region, web_page, notes) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, mobilePhone);
            stmt.setString(5, jobTitle);
            stmt.setString(6, company);
            stmt.setString(7, homePhone);
            stmt.setString(8, phone);
            stmt.setString(9, faxNumber);
            stmt.setString(10, address);
            stmt.setString(11, city);
            stmt.setString(12, stateProvince);
            stmt.setString(13, zipPostalCode);
            stmt.setString(14, countryRegion);
            stmt.setString(15, webPage);
            stmt.setString(16, notes);

            stmt.executeUpdate();
            loadClients(conn); // Assuming this loads from the `customers` table
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding client: " + e.getMessage());
        }
    }

    private void updateClient(Connection conn) {
        if (selectedClientId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a client to update.");
            return;
        }

        // Get all the input values
        firstName = firstNameField.getText().isEmpty() ? "" : firstNameField.getText();
        lastName = lastNameField.getText().isEmpty() ? "" : lastNameField.getText();
        email = emailAddressField.getText().isEmpty() ? "" : emailAddressField.getText();
        phone = businessPhoneField.getText().isEmpty() ? "" : businessPhoneField.getText();
        mobilePhone = mobilePhoneField.getText().isEmpty() ? "" : mobilePhoneField.getText();
        jobTitle = jobTitleField.getText().isEmpty() ? "" : jobTitleField.getText();
        company = companyField.getText().isEmpty() ? "" : companyField.getText();
        homePhone = homePhoneField.getText().isEmpty() ? "" : homePhoneField.getText();
        faxNumber = faxNumberField.getText().isEmpty() ? "" : faxNumberField.getText();
        address = addressField.getText().isEmpty() ? "" : addressField.getText();
        city = cityField.getText().isEmpty() ? "" : cityField.getText();
        stateProvince = stateProvinceField.getText().isEmpty() ? "" : stateProvinceField.getText();
        zipPostalCode = zipPostalCodeField.getText().isEmpty() ? "" : zipPostalCodeField.getText();
        countryRegion = countryRegionField.getText().isEmpty() ? "" : countryRegionField.getText();
        webPage = webPageField.getText().isEmpty() ? "" : webPageField.getText();
        notes = notesField.getText().isEmpty() ? "" : notesField.getText();

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this client?",
                "Confirm update", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;
        try (
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE customers SET first_name=?, last_name=?, email_address=?, mobile_phone=?, job_title=?, company=?, home_phone=?, "
                                +
                                "business_phone=?, fax_number=?, address=?, city=?, state_province=?, zip_postal_code=?, country_region=?, web_page=?, notes=? "
                                +
                                "WHERE id=?")) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, mobilePhone);
            stmt.setString(5, jobTitle);
            stmt.setString(6, company);
            stmt.setString(7, homePhone);
            stmt.setString(8, phone);
            stmt.setString(9, faxNumber);
            stmt.setString(10, address);
            stmt.setString(11, city);
            stmt.setString(12, stateProvince);
            stmt.setString(13, zipPostalCode);
            stmt.setString(14, countryRegion);
            stmt.setString(15, webPage);
            stmt.setString(16, notes);
            stmt.setInt(17, selectedClientId);

            stmt.executeUpdate();
            loadClients(conn); // Assuming this loads from the `clients` table
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating client: " + e.getMessage());
        }
    }

    private void deleteClient(Connection conn) {
        if (selectedClientId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a client to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        try (
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM customers WHERE id=?")) {
            stmt.setInt(1, selectedClientId);
            stmt.executeUpdate();
            loadClients(conn);
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting client: " + e.getMessage());
        }
    }

    private void clearForm() {
        selectedClientId = -1;
        firstNameField.setText("");
        emailAddressField.setText("");
        homePhoneField.setText("");
        clientsTable.clearSelection();
    }
}
