import java.util.List;
import java.util.Scanner;

public class VehiclePickup {
    private Database database;
    private LeaseContract leaseContract;
    private GoogleMaps googleMaps;
    private Dealership dealership;
    private Calendar calendar;
    private QR qr;
    private Wallet wallet;
    private Email emailService;
    private Database.UserDetails userDetails;

    public VehiclePickup(Database database, LeaseContract leaseContract, GoogleMaps googleMaps, Dealership dealership, Calendar calendar, Wallet wallet, Email emailService, Database.UserDetails userDetails) {
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
        List<LeaseContract.Leasing> pendingLeases = leaseContract.getUserLeasesByStatus(userDetails.getUsername(), "Pending");
        if (pendingLeases.isEmpty()) {
            System.out.println("No pending leases available.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Pending Leases:");
        for (int i = 0; i < pendingLeases.size(); i++) {
            LeaseContract.Leasing lease = pendingLeases.get(i);
            System.out.println((i + 1) + ". Lease ID: " + lease.getLeaseID() + ", Vehicle ID: " + lease.getVehicleID() + ", Status: " + lease.getStatus());
        }

        System.out.println("Select a lease to proceed with vehicle pickup (Enter number):");
        int leaseChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (leaseChoice < 1 || leaseChoice > pendingLeases.size()) {
            System.out.println("Invalid choice. Returning to home screen.");
            return;
        }

        LeaseContract.Leasing selectedLease = pendingLeases.get(leaseChoice - 1);
        googleMaps.syncLocation();

        List<String> dealerships = dealership.retrieveDealerships();
        System.out.println("Available Dealerships:");
        for (int i = 0; i < dealerships.size(); i++) {
            System.out.println((i + 1) + ". " + dealerships.get(i));
        }

        System.out.println("Select a dealership (Enter number):");
        int dealershipChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (dealershipChoice < 1 || dealershipChoice > dealerships.size()) {
            System.out.println("Invalid choice. Returning to home screen.");
            return;
        }

        String selectedDealership = dealerships.get(dealershipChoice - 1);
        Vehicle.VehicleDetails vehicleDetails = new Vehicle().fetchVehicleDetails(selectedLease.getVehicleID());

        if (vehicleDetails.getStatus().equals("Available")) {
            if (!dealership.isVehicleAvailableAtDealership(selectedLease.getVehicleID(), selectedDealership)) {
                System.out.println("The selected vehicle is not available at the chosen dealership.");
                return;
            }
        }else if (vehicleDetails.getStatus().equals("UponRequest")) {
            System.out.println("The selected vehicle is upon request and needs to be prepared.");
            Thread.sleep(1000);
            monitorPreparation();
        }

            List<String> pickupTimes = dealership.checkAvailability(selectedDealership);
            System.out.println("Available Pickup Times:");
            for (int i = 0; i < pickupTimes.size(); i++) {
                System.out.println((i + 1) + ". " + pickupTimes.get(i));
            }

            System.out.println("Select a pickup time (Enter number):");
            int timeChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (timeChoice < 1 || timeChoice > pickupTimes.size()) {
                System.out.println("Invalid choice. Returning to home screen.");
                return;
            }

            String selectedTime = pickupTimes.get(timeChoice - 1);
            if (calendar.scheduleUserAppointment(selectedTime) && dealership.scheduleDealershipAppointment(selectedTime)) {
                dealership.sendEmail("Pickup scheduled for: " + selectedTime);
                String qrCode = qr.generateQRCode();
                wallet.importInWallet(qrCode);
                System.out.println("QR Code for pickup: " + qrCode);
                System.out.println("Scan the QR code with your wallet app to complete the process.");

                System.out.println("Scan QR Code with wallet app (Enter 'scan' to simulate):");
                String scanInput = scanner.nextLine();
                if ("scan".equalsIgnoreCase(scanInput) && qr.verifyQRCode(qrCode)) {
                    qr.resendProofEmail();
                    emailService.sendEmail(userDetails.getUsername(), "Proof of pickup email sent.");
                    selectedLease.setStatus("Active");
                    System.out.println("Vehicle pickup process completed successfully.");
                } else {
                    handleBrokenQRCode(selectedLease);
                }
            } else {
                System.out.println("Failed to schedule pickup. Please try again.");
            }

    }

    private void monitorPreparation() throws InterruptedException {
        dealership.monitorPreparation();
        dealership.notifyCompletionEmail();
        emailService.sendEmail(userDetails.getUsername(), "Your vehicle is ready for pickup.");
        System.out.println("Your vehicle is being prepared. A notification email will be sent when ready.");
    }

    private void handleBrokenQRCode(LeaseContract.Leasing selectedLease) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("QR Code verification failed. Report failed verification to dealership? (yes/no):");
        String report = scanner.nextLine();

        if (report.equalsIgnoreCase("yes")) {

            dealership.reportFailedVerification();
            dealership.promptRegenerateCode();
            emailService.sendEmail(userDetails.getUsername(), "QR Code: New QR Code has been sent for your vehicle pickup.");

            Thread.sleep(1000);
            System.out.println("QR Code has been regenerated and sent via email.");
            Thread.sleep(1000);

            System.out.println("You have received a new QR Code email. Please select 'View Emails' from the main menu to proceed.");

            boolean emailViewed = false;

            while (!emailViewed) {

                System.out.println("Enter 'view emails' to open your emails:");
                String emailCommand = scanner.nextLine();

                if (emailCommand.equalsIgnoreCase("view emails")) {
                    List<String> emails = emailService.getEmails(userDetails.getUsername());
                    if (emails.isEmpty()) {
                        System.out.println("You've got no emails.");
                    } else {
                        System.out.println("Emails:");
                        for (int i = 0; i < emails.size(); i++) {
                            System.out.println((i + 1) + ". " + emails.get(i));
                        }
                        System.out.println("Enter the email number to read or '0' to go back:");
                        int emailChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (emailChoice > 0 && emailChoice <= emails.size()) {
                            System.out.println("Email: " + emails.get(emailChoice - 1));
                            emailViewed = true;
                        } else {
                            System.out.println("Invalid choice. Returning to main menu.");
                        }
                    }
                }
            }

            System.out.println("Scan QR Code with wallet app (Enter 'scan' to simulate):");

            String scanInput = scanner.nextLine();

            String newQrCode = qr.generateQRCode();

            if ("scan".equalsIgnoreCase(scanInput) && qr.verifyQRCode(newQrCode)) {

                emailService.sendEmail(userDetails.getUsername(), "Dealership: Proof of pickup email sent.");
                selectedLease.setStatus("Active");
                System.out.println("Vehicle pickup process completed successfully.");

            } else {
                System.out.println("QR Code verification failed. Please try again later.");
            }
        } else {
            System.out.println("QR Code verification failed. Please try again later.");
        }
    }
}