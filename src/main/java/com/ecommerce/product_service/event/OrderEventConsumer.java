package com.ecommerce.product_service.event;


import com.ecommerce.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderEventConsumer {

    private final ProductService productService;
    @KafkaListener(
        topics="order-placed",
        groupId="product-service-group"
    )
    public void consume(OrderPlacedEvent event){
        productService.reduceInventory(event);
    }

}
