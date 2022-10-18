package com.zonaut.playground.reactive.exceptions.responses;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalGeneralErrorResponseTO {

    String code;

    String traceId;

    private GlobalErrorMessageTO message;

}
