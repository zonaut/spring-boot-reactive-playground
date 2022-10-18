package com.zonaut.playground.reactive.controllers.requests;

import com.zonaut.playground.reactive.domain.RequestTransferObject;
import com.zonaut.playground.reactive.domain.types.ProductCategory;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ProductUpdateRequestTO extends RequestTransferObject {

    @NotBlank
    private String name;

    @NotNull
    private ProductCategory category;

}


