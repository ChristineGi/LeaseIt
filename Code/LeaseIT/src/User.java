import java.util.ArrayList;
import java.util.List;

public class User {

    private List<UserDetails> userDetailsList;
    private List<String> emails;

    public User() {

        emails = new ArrayList<>();
        userDetailsList = new ArrayList<>();
        userDetailsList.add(new UserDetails("user", 700, 60000)); // username, credit score, income
        userDetailsList.add(new UserDetails("user2", 649, 50000));
        userDetailsList.add(new UserDetails("user3", 600, 40000));
        emails.add("You've got a new leasing offer!");
    }

    public UserDetails getUserDetails(String username) {
        for (UserDetails user : userDetailsList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
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
