public class TaxGateway {

    public boolean checkCreditworthiness(User.UserDetails userDetails, LeaseContract terms) {

        int creditScore = userDetails.getCreditScore();

        int income = userDetails.getIncome();

        double monthlyPayment = terms.getMonthlyPayment();

        return creditScore >= 650 && monthlyPayment < ((double) income / 12) * 0.2;
    }
}
