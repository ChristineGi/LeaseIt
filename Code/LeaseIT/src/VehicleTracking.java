import java.util.Scanner;

public class VehicleTracking {
    private Cloud cloud;
    private GoogleMaps googleMaps;
    private Message message;
    private LeaseContract leaseContract;

    public VehicleTracking(Cloud cloud, GoogleMaps googleMaps, Message message, LeaseContract leaseContract) {
        this.cloud = cloud;
        this.googleMaps = googleMaps;
        this.message = message;
        this.leaseContract = leaseContract;
    }

    public void requestLocationPermission() throws InterruptedException {
        message.displayMessage("Grant Permission");
    }

    public void connectToCloud() throws InterruptedException {
        boolean connected = cloud.connect();
        if (!connected) {
            handleConnectionFailed();
        }
    }

    public void loadVehicleCoords() throws InterruptedException {
        googleMaps.loadVehicleCoords();
    }

    public void displayData() throws InterruptedException {
        googleMaps.displayData();
    }

    public void completeNavigation() throws InterruptedException {
        googleMaps.completeNavigation();
        message.displayMessage("You've reached your car");
    }

    public void startNavigation() throws InterruptedException {
        googleMaps.startNavigation();
    }

    public void updateLocation() throws InterruptedException {
        googleMaps.updateLocation();
    }

    public void handleConnectionFailed() throws InterruptedException {
        cloud.showMessage("Connection Failed. Please try again.");
        System.out.println("Do you want to retry tracking? (yes/no):");
        Scanner scanner = new Scanner(System.in);
        String retry = scanner.nextLine();

        if (retry.equalsIgnoreCase("yes")) {
            System.out.println("Attempting reconnection...");
            connectToCloud();
        } else {
            System.out.println("Tracking aborted.");
        }
    }

    public void handleVehicleTracking(String userID, LocationConfiguration locationConfig) throws InterruptedException {
        if (leaseContract.checkActiveLease(userID)) {
            System.out.println("Vehicle Tracking Screen");
            String leaseID = leaseContract.getUserLeasesByStatus(userID, "Active").get(0).getLeaseID();

            if (leaseContract.isLocationConfigured(leaseID)) {
                System.out.println("Location configuration is active.");
            } else {
                System.out.println("Location configuration is inactive. Adding location services...");
                locationConfig.setBoundaries();
                System.out.println("Adjust boundaries...");
                locationConfig.adjustBoundaries();
                locationConfig.completeRegistration();
                leaseContract.setLocationConfigurationStatus(leaseID, true);
                cloud.setLocationConfigured(true); // Update cloud status
                connectToCloud();
                message.displayMessage("Success Registration");
            }

            requestLocationPermission();
            System.out.println("Grant location permission? (yes/no):");
            Scanner scanner = new Scanner(System.in);
            String grantPermission = scanner.nextLine();

            if (grantPermission.equalsIgnoreCase("yes")) {
                connectToCloud();
                loadVehicleCoords();
                googleMaps.getBoundarySettings();
                AI ai = new AI();
                ai.analyzeRouteLogs();
                ai.noUnusualActivityDetected();
                googleMaps.locateParkingAndFuel();
                displayData();

                System.out.println("Select Smart Navigation? (yes/no):");
                String selectNavigation = scanner.nextLine();
                if (selectNavigation.equalsIgnoreCase("yes")) {
                    startNavigation();
                    updateLocation();
                    completeNavigation();
                    displayData();
                    System.out.println("Vehicle tracking process completed successfully.");
                }
            }
        } else {
            System.out.println("No active lease found for user: " + userID);
        }
    }
}
