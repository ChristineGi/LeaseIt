import java.util.ArrayList;
import java.util.List;

public class Database {

    private List<VehiclePreferences> vehiclePreferencesList;
    private List<UserDetails> userDetailsList;
    private SessionSettings sessionSettings;

    public Database() {
        // Initialize with some mock data
        vehiclePreferencesList = new ArrayList<>();
        userDetailsList = new ArrayList<>();
        sessionSettings = new SessionSettings();

        // Add mock data
        vehiclePreferencesList.add(new VehiclePreferences("Sedan", 15000, "Toyota"));
        vehiclePreferencesList.add(new VehiclePreferences("SUV", 30000, "Honda"));
        // Add more mock data as needed

        // Add user mock data
        userDetailsList.add(new UserDetails("user", 700, 60000)); // username, credit score, income
        userDetailsList.add(new UserDetails("user2", 650, 50000));
        userDetailsList.add(new UserDetails("user3", 600, 40000));
    }

    public List<VehiclePreferences> getVehiclePreferences() {
        return vehiclePreferencesList;
    }

    public List<UserDetails> getUserDetailsList() {
        return userDetailsList;
    }

    public UserDetails getUserDetails(String username) {
        for (UserDetails user : userDetailsList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void saveSessionSettings(SessionSettings settings) {
        this.sessionSettings = settings;
    }

    public SessionSettings retrieveSessionSettings() {
        return this.sessionSettings;
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

    public static class SessionSettings {
        private String selectedVehicleId;
        private VehiclePreferences preferences;

        public String getSelectedVehicleId() {
            return selectedVehicleId;
        }

        public void setSelectedVehicleId(String selectedVehicleId) {
            this.selectedVehicleId = selectedVehicleId;
        }

        public VehiclePreferences getPreferences() {
            return preferences;
        }

        public void setPreferences(VehiclePreferences preferences) {
            this.preferences = preferences;
        }
    }

    public static class UserDetails {
        private String username;
        private int creditScore;
        private int income;
        private String selectedVehicleId;

        public UserDetails(String username, int creditScore, int income) {
            this.username = username;
            this.creditScore = creditScore;
            this.income = income;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public int getCreditScore() {
            return creditScore;
        }

        public int getIncome() {
            return income;
        }

        public String getSelectedVehicleId() {
            return selectedVehicleId;
        }

        public void setSelectedVehicleId(String selectedVehicleId) {
            this.selectedVehicleId = selectedVehicleId;
        }
    }
}