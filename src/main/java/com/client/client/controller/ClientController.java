package com.client.client.controller;

import com.client.client.domain.Product;
import com.client.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/products")
    public Product product() {
        return clientService.getProduct();
    }
}