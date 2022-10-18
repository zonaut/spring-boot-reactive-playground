package com.zonaut.playground.reactive.mappers;

import com.zonaut.playground.reactive.controllers.responses.ProductResponseTO;
import com.zonaut.playground.reactive.domain.types.ProductCategory;
import com.zonaut.playground.reactive.entities.ProductEntity;
import com.zonaut.playground.reactive.entities.ProductEntityFeaturesJson;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.Instant;
import java.util.UUID;
import java.util.function.BiFunction;

public final class ProductRowMapper {

    private ProductRowMapper() {
    }

    public static final BiFunction<Row, RowMetadata, ProductEntity> PRODUCT_ENTITY_ROW_MAPPER = (row, rowMetaData) -> ProductEntity.builder()
            .id(row.get(ProductEntity.ID, UUID.class))
            .createdAt(row.get(ProductEntity.CREATED_AT, Instant.class))
            .name(row.get(ProductEntity.NAME, String.class))
            .category(ProductCategory.valueOf(row.get(ProductEntity.CATEGORY, String.class)))
            .features(row.get(ProductEntity.FEATURES, ProductEntityFeaturesJson.class))
            .active(Boolean.TRUE.equals(row.get(ProductEntity.ACTIVE, Boolean.class)))
            .price(parsePriceDouble(row)) // TODO Why do we loose hinting for NullPointerException when using helper method?
            .build();

    public static final BiFunction<Row, RowMetadata, ProductResponseTO> PRODUCT_RESPONSE_TO_ROW_MAPPER = (row, rowMetaData) -> ProductResponseTO.builder()
            .id(row.get(ProductEntity.ID, UUID.class))
            .createdAt(row.get(ProductEntity.CREATED_AT, Instant.class))
            .name(row.get(ProductEntity.NAME, String.class))
            .category(ProductCategory.valueOf(row.get(ProductEntity.CATEGORY, String.class)))
            .features(row.get(ProductEntity.FEATURES, ProductEntityFeaturesJson.class))
            .active(Boolean.TRUE.equals(row.get(ProductEntity.ACTIVE, Boolean.class)))
            .price(row.get(ProductEntity.PRICE, Double.class))
            .build();

    private static Double parsePriceDouble(Row row) {
        return row.get(ProductEntity.PRICE, Double.class);
    }
}
