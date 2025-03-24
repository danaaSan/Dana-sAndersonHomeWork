import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestWorkspaceManagement {
    private WorkspaceManagement workspaceManagement;

    @BeforeEach
    void setUp() {
        workspaceManagement = new WorkspaceManagement();
    }


    @Test
    public void testAddSpace() throws SQLException {

        workspaceManagement.addCoworkingSpace("open", 50);
        List<CoworkingSpace> coworkingSpaces = workspaceManagement.getCoworkingSpaces();

        assertEquals(1, coworkingSpaces.size());
        assertEquals("open", coworkingSpaces.get(0).getType());
        assertEquals(50, coworkingSpaces.get(0).getPrice());
        assertEquals(1, coworkingSpaces.get(0).getSpaceId());
        assertTrue( coworkingSpaces.get(0).isAvailable());
    }

    @Test
    public void testAutoIncrementOfSpaceID() throws SQLException {

        workspaceManagement.addCoworkingSpace("open", 50);
        workspaceManagement.addCoworkingSpace("close", 60);
        workspaceManagement.addCoworkingSpace("open", 50);
        List<CoworkingSpace> coworkingSpaces = workspaceManagement.getCoworkingSpaces();

        assertEquals(1, coworkingSpaces.get(0).getSpaceId());
        assertEquals(2, coworkingSpaces.get(1).getSpaceId());
        assertEquals(3, coworkingSpaces.get(2).getSpaceId());
    }

    @Test
    public void testSpaceRemovedSuccessfully() throws SQLException {

        workspaceManagement.addCoworkingSpace("open", 50);

        List<CoworkingSpace> coworkingSpaces = workspaceManagement.getCoworkingSpaces();
        workspaceManagement.removeSpace(coworkingSpaces.get(0).getSpaceId());

        assertEquals(0, coworkingSpaces.size());
    }

    @Test
    public void testSpaceIsNotRemoved() throws SQLException {

        workspaceManagement.addCoworkingSpace("open", 50);

        List<CoworkingSpace> coworkingSpaces = workspaceManagement.getCoworkingSpaces();
        workspaceManagement.removeSpace(99);

        assertEquals(1, coworkingSpaces.size());
    }

    @Test
    public void testIsAvailable() throws SQLException {
        // Arrange
        workspaceManagement.addCoworkingSpace("open", 50);
        workspaceManagement.addCoworkingSpace("close", 60);
        workspaceManagement.addCoworkingSpace("open", 50);
        List<CoworkingSpace> coworkingSpaces = workspaceManagement.getCoworkingSpaces();
        coworkingSpaces.get(1).setAvailable(false);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act
        outContent.reset(); // Очищаем System.out перед вызовом метода
        workspaceManagement.availableSpacesInfo();
        String output = outContent.toString().trim();

        // Assert
        assertTrue(output.contains("open"));
        assertFalse(output.contains("close")); // Оно должно быть скрыто, так как setAvailable(false)
    }

    @Test
    public void testBookingSuccessfully() throws SQLException {
        // Arrange
        workspaceManagement.addCoworkingSpace("open", 50);
        int spaceId = workspaceManagement.getCoworkingSpaces().get(0).getSpaceId();

        // Act
        workspaceManagement.bookingSpace("Dana", spaceId, "2025-03-20", "10:00");
        List<Booking> bookings = workspaceManagement.getBookings();

        assertEquals(1, bookings.size());
        assertEquals(spaceId, bookings.get(0).getSpaceId());
        assertEquals("Dana", bookings.get(0).getCustomerName());
        assertEquals("2025-03-20", bookings.get(0).getDate());
        assertEquals("10:00", bookings.get(0).getTime());
        assertEquals(1, bookings.get(0).getBookingId());
    }

    @Test
    public void testBookingFailed() throws SQLException {
        // Arrange
        workspaceManagement.addCoworkingSpace("open", 50);
        int spaceId = workspaceManagement.getCoworkingSpaces().get(0).getSpaceId();

        // Act
        workspaceManagement.bookingSpace("Dana", spaceId+99, "2025-03-20", "10:00");
        List<Booking> bookings = workspaceManagement.getBookings();

        assertEquals(0, bookings.size());

    }

    @Test
    public void testAutoIncrementOfBookingID() throws SQLException {

        workspaceManagement.addCoworkingSpace("open", 50);
        workspaceManagement.addCoworkingSpace("close", 60);
        workspaceManagement.addCoworkingSpace("open", 50);

        workspaceManagement.bookingSpace("Dana", 1, "2025-03-20", "10:00");
        workspaceManagement.bookingSpace("Lia", 2, "2025-03-20", "10:00");
        workspaceManagement.bookingSpace("Kain", 3, "2025-03-20", "10:00");

        List<Booking> bookings = workspaceManagement.getBookings();

        assertEquals(1, bookings.get(0).getBookingId());
        assertEquals(2, bookings.get(1).getBookingId());
        assertEquals(3, bookings.get(2).getBookingId());
    }

    @Test
    public void testChangingAvailabilityStatusAfterBooking() throws SQLException {
        // Arrange
        workspaceManagement.addCoworkingSpace("open", 50);
        int spaceId = workspaceManagement.getCoworkingSpaces().get(0).getSpaceId();

        // Act
        workspaceManagement.bookingSpace("Dana", spaceId, "2025-03-20", "10:00");

        assertFalse( workspaceManagement.getCoworkingSpaces().get(0).isAvailable());
    }

    @Test
    public void testCancelBookingSuccessfully() throws SQLException {
        // Arrange
        workspaceManagement.addCoworkingSpace("open", 50);
        int spaceId = workspaceManagement.getCoworkingSpaces().get(0).getSpaceId();

        // Act
        workspaceManagement.bookingSpace("Dana", spaceId, "2025-03-20", "10:00");
        workspaceManagement.cancelBooking(spaceId);
        List<Booking> bookings = workspaceManagement.getBookings();

        assertEquals(0, bookings.size());
    }

    @Test
    public void testBookingIsNotCancelled() throws SQLException {
        // Arrange
        workspaceManagement.addCoworkingSpace("open", 50);
        int spaceId = workspaceManagement.getCoworkingSpaces().get(0).getSpaceId();

        // Act
        workspaceManagement.bookingSpace("Dana", spaceId, "2025-03-20", "10:00");
        workspaceManagement.cancelBooking(spaceId+99);
        List<Booking> bookings = workspaceManagement.getBookings();

        assertEquals(1, bookings.size());

    }

    @Test
    public void testChangingAvailabilityStatusAfterCancelBooking() throws SQLException {
        // Arrange
        workspaceManagement.addCoworkingSpace("open", 50);
        int spaceId = workspaceManagement.getCoworkingSpaces().get(0).getSpaceId();

        // Act
        workspaceManagement.bookingSpace("Dana", spaceId, "2025-03-20", "10:00");
        workspaceManagement.cancelBooking(spaceId);

        assertTrue( workspaceManagement.getCoworkingSpaces().get(0).isAvailable());
    }

    @Test
    public void testCustomersBookingExist() throws SQLException {

        workspaceManagement.addCoworkingSpace("open", 50);
        workspaceManagement.addCoworkingSpace("close", 60);

        workspaceManagement.bookingSpace("Dana", 1, "2025-03-20", "10:00");
        workspaceManagement.bookingSpace("Dana", 2, "2025-04-20", "15:00");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act
        outContent.reset(); // Очищаем System.out перед вызовом метода
        workspaceManagement.customersBooking("Dana");
        String output = outContent.toString().trim();

        // Assert
        assertTrue(output.contains("Dana"));
        assertTrue(output.contains("2025-03-20"));
        assertTrue(output.contains("10:00"));
        assertTrue(output.contains("2025-04-20"));
        assertTrue(output.contains("15:00"));
    }

    @Test
    public void testCustomersBookingDoesNotExist() throws SQLException {

        workspaceManagement.addCoworkingSpace("open", 50);
        workspaceManagement.bookingSpace("Dana", 1, "2025-03-20", "10:00");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act
        outContent.reset(); // Очищаем System.out перед вызовом метода
        workspaceManagement.customersBooking("Kain");
        String output = outContent.toString().trim();

        // Assert
        assertFalse(output.contains("Kain"));
        assertFalse(output.contains("2025-03-20"));
        assertFalse(output.contains("10:00"));
    }




}
