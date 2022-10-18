package com.zonaut.playground.reactive.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zonaut.playground.reactive.domain.EntityObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Table(value = ProductReviewEntity.PRODUCT_REVIEWS_TABLE_NAME)
public class ProductReviewEntity extends EntityObject<UUID> {

    protected static final String PRODUCT_REVIEWS_TABLE_NAME = "product_reviews";

    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String PRODUCT_ID = "product_id";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String CONTENT = "content";
    public static final String STARS = "stars";

    @Id
    @Column(ID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(CREATED_AT)
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @Column(UPDATED_AT)
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;

    @Column(PRODUCT_ID)
    @Setter(AccessLevel.NONE)
    private UUID productId;

    @Column(CUSTOMER_ID)
    @Setter(AccessLevel.NONE)
    private UUID customerId;

    @Column(CONTENT)
    private String content;

    @Column(STARS)
    private int stars;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return this.createdAt == null;
    }
}
