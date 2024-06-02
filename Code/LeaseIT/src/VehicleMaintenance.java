import java.util.List;
import java.util.Scanner;

public class VehicleMaintenance {
    private Vehicle vehicle;
    private AI ai;
    private Cloud cloud;
    private MaintenanceCenter maintenanceCenter;
    private Email emailService;
    private Calendar calendar;
    UrgentService urgentService;
    private User userDetails;
    private LeaseContract leaseContract;

    public VehicleMaintenance(Vehicle vehicle, AI ai, Cloud cloud, MaintenanceCenter maintenanceCenter, Email emailService, Calendar calendar, User userDetails, LeaseContract leaseContract) {
        this.vehicle = vehicle;
        this.ai = ai;
        this.cloud = cloud;
        this.maintenanceCenter = maintenanceCenter;
        this.emailService = emailService;
        this.calendar = calendar;
        this.urgentService = new UrgentService();
        this.userDetails = userDetails;
        this.leaseContract = leaseContract;
    }

    public boolean hasActiveLease() {
        List<LeaseContract.LeasingSubscriptions> activeLeases = leaseContract.getUserLeasesByStatus(userDetails.getUsername(), "Active");
        return !activeLeases.isEmpty();
    }

    public void showVehicleMaintenace () throws InterruptedException {

        Scanner scanner = new Scanner(System.in);

        if (hasActiveLease()) {

            System.out.println("1. Maintenance Check");
            System.out.println("2. Urgent Maintenance");
            System.out.print("Select an option: ");
            int maintenanceChoice = scanner.nextInt();

            if (maintenanceChoice == 1) {
                performMaintenanceCheck();

            } else if (maintenanceChoice == 2) {
                urgentService.displayIssueDescription();
                urgentService.submitIssueDescription();
            }
        } else {
            System.out.println("No active leases found. Cannot perform vehicle maintenance.");
        }
    }
    public void performMaintenanceCheck() throws InterruptedException {

        List<LeaseContract.LeasingSubscriptions> activeLeases = leaseContract.getUserLeasesByStatus(userDetails.getUsername(), "Active");
        LeaseContract.LeasingSubscriptions activeLease = activeLeases.get(0); // Assuming the user has one active lease
        Vehicle vehicleDetails = vehicle.fetchVehicleDetails(activeLease.getVehicleID());

        if (vehicleDetails == null) {
            System.out.println("Vehicle details not found.");
            return;
        }

        vehicle.requestVehicleData();
        vehicle.getVehicleStatusData();
        vehicle.getMaintenanceHistory();
        ai.analyzeMaintenanceNeeds();

        System.out.println("\nAnalyzing maintenance needs...");
        boolean status = ai.analyzeMaintenanceNeeds();

        if(status){
            displayStatus(ai.getAnalysisResults());
            selectMaintenance();
        } else {
            displayStatus("Vehicle does not need maintenance");
            enablePeriodicChecksPrompt();
        }
    }

    private void enablePeriodicChecksPrompt() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nDo you want to enable periodic checks? (yes/no): ");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            enablePeriodicChecks();
        }
    }

    private void enablePeriodicChecks() throws InterruptedException {
        ai.enableScheduler();
        emailService.sendEmail(userDetails.getUsername(), "Periodic checks enabled for your vehicle.");
    }

    public void displayStatus(String status) {
        System.out.println("\nVehicle Maintenance Status: " + status);
    }

    public void selectMaintenance() throws InterruptedException {
        List<String> centers = maintenanceCenter.getAuthorizedCenters();
        displayCenters(centers);
        int centerIndex = selectCenter(centers.size());
        List<String> schedule = maintenanceCenter.getMaintenanceSchedule(centers.get(centerIndex));
        displaySchedule(schedule);
        int appointmentIndex = selectAppointment(schedule.size());
        String selectedAppointment = schedule.get(appointmentIndex);
        maintenanceCenter.confirmAppointment(centers.get(centerIndex), selectedAppointment);
        calendar.syncUserCalendar(selectedAppointment);
        emailService.sendEmail(userDetails.getUsername(), "Your maintenance appointment is confirmed for " + selectedAppointment);
    }

    private void displayCenters(List<String> centers) {
        System.out.println("\nAuthorized Maintenance Centers:");
        for (int i = 0; i < centers.size(); i++) {
            System.out.println((i + 1) + ". " + centers.get(i));
        }
    }

    private int selectCenter(int maxIndex) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a Maintenance Center (Enter number): ");
        int index = scanner.nextInt() - 1;
        if (index < 0 || index >= maxIndex) {
            System.out.println("\nInvalid selection. Please try again.");
            return selectCenter(maxIndex);
        }
        return index;
    }

    private void displaySchedule(List<String> schedule) {
        System.out.println("\nAvailable Maintenance Schedule:");
        for (int i = 0; i < schedule.size(); i++) {
            System.out.println((i + 1) + ". " + schedule.get(i));
        }
    }

    private int selectAppointment(int maxIndex) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select an Appointment Time (Enter number): ");
        int index = scanner.nextInt() - 1;
        if (index < 0 || index >= maxIndex) {
            System.out.println("\nInvalid selection. Please try again.");
            return selectAppointment(maxIndex);
        }
        return index;
    }

    public class UrgentService {

        public void displayIssueDescription() {
            System.out.print("\nPlease describe the issue you are experiencing with your vehicle: ");
        }

        public void submitIssueDescription() {
            Scanner scanner = new Scanner(System.in);
            String issueDescription = scanner.nextLine();
            System.out.println("\nIssue submitted: " + issueDescription);
            analyzeRisk(issueDescription);
        }

        private void analyzeRisk(String issueDescription) {
            ai.analyzeRisk(issueDescription);
            prioritizeUrgentMaintenance();
        }

        private void prioritizeUrgentMaintenance() {
            System.out.println("\nPrioritizing urgent maintenance...");
            maintenanceCenter.prioritizeUrgentMaintenance();
            try {
                selectMaintenance();
            } catch (InterruptedException e) {
                System.out.println("\nError while scheduling maintenance: " + e.getMessage());
            }
        }
    }
}
