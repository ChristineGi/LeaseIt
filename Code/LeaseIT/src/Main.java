import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
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

        // Initialize UI
        VehicleLeasing vehicleLeasing = new VehicleLeasing(database, vehicle, leaseContract, taxGateway, messageService, paymentGateway, emailService);
        Scanner scanner = new Scanner(System.in);

        // Login system
        Database.UserDetails userDetails = null;
        while (true) {
            System.out.println("Login Screen");
            System.out.println("Enter your username:");
            loggedInUsername = scanner.nextLine();
            userDetails = database.getUserDetails(loggedInUsername);
            if (userDetails == null) {
                System.out.println("Username not found. Try again.");
            } else {
                vehicleLeasing.setUserDetails(userDetails);
                System.out.println("Login successful. Welcome, " + loggedInUsername + "!");
                break;
            }
        }

        while (true) {
            try {
                System.out.println("Home Screen");
                System.out.println("1. Vehicle Leasing");
                System.out.println("2. View Emails");
                System.out.println("3. Exit");
                int choice = scanner.nextInt();

                if (choice == 3) {
                    break;
                }

                if (choice == 1) {
                    vehicleLeasing.showLeasingVehicleScreen();

                    try {
                        // Entering leasing preferences
                        System.out.println("Enter your preferences:");

                        // Display distinct vehicle types
                        Set<String> vehicleTypes = vehicle.getVehicleTypes();
                        System.out.println("Vehicle Type (select from options): " + vehicleTypes);
                        scanner.nextLine();  // Consume newline
                        String vehicleType = scanner.nextLine();

                        // Display distinct vehicle brands
                        Set<String> vehicleBrands = vehicle.getVehicleBrands();
                        System.out.println("Preferred Brands (select from options): " + vehicleBrands);
                        String preferredBrands = scanner.nextLine();

                        System.out.println("Budget:");
                        int budget = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        Database.VehiclePreferences preferences = new Database.VehiclePreferences(vehicleType, budget, preferredBrands);
                        boolean vehiclesAvailable = vehicleLeasing.submitPreferences(preferences);
                        if (!vehiclesAvailable) {
                            continue; // Stop the process if no vehicles are found
                        }

                        // Selecting a vehicle
                        System.out.println("Select a vehicle (Enter vehicle ID):");
                        String vehicleId = scanner.nextLine();
                        vehicleLeasing.selectVehicle(vehicleId);

                        // Set the selected vehicle ID in userDetails
                        userDetails.setSelectedVehicleId(vehicleId);
                        vehicleLeasing.confirmLeasingTerms(userDetails);

                        System.out.println("Returning to Home Screen...");
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                } else if (choice == 2) {
                    List<String> emails = emailService.getEmails(loggedInUsername);
                    if (emails.isEmpty()) {
                        System.out.println("You've got no emails.");
                    } else {
                        System.out.println("Emails:");
                        for (int i = 0; i < emails.size(); i++) {
                            System.out.println((i + 1) + ". " + emails.get(i));
                        }
                        System.out.println("Enter the email number to continue the leasing process or '0' to go back:");
                        int emailChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (emailChoice > 0 && emailChoice <= emails.size()) {
                            vehicleLeasing.continueLeasingProcess();
                        }
                    }
                } else {
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the buffer
            }
        }

        scanner.close();
    }
}
