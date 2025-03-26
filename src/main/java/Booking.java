
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int bookingId;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private CoworkingSpace coworkingSpace;  // Ссылка на пространство

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;  // Ссылка на пользователя

    @Column(name = "booking_date", nullable = false)
    private LocalDate date;

    @Column(name = "booking_time", nullable = false)
    private LocalTime time;

    public Booking() {}

    public Booking(CoworkingSpace coworkingSpace, User customer, LocalDate date, LocalTime time) {
        this.coworkingSpace = coworkingSpace;
        this.customer = customer;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", coworkingSpace=" + coworkingSpace.getSpaceId() +
                ", customer=" + customer.getId() +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    // Геттеры и сеттеры
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public CoworkingSpace getCoworkingSpace() { return coworkingSpace; }
    public void setCoworkingSpace(CoworkingSpace coworkingSpace) { this.coworkingSpace = coworkingSpace; }

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
}
