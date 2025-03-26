import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class WorkspaceManagement {

    private EntityManager entityManager;

    public WorkspaceManagement() {
        entityManager = JPAUtil.getEntityManager();
    }

    public void addCoworkingSpace( SpaceType type, double price){
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(new CoworkingSpace(price, type));
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            e.printStackTrace(); // Логирование ошибки
        }

    }

    public void removeSpace(int id) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();

            CoworkingSpace space = entityManager.find(CoworkingSpace.class, id);
            if (space != null) {
                entityManager.remove(space);
            } else {
                System.out.println("Coworking space with ID " + id + " not found.");
            }

            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            e.printStackTrace(); // Логирование ошибки
        }
    }

    public void addUser(String name, String surname, String email, String userType) {
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            User user;
            if ("Admin".equalsIgnoreCase(userType)) {
                user = new Admin(name, surname, email);
            } else if ("Customer".equalsIgnoreCase(userType)) {
                user = new Customer(name, surname, email);
            } else {
                System.out.println("Invalid user type: " + userType);
                throw new IllegalArgumentException("Invalid user type: " + userType);
            }

            entityManager.persist(user);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            e.printStackTrace();
        }
    }


    public void addBooking(int spaceId, int userId, LocalDate date, LocalTime time) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            CoworkingSpace coworkingSpace = entityManager.find(CoworkingSpace.class, spaceId);
            if (coworkingSpace == null) {
                System.out.println("Space ID " + spaceId + " is no");
                return;
            }
            if (!coworkingSpace.isAvailable()) {
                System.out.println("Space ID " + spaceId + " is not available.");
                return;
            }
            User user = entityManager.find(User.class, userId);
            if (user == null) {
                System.out.println("User ID " + userId + " no found");
                return;
            }
            Booking booking = new Booking(coworkingSpace, user, date, time);
            entityManager.persist(booking);
            coworkingSpace.setAvailable(false);
            entityManager.merge(coworkingSpace);
            transaction.commit();
            System.out.println("Success");
        }  catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void customersBooking(int userId) {
        try {
            // Запрос на получение всех бронирований пользователя
            TypedQuery<Booking> query = entityManager.createQuery(
                    "SELECT b FROM Booking b WHERE b.customer.id = :userId", Booking.class
            );
            query.setParameter("userId", userId);

            List<Booking> bookings = query.getResultList();

            if (bookings.isEmpty()) {
                System.out.println("User with ID " + userId + "dont have booking");
            } else {
                System.out.println("Bookings: " + userId + ":");
                for (Booking booking : bookings) {
                    System.out.println(booking);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spacesInfo() {
        entityManager.createQuery("SELECT c FROM CoworkingSpace c", CoworkingSpace.class)
                .getResultList()
                .forEach(System.out::println);
    }


    public void availableSpacesInfo() {
        entityManager.createQuery("SELECT c FROM CoworkingSpace c WHERE c.isAvailable = true", CoworkingSpace.class)
                .getResultList()
                .forEach(System.out::println);
    }


    public void cancelBooking(int bookingId) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            Booking booking = entityManager.find(Booking.class, bookingId);
            if (booking == null) {
                System.out.println("Booking ID " + bookingId + " not found");
                return;
            }

            CoworkingSpace coworkingSpace = booking.getCoworkingSpace();
            coworkingSpace.setAvailable(true);
            entityManager.merge(coworkingSpace);

            entityManager.remove(booking);

            transaction.commit();
            System.out.println("Success");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    public void bookingInfo() {
        entityManager.createQuery("SELECT b FROM Booking b", Booking.class)
                .getResultList()
                .forEach(System.out::println);
    }

}
