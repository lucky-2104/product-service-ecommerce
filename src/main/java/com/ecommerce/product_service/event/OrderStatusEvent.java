package com.ecommerce.product_service.event;

import java.util.UUID;

public record OrderStatusEvent(
        UUID orderId,
        String reason
) {
}
