package com.ledger.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime timestamp;
    private final String note;
    private final String counterparty;

    public Transaction(String type, double amount, double balanceAfter) {
        this(type, amount, balanceAfter, null, null);
    }

    public Transaction(String type, double amount, double balanceAfter,
                        String note, String counterparty) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.note = note;
        this.counterparty = counterparty;
    }

    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getNote() { return note; }
    public String getCounterparty() { return counterparty; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format(
                "%s | %-12s $%,10.2f | Balance after: $%,.2f",
                timestamp.format(FORMAT), type, amount, balanceAfter));
        if (counterparty != null && !counterparty.isBlank()) {
            sb.append(" | With: ").append(counterparty);
        }
        if (note != null && !note.isBlank()) {
            sb.append(" | Note: ").append(note);
        }
        return sb.toString();
    }
}
