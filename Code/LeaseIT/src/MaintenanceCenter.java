import java.util.Arrays;
import java.util.List;

public class MaintenanceCenter {
    public List<String> getAuthorizedCenters() {
        return Arrays.asList("Center 1", "Center 2", "Center 3");
    }

    public List<String> getMaintenanceSchedule(String center) {
        return Arrays.asList("10:00 AM", "11:00 AM", "1:00 PM", "2:00 PM");
    }

    public void confirmAppointment(String center, String appointment) {
        System.out.println("Appointment confirmed at " + center + " for " + appointment);
    }
}
