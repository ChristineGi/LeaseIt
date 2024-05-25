public class QR {
    private Email emailService;
    private Database.UserDetails userDetails;

    public QR(Email emailService, Database.UserDetails userDetails) {
        this.emailService = emailService;
        this.userDetails = userDetails;
    }

    public String generateQRCode() {
        // Logic to generate a QR code
        return "QRCode123456";
    }

    public boolean verifyQRCode(String qrCode) {
        // Logic to verify the QR code
        System.out.println("\nQR Code verified: " + qrCode);
        return true;
    }

    public void resendProofEmail() {
        // Logic to resend proof email
        String message = "Proof email resent.";
        emailService.sendEmail(userDetails.getUsername(), message);
        System.out.println(message);
    }
}
