import java.util.Scanner;

public class BankApp {
    private static final Bank bank = new Bank("Ledger — Core Banking Engine");
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Welcome to " + bank.getBankName() + " ===");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> openAccount();
                case "2" -> deposit();
                case "3" -> withdraw();
                case "4" -> transfer();
                case "5" -> viewAccount();
                case "6" -> listAllAccounts();
                case "7" -> applySavingsInterest();
                case "0" -> {
                    running = false;
                    System.out.println("Thanks for banking with us. Goodbye!");
                }
                default -> System.out.println("Invalid option, try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("""

                ---------------------------------
                1. Open new account
                2. Deposit
                3. Withdraw
                4. Transfer between accounts
                5. View account details
                6. List all accounts
                7. Apply monthly interest (savings)
                0. Exit
                ---------------------------------""");
        System.out.print("Choose an option: ");
    }

    private static void openAccount() {
        try {
            System.out.print("Owner name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Account type (checking/savings): ");
            String type = scanner.nextLine().trim();

            System.out.print("Opening balance: ");
            double opening = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Note (optional): ");
            String note = scanner.nextLine().trim();

            Account acc = bank.openAccount(name, type, opening, note.isBlank() ? null : note);
            System.out.println("Account created successfully:");
            System.out.println(acc);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deposit() {
        try {
            int accNum = readAccountNumber();
            System.out.print("Amount to deposit: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Note (optional): ");
            String note = scanner.nextLine().trim();
            bank.getAccount(accNum).deposit(amount, note.isBlank() ? null : note);
            System.out.println("Deposit successful. New balance: $"
                    + String.format("%.2f", bank.getAccount(accNum).getBalance()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void withdraw() {
        try {
            int accNum = readAccountNumber();
            System.out.print("Amount to withdraw: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Note (optional): ");
            String note = scanner.nextLine().trim();
            bank.getAccount(accNum).withdraw(amount, note.isBlank() ? null : note);
            System.out.println("Withdrawal successful. New balance: $"
                    + String.format("%.2f", bank.getAccount(accNum).getBalance()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void transfer() {
        try {
            System.out.print("From account number: ");
            int from = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("To account number: ");
            int to = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Amount to transfer: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Note (optional): ");
            String note = scanner.nextLine().trim();
            bank.transfer(from, to, amount, note.isBlank() ? null : note);
            System.out.println("Transfer successful.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAccount() {
        try {
            int accNum = readAccountNumber();
            Account acc = bank.getAccount(accNum);
            System.out.println(acc);
            System.out.println("Transaction history:");
            if (acc.getHistory().isEmpty()) {
                System.out.println("  (no transactions yet)");
            } else {
                for (Transaction t : acc.getHistory()) {
                    System.out.println("  " + t);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listAllAccounts() {
        if (bank.getAllAccounts().isEmpty()) {
            System.out.println("No accounts yet.");
            return;
        }
        System.out.println("All accounts at " + bank.getBankName() + ":");
        for (Account acc : bank.getAllAccounts().values()) {
            System.out.println("  " + acc);
        }
        System.out.printf("Total bank holdings: $%.2f%n", bank.getTotalHoldings());
    }

    private static void applySavingsInterest() {
        int count = 0;
        for (Account acc : bank.getAllAccounts().values()) {
            if (acc instanceof SavingsAccount savings) {
                savings.applyMonthlyInterest();
                count++;
            }
        }
        System.out.println("Interest applied to " + count + " savings account(s).");
    }

    private static int readAccountNumber() {
        System.out.print("Account number: ");
        return Integer.parseInt(scanner.nextLine().trim());
    }
}
