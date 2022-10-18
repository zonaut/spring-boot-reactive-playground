package com.zonaut.playground.reactive.entities;

import com.zonaut.playground.reactive.domain.EntityObject;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Table(value = OrderLineEntity.ORDER_LINES_TABLE_NAME)
public class OrderLineEntity extends EntityObject<Long> {

    protected static final String ORDER_LINES_TABLE_NAME = "order_lines";

    public static final String ID = "id";
    public static final String ORDER_ID = "order_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String NUMBER_OF = "number_of";

    @Id
    @Column(ID)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(ORDER_ID)
    @Setter(AccessLevel.NONE)
    private UUID orderId;

    @Column(PRODUCT_ID)
    @Setter(AccessLevel.NONE)
    private UUID productId;

    @Column(NUMBER_OF)
    private int numberOf;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
