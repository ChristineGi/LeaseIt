public class TaxGateway {

    public boolean checkCreditworthiness(Database.UserDetails userDetails, LeaseContract.LeasingTerms terms) {
        // Check creditworthiness based on user's credit score and income
        int creditScore = userDetails.getCreditScore();
        int income = userDetails.getIncome();
        double monthlyPayment = terms.getMonthlyPayment();

        // Simple creditworthiness check logic
        // Approve if credit score is above 650 and monthly payment is less than 20% of monthly income
        if (creditScore >= 650 && monthlyPayment < (income / 12) * 0.2) {
            return true;
        }
        return false;
    }
}
