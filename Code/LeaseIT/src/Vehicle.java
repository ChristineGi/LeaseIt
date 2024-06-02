import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Vehicle {

    private List<Vehicle> vehicleDetailsList;

    private String vehicleId;
    private String type;
    private String make;
    private String model;
    private int year;
    private int price;
    private boolean leased;
    private String dealership;
    private String status; // "Available" or "Upon Request"


    public Vehicle() {
            vehicleDetailsList = new ArrayList<>();
            vehicleDetailsList.add(new Vehicle("1", "Sedan", "Toyota", "Camry", 2021, 15000, false, "Dealership 1", "Available"));
            vehicleDetailsList.add(new Vehicle("2", "SUV", "Honda", "CR-V", 2022, 20000, false, "Dealership 1", "Available"));
            vehicleDetailsList.add(new Vehicle("3", "Sedan", "Honda", "Accord", 2021, 18000, false, "Dealership 1", "Available"));
            vehicleDetailsList.add(new Vehicle("4", "SUV", "Toyota", "RAV4", 2022, 25000, false, "Dealership 1", "Available"));
            vehicleDetailsList.add(new Vehicle("5", "Truck", "Ford", "F-150", 2021, 30000, false, "Dealership 1", "UponRequest"));
            vehicleDetailsList.add(new Vehicle("6", "Sedan", "Chevrolet", "Malibu", 2022, 17000, false, "Dealership 1", "Available"));
            vehicleDetailsList.add(new Vehicle("7", "SUV", "Chevrolet", "Tahoe", 2022, 35000, false, "Dealership 2", "UponRequest"));
            vehicleDetailsList.add(new Vehicle("8", "Sedan", "Nissan", "Altima", 2021, 16000, false, "Dealership 2", "Available"));
            vehicleDetailsList.add(new Vehicle("9", "SUV", "Nissan", "Rogue", 2022, 22000, false, "Dealership 2", "Available"));
            vehicleDetailsList.add(new Vehicle("10", "Truck", "Ram", "1500", 2021, 32000, false, "Dealership 2", "Available"));
            vehicleDetailsList.add(new Vehicle("11", "Sedan", "Hyundai", "Elantra", 2022, 14000, false, "Dealership 2", "Available"));
            vehicleDetailsList.add(new Vehicle("12", "SUV", "Hyundai", "Tucson", 2022, 21000, false, "Dealership 2", "Available"));
            vehicleDetailsList.add(new Vehicle("13", "Sedan", "Kia", "Optima", 2021, 15500, false, "Dealership 2", "UponRequest"));
            vehicleDetailsList.add(new Vehicle("14", "SUV", "Kia", "Sorento", 2022, 23000, false, "Dealership 2", "Available"));
            vehicleDetailsList.add(new Vehicle("15", "Truck", "GMC", "Sierra", 2021, 31000, false, "Dealership 2", "Available"));
        }

    public Vehicle(String vehicleId, String type, String make, String model, int year, int price, boolean leased, String dealership, String status) {
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

    public String getVehicleId() { return vehicleId; }
    public String getType() { return type; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public int getPrice() { return price; }
    public boolean isLeased() { return leased; }
    public void setLeased(boolean leased) {this.leased = leased;}
    public String getDealership() {return dealership;}
    public void setDealership(String dealership) {this.dealership = dealership;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}


    public List<Set<String>> getVehiclePreferences() {

        List<Set<String>> preferencesList = new ArrayList<>();

        Set<String> types = vehicleDetailsList.stream()
                .map(Vehicle::getType)
                .collect(Collectors.toSet());

        Set<String> brands = vehicleDetailsList.stream()
                .map(Vehicle::getMake)
                .collect(Collectors.toSet());

        preferencesList.add(types);
        preferencesList.add(brands);
        return preferencesList;
    }


    public List<Vehicle> searchVehicles(VehiclePreferences preferences) {

        List<Vehicle> filteredVehicles = new ArrayList<>();
        int budget = preferences.getBudget();
        String vehicleType = preferences.getVehicleType().toLowerCase();
        String preferredBrand = preferences.getPreferredBrands().toLowerCase();
        int budgetTolerance = 2000;

        for (Vehicle details : vehicleDetailsList) {
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


    public Vehicle fetchVehicleDetails(String vehicleId) {
        for (Vehicle details : vehicleDetailsList) {
            if (details.getVehicleId().equals(vehicleId)) {
                return details;
            }
        }
        return null;
    }



    public void requestVehicleData() {
        System.out.println("\nRequesting vehicle data...");
    }

    public void getVehicleStatusData() throws InterruptedException {
        System.out.println("\nGetting vehicle status data from cloud...");
        Thread.sleep(1000);
    }

    public void getMaintenanceHistory() throws InterruptedException {
        System.out.println("\nGetting maintenance history from cloud...");
        Thread.sleep(1000);
    }



    public List<Vehicle> getLeasedVehicles() {
        List<Vehicle> leasedVehicles = new ArrayList<>();
        for (Vehicle details : vehicleDetailsList) {
            if (details.isLeased()) {
                leasedVehicles.add(details);
            }
        }
        return leasedVehicles;
    }

    public void leaseVehicle(String vehicleId) {
        for (Vehicle details : vehicleDetailsList) {
            if (details.getVehicleId().equals(vehicleId)) {
                details.setLeased(true);
                break;
            }
        }
    }
}
