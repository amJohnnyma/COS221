# COS221 Java Swing Database GUI

A multi-tab Java Swing GUI for managing employees, products, clients, and reports using a **MariaDB** backend. Built as a university practical assignment 4 for COS221.

[PDF Submission](https://docs.google.com/document/d/1fvpxCKiTZTQ3VOhYBC-psxKQofzsLGdpl6GZVxZgUAE/edit?usp=sharing)

---

## Features

- **Employees Tab**  
  - Displays all employees in a table  
  - Live filter

- **Products Tab**  
  - View available products  
  - Add new products through a popup form  
  - Modify and delete product entries
  - Live filter

- **Report Tab**  
  - Generate live reports
  - Real-time data fetching from the database

- **Notifications Tab**  
  - Full customer management interface  
  - Add, update, and delete clients  
  - Display full customer information when selected, including:
    - Name, company, phone numbers, address, email, job title, notes, etc.  
  - Table only shows summarized data (ID, Name, Email, Phone) for better UX

---

## Technologies Used

- **Java Swing** (GUI framework)  
- **MariaDB** (Relational database)  
- **JDBC** (Database connectivity)  
- **Maven** (Jar management)

---

## Project Structure
```
src/
├── data/
│   └── northwind/
│       ├── northwind-schema.sql
│       ├── northwind-data.sql
│       ├── northwind.mwb
│       └── northwind-erd.pdf
├── main/
│   ├── java/
│   │   └── cos/
│   │       ├── Main.java
│   │       ├── DatabaseConnector.java
│   │       ├── EmployeesTab.java
│   │       ├── ProductsTab.java
│   │       ├── NotificationsTab.java
│   │       └── ReportTab.java
│   └── resources/
│       └── .dbconfig.properties
└── target/
.gitignore
22.png
blackbox.drawio.png
COS_221_Assignment4_2025.pdf
dbconfig.png
ERT3.erd
pom.xml
README.md
tasks.txt
```



### Requirements
---
- Java 17+  
- MariaDB Server (Running)  
- IntelliJ / VS Code / Any IDE  
- JDBC Driver (e.g., `mariadb-java-client-*.jar`)
- Maven
---
### Database Setup

1. Ensure your MariaDB server is running.

### Then run:
```
mysql -u root -p u23536030_northwind < /path/to/dir/northwind-schema.sql
```
```
mysql -u root -p u23536030_northwind < /path/to/dir/northwind-data.sql
```

### Login to MariaDB
mysql -u root -p

### How to Run

1. ``` git clone https://github.com/amJohnnyma/COS221.git ```
2. Ensure the .dbconfig.properties file exists and is correctly configured.
3. Compile: ```mvn compile```
4. Run: ```mvn exec:java```

5. or open using vscode codespace
---

### Author
[Dewald Colesky] - [Github](https://github.com/amJohnnyma)

## Notes

Customer data is internally stored and updated in the table using full objects, ensuring that selection dynamically fills all form fields—even those not shown in the table.
