package com.zonaut.playground.reactive.repositories;

import com.zonaut.playground.reactive.entities.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductRepository extends R2dbcRepository<ProductEntity, UUID>, ProductRepositoryCustom {

    Flux<ProductEntity> findAllBy(Pageable pageable);

    Flux<ProductEntity> findByNameLikeIgnoreCase(String name);

}
