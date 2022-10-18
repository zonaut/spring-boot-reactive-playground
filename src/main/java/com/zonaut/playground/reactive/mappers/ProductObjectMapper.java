package com.zonaut.playground.reactive.mappers;

import com.zonaut.playground.reactive.controllers.requests.ProductCreateRequestTO;
import com.zonaut.playground.reactive.controllers.requests.ProductUpdateRequestTO;
import com.zonaut.playground.reactive.controllers.responses.ProductResponseTO;
import com.zonaut.playground.reactive.entities.ProductEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.zonaut.playground.reactive.controllers.ProductController.PRODUCT_ENTITY_FEATURES_JSON;

public final class ProductObjectMapper {

    private ProductObjectMapper() {
    }

    public static Mono<ProductResponseTO> mapProductEntityToProductResponseTOMono(ProductEntity productEntity) {
        return Mono.just(mapProductEntityToProductResponseTO(productEntity));
    }

    public static Mono<ProductEntity> mapProductCreateRequestTOToProductEntityMono(ProductCreateRequestTO createProduct) {
        return Mono.just(mapProductCreateRequestTOToProductEntity(createProduct));
    }

    public static ProductResponseTO mapProductEntityToProductResponseTO(ProductEntity productEntity) {
        return ProductResponseTO.builder()
            .id(productEntity.getId())
            .createdAt(productEntity.getCreatedAt())
            .name(productEntity.getName())
            .category(productEntity.getCategory())
            .active(productEntity.isActive())
            .price(productEntity.getPrice())
            .features(productEntity.getFeatures())
            .build();
    }

    public static ProductEntity mapProductCreateRequestTOToProductEntity(ProductCreateRequestTO createProduct) {
        return ProductEntity.builder()
            .id(UUID.randomUUID())
            .name(createProduct.getName())
            .category(createProduct.getCategory())
            .active(true)
            .price(9.99)
            .features(PRODUCT_ENTITY_FEATURES_JSON)
            .build();
    }

    public static void mapProductUpdateRequestTOToProductEntity(ProductEntity existingProduct, ProductUpdateRequestTO updateProduct) {
        existingProduct.setName(updateProduct.getName());
        existingProduct.setCategory(updateProduct.getCategory());
    }
}
