package com.zonaut.playground.reactive.repositories;

import lombok.AllArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {

    private final DatabaseClient client;

    @Override
    public Mono<Void> rightToForgetBatch() {
        // we return here so that the calling client can subscribe and start the chain
        return Mono.from(client.getConnectionFactory().create())
                .flatMapMany(connection -> Flux.from(connection
                        .createBatch()
                        .add("DELETE FROM orders where customer_id = 1")
                        .add("DELETE FROM customers where id = 1")
                        .execute()))
                .then();
        // then(): Return a Mono<Void> that completes when this Flux completes.
        // This will actively ignore the sequence and only replay completion or error signals.
    }
}
