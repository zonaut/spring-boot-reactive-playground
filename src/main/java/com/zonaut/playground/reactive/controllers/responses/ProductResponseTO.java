package com.zonaut.playground.reactive.controllers.responses;

import com.zonaut.playground.reactive.domain.ResponseTransferObject;
import com.zonaut.playground.reactive.domain.types.ProductCategory;
import com.zonaut.playground.reactive.entities.ProductEntityFeaturesJson;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ProductResponseTO extends ResponseTransferObject {

    private UUID id;
    private Instant createdAt;
    private String name;
    private ProductCategory category;
    private boolean active;
    private double price;
    private ProductEntityFeaturesJson features;

}


