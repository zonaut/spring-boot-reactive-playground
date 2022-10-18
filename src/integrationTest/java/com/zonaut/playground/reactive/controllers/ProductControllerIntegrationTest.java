package com.zonaut.playground.reactive.controllers;

import com.zonaut.playground.reactive.controllers.events.ProductEvent;
import com.zonaut.playground.reactive.controllers.requests.ProductCreateRequestTO;
import com.zonaut.playground.reactive.controllers.responses.ProductResponseTO;
import com.zonaut.playground.reactive.domain.types.ProductCategory;
import com.zonaut.playground.reactive.entities.ProductEntity;
import com.zonaut.playground.reactive.exceptions.responses.GlobalValidationErrorResponseTO;
import com.zonaut.playground.reactive.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.UUID;

import static com.zonaut.playground.reactive.controllers.ProductController.API_V_1_PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

@WebFluxTest(ProductController.class)
class ProductControllerIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void getAllProducts() {
        ProductEntity entity = ProductEntity.builder()
            .id(UUID.randomUUID())
            .createdAt(Instant.now())
            .name("test-name")
            .category(ProductCategory.COMPUTERS)
            .active(true)
            .build();

        when(productRepository.findAll()).thenReturn(Flux.just(entity));

        webClient
            .get().uri(API_V_1_PRODUCTS)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(ProductResponseTO.class)
//            .isEqualTo(null)
        ;

        webClient
            .get().uri(API_V_1_PRODUCTS)
            .header(ACCEPT, APPLICATION_NDJSON_VALUE)
            .exchange()
            .expectHeader().contentType(APPLICATION_NDJSON_VALUE)
            .expectStatus()
            .isOk()
            .expectBody(ProductResponseTO.class);

//        webClient
//                .get().uri(API_V_1_PRODUCTS)
//                .header(ACCEPT, TEXT_EVENT_STREAM_VALUE)
//                .exchange()
//                .expectHeader().contentType(TEXT_EVENT_STREAM_VALUE)
//                .expectStatus()
//                .isOk()
//                .returnResult(ProductResponseTO.class)
//                .getResponseBody()
//                .take(1);
    }

    @Test
    void testProductEvents() {
        ProductEvent expectedEvent = new ProductEvent(0L, "Product Event");

        FluxExchangeResult<ProductEvent> result =
            webClient.get().uri(API_V_1_PRODUCTS + "/events")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductEvent.class);

        StepVerifier.create(result.getResponseBody())
            .expectNext(expectedEvent)
            .expectNextCount(2)
            .consumeNextWith(event -> assertEquals(Long.valueOf(3), event.getEventId()))
            .thenCancel()
            .verify();
    }

    @Test
    void createProduct() {
        ProductCreateRequestTO productCreateRequestTO = ProductCreateRequestTO.builder()
            .name("")
            .category(ProductCategory.COMPUTERS)
            .build();

        webClient
            .post().uri(API_V_1_PRODUCTS)
            .bodyValue(productCreateRequestTO)
            .exchange()
            .expectStatus().isBadRequest()
//                .expectBody()
//                .jsonPath("$.messages[0].name").isEqualTo(ProductEntity.NAME)
//                .jsonPath("$.messages[0].message").isEqualTo("must not be blank")
            .expectBody(GlobalValidationErrorResponseTO.class)
            .consumeWith(response -> {
                assertThat(response.getResponseBody()).isNotNull();
                assertThat(response.getResponseBody().getMessages().size()).isEqualTo(1);
                assertThat(response.getResponseBody().getMessages().get(0).getName()).isEqualTo(ProductEntity.NAME);
                assertThat(response.getResponseBody().getMessages().get(0).getMessage()).isEqualTo("must not be blank");
            })
        ;
    }
}
