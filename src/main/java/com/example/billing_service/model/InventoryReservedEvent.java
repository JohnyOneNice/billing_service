package com.example.billing_service.model;

import lombok.*;
import java.util.UUID;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReservedEvent {
    private UUID orderId;
    private UUID userId;
    private int price;
    private Long deliverySlotId;
    private LocalDate deliveryDate;
} 