import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LeaseContract {

    private List<Leasing> leases;

    public LeaseContract() {
        leases = new ArrayList<>();
    }

    public LeasingTerms calculateLeasingTerms(Vehicle.VehicleDetails vehicleDetails) {
        LeasingTerms terms = new LeasingTerms();
        terms.setMonthlyPayment(vehicleDetails.getPrice() / 36.0);
        terms.setLeaseTerm(36);
        terms.setMileageLimit(12000);
        return terms;
    }

    public void finalizeLeaseContract() {
        System.out.println("Lease contract finalized.");
    }

    public LeasingTerms requestLeasingTerms(LeasingTerms terms) {
        return terms;
    }

    public void addLease(String vehicleID, String userID) {
        String leaseID = UUID.randomUUID().toString();
        Leasing lease = new Leasing(leaseID, vehicleID, userID, "Pending", false);
        leases.add(lease);
        System.out.println("Lease added: " + leaseID);
    }

    public List<Leasing> getLeases() {
        return leases;
    }

    public List<Leasing> getUserLeases(String userID) {
        List<Leasing> userLeases = new ArrayList<>();
        for (Leasing lease : leases) {
            if (lease.getUserID().equals(userID)) {
                userLeases.add(lease);
            }
        }
        return userLeases;
    }

    public List<Leasing> getUserLeasesByStatus(String userID, String status) {
        List<Leasing> userLeases = new ArrayList<>();
        for (Leasing lease : leases) {
            if (lease.getUserID().equals(userID) && lease.getStatus().equals(status)) {
                userLeases.add(lease);
            }
        }
        return userLeases;
    }

    public boolean checkActiveLease(String userID) {
        for (Leasing lease : leases) {
            if (lease.getUserID().equals(userID) && lease.getStatus().equals("Active")) {
                return true;
            }
        }
        return false;
    }

    public Vehicle.VehicleDetails getVehicleInfo(String leaseID) {
        for (Leasing lease : leases) {
            if (lease.getLeaseID().equals(leaseID)) {
                return new Vehicle().fetchVehicleDetails(lease.getVehicleID());
            }
        }
        return null;
    }

    public void setLocationConfigurationStatus(String leaseID, boolean status) {
        for (Leasing lease : leases) {
            if (lease.getLeaseID().equals(leaseID)) {
                lease.setLocationConfigured(status);
                break;
            }
        }
    }

    public boolean isLocationConfigured(String leaseID) {
        for (Leasing lease : leases) {
            if (lease.getLeaseID().equals(leaseID)) {
                return lease.isLocationConfigured();
            }
        }
        return false;
    }

    public static class LeasingTerms {
        private double monthlyPayment;
        private int leaseTerm;
        private int mileageLimit;

        public LeasingTerms() {
            this.monthlyPayment = 300.0;
            this.leaseTerm = 36; // 36 months
            this.mileageLimit = 12000; // 12000 miles per year
        }

        public double getMonthlyPayment() {
            return monthlyPayment;
        }

        public void setMonthlyPayment(double monthlyPayment) {
            this.monthlyPayment = monthlyPayment;
        }

        public int getLeaseTerm() {
            return leaseTerm;
        }

        public void setLeaseTerm(int leaseTerm) {
            this.leaseTerm = leaseTerm;
        }

        public int getMileageLimit() {
            return mileageLimit;
        }

        public void setMileageLimit(int mileageLimit) {
            this.mileageLimit = mileageLimit;
        }
    }

    public static class Leasing {
        private String leaseID;
        private String vehicleID;
        private String userID;
        private String status;
        private boolean locationConfigured;

        public Leasing(String leaseID, String vehicleID, String userID, String status, boolean locationConfigured) {
            this.leaseID = leaseID;
            this.vehicleID = vehicleID;
            this.userID = userID;
            this.status = status;
            this.locationConfigured = locationConfigured;
        }

        public String getLeaseID() {
            return leaseID;
        }

        public String getVehicleID() {
            return vehicleID;
        }

        public String getUserID() {
            return userID;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isLocationConfigured() {
            return locationConfigured;
        }

        public void setLocationConfigured(boolean locationConfigured) {
            this.locationConfigured = locationConfigured;
        }
    }
}
