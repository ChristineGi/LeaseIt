import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

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
        UIVehicleLeasing vehicleLeasingUI = new UIVehicleLeasing(database, vehicle, leaseContract, taxGateway, messageService, paymentGateway, emailService);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Home Screen");
                System.out.println("1. Vehicle Leasing");
                System.out.println("2. Exit");
                int choice = scanner.nextInt();

                if (choice == 2) {
                    break;
                }

                if (choice == 1) {
                    vehicleLeasingUI.showLeasingVehicleScreen();

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
                        vehicleLeasingUI.submitPreferences(preferences);

                        // Selecting a vehicle
                        System.out.println("Select a vehicle (Enter vehicle ID):");
                        String vehicleId = scanner.nextLine();
                        vehicleLeasingUI.selectVehicle(vehicleId);

                        // Confirming leasing terms
                        Database.LeasingTerms terms = new Database.LeasingTerms();
                        Database.UserDetails userDetails = new Database.UserDetails(); // Assuming we have user details
                        vehicleLeasingUI.confirmLeasingTerms(userDetails, terms);

                        // Confirm consent and proceed to payment
                        System.out.println("Accept the leasing terms? (yes/no):");
                        String consent = scanner.nextLine();
                        if (consent.equalsIgnoreCase("yes")) {
                            Database.PaymentDetails paymentDetails = new Database.PaymentDetails();
                            vehicleLeasingUI.proceedToPayment(paymentDetails);
                        }

                        System.out.println("Returning to Home Screen...");
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid choice. Please select 1 or 2.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the buffer
            }
        }

        scanner.close();
    }
}
