

import java.io.*;
import java.util.*;

public class WorkspaceManagement {

    private List<CoworkingSpace> coworkingSpaces;
    private List<Booking> bookings;
    private int reservationCounter = 1; //for autoincrement id
    private int spaceCounter = 1;

    public WorkspaceManagement() {
        this.coworkingSpaces = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public void addCoworkingSpace(String type, double price) {
        CoworkingSpace coworkingSpace = new CoworkingSpace(spaceCounter++, type, price);
        coworkingSpaces.add(coworkingSpace);
        System.out.println("Space added successfully!");
    }
    public void removeSpace(int id) {
        if (coworkingSpaces.removeIf(space -> space.getSpaceId() == id)) {
            System.out.println("Coworking space with ID " + id + " removed successfully.");
        } else {
            System.out.println("Coworking space with ID " + id + " not found.");
        }
    }

    public void spacesInfo() {
        coworkingSpaces.forEach(System.out::println);
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
}
