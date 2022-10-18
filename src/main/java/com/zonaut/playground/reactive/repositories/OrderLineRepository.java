package com.zonaut.playground.reactive.repositories;

import com.zonaut.playground.reactive.entities.OrderLineEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface OrderLineRepository extends R2dbcRepository<OrderLineEntity, Long> {

    Flux<OrderLineEntity> findAllByOrderId(UUID orderId);

}
