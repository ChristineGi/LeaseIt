import java.util.Scanner;
import java.util.List;

public class Main {

    private static String loggedInUsername;

    public static void main(String[] args) {

        // Initialize services
        User database = new User();
        Vehicle vehicle = new Vehicle();
        Email emailService = new Email();
        LeaseContract leaseContract = new LeaseContract();
        PaymentGateway paymentGateway = new PaymentGateway();
        TaxGateway taxGateway = new TaxGateway();
        Message messageService = new Message();
        GoogleMaps googleMaps = new GoogleMaps();
        Dealership dealership = new Dealership();
        Calendar calendar = new Calendar();
        Wallet wallet = new Wallet();
        Cloud cloud = new Cloud();
        AI ai = new AI();
        LocationConfiguration locationConfig = new LocationConfiguration(false);
        MaintenanceCenter maintenanceCenter = new MaintenanceCenter();

        // Initialize UI
        VehicleLeasing vehicleLeasing = new VehicleLeasing(vehicle, leaseContract, taxGateway, messageService, paymentGateway, emailService);
        VehicleTracking vehicleTracking = new VehicleTracking(cloud, googleMaps, messageService, leaseContract);
        Scanner scanner = new Scanner(System.in);


        // Login system
        User userDetails = null;
        while (true) {

            System.out.println("\n------- LeaseIT -------");
            System.out.print("Enter your username: ");

            loggedInUsername = scanner.nextLine();
            userDetails = database.getUserDetails(loggedInUsername);
            if (userDetails == null) {
                System.out.println("\nUsername not found. Try again.");
            } else {
                vehicleLeasing.setUserDetails(userDetails);
                System.out.println("\nLogin successful. Welcome, " + loggedInUsername + "!");
                break;
            }
        }

        VehiclePickup vehiclePickup = new VehiclePickup(database, leaseContract, googleMaps, dealership, calendar, wallet, emailService, userDetails);
        VehicleMaintenance vehicleMaintenance = new VehicleMaintenance(vehicle, ai, cloud, maintenanceCenter, emailService, calendar, userDetails, leaseContract);

        while (true) {

            try {
                System.out.println("\n------- Home Screen -------");
                System.out.println("1. Vehicle Leasing");
                System.out.println("2. Vehicle Pickup");
                System.out.println("3. Vehicle Tracking");
                System.out.println("4. Vehicle Maintenance");
                System.out.println("---------------------------\n");

                System.out.println("------- Supportive Features -------");
                System.out.println("5. View Emails");
                System.out.println("6. Exit");
                System.out.println("-----------------------------------\n");

                System.out.print("Select: ");
                int choice = scanner.nextInt();

                if (choice == 6) {
                    break;
                }

                if (choice == 1) {
                    vehicleLeasing.showLeasingVehicleScreen();

                } else if (choice == 2) {
                    vehiclePickup.showVehiclePickup();

                } else if (choice == 3) {
                    vehicleTracking.showVehicleTracking(userDetails.getUsername(), locationConfig);

                } else if (choice == 4) {
                    vehicleMaintenance.showVehicleMaintenace();

                } else if (choice == 5) {
                    emailService.viewEmails(loggedInUsername);

                } else {
                    System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}
