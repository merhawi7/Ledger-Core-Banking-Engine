import java.util.HashMap;
import java.util.Map;

public class Bank {
    private final Map<Integer, Account> accounts = new HashMap<>();
    private final String bankName;

    public Bank(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public Account openAccount(String ownerName, String type, double openingBalance) {
        return openAccount(ownerName, type, openingBalance, null);
    }

    public Account openAccount(String ownerName, String type, double openingBalance, String note) {
        Account account;
        if (type.equalsIgnoreCase("savings")) {
            account = new SavingsAccount(ownerName, openingBalance, note);
        } else {
            account = new CheckingAccount(ownerName, openingBalance, note);
        }
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    public Account getAccount(int accountNumber) {
        Account acc = accounts.get(accountNumber);
        if (acc == null) {
            throw new IllegalArgumentException("No account found with number " + accountNumber);
        }
        return acc;
    }

    public void transfer(int fromAccountNumber, int toAccountNumber, double amount) {
        transfer(fromAccountNumber, toAccountNumber, amount, null);
    }

    public void transfer(int fromAccountNumber, int toAccountNumber, double amount, String note) {
        Account from = getAccount(fromAccountNumber);
        Account to = getAccount(toAccountNumber);
        String toLabel = "#" + toAccountNumber + " - " + to.getOwnerName();
        String fromLabel = "#" + fromAccountNumber + " - " + from.getOwnerName();
        from.transferOut(amount, note, toLabel);
        to.transferIn(amount, note, fromLabel);
    }

    public double getTotalHoldings() {
        double total = 0;
        for (Account acc : accounts.values()) {
            total += acc.getBalance();
        }
        return total;
    }

    public Map<Integer, Account> getAllAccounts() {
        return accounts;
    }
}
