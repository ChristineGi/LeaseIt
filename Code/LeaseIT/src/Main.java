import java.util.Scanner;

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
            System.out.println("Home Screen");
            System.out.println("1. Vehicle Leasing");
            System.out.println("2. Exit");
            int choice = scanner.nextInt();

            if (choice == 2) {
                break;
            }

            if (choice == 1) {
                vehicleLeasingUI.showLeasingVehicleScreen();

                // Entering leasing preferences
                System.out.println("Enter your preferences:");
                System.out.println("Vehicle Type:");
                scanner.nextLine();  // Consume newline
                String vehicleType = scanner.nextLine();
                System.out.println("Budget Range:");
                String budgetRange = scanner.nextLine();
                System.out.println("Preferred Brands:");
                String preferredBrands = scanner.nextLine();

                Database.VehiclePreferences preferences = new Database.VehiclePreferences(vehicleType, budgetRange, preferredBrands);
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
            }
        }

        scanner.close();
    }
}
