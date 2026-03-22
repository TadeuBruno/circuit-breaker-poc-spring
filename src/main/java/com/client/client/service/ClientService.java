package com.client.client.service;

import com.client.client.client.ProductClient;
import com.client.client.domain.Product;
import com.client.client.mapper.ProductMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ProductClient client;
    private final ProductMapper mapper;

    @CircuitBreaker(name = "clientCB", fallbackMethod = "getProductFallback")
    public Product getProduct() {
        return mapper.toDomain(client.getProduct());
    }

    public Product getProductFallback(Throwable t) {
        Product product = new Product();
        product.setName("Produto fallback");
        product.setPrice("0");
        return product;
    }
}