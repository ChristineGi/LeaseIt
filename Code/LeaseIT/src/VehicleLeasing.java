import java.util.List;
import java.util.Scanner;

public class VehicleLeasing {

    private Database database;
    private Vehicle vehicle;
    private LeaseContract leaseContract;
    private TaxGateway taxGateway;
    private Message messageService;
    private PaymentGateway paymentGateway;
    private Email emailService;
    private Database.UserDetails userDetails;
    private Database.VehiclePreferences currentPreferences;

    public VehicleLeasing(Database database, Vehicle vehicle, LeaseContract leaseContract, TaxGateway taxGateway, Message messageService, PaymentGateway paymentGateway, Email emailService) {
        this.database = database;
        this.vehicle = vehicle;
        this.leaseContract = leaseContract;
        this.taxGateway = taxGateway;
        this.messageService = messageService;
        this.paymentGateway = paymentGateway;
        this.emailService = emailService;
        this.userDetails = null;
        this.currentPreferences = null;
    }

    public void setUserDetails(Database.UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public void showLeasingVehicleScreen() {
        System.out.println("Vehicle Leasing Screen");
        List<Database.VehiclePreferences> preferences = database.getVehiclePreferences();
        // Display preferences to user
    }

    public boolean submitPreferences(Database.VehiclePreferences preferences) {
        this.currentPreferences = preferences;
        List<Vehicle.VehicleDetails> vehicles = vehicle.searchVehicles(preferences);
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles available with the specified preferences.");
            return false;
        }
        System.out.println("Available Vehicles:");
        for (Vehicle.VehicleDetails v : vehicles) {
            System.out.println("ID: " + v.getVehicleId() + ", Type: " + v.getType() + ", Make: " + v.getMake() + ", Model: " + v.getModel());
        }
        return true;
    }

    public void selectVehicle(String vehicleId) {
        Vehicle.VehicleDetails details = vehicle.fetchVehicleDetails(vehicleId);
        if (details != null) {
            System.out.println("Selected Vehicle Details:");
            System.out.println("ID: " + details.getVehicleId() + ", Type: " + details.getType() + ", Make: " + details.getMake() + ", Model: " + details.getModel() + ", Year: " + details.getYear() + ", Price: " + details.getPrice());
            userDetails.setSelectedVehicleId(vehicleId); // Set the selected vehicle ID
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    public void confirmLeasingTerms(Database.UserDetails userDetails) {
        try {
            System.out.println("System connects to Tax Gateway...");
            Thread.sleep(1000); // Simulate loading
            System.out.println("Fetching Data...");
            Thread.sleep(1000); // Simulate loading

            System.out.println("Calculating Leasing Terms...");
            Thread.sleep(1000); // Simulate loading
            Vehicle.VehicleDetails selectedVehicleDetails = vehicle.fetchVehicleDetails(userDetails.getSelectedVehicleId());
            LeaseContract.LeasingTerms calculatedTerms = leaseContract.calculateLeasingTerms(selectedVehicleDetails);

            boolean isCreditWorthy = taxGateway.checkCreditworthiness(userDetails, calculatedTerms);
            if (isCreditWorthy) {
                System.out.println("Check Creditworthiness: Approved");
                Thread.sleep(1000); // Simulate loading

                System.out.println("Displaying Leasing Terms:");
                System.out.println("Monthly Payment: " + calculatedTerms.getMonthlyPayment());
                System.out.println("Lease Term: " + calculatedTerms.getLeaseTerm() + " months");
                System.out.println("Mileage Limit: " + calculatedTerms.getMileageLimit() + " miles");

                Scanner scanner = new Scanner(System.in);

                System.out.println("Approve Leasing Terms ? (yes/no):");
                String consest = scanner.nextLine();
                if (consest.equalsIgnoreCase("yes")) {
                    messageService.displayApprovalMessage("Leasing terms accepted !");

                }
                LeaseContract.LeasingTerms finalTerms = leaseContract.requestLeasingTerms(calculatedTerms);
                leaseContract.finalizeLeaseContract();

                // Prompt user to proceed to payment
                System.out.println("Proceed to Payment? (yes/no):");
                String pay = scanner.nextLine();
                if (pay.equalsIgnoreCase("yes")) {
                    PaymentGateway.PaymentDetails paymentDetails = new PaymentGateway.PaymentDetails();
                    proceedToPayment(paymentDetails);
                } else {
                    abandonLeasingProcess();
                }
            } else {
                System.out.println("Check Creditworthiness: Rejected");
                Thread.sleep(1000); // Simulate loading
                messageService.displayRejectionMessage("Rejected");
                System.out.println("Creditworthiness check failed. Please revise your application.");
            }
        } catch (InterruptedException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void proceedToPayment(PaymentGateway.PaymentDetails paymentDetails) throws InterruptedException {
        PaymentGateway.PaymentConfirmation confirmation = paymentGateway.computePayment(paymentDetails);
        if (confirmation != null) {
            messageService.displayMessage("Payment Successful: " + confirmation.getTransactionId());
        }
    }

    public void abandonLeasingProcess() throws InterruptedException {
        Database.SessionSettings settings = new Database.SessionSettings();
        settings.setSelectedVehicleId(userDetails.getSelectedVehicleId());
        settings.setPreferences(currentPreferences);
        System.out.println("Saving Session Info ...");
        Thread.sleep(1000); // Simulate loading
        database.saveSessionSettings(settings);
        emailService.sendEmail(userDetails.getUsername(), "LeaseIT: Session Abandoned !");
        System.out.println("Leasing process abandoned...");
        Thread.sleep(1000); // Simulate loading

    }

    public void continueLeasingProcess() {
        Database.SessionSettings settings = database.retrieveSessionSettings();
        if (settings != null) {
            System.out.println("Continuing your previous session...");
            userDetails.setSelectedVehicleId(settings.getSelectedVehicleId());
            currentPreferences = settings.getPreferences();
            submitPreferences(currentPreferences);
            selectVehicle(settings.getSelectedVehicleId());
            confirmLeasingTerms(userDetails);
        }
    }
}
