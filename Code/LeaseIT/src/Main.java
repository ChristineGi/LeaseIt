import java.util.Scanner;
import java.util.Set;
import java.util.List;

public class Main {

    private static String loggedInUsername;

    public static void main(String[] args) {

        // Initialize services
        Database database = new Database();
        Vehicle vehicle = new Vehicle();
        LeaseContract leaseContract = new LeaseContract();
        PaymentGateway paymentGateway = new PaymentGateway();
        TaxGateway taxGateway = new TaxGateway();
        Message messageService = new Message();
        Email emailService = new Email();
        GoogleMaps googleMaps = new GoogleMaps();
        Dealership dealership = new Dealership();
        Calendar calendar = new Calendar();
        Wallet wallet = new Wallet();
        Cloud cloud = new Cloud();
        AI ai = new AI();
        LocationConfiguration locationConfig = new LocationConfiguration(false);
        MaintenanceCenter maintenanceCenter = new MaintenanceCenter();

        // Initialize UI
        VehicleLeasing vehicleLeasing = new VehicleLeasing(database, vehicle, leaseContract, taxGateway, messageService, paymentGateway, emailService);
        VehicleTracking vehicleTracking = new VehicleTracking(cloud, googleMaps, messageService, leaseContract);
        Scanner scanner = new Scanner(System.in);

        // Login system
        Database.UserDetails userDetails = null;
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
                System.out.println("5. View Leasing Subscriptions");
                System.out.println("6. View Emails");
                System.out.println("7. Exit");
                System.out.println("-----------------------------------\n");

                System.out.print("Select: ");
                int choice = scanner.nextInt();

                if (choice == 7) {
                    break;
                }

                if (choice == 1) {

                    vehicleLeasing.showLeasingVehicleScreen();

                    try {
                        // Entering leasing preferences
                        System.out.println("\nEnter your preferences: ");

                        // Display distinct vehicle types
                        Set<String> vehicleTypes = vehicle.getVehicleTypes();
                        System.out.print("\nVehicle Type\n" + vehicleTypes + "\nSelect from Options: ");
                        scanner.nextLine();  // Consume newline
                        String vehicleType = scanner.nextLine();

                        // Display distinct vehicle brands
                        Set<String> vehicleBrands = vehicle.getVehicleBrands();
                        System.out.print("\nPreferred Brands\n" + vehicleBrands + "\nSelect from Options: ");
                        String preferredBrands = scanner.nextLine();

                        System.out.print("\nBudget Price: ");
                        int budget = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        Database.VehiclePreferences preferences = new Database.VehiclePreferences(vehicleType, budget, preferredBrands);
                        boolean vehiclesAvailable = vehicleLeasing.submitPreferences(preferences);
                        if (!vehiclesAvailable) {
                            continue; // Stop the process if no vehicles are found
                        }

                        // Selecting a vehicle
                        String vehicleId;
                        while (true) {
                            System.out.print("\nSelect a vehicle (Enter vehicle ID): ");
                            vehicleId = scanner.nextLine();
                            if (vehicleLeasing.isValidVehicleSelection(vehicleId, preferences)) {
                                break;
                            } else {
                                System.out.println("\nInvalid vehicle ID. Please select a valid vehicle ID from the list.");
                            }
                        }
                        vehicleLeasing.selectVehicle(vehicleId);

                        // Set the selected vehicle ID in userDetails
                        userDetails.setSelectedVehicleId(vehicleId);
                        vehicleLeasing.confirmLeasingTerms(userDetails);

                        System.out.println("\nReturning to Home Screen...");
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                } else if (choice == 6) {
                    List<String> emails = emailService.getEmails(loggedInUsername);
                    if (emails.isEmpty()) {
                        System.out.println("\nYou've got no emails !");
                    } else {
                        System.out.println("\n**** Emails ****\n");
                        for (int i = 0; i < emails.size(); i++) {
                            System.out.println((i + 1) + ". " + emails.get(i));
                        }
                        System.out.print("\nEnter the email number to read, or '0' to go back: ");
                        String emailChoice = scanner.next();
                        if (emailChoice.equals("0")) {
                            continue;
                        } else if (emailChoice.startsWith("d")) {
                            int emailIndex = Integer.parseInt(emailChoice.substring(1)) - 1;
                            emailService.deleteEmail(loggedInUsername, emailIndex);
                        } else {
                            int emailIndex = Integer.parseInt(emailChoice) - 1;
                            System.out.println("\nEmail: " + emails.get(emailIndex));
                            vehicleLeasing.continueLeasingProcess();
                        }
                    }
                } else if (choice == 2) {
                    vehiclePickup.showVehiclePickup();
                } else if (choice == 3) {
                    vehicleTracking.handleVehicleTracking(userDetails.getUsername(), locationConfig);
                } else if (choice == 4) {
                    if (vehicleMaintenance.hasActiveLease()) {
                        System.out.println("1. Maintenance Check");
                        System.out.println("2. Urgent Maintenance");
                        System.out.print("Select an option: ");
                        int maintenanceChoice = scanner.nextInt();
                        if (maintenanceChoice == 1) {
                            vehicleMaintenance.performMaintenanceCheck();
                        } else if (maintenanceChoice == 2) {
                            vehicleMaintenance.urgentService.displayIssueDescription();
                            vehicleMaintenance.urgentService.submitIssueDescription();
                        }
                    } else {
                        System.out.println("No active leases found. Cannot perform vehicle maintenance.");
                    }
                }
                else if (choice == 5) {
                    vehicleLeasing.viewLeasingSubscriptions();
                }
                else {
                    System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the buffer
            }
        }

        scanner.close();
    }
}
