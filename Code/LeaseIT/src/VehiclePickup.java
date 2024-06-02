import java.util.List;
import java.util.Scanner;

public class VehiclePickup {
    private User database;
    private LeaseContract leaseContract;
    private GoogleMaps googleMaps;
    private Dealership dealership;
    private Calendar calendar;
    private QR qr;
    private Wallet wallet;
    private Email emailService;
    private User userDetails;

    public VehiclePickup(User database, LeaseContract leaseContract, GoogleMaps googleMaps, Dealership dealership, Calendar calendar, Wallet wallet, Email emailService, User userDetails) {
        this.database = database;
        this.leaseContract = leaseContract;
        this.googleMaps = googleMaps;
        this.dealership = dealership;
        this.calendar = calendar;
        this.wallet = wallet;
        this.emailService = emailService;
        this.userDetails = userDetails;
        this.qr = new QR(emailService, userDetails);
    }

    public void showVehiclePickup() throws InterruptedException {

        List<LeaseContract.LeasingSubscriptions> pendingLeases = leaseContract.getUserLeasesByStatus(userDetails.getUsername(), "Pending");
        if (pendingLeases.isEmpty()) {
            System.out.println("\nNo pending leases available.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n**** Pending Leases ****");
        for (int i = 0; i < pendingLeases.size(); i++) {
            LeaseContract.LeasingSubscriptions lease = pendingLeases.get(i);
            System.out.println((i + 1) + ". Lease ID: " + lease.getLeaseID() + ", Vehicle ID: " + lease.getVehicleID() + ", Status: " + lease.getStatus());
        }

        System.out.print("Select a lease to proceed with vehicle pickup (Enter number): ");
        int leaseChoice = scanner.nextInt();
        scanner.nextLine();

        LeaseContract.LeasingSubscriptions selectedLease = pendingLeases.get(leaseChoice - 1);

        googleMaps.syncLocation();

        List<String> dealerships = dealership.retrieveDealerships();
        System.out.println("\nAvailable Dealerships:");
        for (int i = 0; i < dealerships.size(); i++) {
            System.out.println((i + 1) + ". " + dealerships.get(i));
        }

        System.out.print("Select a dealership (Enter number): ");
        int dealershipChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (dealershipChoice < 1 || dealershipChoice > dealerships.size()) {
            System.out.println("\nInvalid choice. Returning to home screen.");
            return;
        }

        String selectedDealership = dealerships.get(dealershipChoice - 1);

        Vehicle vehicleDetails = new Vehicle().fetchVehicleDetails(selectedLease.getVehicleID());

        if (vehicleDetails.getStatus().equals("Available")) {
            if (!dealership.isVehicleAvailableAtDealership(selectedLease.getVehicleID(), selectedDealership)) {
                System.out.println("\nThe selected vehicle is not available at the chosen dealership.");
                return;
            }
        } else if (vehicleDetails.getStatus().equals("UponRequest")) {
            dealership.notifyDelay();
            Thread.sleep(1000);
            monitorPreparation();
        }

        List<String> pickupTimes = dealership.checkAvailability(selectedDealership);

        System.out.println("\nAvailable Pickup Times:");
        for (int i = 0; i < pickupTimes.size(); i++) {
            System.out.println((i + 1) + ". " + pickupTimes.get(i));
        }

        System.out.print("Select a pickup time (Enter number): ");
        int timeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (timeChoice < 1 || timeChoice > pickupTimes.size()) {
            System.out.println("\nInvalid choice. Returning to home screen.");
            return;
        }

        String selectedTime = pickupTimes.get(timeChoice - 1);
        if (calendar.scheduleUserAppointment(selectedTime) && dealership.scheduleDealershipAppointment(selectedTime)) {
            dealership.sendEmail("Pickup scheduled for: " + selectedTime);
            String qrCode = qr.generateQRCode();
            wallet.importInWallet(qrCode);

            if (verifyQRCode(qrCode)) {
                emailService.sendEmail(userDetails.getUsername(), "Dealership: Proof of pickup email sent.");
                selectedLease.setStatus("Active");
                System.out.println("\nVehicle pickup process completed successfully.");
            } else {
                handleBrokenQRCode(selectedLease);
            }
        } else {
            System.out.println("\nFailed to schedule pickup. Please try again.");
        }
    }

    private boolean verifyQRCode(String qrCode) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nScan QR Code with wallet app (Enter 'scan' to simulate): ");
        String scanInput = scanner.nextLine();
        return "scan".equalsIgnoreCase(scanInput) && qr.verifyQRCode(qrCode);
    }

    private void monitorPreparation() throws InterruptedException {
        dealership.monitorPreparation();
        dealership.notifyCompletionEmail();
        emailService.sendEmail(userDetails.getUsername(), "Dealership: Your vehicle is ready for pickup.");
    }

    public void promptRegenerateCode() {
        System.out.println("\nDealership prompted to regenerate QR code.");
    }

    private void handleBrokenQRCode(LeaseContract.LeasingSubscriptions selectedLease) throws InterruptedException {

        dealership.reportFailedVerification();
        promptRegenerateCode();

        System.out.println("\nQR Code has been regenerated and sent via email.");
        Thread.sleep(1000);

        emailService.sendEmail(userDetails.getUsername(), "Pickup: QR Code: New QR Code has been sent for your vehicle pickup.");
        Thread.sleep(1000);

        boolean emailViewed = false;
        while (!emailViewed) {

        emailService.viewEmails(userDetails.getUsername());
        emailViewed = true;

        }
        String newQrCode = qr.generateQRCode();
        if (verifyQRCode(newQrCode)) {
            emailService.sendEmail(userDetails.getUsername(), "Dealership: Proof of pickup email sent.");
            selectedLease.setStatus("Active");
            System.out.println("\nVehicle pickup process completed successfully.");
        } else {
            System.out.println("\nQR Code verification failed. Please try again later.");
        }
    }
}
