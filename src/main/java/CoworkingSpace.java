import jakarta.persistence.*;

@Entity
@Table(name = "coworking_space")
public class CoworkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "space_id")
    private int spaceId;

    @Column(name = "price", nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SpaceType type;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    public CoworkingSpace() {}

    public CoworkingSpace(double price, SpaceType type) {
        this.price = price;
        this.type = type;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return "CoworkingSpace{" +
                "SpaceId=" + spaceId +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", isAvailable='" + isAvailable + '\'' +
                '}';
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public SpaceType getType() {
        return type;
    }

    public void setType(SpaceType type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

}

