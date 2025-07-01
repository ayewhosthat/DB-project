//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.sql.*;
import java.util.Scanner;

public class Restaurant {
    public static final String URL = "jdbc:db2://winter2025-comp421.cs.mcgill.ca:50000/comp421";
    public static final String USER = "REDACTED";
    public static final String PASSWORD = "REDACTED";
    public static void main(String[] args) throws SQLException {
        DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Select an option:");
                System.out.println("1. Add Employee");
                System.out.println("2. Search for Employee");
                System.out.println("3. Check Inventory");
                System.out.println("4. View Bill");
                System.out.println("5. Make Reservation");
                System.out.println("6. Quit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> addEmployee(conn, scanner);
                    case 2 -> searchEmployee(conn, scanner);
                    case 3 -> checkInventory(conn, scanner);
                    case 4 -> viewBills(conn, scanner);
                    case 5 -> makeReservation(conn, scanner);
                    case 6 -> {
                        System.out.println("Exiting...");
                        conn.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }

    private static void addEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("First Name: ");
        String fName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lName = scanner.nextLine();
        System.out.print("Shift: ");
        String shift = scanner.nextLine();
        System.out.print("Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Position (Chef/Manager/Host/Waiter): ");
        String position = scanner.nextLine();

//        this is a loop to extract the number of rows within the employee table
        int rowCount = 0;
        String sql_stmt = "SELECT * FROM Employee";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql_stmt)) {

            while (rs.next()) {
                rowCount++;
            }

        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }

        int eid = rowCount + 1;
//        setting eid to be one more than row count

        String sql = "INSERT INTO Employee (eid, fName, lName, shift, wage) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eid);
            stmt.setString(2, fName);
            stmt.setString(3, lName);
            stmt.setString(4, shift);
            stmt.setDouble(5, salary);
            stmt.executeUpdate();
            System.out.println("Employee added with EID: " + eid);


        }
        catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }

//        now we also insert into the according chef/manager/host/waiter table
        switch (position) {
            case "Manager" -> {
                String sql_manager = "INSERT INTO Manager (eid) VALUES (?)";
                try (PreparedStatement stmt_manager = conn.prepareStatement(sql_manager)) {
                    stmt_manager.setInt(1, eid);
                    stmt_manager.executeUpdate();
                }
                catch (SQLException e) {
                    System.out.println("Error executing query: " + e.getMessage());
                }
            }
            case "Host" -> {
                String sql_host = "INSERT INTO Host (eid) VALUES (?)";
                try (PreparedStatement stmt_host = conn.prepareStatement(sql_host)) {
                    stmt_host.setInt(1, eid);
                    stmt_host.executeUpdate();
                }
                catch (SQLException e) {
                    System.out.println("Error executing query: " + e.getMessage());
                }
            }
            case "Waiter" -> {
                String sql_waiter = "INSERT INTO Waiter (eid) VALUES (?)";
                try (PreparedStatement stmt_waiter = conn.prepareStatement(sql_waiter)) {
                    stmt_waiter.setInt(1, eid);
                    stmt_waiter.executeUpdate();
                }
                catch (SQLException e) {
                    System.out.println("Error executing query: " + e.getMessage());
                }
            }
            case "Chef" -> {
                System.out.println("Enter a rank 1 through 5:");
                int rank = scanner.nextInt();
                String sql_chef = "INSERT INTO Chef (eid, rank) VALUES (?, ?)";
                try (PreparedStatement stmt_chef = conn.prepareStatement(sql_chef)) {
                    stmt_chef.setInt(1, eid);
                    stmt_chef.setInt(2, rank);
                    stmt_chef.executeUpdate();
                }
                catch (SQLException e) {
                    System.out.println("Error executing query: " + e.getMessage());
                }
            }
        }
    }
    public static void searchEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter the EID of the employee you want to search for:");
        int eid = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT eid, fName, lName FROM Employee WHERE eid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Employee found:");
                    System.out.println("EID: " + rs.getInt("eid"));
                    System.out.println("First Name: " + rs.getString("fName"));
                    System.out.println("Last Name: " + rs.getString("lName"));
                } else {
                    System.out.println("Employee not found.");
                }
            }
            catch (SQLException e) {
                System.out.println("Error executing query: " + e.getMessage());
            }
        }

    }

    public static void checkInventory(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter the name of the item you want to check:");
        String name = scanner.nextLine();

        String sql = "SELECT name, stock FROM Ingredient WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Item found:");
                    System.out.println("Item Name: " + rs.getString("name"));
                    System.out.println("Quantity: " + rs.getBigDecimal("stock"));
                } else {
                    System.out.println("Item not found.");
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }

    public static void viewBills(Connection conn, Scanner scanner) throws SQLException {
//        can either make it so that we view all the bills, or a specific bill extracted by bill id
        System.out.println("Show all bills (Y/y) or search for a specific bill? (N/n)");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            String allBills = "SELECT * FROM Bill";
            Statement stmt = conn.createStatement();
            try (ResultSet rs = stmt.executeQuery(allBills)) {
                while (rs.next()) {
                    int billId = rs.getInt("billId");
                    Date date = rs.getDate("date");
                    Time time = rs.getTime("time");
                    float amount = rs.getFloat("amtpaid");
                    System.out.println("Bill ID: " + billId + " " + "Date: " + date + " " + "Time: " + time + " " + "Amount: " + amount);
                }
            }
            catch (SQLException e) {
                System.out.println("Error executing query: " + e.getMessage());
            }
        } else if (choice.equalsIgnoreCase("n")) {
            System.out.println("Enter a bill id:");
            int billId = scanner.nextInt();
            String byId = "SELECT * FROM Bill WHERE billId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(byId)) {
                stmt.setInt(1, billId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("billId");
                    Date date = rs.getDate("date");
                    Time time = rs.getTime("time");
                    float amount = rs.getFloat("amtpaid");
                    System.out.println("Bill ID: " + id + " " + "Date: " + date + " " + "Time: " + time + " " + "Amount: " + amount);
                }
            }
            catch (SQLException e) {
                System.out.println("Error executing query: " + e.getMessage());
            }
        }


    }
    public static void makeReservation(Connection conn, Scanner scanner) throws SQLException {
//        loop to get number of reservations
        String loop = "SELECT * FROM Reservation";
        Statement loop_stmt = conn.createStatement();
        int num_res = 0;
        try (ResultSet rs = loop_stmt.executeQuery(loop)) {
            while (rs.next()) {
                num_res++;
            }
        }

        int resId = num_res + 1;

        System.out.println("First name:");
        String fName = scanner.nextLine();
        System.out.println("Last name:");
        String lName = scanner.nextLine();
        System.out.println("Date (YYYY-MM-DD): ");
        Date date = Date.valueOf(scanner.nextLine());
        System.out.println("What time will the reservation be for?:");
        Time time = Time.valueOf(scanner.nextLine());
        System.out.println("How many people will be a part of this reservation?:");
        int num = scanner.nextInt();
        System.out.println("Please enter the host id:");
        int hostId = scanner.nextInt();

        String sql = "INSERT INTO Reservation (resId, fName, lName, date, time, size, hostId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resId);
            stmt.setString(2, fName);
            stmt.setString(3, lName);
            stmt.setDate(4, date);
            stmt.setTime(5, time);
            stmt.setInt(6, num);
            stmt.setInt(7, hostId);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


}
