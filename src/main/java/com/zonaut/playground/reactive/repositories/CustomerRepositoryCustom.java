package com.zonaut.playground.reactive.repositories;

import reactor.core.publisher.Mono;

public interface CustomerRepositoryCustom {

    Mono<Void> rightToForgetBatch();

}
