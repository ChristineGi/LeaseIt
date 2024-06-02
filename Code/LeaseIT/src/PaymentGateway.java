import java.util.UUID;

public class PaymentGateway {

    public void completePayment() {
        try {
            System.out.println("\nProcessing payment...");
            Thread.sleep(1000);

            String transactionId = UUID.randomUUID().toString();
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            System.out.println("An error occurred during payment processing: " + e.getMessage());
        }
    }

}
