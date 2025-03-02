package Course.Coworking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public void removeCoworkingSpace(int spaceId){
        boolean removed = coworkingSpaces.removeIf(coworkingSpace -> coworkingSpace.getSpaceId()==spaceId);
        if (!removed) {
            System.out.println("Error: No coworking space found with ID " + spaceId);
        } else {
            System.out.println("Coworking space with ID " + spaceId + " removed successfully.");
        }
    }
    public void spacesInfo(){
        for (CoworkingSpace coworkingSpace: coworkingSpaces) {
            System.out.println(coworkingSpace.toString());
        }
    }
    public void availableSpacesInfo(){
        for (CoworkingSpace coworkingSpace: coworkingSpaces) {
            if(coworkingSpace.isAvailable()) {
                System.out.println(coworkingSpace.toString());
            }
        }
    }

    public void bookingSpace(String customerName, int spaceId, String date, String time) {
        for (CoworkingSpace coworkingSpace: coworkingSpaces){
            if(coworkingSpace.getSpaceId()==spaceId && coworkingSpace.isAvailable()){
                bookings.add(new Booking(reservationCounter++, spaceId, customerName,  date, time));
                coworkingSpace.setAvailable(false);
            }
        }
    }
    public void cancelBooking(int bookingId){
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            if (booking.getBookingId() == bookingId) {
                iterator.remove(); // Безопасное удаление через итератор
                System.out.println("Booking cancelled successfully.");
                for (CoworkingSpace coworkingSpace : coworkingSpaces) {
                    if (coworkingSpace.getSpaceId() == booking.getSpaceId()) {
                        coworkingSpace.setAvailable(true);
                    }
                }
                return; // Прерываем выполнение, так как бронирование найдено и удалено
            }
        }
    }
    public  void bookingInfo(){
        for (Booking booking:bookings) {
            System.out.println(booking.toString());
        }
    }
    public void customersBooking(String name){
        for (Booking booking:bookings) {
            if (booking.getCustomerName().equalsIgnoreCase(name)){
                System.out.println(booking.toString());
            }
        }
    }




}
