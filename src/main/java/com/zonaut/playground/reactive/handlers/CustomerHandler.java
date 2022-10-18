package com.zonaut.playground.reactive.handlers;

import com.zonaut.playground.reactive.controllers.requests.CustomerCreateRequestTO;
import com.zonaut.playground.reactive.controllers.responses.CustomerResponseTO;
import com.zonaut.playground.reactive.mappers.CustomerObjectMapper;
import com.zonaut.playground.reactive.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
@AllArgsConstructor
public class CustomerHandler {

    private final CustomerRepository customerRepository;

    public Mono<ServerResponse> getAllCustomerFromHandler(ServerRequest req) {
        log.info("getAllCustomerFromHandler called");

        return ok().body(this.customerRepository.findAll()
                .flatMap(CustomerObjectMapper::mapCustomerEntityToCustomerResponseTOMono), CustomerResponseTO.class);
    }

    public Mono<ServerResponse> createCustomerFromHandler(ServerRequest req) {
        log.info("createCustomerFromHandler called");

        return req.bodyToMono(CustomerCreateRequestTO.class)
                .flatMap(CustomerObjectMapper::mapCustomerCreateRequestTOToCustomerEntityMono)
                .flatMap(this.customerRepository::save)
                .flatMap(CustomerObjectMapper::mapCustomerEntityToCustomerResponseTOMono)
                .flatMap(customerResponseTO -> ServerResponse
                        .ok()
                        .body(Mono.just(customerResponseTO), CustomerCreateRequestTO.class));
    }

}
