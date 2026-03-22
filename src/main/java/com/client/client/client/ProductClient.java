package com.client.client.client;

import com.client.client.domain.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "product-service",
        url = "${services.product.url}"
)
public interface ProductClient {

    @GetMapping("/products")
    ProductResponse getProduct();
}