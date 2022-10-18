package com.zonaut.playground.reactive.config.converters.enums;

import com.zonaut.playground.reactive.domain.types.OrderStatus;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;

public class EnumConverters {

    public static class OrderStatusConverter extends EnumWriteSupport<OrderStatus> {
    }

}
