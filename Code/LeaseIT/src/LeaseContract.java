import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LeaseContract {

    private List<LeasingSubscriptions> leases;
    private double monthlyPayment;
    private int leaseTerm;
    private int mileageLimit;

    public LeaseContract() {
        leases = new ArrayList<>();
        this.monthlyPayment = 300.0;
        this.leaseTerm = 36; // 36 months
        this.mileageLimit = 12000; // 12000 miles per year
    }

    public void calculateLeasingTerms(Vehicle vehicleDetails) {
        this.monthlyPayment = vehicleDetails.getPrice() / 36.0;
        this.leaseTerm = 36;
        this.mileageLimit = 12000;
    }

    public void requestLeasingTerms() {
        System.out.println("\n----------  Leasing Terms ----------");
        System.out.printf("Monthly Payment: %.2f $", this.monthlyPayment);
        System.out.println("\nLease Term: " + this.leaseTerm + " months");
        System.out.println("Mileage Limit: " + this.mileageLimit + " miles");
        System.out.println("------------------------------------");
    }

    public void addLease(String vehicleID, String userID) {
        String leaseID = UUID.randomUUID().toString();
        LeasingSubscriptions lease = new LeasingSubscriptions(leaseID, vehicleID, userID, "Pending", false);
        leases.add(lease);
    }


    public List<LeasingSubscriptions> getUserLeasesByStatus(String userID, String status) {
        List<LeasingSubscriptions> userLeases = new ArrayList<>();
        for (LeasingSubscriptions lease : leases) {
            if (lease.getUserID().equals(userID) && lease.getStatus().equals(status)) {
                userLeases.add(lease);
            }
        }
        return userLeases;
    }

    public boolean checkActiveLease(String userID) {
        for (LeasingSubscriptions lease : leases) {
            if (lease.getUserID().equals(userID) && lease.getStatus().equals("Active")) {
                return true;
            }
        }
        return false;
    }


    public void getVehicleInfo(String leaseID) {
        for (LeasingSubscriptions lease : leases) {
            if (lease.getLeaseID().equals(leaseID)) {
                new Vehicle().fetchVehicleDetails(lease.getVehicleID());
                return;
            }
        }
    }


    public void setLocationConfigurationStatus(String leaseID, boolean status) {
        for (LeasingSubscriptions lease : leases) {
            if (lease.getLeaseID().equals(leaseID)) {
                lease.setLocationConfigured(status);
                break;
            }
        }
    }

    public boolean isLocationConfigured(String leaseID) {
        for (LeasingSubscriptions lease : leases) {
            if (lease.getLeaseID().equals(leaseID)) {
                return lease.isLocationConfigured();
            }
        }
        return false;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }


    public static class LeasingSubscriptions {
        private String leaseID;
        private String vehicleID;
        private String userID;
        private String status;
        private boolean locationConfigured;

        public LeasingSubscriptions(String leaseID, String vehicleID, String userID, String status, boolean locationConfigured) {
            this.leaseID = leaseID;
            this.vehicleID = vehicleID;
            this.userID = userID;
            this.status = status;
            this.locationConfigured = locationConfigured;
        }

        public String getLeaseID() {return leaseID;}
        public String getVehicleID() {return vehicleID;}
        public String getUserID() {return userID;}
        public String getStatus() {return status;}
        public void setStatus(String status) {this.status = status;}
        public boolean isLocationConfigured() {return locationConfigured;}
        public void setLocationConfigured(boolean locationConfigured) {this.locationConfigured = locationConfigured;}
    }
}
