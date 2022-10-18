package com.zonaut.playground.reactive.controllers;

import com.zonaut.playground.reactive.controllers.requests.CustomerCreateRequestTO;
import com.zonaut.playground.reactive.controllers.responses.CustomerResponseTO;
import com.zonaut.playground.reactive.entities.CustomerEntity;
import com.zonaut.playground.reactive.mappers.CustomerObjectMapper;
import com.zonaut.playground.reactive.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = CustomerController.API_V_1_CUSTOMERS)
public class CustomerController {

    public static final String API_V_1_CUSTOMERS = "/v1/customers";

    private final CustomerRepository customerRepository;

    // https://stackoverflow.com/questions/52098863/whats-the-difference-between-text-event-stream-and-application-streamjson
    @GetMapping(produces = {APPLICATION_JSON_VALUE, APPLICATION_NDJSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flux<CustomerResponseTO> getAllCustomers() {
        log.info("getAllCustomers called");

        return customerRepository.findAll().flatMap(CustomerObjectMapper::mapCustomerEntityToCustomerResponseTOMono);
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public Mono<CustomerResponseTO> createCustomer(@Valid @RequestBody CustomerCreateRequestTO createCustomer) {
        log.info("createCustomer called with {}", createCustomer);

        CustomerEntity customer = CustomerObjectMapper.mapCustomerCreateRequestTOToCustomerEntity(createCustomer);
        return customerRepository.save(customer)
                .flatMap(CustomerObjectMapper::mapCustomerEntityToCustomerResponseTOMono);
    }
}
