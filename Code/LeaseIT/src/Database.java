import java.util.ArrayList;
import java.util.List;

public class Database {

    private List<VehiclePreferences> vehiclePreferencesList;
    private List<Vehicle> vehicles;
    private List<VehicleDetails> vehicleDetailsList;

    public Database() {
        // Initialize with some mock data
        vehiclePreferencesList = new ArrayList<>();
        vehicles = new ArrayList<>();
        vehicleDetailsList = new ArrayList<>();

        // Add mock data
        vehiclePreferencesList.add(new VehiclePreferences("Sedan", "15000-20000", "Toyota"));
        vehiclePreferencesList.add(new VehiclePreferences("SUV", "20000-30000", "Honda"));
        vehicles.add(new Vehicle("V1234", "Sedan", "Toyota", "Camry"));
        vehicles.add(new Vehicle("V5678", "SUV", "Honda", "CR-V"));
        vehicleDetailsList.add(new VehicleDetails("V1234", "Sedan", "Toyota", "Camry", 2021, 15000));
        vehicleDetailsList.add(new VehicleDetails("V5678", "SUV", "Honda", "CR-V", 2022, 20000));
    }

    public List<VehiclePreferences> getVehiclePreferences() {
        return vehiclePreferencesList;
    }

    public List<Vehicle> searchVehicles(VehiclePreferences preferences) {
        return vehicles; // Simplified for this example
    }

    public VehicleDetails fetchVehicleDetails(String vehicleId) {
        for (VehicleDetails details : vehicleDetailsList) {
            if (details.getVehicleId().equals(vehicleId)) {
                return details;
            }
        }
        return null;
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
        private String budgetRange;
        private String preferredBrands;

        public VehiclePreferences(String vehicleType, String budgetRange, String preferredBrands) {
            this.vehicleType = vehicleType;
            this.budgetRange = budgetRange;
            this.preferredBrands = preferredBrands;
        }

        // Getters and Setters
    }

    public static class Vehicle {
        private String vehicleId;
        private String type;
        private String make;
        private String model;

        public Vehicle(String vehicleId, String type, String make, String model) {
            this.vehicleId = vehicleId;
            this.type = type;
            this.make = make;
            this.model = model;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public String getType() {
            return type;
        }

        public String getMake() {
            return make;
        }

        public String getModel() {
            return model;
        }

        // Other getters and setters
    }

    public static class VehicleDetails {
        private String vehicleId;
        private String type;
        private String make;
        private String model;
        private int year;
        private int price;

        public VehicleDetails(String vehicleId, String type, String make, String model, int year, int price) {
            this.vehicleId = vehicleId;
            this.type = type;
            this.make = make;
            this.model = model;
            this.year = year;
            this.price = price;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public String getType() {
            return type;
        }

        public String getMake() {
            return make;
        }

        public String getModel() {
            return model;
        }

        public int getYear() {
            return year;
        }

        public int getPrice() {
            return price;
        }

        // Other getters and setters
    }

    public static class LeasingTerms {
        // Fields and methods for leasing terms
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
