public class LocationConfiguration {
    private boolean active;

    public LocationConfiguration(boolean active) {
        this.active = active;
    }

    public boolean isLocationActive() {
        return active;
    }

    public void setBoundaries() throws InterruptedException {
        System.out.println("\nSetting boundaries...");
        Thread.sleep(1000);

    }

    public void adjustBoundaries() throws InterruptedException {
        System.out.println("\nAdjusting boundaries...");
        Thread.sleep(1000);
        VehicleTracking.completeRegistration();

    }


    public void getBoundarySettings() throws InterruptedException {
        System.out.println("\nGetting boundary settings...");
        Thread.sleep(1000);

    }
}
