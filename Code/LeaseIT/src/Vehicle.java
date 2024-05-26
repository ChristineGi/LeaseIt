import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Vehicle {

    private List<VehicleDetails> vehicleDetailsList;

    public Vehicle() {
        vehicleDetailsList = new ArrayList<>();
        vehicleDetailsList.add(new VehicleDetails("1", "Sedan", "Toyota", "Camry", 2021, 15000,false,"Dealership 1", "Available"));
        vehicleDetailsList.add(new VehicleDetails("2", "SUV", "Honda", "CR-V", 2022, 20000,false,"Dealership 1", "Available"));
        vehicleDetailsList.add(new VehicleDetails("3", "Sedan", "Honda", "Accord", 2021, 18000,false,"Dealership 1", "Available"));
        vehicleDetailsList.add(new VehicleDetails("4", "SUV", "Toyota", "RAV4", 2022, 25000,false,"Dealership 1", "Available"));
        vehicleDetailsList.add(new VehicleDetails("5", "Truck", "Ford", "F-150", 2021, 30000,false,"Dealership 1", "UponRequest"));
        vehicleDetailsList.add(new VehicleDetails("6", "Sedan", "Chevrolet", "Malibu", 2022, 17000,false,"Dealership 1", "Available"));
        vehicleDetailsList.add(new VehicleDetails("7", "SUV", "Chevrolet", "Tahoe", 2022, 35000,false,"Dealership 2", "UponRequest"));
        vehicleDetailsList.add(new VehicleDetails("8", "Sedan", "Nissan", "Altima", 2021, 16000,false,"Dealership 2", "Available"));
        vehicleDetailsList.add(new VehicleDetails("9", "SUV", "Nissan", "Rogue", 2022, 22000,false,"Dealership 2", "Available"));
        vehicleDetailsList.add(new VehicleDetails("10", "Truck", "Ram", "1500", 2021, 32000,false,"Dealership 2", "Available"));
        vehicleDetailsList.add(new VehicleDetails("11", "Sedan", "Hyundai", "Elantra", 2022, 14000,false,"Dealership 2", "Available"));
        vehicleDetailsList.add(new VehicleDetails("12", "SUV", "Hyundai", "Tucson", 2022, 21000,false,"Dealership 2", "Available"));
        vehicleDetailsList.add(new VehicleDetails("13", "Sedan", "Kia", "Optima", 2021, 15500,false,"Dealership 2", "UponRequest"));
        vehicleDetailsList.add(new VehicleDetails("14", "SUV", "Kia", "Sorento", 2022, 23000,false,"Dealership 2", "Available"));
        vehicleDetailsList.add(new VehicleDetails("15", "Truck", "GMC", "Sierra", 2021, 31000,false,"Dealership 2", "Available"));
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

    public void requestVehicleData() {
        System.out.println("\nRequesting vehicle data...");
    }

    public void getVehicleStatusData() throws InterruptedException {
        System.out.println("\nGetting vehicle status data from cloud...");
        Thread.sleep(1000);
        // Simulate data retrieval
    }

    public void getMaintenanceHistory() throws InterruptedException {
        System.out.println("\nGetting maintenance history from cloud...");
        Thread.sleep(1000);
        // Simulate data retrieval
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

        public List<VehicleDetails> getLeasedVehicles() {
            List<VehicleDetails> leasedVehicles = new ArrayList<>();
            for (VehicleDetails details : vehicleDetailsList) {
                if (details.isLeased()) {
                    leasedVehicles.add(details);
                }
            }
            return leasedVehicles;
        }

        public void leaseVehicle(String vehicleId) {
            for (VehicleDetails details : vehicleDetailsList) {
                if (details.getVehicleId().equals(vehicleId)) {
                    details.setLeased(true);
                    break;
                }
            }
        }

        public static class VehicleDetails {
            private String vehicleId;
            private String type;
            private String make;
            private String model;
            private int year;
            private int price;
            private boolean leased;
            private String dealership;
            private String status; // "Available" or "Upon Request"

            public VehicleDetails(String vehicleId, String type, String make, String model, int year, int price, boolean leased, String dealership, String status) {
                this.vehicleId = vehicleId;
                this.type = type;
                this.make = make;
                this.model = model;
                this.year = year;
                this.price = price;
                this.leased = leased;
                this.dealership = dealership;
                this.status = status;
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

            public boolean isLeased() {
                return leased;
            }

            public void setLeased(boolean leased) {
                this.leased = leased;
            }

            public String getDealership() {
                return dealership;
            }

            public void setDealership(String dealership) {
                this.dealership = dealership;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
