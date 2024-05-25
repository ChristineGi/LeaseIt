import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class VehicleMaintenance {
    private Vehicle vehicle;
    private AI ai;
    private Cloud cloud;
    private MaintenanceCenter maintenanceCenter;
    private Email emailService;
    private Calendar calendar;
    UrgentService urgentService;
    private Database.UserDetails userDetails;
    private LeaseContract leaseContract;

    public VehicleMaintenance(Vehicle vehicle, AI ai, Cloud cloud, MaintenanceCenter maintenanceCenter, Email emailService, Calendar calendar, Database.UserDetails userDetails, LeaseContract leaseContract) {
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
        List<LeaseContract.Leasing> activeLeases = leaseContract.getUserLeasesByStatus(userDetails.getUsername(), "Active");
        return !activeLeases.isEmpty();
    }

    public void performMaintenanceCheck() throws InterruptedException {
        List<LeaseContract.Leasing> activeLeases = leaseContract.getUserLeasesByStatus(userDetails.getUsername(), "Active");
        LeaseContract.Leasing activeLease = activeLeases.get(0); // Assuming the user has one active lease
        Vehicle.VehicleDetails vehicleDetails = vehicle.fetchVehicleDetails(activeLease.getVehicleID());

        if (vehicleDetails == null) {
            System.out.println("Vehicle details not found.");
            return;
        }

        vehicle.requestVehicleData();
        vehicle.getVehicleStatusData();
        vehicle.getMaintenanceHistory();
        ai.analyzeMaintenanceNeeds();

        Random random = new Random();
        boolean needsMaintenance = random.nextBoolean(); // Simulate 50/50 chance

        if (needsMaintenance) {
            displayStatus("Vehicle needs maintenance");
            displayStatus(ai.getAnalysisResults());
        } else {
            displayStatus("Vehicle does not need maintenance");
            enablePeriodicChecksPrompt();
        }
    }

    private void enablePeriodicChecksPrompt() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Vehicle does not need maintenance. Do you want to enable periodic checks? (yes/no):");
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
        System.out.println("Vehicle Maintenance Status: " + status);
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
        System.out.println("Authorized Maintenance Centers:");
        for (int i = 0; i < centers.size(); i++) {
            System.out.println((i + 1) + ". " + centers.get(i));
        }
    }

    private int selectCenter(int maxIndex) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select a Maintenance Center (Enter number):");
        int index = scanner.nextInt() - 1;
        if (index < 0 || index >= maxIndex) {
            System.out.println("Invalid selection. Please try again.");
            return selectCenter(maxIndex);
        }
        return index;
    }

    private void displaySchedule(List<String> schedule) {
        System.out.println("Available Maintenance Schedule:");
        for (int i = 0; i < schedule.size(); i++) {
            System.out.println((i + 1) + ". " + schedule.get(i));
        }
    }

    private int selectAppointment(int maxIndex) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select an Appointment Time (Enter number):");
        int index = scanner.nextInt() - 1;
        if (index < 0 || index >= maxIndex) {
            System.out.println("Invalid selection. Please try again.");
            return selectAppointment(maxIndex);
        }
        return index;
    }

    public class UrgentService {
        public void displayIssueDescription() {
            System.out.println("Please describe the issue you are experiencing with your vehicle:");
        }

        public void submitIssueDescription() {
            Scanner scanner = new Scanner(System.in);
            String issueDescription = scanner.nextLine();
            System.out.println("Issue submitted: " + issueDescription);
            analyzeRisk(issueDescription);
        }

        private void analyzeRisk(String issueDescription) {
            System.out.println("Analyzing risk of the issue...");
            ai.analyzeRisk(issueDescription);
            prioritizeUrgentMaintenance();
        }

        private void prioritizeUrgentMaintenance() {
            System.out.println("Prioritizing urgent maintenance...");
            maintenanceCenter.prioritizeUrgentMaintenance();
            try {
                selectMaintenance();
            } catch (InterruptedException e) {
                System.out.println("Error while scheduling maintenance: " + e.getMessage());
            }
        }
    }
}
