package com.ecommerce.product_service.event;

import java.util.List;
import java.util.UUID;

public record OrderPlacedEvent(
        UUID orderId,
        List<OrderItemEvent> items
) {
}
