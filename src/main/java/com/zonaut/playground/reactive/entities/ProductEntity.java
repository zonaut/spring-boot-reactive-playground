package com.zonaut.playground.reactive.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zonaut.playground.reactive.domain.EntityObject;
import com.zonaut.playground.reactive.domain.types.ProductCategory;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Table(value = ProductEntity.PRODUCTS_TABLE_NAME)
public class ProductEntity extends EntityObject<UUID> {

    protected static final String PRODUCTS_TABLE_NAME = "products";

    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String NAME = "name";
    public static final String CATEGORY = "category";
    public static final String FEATURES = "features";
    public static final String ACTIVE = "active";
    public static final String PRICE = "price";

    @Id
    @Column(ID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(CREATED_AT)
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @Column(NAME)
    private String name;

    @Column(CATEGORY)
    private ProductCategory category;

    @Column(FEATURES)
    private ProductEntityFeaturesJson features;

    @Column(ACTIVE)
    private boolean active;

    @Column(PRICE)
    private double price;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return this.createdAt == null;
    }
}
