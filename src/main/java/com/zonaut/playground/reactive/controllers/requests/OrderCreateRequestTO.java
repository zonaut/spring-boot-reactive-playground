package com.zonaut.playground.reactive.controllers.requests;

import com.zonaut.playground.reactive.domain.RequestTransferObject;
import com.zonaut.playground.reactive.domain.types.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class OrderCreateRequestTO extends RequestTransferObject {

    @NotNull
    private OrderStatus status;

}
