package com.zonaut.playground.reactive.handlers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class HandlerRouter {

    // TODO This need a lot of extra configuration because this is a low level approach
    //      - For Swagger more annotation are needed
    //          - https://springdoc.org/#spring-webfluxwebmvc-fn-with-functional-endpoints
    //      - Global exception handling doesn't work the same and needs separate configuration
    //      - No Bean validation out of the box
    //      - ...

    @Bean
    public RouterFunction<ServerResponse> routesForCustomer(CustomerHandler customerHandler) {
        return route()
                .GET("/handlers/customers", customerHandler::getAllCustomerFromHandler)
                .POST("/handlers/customers", customerHandler::createCustomerFromHandler)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> routesForProducts(ProductHandler productHandler) {
        return route()
                .GET("/handlers/products", productHandler::getAllProductsFromHandler)
                .build();
    }

}
