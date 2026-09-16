package com.ledger.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private static int nextAccountNumber = 1001;

    private final int accountNumber;
    private final String ownerName;
    private double balance;
    private final List<Transaction> history;

    public Account(String ownerName, double openingBalance) {
        this(ownerName, openingBalance, null);
    }

    public Account(String ownerName, double openingBalance, String note) {
        this.accountNumber = nextAccountNumber++;
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.history = new ArrayList<>();
        if (openingBalance > 0) {
            history.add(new Transaction("OPEN", openingBalance, balance, note, null));
        }
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public abstract String getAccountType();

    public void deposit(double amount) {
        deposit(amount, null);
    }

    public void deposit(double amount, String note) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        applyBalanceChange(amount, "DEPOSIT", amount, note, null);
    }

    public void withdraw(double amount) {
        withdraw(amount, null);
    }

    public void withdraw(double amount, String note) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > getAvailableFunds()) {
            throw new IllegalStateException("Insufficient funds.");
        }
        applyBalanceChange(-amount, "WITHDRAW", amount, note, null);
    }

    public void transferOut(double amount, String note, String counterparty) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }
        if (amount > getAvailableFunds()) {
            throw new IllegalStateException("Insufficient funds.");
        }
        applyBalanceChange(-amount, "TRANSFER OUT", amount, note, counterparty);
    }

    public void transferIn(double amount, String note, String counterparty) {
        applyBalanceChange(amount, "TRANSFER IN", amount, note, counterparty);
    }

    protected double getAvailableFunds() {
        return balance;
    }

    protected void applyBalanceChange(double delta, String label, double displayAmount,
                                       String note, String counterparty) {
        balance += delta;
        history.add(new Transaction(label, displayAmount, balance, note, counterparty));
    }

    @Override
    public String toString() {
        return String.format("[%d] %-10s %-15s Balance: $%.2f",
                accountNumber, getAccountType(), ownerName, balance);
    }
}
