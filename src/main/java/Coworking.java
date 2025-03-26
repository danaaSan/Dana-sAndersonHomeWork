
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Scanner;

public class Coworking {
    private static Scanner in = new Scanner(System.in);
    private static WorkspaceManagement management = new WorkspaceManagement();

    public static void main(String[] args) throws SQLException {
        int choice;

        do {
            choice = showMainMenu();
            switch (choice) {
                case 1 -> adminMenu();
                case 2 -> customerMenu();
                case 0 -> {
                    System.out.println("By");
                    in.close();
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 0);
    }

    private static int showMainMenu() {
        System.out.println("Welcome to the Coworking");
        System.out.println("Login please");
        System.out.println("1. Admin Login");
        System.out.println("2. User Login");
        System.out.println("0. Exit");
        return in.nextInt();
    }

    private static void adminMenu() throws SQLException {
        int adminChoice;
        do {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Add a new coworking space");
            System.out.println("2. Remove a coworking space");
            System.out.println("3. View all reservations");
            System.out.println("4. View all spaces");
            System.out.println("5. Add a new user");
            System.out.println("0. Exit");
            adminChoice = in.nextInt();

            switch (adminChoice) {
                case 1 -> addCoworkingSpace();
                case 2 -> removeCoworkingSpace();
                case 3 -> management.bookingInfo();
                case 4 -> management.spacesInfo();
                case 5 -> addNewUser();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (adminChoice != 0); 
    }

    private static void addNewUser() {
        System.out.println("Enter name: ");
        String name = in.nextLine();
        System.out.println("Enter surname: ");
        String surname = in.nextLine();
        System.out.println("Enter email: ");
        String email = in.nextLine();
        System.out.println("Enter user type: ");
        String userType= in.nextLine();
        management.addUser(name, surname, email, userType);
    }

    private static void addCoworkingSpace() {
        System.out.println("Enter space Type: ");
        in.nextLine();
        String type = in.nextLine();
        System.out.println("Enter Price: ");
        double price = in.nextDouble();
        management.addCoworkingSpace(SpaceType.valueOf(type), price);
    }

    private static void removeCoworkingSpace()  {
        System.out.println("Enter space id: ");
        int spaceId = in.nextInt();
        management.removeSpace(spaceId);
    }

    private static void customerMenu() {
        int customerChoice;
        do {
            System.out.println("\nCustomer Menu");
            System.out.println("1. View Available Spaces");
            System.out.println("2. Make reservation");
            System.out.println("3. View My reservations");
            System.out.println("4. Cancel reservation");
            System.out.println("0. Exit");
            customerChoice = in.nextInt();

            switch (customerChoice) {
                case 1 -> management.availableSpacesInfo();
                case 2 -> makeReservation();
                case 3 -> viewMyReservations();
                case 4 -> cancelReservation();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (customerChoice != 0);
    }

    private static void makeReservation() {
        System.out.println("Enter User ID, Workspace ID, Date, Time:");
        int userId = in.nextInt();
        int workspaceId = in.nextInt();
        LocalDate date = LocalDate.parse(in.next());
        LocalTime time = LocalTime.parse(in.next());
        management.addBooking( workspaceId,userId, date, time);
    }

    private static void viewMyReservations() {
        System.out.println("Enter User Id");
        int userId = in.nextInt();
        management.customersBooking(userId);
    }

    private static void cancelReservation() {
        System.out.println("Enter Reservation ID to Cancel:");
        int resId = in.nextInt();
        management.cancelBooking(resId);
    }
}