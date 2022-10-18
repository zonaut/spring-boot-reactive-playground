package com.zonaut.playground.reactive.repositories;

import com.zonaut.playground.reactive.controllers.responses.ProductResponseTO;
import com.zonaut.playground.reactive.entities.ProductEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepositoryCustom {

    Flux<ProductEntity> findByNameLike(String name);

    Mono<UUID> insertProductAndReturnId(ProductEntity product);

    Flux<ProductResponseTO> bulkInsertRandomProducts();

}
