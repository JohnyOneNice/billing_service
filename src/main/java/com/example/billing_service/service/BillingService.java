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
    public boolean existsByUserId(UUID userId) {
        return walletRepository.existsByUserId(userId);
    }

    /*
     * Создаёт кошелек для пользователя, если его еще не существует,
     * и возвращает созданную сущность Wallet.
     */

    public Wallet createWalletAndReturn(UUID userId) {
        if (walletRepository.existsByUserId(userId)) {
            throw new RuntimeException("Кошелек уже существует для пользователя с ID: " + userId);
        }

        Wallet wallet = new Wallet();
        wallet.setUserId(userId);     //  Передается от userservice
        wallet.setBalance(0L);        // дефолтный баланс

        return walletRepository.save(wallet);
    }

    /*
     * Возвращает текущий баланс пользователя.
     */

    public Long getBalance(UUID userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Кошелёк не найден"))
                .getBalance();
    }

    /*
     * Пополняет кошелек указанной суммой.
     */

    public void topUp(UUID userId, Long amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Кошелёк не найден для пользователя: " + userId));

        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
    }

    /*
     * Снимает средства с кошелька. Если средств недостаточно — выбрасывает исключение.
     */

    public void withdraw(UUID userId, Long amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Кошелёк не найден"));

        if (wallet.getBalance() < amount) {
            throw new RuntimeException("Недостаточно средств на счету");
        }
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);
    }

}