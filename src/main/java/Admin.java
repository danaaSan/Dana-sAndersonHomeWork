import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Admin")
public class Admin extends User {
    public Admin() {}
    public Admin(String name, String surname, String email) {
        super(name, surname, email);
    }
}
