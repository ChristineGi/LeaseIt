import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaintenanceCenter {
    public List<String> getAuthorizedCenters() {
        return Arrays.asList("Center 1", "Center 2", "Center 3");
    }

    public List<String> getMaintenanceSchedule(String center) {
        return Arrays.asList("10:00 AM", "12:00 PM", "02:00 PM");
    }

    public void confirmAppointment(String center, String appointment) {
        System.out.println("Appointment confirmed at " + center + " for " + appointment);
    }

    public void prioritizeUrgentMaintenance() {
        System.out.println("\nUrgent maintenance has been prioritized.");
    }
}
