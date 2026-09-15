public class SavingsAccount extends Account {

    private static final double ANNUAL_INTEREST_RATE = 0.03; // 3%

    public SavingsAccount(String ownerName, double openingBalance) {
        super(ownerName, openingBalance);
    }

    public SavingsAccount(String ownerName, double openingBalance, String note) {
        super(ownerName, openingBalance, note);
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    public void applyMonthlyInterest() {
        double monthlyRate = ANNUAL_INTEREST_RATE / 12;
        double interest = getBalance() * monthlyRate;
        if (interest > 0) {
            applyBalanceChange(interest, "INTEREST", interest, null, null);
        }
    }
}
