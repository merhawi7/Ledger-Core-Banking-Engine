package com.ledger.controller;

import com.ledger.dto.AmountRequest;
import com.ledger.dto.OpenAccountRequest;
import com.ledger.dto.TransferRequest;
import com.ledger.model.Account;
import com.ledger.service.Bank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BankController {

    private final Bank bank;

    public BankController(Bank bank) {
        this.bank = bank;
    }

    @GetMapping("/accounts")
    public List<Map<String, Object>> listAccounts() {
        return bank.getAllAccounts().values().stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<?> viewAccount(@PathVariable int accountNumber) {
        try {
            Account acc = bank.getAccount(accountNumber);
            Map<String, Object> result = toSummary(acc);
            result.put("history", acc.getHistory().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList()));
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/accounts")
    public ResponseEntity<?> openAccount(@RequestBody OpenAccountRequest req) {
        try {
            Account acc = bank.openAccount(req.ownerName, req.type, req.openingBalance, req.note);
            return ResponseEntity.ok(toSummary(acc));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/accounts/{accountNumber}/deposit")
    public ResponseEntity<?> deposit(@PathVariable int accountNumber, @RequestBody AmountRequest req) {
        try {
            Account acc = bank.getAccount(accountNumber);
            acc.deposit(req.amount, req.note);
            return ResponseEntity.ok(toSummary(acc));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/accounts/{accountNumber}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable int accountNumber, @RequestBody AmountRequest req) {
        try {
            Account acc = bank.getAccount(accountNumber);
            acc.withdraw(req.amount, req.note);
            return ResponseEntity.ok(toSummary(acc));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest req) {
        try {
            bank.transfer(req.fromAccountNumber, req.toAccountNumber, req.amount, req.note);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/interest/apply")
    public ResponseEntity<?> applyInterest() {
        int count = bank.applyInterestToAllSavings();
        return ResponseEntity.ok(Map.of("accountsUpdated", count));
    }

    @GetMapping("/total-holdings")
    public Map<String, Object> totalHoldings() {
        return Map.of("bankName", bank.getBankName(), "totalHoldings", bank.getTotalHoldings());
    }

    private Map<String, Object> toSummary(Account acc) {
        return new java.util.LinkedHashMap<>(Map.of(
                "accountNumber", acc.getAccountNumber(),
                "ownerName", acc.getOwnerName(),
                "accountType", acc.getAccountType(),
                "balance", acc.getBalance()
        ));
    }
}
