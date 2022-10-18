package com.zonaut.playground.reactive.exceptions.responses;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GlobalValidationErrorResponseTO {

    String code;

    String traceId;

    @Builder.Default
    private List<GlobalErrorMessageTO> messages = new ArrayList<>();

}

