package com.example.billing_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table (name = "Wallet")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Wallet {
    @Id
    private UUID userId; // ID пользователя из userservice
    private Long balance; // В копейках

    public Wallet(UUID userId, long l) {
    }
}