package com.zonaut.playground.reactive.controllers.requests;

import com.zonaut.playground.reactive.domain.RequestTransferObject;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

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
