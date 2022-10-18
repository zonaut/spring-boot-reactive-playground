package com.zonaut.playground.reactive.repositories;

import com.zonaut.playground.reactive.entities.OrderEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.data.relational.core.query.Criteria.where;

@AllArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final R2dbcEntityTemplate template;

    @Override
    public Mono<OrderEntity> findOrderByCustomerId(UUID customerId) {
        return this.template.selectOne(Query.query(
                        where(OrderEntity.CUSTOMER_ID).is(customerId)),
                OrderEntity.class);
    }
}
