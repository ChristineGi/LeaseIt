public class Message {

    public void displayApprovalMessage(String message) throws InterruptedException {
        System.out.println("Approval Message: " + message);
        Thread.sleep(1000);
    }

    public void displayRejectionMessage(String message) throws InterruptedException {
        System.out.println("Rejection Message: " + message);
        Thread.sleep(1000);
    }

    public void displayMessage(String message) throws InterruptedException {
        System.out.println("Message: " + message);
        Thread.sleep(1000);

    }
}
