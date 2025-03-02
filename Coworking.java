package Course.Coworking;

import java.util.Scanner;

public class Coworking {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        WorkspaceManagement management = new WorkspaceManagement();
        int choice = -1;

        while (choice != 0) {
            System.out.println("Welcome to the Coworking");
            System.out.println("Login please");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("0. Exit");

            choice = in.nextInt();

            if (choice == 1) {
                int adminChoice = -1;
                while (adminChoice !=0) {
                    System.out.println("Admin Menu");
                    System.out.println("1. Add a new coworking space");
                    System.out.println("2. Remove a coworking space");
                    System.out.println("3. View all reservations");
                    System.out.println("4. View all spaces");
                    System.out.println("0. Exit");

                    adminChoice = in.nextInt();

                    switch (adminChoice){
                        case 1:
                            System.out.println("Enter space Type: ");
                            String type = in.next();
                            System.out.println("Enter Price: ");
                            double price = in.nextDouble();
                            management.addCoworkingSpace(type, price);
                            break;
                        case 2:
                            System.out.println("Enter space id: ");
                            int spaceId = in.nextInt();
                            in.nextLine();
                            management.removeCoworkingSpace(spaceId);
                            break;
                        case 3:
                            management.bookingInfo();
                            break;
                        case 4:
                            management.spacesInfo();
                            break;
                        case 0:
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + adminChoice);
                    }
                }
            } else if (choice == 2) {
                int customerChoice = -1;
                while (customerChoice !=0) {
                    System.out.println("Customer Menu");
                    System.out.println("1. View Available Spaces");
                    System.out.println("2. Make reservation");
                    System.out.println("3. View My reservations");
                    System.out.println("4. Cancel reservation");
                    System.out.println("0. Exit");

                    customerChoice = in.nextInt();

                    switch (customerChoice){
                        case 1:
                            management.availableSpacesInfo();
                            break;
                        case 2:
                            System.out.println("Enter Name, Workspace ID, Date, Time:");
                            String name = in.next();
                            int workspaceId = in.nextInt();
                            in.nextLine();
                            String date = in.next();
                            String time = in.next();
                            management.bookingSpace(name, workspaceId, date, time);
                            break;
                        case 3:
                            System.out.println("Enter Name");
                            name = in.next();
                            management.customersBooking(name);
                            break;
                        case 4:
                            System.out.println("Enter Reservation ID to Cancel:");
                            int resId = in.nextInt();
                            in.nextLine();
                            management.cancelBooking(resId);
                            break;
                        case 0:
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " +  customerChoice);
                    }
                }
            }else {
                break;
            }
        }
    }
}
