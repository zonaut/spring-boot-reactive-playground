package com.zonaut.playground.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTest {

    @Test
    public void testError() {
        Flux<String> flux = Flux.just("A", "B")
            .map(a -> {
                if (a.equals("B")) {
                    throw new RuntimeException("ERROR");
                }
                return a;
            })
            .log();

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNext("A")
            .expectError()
            .verify();
    }

}
