package com.zonaut.playground.reactive.repositories;//package com.zonaut.playground.service.products.repositories;

import com.zonaut.playground.reactive.BaseRepositoryIntegrationTest;
import com.zonaut.playground.reactive.domain.types.ProductCategory;
import com.zonaut.playground.reactive.entities.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

import static com.zonaut.playground.reactive.repositories.ProductRepositoryCustomImpl.PRODUCTS_BULK_INSERT_COUNT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testDatabaseClient() {
        this.databaseClient.sql("SELECT * FROM products").fetch()
                .first()
                .map(row -> ProductEntity.builder()
                        .id((UUID) row.get("id"))
                        .name((String) row.get("name"))
                        .build())
                .as(StepVerifier::create)
                .assertNext(actual -> { // This is the one from our stubs
                    assertThat(actual.getId()).isEqualTo(UUID.fromString("b226fe6c-b6f6-4def-8c93-a5c2220bae7c"));
                    assertThat(actual.getName()).isEqualTo("chocolate");
                })
                .verifyComplete();
    }

    @Test
    void bulkInsertRandomProducts() {
        this.productRepository.bulkInsertRandomProducts()
                .log()
                .as(StepVerifier::create)
                .expectNextCount(PRODUCTS_BULK_INSERT_COUNT)
                .verifyComplete();
    }

    @Test
    void findAllByNameLikeIgnoreCase() {
        this.productRepository.findByNameLikeIgnoreCase("%sa%")
            .log()
            .as(StepVerifier::create)
            .expectNextCount(2)
            .verifyComplete();
    }
}
