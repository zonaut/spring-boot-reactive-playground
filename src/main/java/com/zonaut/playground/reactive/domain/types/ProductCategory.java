package com.zonaut.playground.reactive.domain.types;

import java.util.EnumSet;
import java.util.Set;

public enum ProductCategory {

    FOOD,
    COMPUTERS,
    GARDEN,
    SPORTS
    ;

    public static final Set<ProductCategory> ALL_VALUES = EnumSet.allOf(ProductCategory.class);

}
