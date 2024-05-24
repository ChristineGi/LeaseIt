import java.util.UUID;

public class PaymentGateway {

    public PaymentConfirmation computePayment(PaymentGateway.PaymentDetails details) {
        try {
            System.out.println("Processing payment...");
            Thread.sleep(2000); // Simulate processing time

            // Generate a unique transaction ID
            String transactionId = UUID.randomUUID().toString();

            PaymentConfirmation confirmation = new PaymentConfirmation(transactionId);
            Thread.sleep(2000); // Simulate processing time
            System.out.println("Payment successful! Transaction ID: " + confirmation.getTransactionId());

            return confirmation;
        } catch (InterruptedException e) {
            System.out.println("An error occurred during payment processing: " + e.getMessage());
            return null;
        }
    }

    public static class PaymentDetails {
        // Fields and methods for payment details
    }

    public static class PaymentConfirmation {
        private String transactionId;

        public PaymentConfirmation(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getTransactionId() {
            return transactionId;
        }
    }
}
