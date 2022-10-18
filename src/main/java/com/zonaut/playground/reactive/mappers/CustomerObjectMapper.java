package com.zonaut.playground.reactive.mappers;

import com.zonaut.playground.reactive.controllers.requests.CustomerCreateRequestTO;
import com.zonaut.playground.reactive.controllers.responses.CustomerResponseTO;
import com.zonaut.playground.reactive.entities.CustomerEntity;
import com.zonaut.playground.reactive.entities.CustomerEntityInfoJson;
import io.r2dbc.postgresql.codec.Json;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.zonaut.playground.reactive.config.JacksonConfig.writeAsBytes;

public final class CustomerObjectMapper {

    public static final Json JSON_EXAMPLE = Json.of(writeAsBytes(CustomerEntityInfoJson.builder()
            .example1("a".repeat(50))
            .example2("b".repeat(50))
            .build()));

    private CustomerObjectMapper() {
    }

    public static Mono<CustomerResponseTO> mapCustomerEntityToCustomerResponseTOMono(CustomerEntity customerEntity) {
        return Mono.just(mapCustomerEntityToCustomerResponseTO(customerEntity));
    }

    public static Mono<CustomerEntity> mapCustomerCreateRequestTOToCustomerEntityMono(CustomerCreateRequestTO createCustomer) {
        return Mono.just(mapCustomerCreateRequestTOToCustomerEntity(createCustomer));
    }

    public static CustomerResponseTO mapCustomerEntityToCustomerResponseTO(CustomerEntity customerEntity) {
        return CustomerResponseTO.builder()
                .id(customerEntity.getId())
                .username(customerEntity.getUsername())
                .build();
    }

    public static CustomerEntity mapCustomerCreateRequestTOToCustomerEntity(CustomerCreateRequestTO createCustomer) {
        return CustomerEntity.builder()
                .id(UUID.randomUUID())
                .username(createCustomer.getUsername())
                .info(JSON_EXAMPLE)
                .build();
    }
}
