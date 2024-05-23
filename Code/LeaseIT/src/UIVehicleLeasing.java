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
    }

    public void submitPreferences(Database.VehiclePreferences preferences) {
        List<Database.Vehicle> vehicles = database.searchVehicles(preferences);
        System.out.println("Available Vehicles:");
        for (Database.Vehicle v : vehicles) {
            System.out.println("ID: " + v.getVehicleId() + ", Type: " + v.getType() + ", Make: " + v.getMake() + ", Model: " + v.getModel());
        }
    }

    public void selectVehicle(String vehicleId) {
        Database.VehicleDetails details = database.fetchVehicleDetails(vehicleId);
        if (details != null) {
            System.out.println("Selected Vehicle Details:");
            System.out.println("ID: " + details.getVehicleId() + ", Type: " + details.getType() + ", Make: " + details.getMake() + ", Model: " + details.getModel() + ", Year: " + details.getYear() + ", Price: " + details.getPrice());
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    public void confirmLeasingTerms(Database.UserDetails userDetails, Database.LeasingTerms terms) {
        boolean isCreditWorthy = taxGateway.checkCreditworthiness(userDetails, terms);
        if (isCreditWorthy) {
            messageService.displayApprovalMessage("Approved");
            leaseContract.finalizeLeaseContract();
        } else {
            messageService.displayRejectionMessage("Rejected");
            System.out.println("Creditworthiness check failed. Please revise your application.");
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
