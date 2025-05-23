package com.example.billing_service.controller;

import com.example.billing_service.model.Wallet;
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
        if (billingService.walletRepository.existsByUserId(userId)) {
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

    @PostMapping("/withdraw/{userId}")
    public ResponseEntity<Void> withdraw(@PathVariable UUID userId, @RequestParam("amount") @Min(1) Long amount) {
        billingService.withdraw(userId, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Long> getBalance(@PathVariable UUID userId) {
        return ResponseEntity.ok(billingService.getBalance(userId));
    }
}
