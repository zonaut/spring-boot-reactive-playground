package com.zonaut.playground.reactive.repositories;

import com.zonaut.playground.reactive.entities.OrderEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderRepositoryCustom {

    Mono<OrderEntity> findOrderByCustomerId(UUID customerId);

}
