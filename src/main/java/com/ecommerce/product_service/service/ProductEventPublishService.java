package com.ecommerce.product_service.service;


import com.ecommerce.product_service.event.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventPublishService {

    private final static String TOPIC = "order-status";
    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

    public void publish(OrderStatusEvent event){
        kafkaTemplate.send(
                TOPIC,
                event.orderId().toString(),
                event
        );
    }



}
