package com.zonaut.playground.reactive.controllers;

import com.zonaut.playground.reactive.controllers.requests.OrderCreateRequestTO;
import com.zonaut.playground.reactive.controllers.responses.OrderLineResponseTO;
import com.zonaut.playground.reactive.controllers.responses.OrderResponseTO;
import com.zonaut.playground.reactive.domain.types.OrderStatus;
import com.zonaut.playground.reactive.entities.OrderEntity;
import com.zonaut.playground.reactive.repositories.OrderLineRepository;
import com.zonaut.playground.reactive.repositories.OrderRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = OrderController.API_V_1_ORDERS)
public class OrderController {

    public static final String API_V_1_ORDERS = "/v1/orders";

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    // https://stackoverflow.com/questions/52098863/whats-the-difference-between-text-event-stream-and-application-streamjson
    @GetMapping(produces = {APPLICATION_JSON_VALUE, APPLICATION_NDJSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flux<OrderEntity> getAllOrders() {
        log.info("getAllOrders called");

        return orderRepository.findAll()
                .map(orderEntity -> OrderEntity.builder()
                        .id(orderEntity.getId())
                        .createdAt(orderEntity.getCreatedAt())
                        .customerId(orderEntity.getCustomerId())
                        .status(orderEntity.getStatus())
                        .build());
    }

    @GetMapping("/{id}")
    public Mono<OrderResponseTO> getOrderById(@PathVariable("id") UUID id) {
        log.info("getOrderById called for id {}", id);

        return orderRepository.findById(id)
                .zipWith(orderLineRepository.findAllByOrderId(id).collectList())
                .map(tuple2WithOrderAndOrderLineList -> OrderResponseTO.builder()
                        .id(tuple2WithOrderAndOrderLineList.getT1().getId())
                        .customerId(tuple2WithOrderAndOrderLineList.getT1().getCustomerId())
                        .status(tuple2WithOrderAndOrderLineList.getT1().getStatus())
                        .lines(tuple2WithOrderAndOrderLineList.getT2().stream()
                                .map(orderLineEntity -> OrderLineResponseTO.builder()
                                        .id(orderLineEntity.getId())
                                        .orderId(orderLineEntity.getOrderId())
                                        .productId(orderLineEntity.getProductId())
                                        .numberOf(orderLineEntity.getNumberOf())
                                        .build())
                                .toList())
                        .build());
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public Mono<OrderEntity> createOrder(@Valid @RequestBody OrderCreateRequestTO createOrder) {
        log.info("createOrder called with {}", createOrder);

        OrderEntity order = OrderEntity.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.CREATED)
                .customerId(UUID.fromString("4643d924-584f-4d7a-9134-5d0105b19d76"))
                .build();

        return orderRepository.save(order);
    }
}
