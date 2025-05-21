package com.example.billing_service.service;

import com.example.billing_service.model.Wallet;
import com.example.billing_service.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final WalletRepository walletRepository;

    public void createWallet(UUID userId) {
        walletRepository.findById(userId).orElseGet(() -> {
            Wallet wallet = new Wallet(userId, 0L);
            return walletRepository.save(wallet);
        });
    }

    public void topUp(UUID userId, Long amount) {
        Wallet wallet = walletRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Кошелёк не найден для пользователя: " + userId));
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
    }

    public void withdraw(UUID userId, Long amount) {
        Wallet wallet = walletRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Кошелёк не найден"));
        if (wallet.getBalance() < amount) {
            throw new RuntimeException("Недостаточно средств на счету");
        }
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);
    }

    public Long getBalance(UUID userId) {
        return walletRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Кошелёк не найден")).getBalance();
    }
}