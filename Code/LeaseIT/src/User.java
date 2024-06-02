import java.util.ArrayList;
import java.util.List;

public class User {

    private List<User> userDetailsList;
    private List<String> emails;

    private String username;
    private int creditScore;
    private int income;
    private String selectedVehicleId;

    public User(String username, int creditScore, int income) {
        this.username = username;
        this.creditScore = creditScore;
        this.income = income;
        this.emails = new ArrayList<>();
    }

    public User() {

        emails = new ArrayList<>();
        userDetailsList = new ArrayList<>();
        userDetailsList.add(new User("user", 700, 60000)); // username, credit score, income
        userDetailsList.add(new User("user2", 649, 50000));
        userDetailsList.add(new User("user3", 600, 40000));
        emails.add("You've got a new leasing offer!");
    }


    // for Login
    public User getUserDetails(String username) {
        for (User user : userDetailsList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
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
