import java.io.*;
import java.sql.*;
import java.util.*;

public class WorkspaceManagement {

    private List<CoworkingSpace> coworkingSpaces;
    private List<Booking> bookings;
    private int reservationCounter = 1; //for autoincrement id
    private int spaceCounter = 1;

    private static final String URL = "jdbc:postgresql://localhost:5432/andersenCoworking";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Dana123";
    Connection dbConnection;

    public WorkspaceManagement() {
        this.coworkingSpaces = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public void addCoworkingSpace(String type, double price) throws SQLException {
        String sql = "INSERT INTO coworking_space (type, price, is_available) VALUES (?, ?, ?)";

        try {
            CoworkingSpace coworkingSpace = new CoworkingSpace(spaceCounter++, type, price);
            dbConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = dbConnection.prepareStatement(sql);
            pstmt.setString(1, coworkingSpace.getType());
            pstmt.setDouble(2, coworkingSpace.getPrice());
            pstmt.setBoolean(3, coworkingSpace.isAvailable());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                coworkingSpaces.add(coworkingSpace);
                System.out.println("Coworking space added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeSpace(int id) {
        String sql ="DELETE FROM coworking_space WHERE space_id = ?";

        try {
            dbConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = dbConnection.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                coworkingSpaces.removeIf(space -> space.getSpaceId() == id);
                System.out.println("Coworking space deleted successfully.");
            } else {
                System.out.println("No space found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting coworking space: " + e.getMessage());
        }

    }

    public void addUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try {
            dbConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement= dbConnection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role.toUpperCase());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("Failed to add user.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    public void addBookingDB(int spaceId, int userId, String customerName, String date, String time) {
        String sql = "INSERT INTO booking (space_id, user_id, customer_name, date, time) VALUES (?, ?, ?, ?, ?)";

        try {
            dbConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = dbConnection.prepareStatement(sql);
            pstmt.setInt(1, spaceId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, customerName);
            pstmt.setString(4, date);
            pstmt.setString(5, time);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("added successfully.!");
            } else {
                System.out.println("Failed to add");
            }
        } catch (SQLException e) {
            System.out.println("SQL: " + e.getMessage());
        }
    }

    public void customersBookingDB(int userId) {
        String sql = "SELECT booking_id, space_id, customer_name, date, time FROM booking WHERE user_id = ?";

        try {
            dbConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = dbConnection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n Your Bookings:");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                int spaceId = rs.getInt("space_id");
                String customerName = rs.getString("customer_name");
                String date = rs.getString("date");
                String time = rs.getString("time");

                System.out.printf("ID Booking #%d | Space ID: %d | Name: %s | Date: %s | Time: %s%n",
                        bookingId, spaceId, customerName, date, time);
            }
            System.out.println("--------------------------------------------------");

        } catch (SQLException e) {
            System.out.println(" Error retrieving bookings: " + e.getMessage());
        }
    }

    public void spacesInfo() {
        String sql = "SELECT space_id, type, price, is_available FROM coworking_space";

        try {
            dbConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = dbConnection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("\n Available Coworking Spaces:");
            System.out.println("--------------------------------------------------");

            while (rs.next()) {
                int spaceId = rs.getInt("space_id");
                String type = rs.getString("type");
                double price = rs.getDouble("price");
                boolean isAvailable = rs.getBoolean("is_available");

                System.out.printf(" Space ID: %d | Type: %s | Price: $%.2f | Available: %s%n",
                        spaceId, type, price, isAvailable ? "Yes" : " No");
            }
            System.out.println("--------------------------------------------------");

        } catch (SQLException e) {
            System.out.println(" Error retrieving coworking spaces: " + e.getMessage());
        }
    }

    public void availableSpacesInfo(){
        coworkingSpaces.stream()
                .filter(CoworkingSpace::isAvailable)
                .forEach(System.out::println);

    }

    public void bookingSpace(String customerName, int spaceId, String date, String time) {
        Optional<CoworkingSpace> spaceOpt = coworkingSpaces.stream()
                .filter(space -> space.getSpaceId() == spaceId && space.isAvailable())
                .findFirst();

        spaceOpt.ifPresentOrElse(space -> {
            bookings.add(new Booking(reservationCounter++, spaceId, customerName, date, time));
            space.setAvailable(false);
            System.out.println("Booking successful!");
        }, () -> System.out.println("Space ID " + spaceId + " is not available."));
    }

    public void cancelBooking(int bookingId) {
        Optional<Booking> bookingToCancel = bookings.stream()
                .filter(booking -> booking.getBookingId() == bookingId)
                .findFirst();

        bookingToCancel.ifPresentOrElse(booking -> {
            bookings.remove(booking);
            coworkingSpaces.stream()
                    .filter(space -> space.getSpaceId() == booking.getSpaceId())
                    .findFirst()
                    .ifPresent(space -> space.setAvailable(true));

            System.out.println("Booking cancelled successfully.");
        }, () -> System.out.println("Booking with ID " + bookingId + " not found."));
    }

    public void bookingInfo() {
        bookings.forEach(System.out::println);
    }

    public void customersBooking(String name){
        bookings.stream()
                .filter(booking -> booking.getCustomerName().equalsIgnoreCase(name))
                .forEach(System.out::println);
    }

    public void saveSpacesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/java/spaces.txt"))) {
            for (CoworkingSpace space :coworkingSpaces) {
                writer.println(space.getSpaceId() + "," + space.getType() + "," + space.getPrice() + "," + space.isAvailable());
            }
            System.out.println("Coworking spaces saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving coworking spaces: " + e.getMessage());
        }
    }

    public void loadSpacesFromFile() {
        try (Scanner scanner = new Scanner(new File("src/main/java/spaces.txt"))) {
            int maxId = 0;
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                int id = Integer.parseInt(data[0]);
                String type = data[1];
                double price = Double.parseDouble(data[2]);
                boolean available = Boolean.parseBoolean(data[3]);
                coworkingSpaces.add(new CoworkingSpace(id, type, price));
                if (id > maxId) maxId = id;
            }
            spaceCounter = maxId + 1; // Обновляем счётчик ID
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found, starting fresh.");
        }
    }

    public List<CoworkingSpace> getCoworkingSpaces() {
        return coworkingSpaces;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setCoworkingSpaces(List<CoworkingSpace> coworkingSpaces) {
        this.coworkingSpaces = coworkingSpaces;
    }
}
