import java.util.Random;

public class Cloud {
    private boolean locationConfigured;
    private Random random;

    public Cloud() {
        this.locationConfigured = false;
        this.random = new Random();
    }

    public void setLocationConfigured(boolean locationConfigured) {
        this.locationConfigured = locationConfigured;
    }

    public boolean connect() {
        if (random.nextBoolean()) {
            if (locationConfigured) {
                System.out.println("\nConnected to cloud.");
                return true;
            } else {
                System.out.println("\nNo Location Configured.");
                return false;
            }
        } else {
            System.out.println("\nConnection Failed.");
            return false;
        }
    }

    public void showConnectionStatus(String message) {
        System.out.println(message);
    }
}
