public class LocationConfiguration {
    private boolean active;

    public LocationConfiguration(boolean active) {
        this.active = active;
    }

    public boolean isLocationActive() {
        return active;
    }

    public void setBoundaries() {
        System.out.println("Setting boundaries...");
    }

    public void adjustBoundaries() {
        System.out.println("Adjusting boundaries...");
    }

    public void completeRegistration() {
        System.out.println("Registration complete.");
    }

    public void getBoundarySettings() {
        System.out.println("Getting boundary settings...");
    }
}
