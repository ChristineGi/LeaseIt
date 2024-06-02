public class QR {

    private Email emailService;
    private User userDetails;

    public QR(Email emailService, User userDetails) {
        this.emailService = emailService;
        this.userDetails = userDetails;
    }

    public String generateQRCode() {
        return "QRCode123456";
    }

    public boolean verifyQRCode(String qrCode) {
        System.out.println("\nQR Code verified: " + qrCode);
        return true;
    }

}
