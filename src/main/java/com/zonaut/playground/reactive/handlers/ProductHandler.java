package com.zonaut.playground.reactive.handlers;

import com.zonaut.playground.reactive.controllers.responses.ProductResponseTO;
import com.zonaut.playground.reactive.mappers.ProductObjectMapper;
import com.zonaut.playground.reactive.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
@AllArgsConstructor
public class ProductHandler {

    private final ProductRepository productRepository;

    public Mono<ServerResponse> getAllProductsFromHandler(ServerRequest req) {
        log.info("getAllProductsFromHandler called");

        return ok().body(this.productRepository.findAll()
                .flatMap(ProductObjectMapper::mapProductEntityToProductResponseTOMono), ProductResponseTO.class);
    }

}
