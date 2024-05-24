import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Vehicle {

    private List<VehicleDetails> vehicleDetailsList;

    public Vehicle() {
        vehicleDetailsList = new ArrayList<>();
        vehicleDetailsList.add(new VehicleDetails("V1234", "Sedan", "Toyota", "Camry", 2021, 15000));
        vehicleDetailsList.add(new VehicleDetails("V5678", "SUV", "Honda", "CR-V", 2022, 20000));
        vehicleDetailsList.add(new VehicleDetails("V9101", "Sedan", "Honda", "Accord", 2021, 18000));
        vehicleDetailsList.add(new VehicleDetails("V1121", "SUV", "Toyota", "RAV4", 2022, 25000));
        vehicleDetailsList.add(new VehicleDetails("V3141", "Truck", "Ford", "F-150", 2021, 30000));
        vehicleDetailsList.add(new VehicleDetails("V5161", "Sedan", "Chevrolet", "Malibu", 2022, 17000));
        vehicleDetailsList.add(new VehicleDetails("V7181", "SUV", "Chevrolet", "Tahoe", 2022, 35000));
        vehicleDetailsList.add(new VehicleDetails("V9202", "Sedan", "Nissan", "Altima", 2021, 16000));
        vehicleDetailsList.add(new VehicleDetails("V1122", "SUV", "Nissan", "Rogue", 2022, 22000));
        vehicleDetailsList.add(new VehicleDetails("V3142", "Truck", "Ram", "1500", 2021, 32000));
        vehicleDetailsList.add(new VehicleDetails("V5162", "Sedan", "Hyundai", "Elantra", 2022, 14000));
        vehicleDetailsList.add(new VehicleDetails("V7182", "SUV", "Hyundai", "Tucson", 2022, 21000));
        vehicleDetailsList.add(new VehicleDetails("V9203", "Sedan", "Kia", "Optima", 2021, 15500));
        vehicleDetailsList.add(new VehicleDetails("V1123", "SUV", "Kia", "Sorento", 2022, 23000));
        vehicleDetailsList.add(new VehicleDetails("V3143", "Truck", "GMC", "Sierra", 2021, 31000));
    }

    public Set<String> getVehicleTypes() {
        return vehicleDetailsList.stream()
                .map(VehicleDetails::getType)
                .collect(Collectors.toSet());
    }

    public Set<String> getVehicleBrands() {
        return vehicleDetailsList.stream()
                .map(VehicleDetails::getMake)
                .collect(Collectors.toSet());
    }

    public List<VehicleDetails> searchVehicles(Database.VehiclePreferences preferences) {
        List<VehicleDetails> filteredVehicles = new ArrayList<>();
        int budget = preferences.getBudget();
        String vehicleType = preferences.getVehicleType().toLowerCase();
        String preferredBrand = preferences.getPreferredBrands().toLowerCase();
        int budgetTolerance = 2000;

        for (VehicleDetails details : vehicleDetailsList) {
            int matchCount = 0;

            if (vehicleType.isEmpty() || details.getType().toLowerCase().equals(vehicleType)) {
                matchCount++;
            }
            if (preferredBrand.isEmpty() || details.getMake().toLowerCase().equals(preferredBrand)) {
                matchCount++;
            }
            if (budget == 0 || (details.getPrice() >= budget - budgetTolerance && details.getPrice() <= budget + budgetTolerance)) {
                matchCount++;
            }

            if (matchCount >= 2) {
                filteredVehicles.add(details);
            }
        }

        return filteredVehicles;
    }

    public VehicleDetails fetchVehicleDetails(String vehicleId) {
        for (VehicleDetails details : vehicleDetailsList) {
            if (details.getVehicleId().equals(vehicleId)) {
                return details;
            }
        }
        return null;
    }

    public LeasingTerms requestLeasingTerms(String vehicleId) {
        return new LeasingTerms();
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
    }

    public static class LeasingTerms {
        // Fields and methods for leasing terms
    }
}
