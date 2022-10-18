package com.zonaut.playground.reactive.exceptions.responses;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalErrorMessageTO {

    private String name;
    private String message;

}

