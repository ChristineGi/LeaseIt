
import  java.util.List;
import java.util.ArrayList;

public class Database {

    private List<VehiclePreferences> vehiclePreferencesList;

    public Database() {
        // Initialize with some mock data
        vehiclePreferencesList = new ArrayList<>();

        // Add mock data
        vehiclePreferencesList.add(new VehiclePreferences("Sedan", 15000, "Toyota"));
        vehiclePreferencesList.add(new VehiclePreferences("SUV", 30000, "Honda"));
        // Add more mock data as needed
    }

    public List<VehiclePreferences> getVehiclePreferences() {
        return vehiclePreferencesList;
    }

    public void saveSessionSettings() {
        // Save session settings to database
    }

    public SessionSettings retrieveSessionSettings() {
        return new SessionSettings();
    }

    // Supporting classes as inner classes

    public static class VehiclePreferences {
        private String vehicleType;
        private int budget;
        private String preferredBrands;

        public VehiclePreferences(String vehicleType, int budget, String preferredBrands) {
            this.vehicleType = vehicleType;
            this.budget = budget;
            this.preferredBrands = preferredBrands;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public int getBudget() {
            return budget;
        }

        public String getPreferredBrands() {
            return preferredBrands;
        }

        // Getters and Setters
    }

    public static class LeasingTerms {
        private double monthlyPayment;
        private int leaseTerm;
        private int mileageLimit;

        public LeasingTerms() {
            // Default values for demonstration
            this.monthlyPayment = 300.0;
            this.leaseTerm = 36; // 36 months
            this.mileageLimit = 12000; // 12000 miles per year
        }

        public double getMonthlyPayment() {
            return monthlyPayment;
        }

        public int getLeaseTerm() {
            return leaseTerm;
        }

        public int getMileageLimit() {
            return mileageLimit;
        }

        // Setters can be added as needed
    }

    public static class PaymentDetails {
        // Fields and methods for payment details
    }

    public static class PaymentConfirmation {
        public String getTransactionId() {
            return "12345"; // Example transaction id
        }
    }

    public static class SessionSettings {
        // Fields and methods for session settings
    }

    public static class UserDetails {
        // Fields and methods for user details
    }
}
