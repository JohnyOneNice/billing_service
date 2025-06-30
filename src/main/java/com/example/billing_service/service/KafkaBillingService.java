package com.example.billing_service.service;

import com.example.billing_service.model.BillingWithdrawedEvent;
import com.example.billing_service.model.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class KafkaBillingService {
    private final BillingService billingService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(KafkaBillingService.class);

    @Value("${billing.events.topic}")
    private String billingEventsTopic;

    @KafkaListener(topics = "inventory-reserved", groupId = "billing-service")
    public void handleInventoryReserved(InventoryReservedEvent event) {
        try {
            log.info("Получено событие inventory-reserved: {}", event);
            billingService.withdraw(event.getUserId(), (long) event.getPrice());
            BillingWithdrawedEvent outEvent = new BillingWithdrawedEvent(
                    event.getOrderId(),
                    event.getUserId(),
                    event.getDeliverySlotId(),
                    event.getDeliveryDate()
            );
            publishBillingWithdrawed(outEvent);
        } catch (Exception e) {
            log.error("Ошибка обработки события inventory-reserved", e);
        }
    }

    public void publishBillingWithdrawed(BillingWithdrawedEvent event) {
        log.info("Пытаюсь отправить событие в {}: {}", billingEventsTopic, event);
        kafkaTemplate.send(billingEventsTopic, event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Сообщение успешно отправлено в {}: {}", billingEventsTopic, event);
                } else {
                    log.error("Ошибка отправки сообщения в {}", billingEventsTopic, ex);
                }
            });
    }
} 