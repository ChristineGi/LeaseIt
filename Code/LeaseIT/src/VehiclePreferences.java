public class VehiclePreferences {
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
}
