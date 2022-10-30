package com.zonaut.playground.reactive.controllers.requests;

import com.zonaut.playground.reactive.domain.RequestTransferObject;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class CustomerCreateRequestTO extends RequestTransferObject {

    @NotBlank
    @Length(min = 5)
    private String username;

}
