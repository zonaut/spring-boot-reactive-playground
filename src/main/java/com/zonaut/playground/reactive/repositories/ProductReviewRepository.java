package com.zonaut.playground.reactive.repositories;

import com.zonaut.playground.reactive.entities.ProductReviewEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface ProductReviewRepository extends R2dbcRepository<ProductReviewEntity, UUID> {

}
