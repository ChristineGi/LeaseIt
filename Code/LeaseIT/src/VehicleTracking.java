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

    public void showAddLocationServices(){
        System.out.println("\nLocation configuration is inactive. Adding location services...");
    }

    public void showVehicleTracking(String userID, LocationConfiguration locationConfig) throws InterruptedException {

        if (leaseContract.checkActiveLease(userID)) {

            System.out.println("\n------- Vehicle Tracking Screen -------");

            String leaseID = leaseContract.getUserLeasesByStatus(userID, "Active").get(0).getLeaseID();

            if (leaseContract.isLocationConfigured(leaseID)) {
                System.out.println("\nLocation configuration is active.");
            } else {

                showAddLocationServices();
                leaseContract.getVehicleInfo(leaseID);
                locationConfig.setBoundaries();
                locationConfig.adjustBoundaries();
                leaseContract.setLocationConfigurationStatus(leaseID, true);
                cloud.setLocationConfigured(true); // Update cloud status
                connectToCloud();
                message.displayMessage("Success Registration");
                Thread.sleep(1000);
                System.out.println("\nReturn to Home Screen...");
                return;
            }

            requestLocationPermission();
            System.out.print("Grant location permission? (yes/no): ");
            Scanner scanner = new Scanner(System.in);
            String grantPermission = scanner.nextLine();

            if (grantPermission.equalsIgnoreCase("yes")) {
                connectToCloud();
                googleMaps.loadVehicleCoords();
                locationConfig.getBoundarySettings();
                AI ai = new AI();
                ai.analyzeRouteLogs();
                ai.noUnusualActivityDetected();
                googleMaps.locateParkingAndFuel();
                displayData();

                System.out.print("\nSelect Smart Navigation? (yes/no): ");
                String selectNavigation = scanner.nextLine();
                if (selectNavigation.equalsIgnoreCase("yes")) {
                    googleMaps.loadRouteToVehicle();
                    googleMaps.startNavigation();
                    googleMaps.updateLocation();
                    googleMaps.completeNavigation();
                    System.out.println("\nVehicle tracking process completed successfully.");
                }
            }
        } else {
            System.out.println("\nNo active lease found for user: " + userID);
        }
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

    public static void completeRegistration() throws InterruptedException {
        System.out.println("\nRegistration complete.");
        Thread.sleep(1000);

    }

    public void displayData() throws InterruptedException {
        googleMaps.displayData();
    }


    public void handleConnectionFailed() throws InterruptedException {
        cloud.showConnectionStatus("\nConnection Failed. Please try again.");
        System.out.print("\nDo you want to retry tracking? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        String retry = scanner.nextLine();

        if (retry.equalsIgnoreCase("yes")) {
            System.out.println("\nAttempting reconnection...");
            connectToCloud();
        } else {
            System.out.println("\nTracking aborted.");
        }
    }

}
