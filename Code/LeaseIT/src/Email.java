import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Email {

    private Map<String, List<String>> userEmails;

    public Email() {
        userEmails = new HashMap<>();
    }

    public void sendEmail(String username, String message) {
        userEmails.computeIfAbsent(username, k -> new ArrayList<>()).add(message);
        System.out.println("\nEmail sent to " + username + " with message: \n" + message);
    }

    public List<String> getEmails(String username) {
        return userEmails.getOrDefault(username, new ArrayList<>());
    }


    public void viewEmails(String username) {
        Scanner scanner = new Scanner(System.in);
        List<String> emails = getEmails(username);
        if (emails.isEmpty()) {
            System.out.println("\nYou've got no emails !");
        } else {
            System.out.println("\n**** Emails ****\n");
            for (int i = 0; i < emails.size(); i++) {
                System.out.println((i + 1) + ". " + emails.get(i));
            }
            System.out.print("\nEnter the email number to interact , or '0' to go back: ");
            String emailChoice = scanner.next();
            if (emailChoice.equals("0")) {
                return;
            } else {
                int emailIndex = Integer.parseInt(emailChoice) - 1;
                System.out.println("\nEmail: " + emails.get(emailIndex));
            }
        }
    }
}
