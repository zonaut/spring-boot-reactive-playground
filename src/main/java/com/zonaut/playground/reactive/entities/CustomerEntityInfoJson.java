package com.zonaut.playground.reactive.entities;

import com.zonaut.playground.reactive.domain.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerEntityInfoJson extends ValueObject {

    private String example1;
    private String example2;

}
