package com.ecommerce.product_service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CacheChecker implements CommandLineRunner {

    private final CacheManager cacheManager;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("CacheManager = " + cacheManager.getClass().getName());

    }
}

