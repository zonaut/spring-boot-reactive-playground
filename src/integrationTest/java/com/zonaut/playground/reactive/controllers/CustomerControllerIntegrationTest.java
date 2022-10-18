package com.zonaut.playground.reactive.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zonaut.playground.reactive.controllers.requests.CustomerCreateRequestTO;
import com.zonaut.playground.reactive.controllers.responses.CustomerResponseTO;
import com.zonaut.playground.reactive.entities.CustomerEntity;
import com.zonaut.playground.reactive.exceptions.responses.GlobalErrorMessageTO;
import com.zonaut.playground.reactive.exceptions.responses.GlobalValidationErrorResponseTO;
import com.zonaut.playground.reactive.mappers.CustomerObjectMapper;
import com.zonaut.playground.reactive.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.zonaut.playground.reactive.controllers.CustomerController.API_V_1_CUSTOMERS;
import static com.zonaut.playground.reactive.mappers.CustomerObjectMapper.JSON_EXAMPLE;
import static com.zonaut.playground.reactive.mappers.CustomerObjectMapper.mapCustomerEntityToCustomerResponseTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(CustomerController.class)
class CustomerControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void getAllCustomers() {
        List<CustomerEntity> customerEntities = Arrays.asList(
                CustomerEntity.builder().id(UUID.randomUUID()).createdAt(Instant.now()).username("username-1").info(JSON_EXAMPLE).build(),
                CustomerEntity.builder().id(UUID.randomUUID()).createdAt(Instant.now()).username("username-2").info(JSON_EXAMPLE).build()
        );
        List<CustomerResponseTO> expectedCustomerResponseTOS = customerEntities.stream()
                .map(CustomerObjectMapper::mapCustomerEntityToCustomerResponseTO)
                .toList();

        when(customerRepository.findAll()).thenReturn(Flux.fromIterable(customerEntities));

        webTestClient.get().uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerResponseTO.class)
                .isEqualTo(expectedCustomerResponseTOS);
    }

    @Test
    void createCustomer() {
        CustomerCreateRequestTO customerCreateRequestTO = CustomerCreateRequestTO.builder().username("username-1").build();
        CustomerEntity customerEntity = CustomerEntity.builder().id(UUID.randomUUID()).createdAt(Instant.now()).username("username-1").info(JSON_EXAMPLE).build();
        CustomerResponseTO customerResponseTO = mapCustomerEntityToCustomerResponseTO(customerEntity);

        when(customerRepository.save(any())).thenReturn(Mono.just(customerEntity));

        webTestClient.post().uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerCreateRequestTO), CustomerCreateRequestTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CustomerResponseTO.class)
                .isEqualTo(customerResponseTO);
    }

    @Test
    void createCustomer_whenUsernameIsInvalid_expectValidationErrors() throws JsonProcessingException {
        CustomerCreateRequestTO customerCreateRequestTO = CustomerCreateRequestTO.builder().username("").build();

        GlobalValidationErrorResponseTO expectedError = GlobalValidationErrorResponseTO.builder()
                .code("4xx_validation_general")
                .traceId("ignore")
                .messages(List.of(
                        GlobalErrorMessageTO.builder().name("username").message("must not be blank").build(),
                        GlobalErrorMessageTO.builder().name("username").message("length must be between 5 and 2147483647").build()
                ))
                .build();

        webTestClient.post().uri(API_V_1_CUSTOMERS)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerCreateRequestTO), CustomerCreateRequestTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(GlobalValidationErrorResponseTO.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody())
                            .usingRecursiveComparison()
                            .ignoringCollectionOrder()
                            .ignoringFields("traceId")
                            .isEqualTo(expectedError);
                });
    }

}
