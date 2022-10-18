package com.zonaut.playground.reactive.repositories;//package com.zonaut.playground.service.products.repositories;

import com.zonaut.playground.reactive.BaseRepositoryIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

class CustomerRepositoryIntegrationTest extends BaseRepositoryIntegrationTest {

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private CustomerRepository customerRepository;
}
