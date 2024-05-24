public class TaxGateway {

    public boolean checkCreditworthiness(Database.UserDetails userDetails, LeaseContract.LeasingTerms terms) {

        int creditScore = userDetails.getCreditScore();

        int income = userDetails.getIncome();

        double monthlyPayment = terms.getMonthlyPayment();

        return creditScore >= 650 && monthlyPayment < ((double) income / 12) * 0.2;
    }
}
