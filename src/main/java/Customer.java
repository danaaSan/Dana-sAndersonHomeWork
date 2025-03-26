import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Customer")
public class Customer extends User {
    public Customer() {}
    public Customer(String name, String surname, String email) {
        super(name, surname, email);
    }
}
