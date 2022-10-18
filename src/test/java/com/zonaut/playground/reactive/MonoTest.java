package com.zonaut.playground.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class MonoTest {

    @Test
    public void monoSubscriber() {
        String expected = "Zonaut";

        Mono<String> mono = Mono.just(expected)
            .log();

        mono.subscribe();

        log.info("-".repeat(50));

        StepVerifier.create(mono)
            .expectNext(expected)
            .verifyComplete();

        log.info("-".repeat(50));

        StepVerifier.create(mono)
            .assertNext(actual -> {
                assertThat(actual).isEqualTo(expected);
            })
            .verifyComplete();
    }


}
