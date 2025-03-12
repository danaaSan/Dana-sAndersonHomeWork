
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
        boolean removed =coworkingSpaces.removeIf(space -> space.getSpaceId() == id);
        if (!removed) {
            System.out.println("Coworking space with ID " + id + " removed successfully.");
        } else {
            System.out.println("Coworking space with ID " + id + " removed successfully.");
        }
    }

    public void spacesInfo(){
        for (CoworkingSpace coworkingSpace: coworkingSpaces) {
            System.out.println(coworkingSpace.toString());
        }
    }
    public void availableSpacesInfo(){
        coworkingSpaces.stream()
                .filter(CoworkingSpace::isAvailable)
                .forEach(System.out::println);

    }

    public void bookingSpace(String customerName, int spaceId, String date, String time) {
        for (CoworkingSpace coworkingSpace: coworkingSpaces){
            if(coworkingSpace.getSpaceId()==spaceId && coworkingSpace.isAvailable()){
                bookings.add(new Booking(reservationCounter++, spaceId, customerName,  date, time));
                coworkingSpace.setAvailable(false);
            }
        }
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

    public  void bookingInfo(){
        for (Booking booking:bookings) {
            System.out.println(booking.toString());
        }
    }
    public void customersBooking(String name){
        bookings.stream()
                .filter(booking -> booking.getCustomerName().equalsIgnoreCase(name))
                .forEach(System.out::println);

    }

    public void saveSpacesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("spaces.txt"))) {
            for (CoworkingSpace space :coworkingSpaces) {
                writer.println(space.getSpaceId() + "," + space.getType() + "," + space.getPrice() + "," + space.isAvailable());
            }
        } catch (IOException e) {
            System.out.println("Error saving coworking spaces: " + e.getMessage());
        }
    }

    public void loadSpacesFromFile() {
        try (Scanner scanner = new Scanner(new File("spaces.txt"))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                int id = Integer.parseInt(data[0]);
                String type = data[1];
                double price = Double.parseDouble(data[2]);
                coworkingSpaces.add(new CoworkingSpace(id, type, price));
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found, starting fresh.");
        }
    }




}
