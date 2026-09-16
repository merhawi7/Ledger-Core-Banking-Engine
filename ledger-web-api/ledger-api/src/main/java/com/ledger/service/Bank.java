package com.ledger.service;

import com.ledger.model.Account;
import com.ledger.model.CheckingAccount;
import com.ledger.model.SavingsAccount;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holds all bank state in memory for the life of the server process.
 * Spring keeps exactly one instance of this (singleton scope by default),
 * so every HTTP request sees the same accounts.
 *
 * Note: this resets whenever the server restarts (e.g. Render's free tier
 * spins down on inactivity). For real persistence, swap this out for a
 * database-backed repository.
 */
@Service
public class Bank {

    private final Map<Integer, Account> accounts = new LinkedHashMap<>();
    private final String bankName = "Ledger — Core Banking Engine";

    public Bank() {
        seedDemoData();
    }

    public String getBankName() {
        return bankName;
    }

    public Account openAccount(String ownerName, String type, double openingBalance, String note) {
        Account account;
        if ("savings".equalsIgnoreCase(type)) {
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

    public int applyInterestToAllSavings() {
        int count = 0;
        for (Account acc : accounts.values()) {
            if (acc instanceof SavingsAccount savings) {
                savings.applyMonthlyInterest();
                count++;
            }
        }
        return count;
    }

    private void seedDemoData() {
        Account mera = openAccount("Mera", "checking", 1_000_000, "Salary");
        mera.deposit(200, "Paycheck");
        Account rah = openAccount("Rah", "savings", 1000, "Savings start");
        transfer(mera.getAccountNumber(), rah.getAccountNumber(), 100, "Split dinner");
        if (rah instanceof SavingsAccount savings) {
            savings.applyMonthlyInterest();
        }
    }
}
