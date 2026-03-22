package com.client.client.mapper;


import com.client.client.domain.Product;
import com.client.client.domain.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toDomain(ProductResponse response) {
        Product product = new Product();
        product.setName(response.getName());
        product.setPrice(response.getPrice());
        return product;
    }
}