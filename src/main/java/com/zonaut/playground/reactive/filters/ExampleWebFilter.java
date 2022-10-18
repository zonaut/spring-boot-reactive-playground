package com.zonaut.playground.reactive.filters;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ExampleWebFilter implements WebFilter {

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        log.info("filter is called, adding headers");

        serverWebExchange.getResponse().getHeaders().add("web-filter", "web-filter-test");

        return webFilterChain.filter(serverWebExchange);
    }
}
