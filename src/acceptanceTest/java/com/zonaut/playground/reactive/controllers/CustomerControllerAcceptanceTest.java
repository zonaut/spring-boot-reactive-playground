package com.zonaut.playground.reactive.controllers;

import com.zonaut.playground.reactive.BaseAcceptanceTest;
import com.zonaut.playground.reactive.controllers.responses.CustomerResponseTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.zonaut.playground.reactive.controllers.CustomerController.API_V_1_CUSTOMERS;

class CustomerControllerAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getAllCustomers() {
        webTestClient.get().uri(API_V_1_CUSTOMERS)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(CustomerResponseTO.class)
            .hasSize(1);
    }
}
