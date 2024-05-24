import java.util.Random;

public class Cloud {
    private boolean locationConfigured;
    private Random random;

    public Cloud() {
        this.locationConfigured = false; // By default, assume no location is configured
        this.random = new Random();
    }

    public void setLocationConfigured(boolean locationConfigured) {
        this.locationConfigured = locationConfigured;
    }

    public boolean connect() {
        if (random.nextBoolean()) { // Simulate a 50% chance of failure
            if (locationConfigured) {
                System.out.println("Connected to cloud.");
                return true;
            } else {
                System.out.println("No Location Configured.");
                return false;
            }
        } else {
            System.out.println("Connection Failed.");
            return false;
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
