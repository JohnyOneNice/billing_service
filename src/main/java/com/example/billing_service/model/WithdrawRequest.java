package com.example.billing_service.model;

import lombok.Data;
import java.util.UUID;

@Data
public class WithdrawRequest {
    private UUID userId;
    private Long amount;
} 