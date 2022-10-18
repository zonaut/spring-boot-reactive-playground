package com.zonaut.playground.reactive.controllers;

import com.zonaut.playground.reactive.BaseAcceptanceTest;
import com.zonaut.playground.reactive.controllers.responses.ProductResponseTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.zonaut.playground.reactive.controllers.ProductController.API_V_1_PRODUCTS;

class ProductControllerAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getAllProducts() {
        webTestClient.get().uri(API_V_1_PRODUCTS)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(ProductResponseTO.class)
            .hasSize(20);
    }
}
