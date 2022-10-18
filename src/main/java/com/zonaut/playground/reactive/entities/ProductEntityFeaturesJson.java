package com.zonaut.playground.reactive.entities;

import com.zonaut.playground.reactive.domain.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductEntityFeaturesJson extends ValueObject {

    private String feature1;
    private String feature2;

}
