# Ledger — Core Banking Engine

A Java console banking system modeling the account logic behind a real bank's
ledger. Features checking and savings accounts with a $100 overdraft limit,
transfers with notes and counterparty tracking, timestamped transaction
history, and monthly interest calculation.

## How to run

```bash
javac *.java
java BankApp
```

## Features
- Open checking or savings accounts
- Deposit / withdraw money, each with an optional note
- Transfer between accounts — logs the counterparty's account number and name
  on both sides of the transfer, plus an optional note
- View full transaction history per account, with timestamps
- Apply monthly interest to savings accounts (3% annual rate)
- Checking accounts support a $100 overdraft limit
- Input validation on all money-moving operations

## Class structure
- `Account` — abstract base class (balance, deposit, transaction history).
  Exposes two protected hooks for subclasses: `getAvailableFunds()` (lets
  `CheckingAccount` add overdraft) and `applyBalanceChange()` (single method
  that adjusts balance and logs exactly one transaction).
- `CheckingAccount` — allows overdraft up to $100
- `SavingsAccount` — supports monthly interest (3% annual rate)
- `Transaction` — logs each account activity with timestamp, optional note,
  and optional counterparty
- `Bank` — manages all accounts, handles transfers (via dedicated
  `transferOut()`/`transferIn()` so each leg logs once), tracks total holdings
- `BankApp` — console menu and main entry point

## Debugging notes
Two bugs came up while building this:
1. **Overdraft logic duplication** — `CheckingAccount` originally re-implemented
   withdraw logic from scratch instead of overriding a shared hook.
2. **Double transaction logging on transfers** — `Bank.transfer()` called both
   the normal withdraw/deposit methods *and* a separate logging call, writing
   two entries per transfer instead of one.

Both were fixed by refactoring `Account` to expose `getAvailableFunds()` and
`applyBalanceChange()` as protected hooks, and adding dedicated
`transferOut()`/`transferIn()` methods for transfers specifically.

## Roadmap / next steps
- Persist data to a real database (PostgreSQL/MySQL) instead of in-memory storage
- Add a Spring Boot REST API layer
- Add JUnit tests covering transfers, overdraft limits, and interest calculations
- Add concurrency safety for simultaneous transactions
