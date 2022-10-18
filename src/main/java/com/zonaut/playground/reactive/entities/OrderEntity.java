package com.zonaut.playground.reactive.entities;

import com.zonaut.playground.reactive.domain.EntityObject;
import com.zonaut.playground.reactive.domain.types.OrderStatus;
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
@Table(value = OrderEntity.ORDERS_TABLE_NAME)
public class OrderEntity extends EntityObject<UUID> {

    protected static final String ORDERS_TABLE_NAME = "orders";

    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String STATUS = "status";

    @Id
    @Column(ID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(CREATED_AT)
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @Column(CUSTOMER_ID)
    @Setter(AccessLevel.NONE)
    private UUID customerId;

    @Column(STATUS)
    private OrderStatus status;

    @Override
    public boolean isNew() {
        return this.createdAt == null;
    }
}
