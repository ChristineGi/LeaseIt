
public class SessionSettings {

    private String selectedVehicleId;
    private VehiclePreferences preferences;
    private SessionSettings sessionSettings;

    public String getSelectedVehicleId() {
        return selectedVehicleId;
    }

    public void setSelectedVehicleId(String selectedVehicleId) {
        this.selectedVehicleId = selectedVehicleId;
    }

    public VehiclePreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(VehiclePreferences preferences) {
        this.preferences = preferences;
    }

    public void saveSessionSettings(SessionSettings settings) {
        this.sessionSettings = settings;
    }

    public SessionSettings retrieveSessionSettings() {
        return this.sessionSettings;
    }
}