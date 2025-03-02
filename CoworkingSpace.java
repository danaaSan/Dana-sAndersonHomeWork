package Course.Coworking;

public class CoworkingSpace {

    private int spaceId;
    private double price;
    private String type;
    private boolean isAvailable;

    public CoworkingSpace(int spaceId, String type, double price) {
        this.spaceId = spaceId;
        this.type = type;
        this.price = price;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
