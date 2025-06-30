package com.example.billing_service.controller;

import com.example.billing_service.model.Wallet;
import com.example.billing_service.model.WithdrawRequest;
import com.example.billing_service.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Min;
import java.util.UUID;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<UUID> createWallet(@PathVariable UUID userId) {
        if (billingService.existsByUserId(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Wallet wallet = billingService.createWalletAndReturn(userId);
        return ResponseEntity.ok(wallet.getId()); // Возвращаем id записи
    }

    @PostMapping("/topup/{userId}")
    public ResponseEntity<Void> topUp(@PathVariable UUID userId, @RequestParam("amount") @Min(1) Long amount) {
        billingService.topUp(userId, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawRequest request) {
        billingService.withdraw(request.getUserId(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Long> getBalance(@PathVariable UUID userId) {
        return ResponseEntity.ok(billingService.getBalance(userId));
    }

    @PostMapping("/refund/{userId}")
    public ResponseEntity<Void> refund(@PathVariable UUID userId, @RequestParam Long amount) {
        try {
            billingService.refund(userId, amount);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
