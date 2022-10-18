package com.zonaut.playground.reactive.utils;

import java.util.Random;

public final class RandomUtil {

    public static final Random RANDOM = new Random();

    private RandomUtil() {
    }

    public static <T extends Enum<T>> T randomEnum(final Class<T> enumClass) {
        final T[] constants = enumClass.getEnumConstants();
        return constants[RANDOM.nextInt(constants.length)];
    }
}
