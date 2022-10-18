package com.zonaut.playground.reactive.repositories;

import com.zonaut.playground.reactive.entities.CustomerEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface CustomerRepository extends R2dbcRepository<CustomerEntity, UUID>, CustomerRepositoryCustom {

}
