import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Email {

    private Map<String, List<String>> userEmails;

    public Email() {
        userEmails = new HashMap<>();
    }

    public void sendEmail(String username, String message) {
        userEmails.computeIfAbsent(username, k -> new ArrayList<>()).add(message);
        System.out.println("Email sent to " + username + " with message: " + message);
    }

    public List<String> getEmails(String username) {
        return userEmails.getOrDefault(username, new ArrayList<>());
    }

    public void deleteEmail(String username, int emailIndex) {
        List<String> emails = userEmails.get(username);
        if (emails != null && emailIndex >= 0 && emailIndex < emails.size()) {
            emails.remove(emailIndex);
            System.out.println("Email at index " + emailIndex + " deleted for user " + username);
        } else {
            System.out.println("Invalid email index.");
        }
    }
}
