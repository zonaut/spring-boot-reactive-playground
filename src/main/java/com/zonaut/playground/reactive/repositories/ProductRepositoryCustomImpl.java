package com.zonaut.playground.reactive.repositories;

import com.zonaut.playground.reactive.controllers.responses.ProductResponseTO;
import com.zonaut.playground.reactive.domain.types.ProductCategory;
import com.zonaut.playground.reactive.entities.ProductEntity;
import com.zonaut.playground.reactive.utils.RandomUtil;
import io.r2dbc.spi.Batch;
import io.r2dbc.spi.Result;
import lombok.AllArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.zonaut.playground.reactive.mappers.ProductRowMapper.PRODUCT_ENTITY_ROW_MAPPER;
import static com.zonaut.playground.reactive.mappers.ProductRowMapper.PRODUCT_RESPONSE_TO_ROW_MAPPER;

@AllArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    public static final int PRODUCTS_BULK_INSERT_COUNT = 20;

    private final DatabaseClient client;

    @Override
    public Flux<ProductEntity> findByNameLike(String name) {
        String query = """
                SELECT * FROM products p
                WHERE p.name LIKE :name
                """;

        return client.sql(query)
                .bind(ProductEntity.NAME, name)
                .map(PRODUCT_ENTITY_ROW_MAPPER)
                .all();
    }

    @Override
    public Mono<UUID> insertProductAndReturnId(ProductEntity product) {
        String query = """
                INSERT INTO products (id, name, category, features, active, price) 
                VALUES (:id, :name, :category, :features, :active, :price)
                """;

        return this.client.sql(query)
                .filter((statement, executeFunction) -> statement.returnGeneratedValues(ProductEntity.CREATED_AT).execute())
                .bind(ProductEntity.ID, UUID.randomUUID())
                .bind(ProductEntity.NAME, product.getName())
                .bind(ProductEntity.CATEGORY, product.getCategory())
                .bind(ProductEntity.FEATURES, product.getFeatures())
                .bind(ProductEntity.ACTIVE, product.isActive())
                .bind(ProductEntity.PRICE, product.getPrice())
                .fetch()
                .first()
                .map(r -> (UUID) r.get(ProductEntity.ID));
    }

    @Override
    public Flux<ProductResponseTO> bulkInsertRandomProducts() {
        List<ProductEntity> randomProducts = Stream.generate(() -> ProductEntity.builder()
                        .id(UUID.randomUUID())
                        .name(UUID.randomUUID().toString().substring(0, 10))
                        .category(RandomUtil.randomEnum(ProductCategory.class))
                        .active(false)
                        .price(9.99)
                        .build())
                .limit(PRODUCTS_BULK_INSERT_COUNT)
                .toList();

        Flux<? extends Result> flux = client.inConnectionMany(connection -> {
            Batch batch = connection.createBatch();

            randomProducts.forEach(p -> {
                // If you want to have a response you need to use RETURNING with the fields you want.
                String query = """
                        INSERT INTO products (id, name, category, active, price) 
                        VALUES ('%s', '%s', '%s', '%s', '%s')
                        RETURNING id, created_at, name, category, features, active, price
                        """.formatted(p.getId(), p.getName(), p.getCategory(), p.isActive(), p.getPrice());
                batch.add(query);
            });

            return Flux.from(batch.execute());
        });

        return flux.flatMap(result -> result.map(PRODUCT_RESPONSE_TO_ROW_MAPPER));
    }
}
