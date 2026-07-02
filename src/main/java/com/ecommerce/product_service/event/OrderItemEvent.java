package com.ecommerce.product_service.event;

import java.util.UUID;

public record OrderItemEvent(
        UUID productId,
        Integer quantity
) {

}
