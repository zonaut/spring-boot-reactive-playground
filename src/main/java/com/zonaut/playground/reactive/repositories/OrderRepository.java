package com.zonaut.playground.reactive.repositories;

import com.zonaut.playground.reactive.entities.OrderEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface OrderRepository extends R2dbcRepository<OrderEntity, UUID>, OrderRepositoryCustom {

}
