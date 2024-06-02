import java.util.ArrayList;
import java.util.List;

public class Dealership {
    public List<String> retrieveDealerships() {
        List<String> dealerships = new ArrayList<>();
        dealerships.add("Dealership 1");
        dealerships.add("Dealership 2");
        return dealerships;
    }

    public List<String> checkAvailability(String dealershipID) {
        List<String> pickupTimes = new ArrayList<>();
        pickupTimes.add("10:00 AM");
        pickupTimes.add("02:00 PM");
        return pickupTimes;
    }

    public boolean isVehicleAvailableAtDealership(String vehicleId, String dealership) {
        Vehicle vehicle = new Vehicle();
        Vehicle details = vehicle.fetchVehicleDetails(vehicleId);
        return details != null && details.getDealership().equals(dealership) && details.getStatus().equals("Available");
    }

    public boolean scheduleDealershipAppointment(String time) {
        // Logic to schedule dealership appointment
        System.out.println("\nDealership appointment scheduled for: " + time);
        return true;
    }

    public void sendEmail(String details) {
        System.out.println("\nEmail sent with details: " + details);
    }

    public void notifyDelay() throws InterruptedException {
        System.out.println("\nThe selected vehicle is upon request and needs to be prepared.");
        Thread.sleep(1000);
    }

    public void monitorPreparation() throws InterruptedException {
        System.out.println("\nMonitoring vehicle preparation...");
        Thread.sleep(3000);

    }

    public void notifyCompletionEmail() throws InterruptedException {
        System.out.println("\nVehicle preparation completed !");
        Thread.sleep(1000);
        System.out.println("\nSending notification email...");
        Thread.sleep(1000);
    }

    public void reportFailedVerification() throws InterruptedException {
        System.out.println("\nReported failed QR code verification to the dealership.");
        Thread.sleep(1000);

    }

}
