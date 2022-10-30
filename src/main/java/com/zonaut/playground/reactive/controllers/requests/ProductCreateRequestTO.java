package com.zonaut.playground.reactive.controllers.requests;

import com.zonaut.playground.reactive.domain.RequestTransferObject;
import com.zonaut.playground.reactive.domain.types.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ProductCreateRequestTO extends RequestTransferObject {

    @NotBlank
    private String name;

    @NotNull
    private ProductCategory category;

}


