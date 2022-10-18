package com.zonaut.playground.reactive.controllers.responses;

import com.zonaut.playground.reactive.domain.ResponseTransferObject;
import com.zonaut.playground.reactive.domain.types.OrderStatus;
import lombok.*;
import lombok.Builder.Default;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class OrderResponseTO extends ResponseTransferObject {

    private UUID id;
    private UUID customerId;
    private OrderStatus status;
    @Default
    private List<OrderLineResponseTO> lines = emptyList();

}
