import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Email {

    private Map<String, List<String>> userEmails;

    public Email() {
        userEmails = new HashMap<>();
    }

    public void sendEmail(String username, String message) throws InterruptedException {
        userEmails.computeIfAbsent(username, k -> new ArrayList<>()).add(message);
        System.out.println("Email successfully sent to " + username + "!") ;
        Thread.sleep(1000); // Simulate loading

    }

    public List<String> getEmails(String username) {
        return userEmails.getOrDefault(username, new ArrayList<>());
    }
}
