package com.ledger.model;

public class CheckingAccount extends Account {

    private static final double OVERDRAFT_LIMIT = 100.0;

    public CheckingAccount(String ownerName, double openingBalance) {
        super(ownerName, openingBalance);
    }

    public CheckingAccount(String ownerName, double openingBalance, String note) {
        super(ownerName, openingBalance, note);
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }

    @Override
    protected double getAvailableFunds() {
        return getBalance() + OVERDRAFT_LIMIT;
    }
}
