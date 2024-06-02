import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class VehicleLeasing {

    private Vehicle vehicle;
    private Email emailService;
    private TaxGateway taxGateway;
    private Message messageService;
    private LeaseContract leaseContract;
    private User.UserDetails userDetails;
    private PaymentGateway paymentGateway;
    private SessionSettings sessionSettings;
    private VehiclePreferences currentPreferences;

    public VehicleLeasing(Vehicle vehicle, LeaseContract leaseContract, TaxGateway taxGateway, Message messageService, PaymentGateway paymentGateway, Email emailService) {
        this.vehicle = vehicle;
        this.leaseContract = leaseContract;
        this.taxGateway = taxGateway;
        this.messageService = messageService;
        this.paymentGateway = paymentGateway;
        this.emailService = emailService;
        this.userDetails = null;
        this.currentPreferences = null;
        sessionSettings = new SessionSettings();
    }

    public void showLeasingVehicleScreen() {

        System.out.println("\n------ Vehicle Leasing Screen ------");

        List<Set<String>> preferences = vehicle.getVehiclePreferences();

        System.out.println("\nVehicle Types: " + preferences.get(0));

        System.out.println("\nVehicle Brands: " + preferences.get(1));

        Scanner scanner = new Scanner(System.in);

        try {

            System.out.println("\nEnter your preferences: ");

            System.out.print("\nSelect Vehicle Type: ");
            String vehicleType = scanner.nextLine();

            System.out.print("\nSelect Preferred Brands: ");
            String preferredBrands = scanner.nextLine();

            System.out.print("\nBudget Price: ");
            int budget = scanner.nextInt();
            scanner.nextLine();

            VehiclePreferences preferencesInput = new VehiclePreferences(vehicleType, budget, preferredBrands);

            boolean vehiclesAvailable = submitPreferences(preferencesInput);

            if (!vehiclesAvailable) {
                return;
            }

            String vehicleId;
            System.out.print("\nSelect a vehicle (Enter vehicle ID): ");
            vehicleId = scanner.nextLine();

            selectVehicle(vehicleId);
            confirmLeasingTerms(userDetails);
            System.out.println("\nReturning to Home Screen...");

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }


    public boolean submitPreferences(VehiclePreferences preferences) {

        System.out.println("Test Data: Vehicle Type: " + preferences.getVehicleType() +
                        ", Budget: " + preferences.getBudget() +
                        ", Credit Score: " + userDetails.getCreditScore());

        this.currentPreferences = preferences;

        List<Vehicle> vehicles = vehicle.searchVehicles(preferences);

        if (vehicles.isEmpty()) {
            System.out.println("\nNo vehicles available with the specified preferences.");
            return false;
        }

        System.out.println("\n--------------------------- Available Vehicles ---------------------------");

        for (Vehicle v : vehicles) {
            System.out.println("ID: " + v.getVehicleId() +
                    ", Type: " + v.getType() +
                    ", Make: " + v.getMake() +
                    ", Model: " + v.getModel() +
                    ", Price: " + v.getPrice() +
                    ", Leased: " + (v.isLeased() ? "Yes" : "No"));
        }

        return true;
    }


    public void selectVehicle(String vehicleId) {

        Vehicle details = vehicle.fetchVehicleDetails(vehicleId);

        if (details != null) {

            System.out.println("\n----------------------------- Selected Vehicle Details -----------------------------");
            System.out.println("ID: " + details.getVehicleId() +
                    ", Type: " + details.getType() +
                    ", Make: " + details.getMake() +
                    ", Model: " + details.getModel() +
                    ", Year: " + details.getYear() +
                    ", Price: " + details.getPrice() +
                    ", Leased: " + (details.isLeased() ? "Yes" : "No"));
            System.out.println("------------------------------------------------------------------------------------");

            userDetails.setSelectedVehicleId(vehicleId); // Set the selected vehicle ID

        } else {
            System.out.println("\nVehicle not found.");
        }
    }


    public void confirmLeasingTerms(User.UserDetails userDetails) {
        try {
            Vehicle selectedVehicleDetails = vehicle.fetchVehicleDetails(userDetails.getSelectedVehicleId());

            System.out.println("\nSystem connects to Tax Gateway...");
            Thread.sleep(1000);

            System.out.println("\nFetching Data...");
            Thread.sleep(1000);

            System.out.println("\nCalculating Leasing Terms...");
            Thread.sleep(1000);

            leaseContract.calculateLeasingTerms(selectedVehicleDetails);

            boolean isCreditWorthy = taxGateway.checkCreditworthiness(userDetails, leaseContract);

            if (isCreditWorthy) {

                System.out.println("\nCheck Creditworthiness: Approved");
                Thread.sleep(1000);

                leaseContract.requestLeasingTerms();

                // Prompt user for approval
                Scanner scanner = new Scanner(System.in);
                System.out.print("\nDo you approve the leasing terms? (yes/no): ");
                String approval = scanner.nextLine();

                if (!approval.equalsIgnoreCase("yes")) {
                    System.out.println("\nLeasing process aborted by user.");
                    return;
                }

                messageService.displayApprovalMessage("Approved!");

                // Prompt user to proceed to payment
                System.out.print("\nProceed to Payment? (yes/no): ");
                String pay = scanner.nextLine();
                if (pay.equalsIgnoreCase("yes")) {
                    proceedToPayment();
                } else {
                    abandonLeasingProcess();
                }
            } else {
                System.out.println("Expected Result: Error: Creditworthiness check failed");
                System.out.println("Actual Result: Error: Creditworthiness check failed");
                System.out.println("\nCheck Creditworthiness: Rejected");
                Thread.sleep(1000);

                messageService.displayRejectionMessage("\nRejected");
                System.out.println("\nCreditworthiness check failed. Please revise your application.");
            }
        } catch (InterruptedException e) {
            System.out.println("\nAn error occurred: " + e.getMessage());
        }
    }


        public void proceedToPayment() throws InterruptedException {
        paymentGateway.completePayment();
        messageService.displayMessage("Successful Payment.");
        vehicle.leaseVehicle(userDetails.getSelectedVehicleId());
        leaseContract.addLease(userDetails.getSelectedVehicleId(), userDetails.getUsername());
    }


    public void abandonLeasingProcess() throws InterruptedException {
        SessionSettings settings = new SessionSettings();
        settings.setSelectedVehicleId(userDetails.getSelectedVehicleId());
        settings.setPreferences(currentPreferences);
        sessionSettings.saveSessionSettings(settings);
        emailService.sendEmail(userDetails.getUsername(), "Session Abandoned: " + settings);
        System.out.println("\nLeasing process abandoned.");

        Thread.sleep(1000);
        emailService.viewEmails(userDetails.getUsername());
        continueLeasingProcess();

    }

    public void continueLeasingProcess() {
        SessionSettings settings = sessionSettings.retrieveSessionSettings();
        if (settings != null) {
            System.out.println("\nContinuing your previous session...");
            userDetails.setSelectedVehicleId(settings.getSelectedVehicleId());
            currentPreferences = settings.getPreferences();
            submitPreferences(currentPreferences);
            selectVehicle(settings.getSelectedVehicleId());
            confirmLeasingTerms(userDetails);
        }
    }

    public void setUserDetails(User.UserDetails userDetails) {
        this.userDetails = userDetails;
    }


}
