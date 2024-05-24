import java.util.List;

public class UIVehicleLeasing {

    private Database database;
    private Vehicle vehicle;
    private LeaseContract leaseContract;
    private TaxGateway taxGateway;
    private Message messageService;
    private PaymentGateway paymentGateway;
    private Email emailService;

    public UIVehicleLeasing(Database database, Vehicle vehicle, LeaseContract leaseContract, TaxGateway taxGateway, Message messageService, PaymentGateway paymentGateway, Email emailService) {
        this.database = database;
        this.vehicle = vehicle;
        this.leaseContract = leaseContract;
        this.taxGateway = taxGateway;
        this.messageService = messageService;
        this.paymentGateway = paymentGateway;
        this.emailService = emailService;
    }

    public void showLeasingVehicleScreen() {
        System.out.println("Vehicle Leasing Screen");
        List<Database.VehiclePreferences> preferences = database.getVehiclePreferences();
        // Display preferences to user
    }

    public void submitPreferences(Database.VehiclePreferences preferences) {
        List<Vehicle.VehicleDetails> vehicles = vehicle.searchVehicles(preferences);
        System.out.println("Available Vehicles:");
        for (Vehicle.VehicleDetails v : vehicles) {
            System.out.println("ID: " + v.getVehicleId() + ", Type: " + v.getType() + ", Make: " + v.getMake() + ", Model: " + v.getModel());
        }
    }

    public void selectVehicle(String vehicleId) {
        Vehicle.VehicleDetails details = vehicle.fetchVehicleDetails(vehicleId);
        if (details != null) {
            System.out.println("Selected Vehicle Details:");
            System.out.println("ID: " + details.getVehicleId() + ", Type: " + details.getType() + ", Make: " + details.getMake() + ", Model: " + details.getModel() + ", Year: " + details.getYear() + ", Price: " + details.getPrice());
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    public void confirmLeasingTerms(Database.UserDetails userDetails, Database.LeasingTerms terms) {
        try {
            System.out.println("System connects to Tax Gateway...");
            Thread.sleep(1000); // Simulate loading
            System.out.println("Fetching Data...");
            Thread.sleep(1000); // Simulate loading
            boolean isCreditWorthy = taxGateway.checkCreditworthiness(userDetails, terms);
            if (isCreditWorthy) {
                System.out.println("Check Creditworthiness: Approved");
                Thread.sleep(1000); // Simulate loading
                System.out.println("Displaying Leasing Terms:");
                // Simulate displaying leasing terms
                System.out.println("Monthly Payment: " + terms.getMonthlyPayment());
                System.out.println("Lease Term: " + terms.getLeaseTerm() + " months");
                System.out.println("Mileage Limit: " + terms.getMileageLimit() + " miles");
                System.out.println("Do you accept the leasing terms? (yes/no):");
                messageService.displayApprovalMessage("Approved");
                leaseContract.finalizeLeaseContract();
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

    public void proceedToPayment(Database.PaymentDetails paymentDetails) {
        Database.PaymentConfirmation confirmation = paymentGateway.computePayment(paymentDetails);
        messageService.displayMessage("Payment Successful: " + confirmation.getTransactionId());
    }

    public void abandonLeasingProcess() {
        database.saveSessionSettings();
        emailService.sendEmail("Session Abandoned");
        System.out.println("Leasing process abandoned.");
    }
}
