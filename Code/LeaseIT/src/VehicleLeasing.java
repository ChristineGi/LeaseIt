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
            System.out.println("ID: " + v.getVehicleId() + ", Type: " + v.getType() + ", Make: " + v.getMake() + ", Model: " + v.getModel() + ", Price: " + v.getPrice() + ", Leased: " + (v.isLeased() ? "Yes" : "No"));
        }
        return true;
    }

    public void selectVehicle(String vehicleId) {
        Vehicle.VehicleDetails details = vehicle.fetchVehicleDetails(vehicleId);
        if (details != null) {
            System.out.println("Selected Vehicle Details:");
            System.out.println("ID: " + details.getVehicleId() + ", Type: " + details.getType() + ", Make: " + details.getMake() + ", Model: " + details.getModel() + ", Year: " + details.getYear() + ", Price: " + details.getPrice() + ", Leased: " + (details.isLeased() ? "Yes" : "No"));
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
                System.out.println("Leasing terms accepted. Proceeding to payment...");

                LeaseContract.LeasingTerms finalTerms = leaseContract.requestLeasingTerms(calculatedTerms);
                messageService.displayApprovalMessage("Approved");
                leaseContract.finalizeLeaseContract();

                // Prompt user to proceed to payment
                Scanner scanner = new Scanner(System.in);
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
            vehicle.leaseVehicle(userDetails.getSelectedVehicleId());
            leaseContract.addLease(userDetails.getSelectedVehicleId(), userDetails.getUsername());
        }
    }

    public void abandonLeasingProcess() {
        Database.SessionSettings settings = new Database.SessionSettings();
        settings.setSelectedVehicleId(userDetails.getSelectedVehicleId());
        settings.setPreferences(currentPreferences);
        database.saveSessionSettings(settings);
        emailService.sendEmail(userDetails.getUsername(), "Session Abandoned: " + settings);
        System.out.println("Leasing process abandoned.");
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

    public void viewLeasingSubscriptions() {
        List<Vehicle.VehicleDetails> leasedVehicles = vehicle.getLeasedVehicles();
        if (leasedVehicles.isEmpty()) {
            System.out.println("You have no active leasing subscriptions.");
        } else {
            System.out.println("Your Leasing Subscriptions:");
            for (Vehicle.VehicleDetails details : leasedVehicles) {
                System.out.println("ID: " + details.getVehicleId() + ", Type: " + details.getType() + ", Make: " + details.getMake()
                        + ", Model: " + details.getModel() + ", Year: " + details.getYear() + ", Price: " + details.getPrice() + ", Status: " + details.getStatus());
            }
        }
        List<LeaseContract.Leasing> pendingLeases = leaseContract.getUserLeasesByStatus(userDetails.getUsername(), "Pending");
        if (pendingLeases.isEmpty()) {
            System.out.println("You have no pending vehicle pickups.");
        } else {
            System.out.println("Your Pending Vehicle Pickups:");
            for (LeaseContract.Leasing lease : pendingLeases) {
                System.out.println("Lease ID: " + lease.getLeaseID() + ", Vehicle ID: " + lease.getVehicleID() + ", Status: " + lease.getStatus());
            }
        }
    }
}
