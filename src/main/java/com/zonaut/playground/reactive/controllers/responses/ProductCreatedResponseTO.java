package com.zonaut.playground.reactive.controllers.responses;

import com.zonaut.playground.reactive.domain.ResponseTransferObject;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ProductCreatedResponseTO extends ResponseTransferObject {

    private UUID id;
    private String name;
    private String info;

}
