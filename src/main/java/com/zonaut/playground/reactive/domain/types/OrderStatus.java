package com.zonaut.playground.reactive.domain.types;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {

    CREATED,
    PROCESSING,
    SHIPPED,
    DELIVERED
    ;

    public static final String ORDER_STATUS_DB_TYPE_NAME = "order_status";

    public static final Set<OrderStatus> ALL_VALUES = EnumSet.allOf(OrderStatus.class);

}
