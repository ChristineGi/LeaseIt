public class Message {

    public void displayApprovalMessage(String message) throws InterruptedException {
        System.out.println("\nApproval Message: " + message);
        Thread.sleep(1000);
    }

    public void displayRejectionMessage(String message) throws InterruptedException {
        System.out.println("\nRejection Message: " + message);
        Thread.sleep(1000);
    }

    public void displayMessage(String message) throws InterruptedException {
        System.out.println("\nMessage: " + message);
        Thread.sleep(1000);

    }
}
