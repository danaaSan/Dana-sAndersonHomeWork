
import java.util.Scanner;

public class Coworking {
    private static Scanner in = new Scanner(System.in);
    private static WorkspaceManagement management = new WorkspaceManagement();

    public static void main(String[] args) {
        management.loadSpacesFromFile();
        int choice;

        do {
            choice = showMainMenu();
            switch (choice) {
                case 1 -> adminMenu();
                case 2 -> customerMenu();
                case 0 -> {
                    System.out.println("Saving data and exiting...");
                    management.saveSpacesToFile();
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

    private static void adminMenu() {
        int adminChoice;
        do {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Add a new coworking space");
            System.out.println("2. Remove a coworking space");
            System.out.println("3. View all reservations");
            System.out.println("4. View all spaces");
            System.out.println("0. Exit");
            adminChoice = in.nextInt();

            switch (adminChoice) {
                case 1 -> addCoworkingSpace();
                case 2 -> removeCoworkingSpace();
                case 3 -> management.bookingInfo();
                case 4 -> management.spacesInfo();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (adminChoice != 0);
    }

    private static void addCoworkingSpace() {
        System.out.println("Enter space Type: ");
        in.nextLine();
        String type = in.nextLine();
        System.out.println("Enter Price: ");
        double price = in.nextDouble();
        management.addCoworkingSpace(type, price);
    }

    private static void removeCoworkingSpace() {
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
        System.out.println("Enter Name, Workspace ID, Date, Time:");
        String name = in.next();
        int workspaceId = in.nextInt();
        String date = in.next();
        String time = in.next();
        management.bookingSpace(name, workspaceId, date, time);
    }

    private static void viewMyReservations() {
        System.out.println("Enter Name");
        String name = in.next();
        management.customersBooking(name);
    }

    private static void cancelReservation() {
        System.out.println("Enter Reservation ID to Cancel:");
        int resId = in.nextInt();
        management.cancelBooking(resId);
    }
}